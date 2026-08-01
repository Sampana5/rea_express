package com.rea.express.serviceImpl;

import com.rea.express.POJO.Cart;
import com.rea.express.POJO.CartItem;
import com.rea.express.POJO.ERole;
import com.rea.express.POJO.PaymentStatus;
import com.rea.express.POJO.Product;
import com.rea.express.POJO.Quote;
import com.rea.express.POJO.QuoteItem;
import com.rea.express.POJO.QuoteStatus;
import com.rea.express.POJO.Role;
import com.rea.express.POJO.User;
import com.rea.express.dao.CartDao;
import com.rea.express.dao.CategoryDao;
import com.rea.express.dao.ProductDao;
import com.rea.express.dao.QuoteDao;
import com.rea.express.dao.UserDao;
import com.rea.express.dto.QuoteCreateRequest;
import com.rea.express.dto.QuoteStatusUpdateRequest;
import com.rea.express.exceptions.ResourceNotFoundException;
import com.rea.express.service.QuoteService;
import com.rea.express.utils.CurrentUserService;
import com.rea.express.wrapper.AdminStatsWrapper;
import com.rea.express.wrapper.QuoteWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter DAY = DateTimeFormatter.BASIC_ISO_DATE;
    private static final EnumSet<QuoteStatus> PAYMENT_READY = EnumSet.of(
            QuoteStatus.AWAITING_PAYMENT, QuoteStatus.PAID, QuoteStatus.FULFILLED);

    private final QuoteDao quoteDao;
    private final CartDao cartDao;
    private final UserDao userDao;
    private final ProductDao productDao;
    private final CategoryDao categoryDao;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public QuoteWrapper createFromCart(QuoteCreateRequest request) {
        User user = currentUserService.requireCurrentUser();
        Cart cart = cartDao.findByUserIdWithItems(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Votre panier est vide."));
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Votre panier est vide.");
        }

        Quote quote = new Quote();
        quote.setReference(generateReference());
        quote.setUser(user);
        quote.setStatus(QuoteStatus.PENDING);
        quote.setPaymentStatus(PaymentStatus.NONE);
        quote.setClientMessage(request != null ? request.getMessage() : null);

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            QuoteItem item = new QuoteItem();
            item.setQuote(quote);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductReference(product.getReference());
            item.setProductImageUrl(product.getImageUrl());
            item.setQuantity(cartItem.getQuantity());
            quote.getItems().add(item);
        }

        Quote saved = quoteDao.save(quote);
        cart.getItems().clear();
        cartDao.save(cart);
        return QuoteWrapper.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuoteWrapper> getMyQuotes() {
        User user = currentUserService.requireCurrentUser();
        return quoteDao.findByUserIdWithItems(user.getId()).stream()
                .map(QuoteWrapper::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuoteWrapper getQuote(Integer id) {
        Quote quote = quoteDao.findByIdWithItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Devis introuvable."));
        User user = currentUserService.requireCurrentUser();
        if (!isAdmin(user) && !quote.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès non autorisé à ce devis.");
        }
        return QuoteWrapper.from(quote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuoteWrapper> getAllQuotes() {
        return quoteDao.findAllWithItems().stream()
                .map(QuoteWrapper::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuoteWrapper updateStatus(Integer id, QuoteStatusUpdateRequest request) {
        Quote quote = quoteDao.findByIdWithItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Devis introuvable."));

        QuoteStatus next = request.getStatus();
        quote.setStatus(next);
        if (request.getAdminNotes() != null) {
            quote.setAdminNotes(request.getAdminNotes());
        }
        if (request.getQuotedAmount() != null) {
            quote.setQuotedAmount(request.getQuotedAmount());
        }

        // Prépare le terrain paiement sans l'activer tant que NONE.
        if (PAYMENT_READY.contains(next) && quote.getPaymentStatus() == PaymentStatus.NONE) {
            quote.setPaymentStatus(PaymentStatus.PENDING);
        }
        if (next == QuoteStatus.PAID) {
            quote.setPaymentStatus(PaymentStatus.PAID);
        }
        if (next == QuoteStatus.CANCELLED) {
            // on laisse paymentStatus tel quel (peut être NONE)
        }

        return QuoteWrapper.from(quoteDao.save(quote));
    }

    @Override
    @Transactional(readOnly = true)
    public AdminStatsWrapper getAdminStats() {
        List<User> users = userDao.findAll();
        long admins = users.stream().filter(this::isAdmin).count();
        long active = users.stream().filter(u -> "true".equalsIgnoreCase(u.getStatus())).count();
        return AdminStatsWrapper.builder()
                .users(users.size())
                .activeUsers(active)
                .admins(admins)
                .products(productDao.count())
                .categories(categoryDao.count())
                .quotes(quoteDao.count())
                .pendingQuotes(quoteDao.countByStatus(QuoteStatus.PENDING)
                        + quoteDao.countByStatus(QuoteStatus.IN_REVIEW))
                .quotedQuotes(quoteDao.countByStatus(QuoteStatus.QUOTED))
                .build();
    }

    private String generateReference() {
        String day = LocalDate.now().format(DAY);
        for (int i = 0; i < 8; i++) {
            String suffix = Integer.toHexString(RANDOM.nextInt(0xFFFF)).toUpperCase(Locale.ROOT);
            while (suffix.length() < 4) {
                suffix = "0" + suffix;
            }
            String ref = "DEV-" + day + "-" + suffix;
            if (!quoteDao.existsByReference(ref)) {
                return ref;
            }
        }
        return "DEV-" + day + "-" + System.currentTimeMillis();
    }

    private boolean isAdmin(User user) {
        if (user.getRoles() == null) {
            return false;
        }
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(role -> role == ERole.ROLE_ADMIN);
    }
}
