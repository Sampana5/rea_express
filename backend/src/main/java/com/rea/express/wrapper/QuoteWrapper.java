package com.rea.express.wrapper;

import com.rea.express.POJO.Quote;
import com.rea.express.POJO.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuoteWrapper {

    private Integer id;
    private String reference;
    private String status;
    private String paymentStatus;
    private String clientMessage;
    private String adminNotes;
    private BigDecimal quotedAmount;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer userId;
    private String userName;
    private String userEmail;
    private String userContact;
    private List<QuoteItemWrapper> items = Collections.emptyList();
    private int totalQuantity;

    public static QuoteWrapper from(Quote quote) {
        QuoteWrapper wrapper = new QuoteWrapper();
        wrapper.setId(quote.getId());
        wrapper.setReference(quote.getReference());
        wrapper.setStatus(quote.getStatus().name());
        wrapper.setPaymentStatus(quote.getPaymentStatus().name());
        wrapper.setClientMessage(quote.getClientMessage());
        wrapper.setAdminNotes(quote.getAdminNotes());
        wrapper.setQuotedAmount(quote.getQuotedAmount());
        wrapper.setCurrency(quote.getCurrency());
        wrapper.setCreatedAt(quote.getCreatedAt());
        wrapper.setUpdatedAt(quote.getUpdatedAt());
        User user = quote.getUser();
        if (user != null) {
            wrapper.setUserId(user.getId());
            wrapper.setUserName(user.getName());
            wrapper.setUserEmail(user.getEmail());
            wrapper.setUserContact(user.getContactNumber());
        }
        List<QuoteItemWrapper> items = quote.getItems() == null
                ? Collections.emptyList()
                : quote.getItems().stream().map(QuoteItemWrapper::from).collect(Collectors.toList());
        wrapper.setItems(items);
        wrapper.setTotalQuantity(items.stream().mapToInt(QuoteItemWrapper::getQuantity).sum());
        return wrapper;
    }
}
