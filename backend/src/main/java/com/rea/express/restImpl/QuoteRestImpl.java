package com.rea.express.restImpl;

import com.rea.express.dto.QuoteCreateRequest;
import com.rea.express.dto.QuoteStatusUpdateRequest;
import com.rea.express.rest.QuoteRest;
import com.rea.express.service.QuoteService;
import com.rea.express.wrapper.AdminStatsWrapper;
import com.rea.express.wrapper.QuoteWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuoteRestImpl implements QuoteRest {

    private final QuoteService quoteService;

    @Override
    public ResponseEntity<QuoteWrapper> create(QuoteCreateRequest request) {
        if (request == null) {
            request = new QuoteCreateRequest();
        }
        return new ResponseEntity<>(quoteService.createFromCart(request), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<QuoteWrapper>> mine() {
        return ResponseEntity.ok(quoteService.getMyQuotes());
    }

    @Override
    public ResponseEntity<QuoteWrapper> getOne(Integer id) {
        return ResponseEntity.ok(quoteService.getQuote(id));
    }

    @Override
    public ResponseEntity<List<QuoteWrapper>> all() {
        return ResponseEntity.ok(quoteService.getAllQuotes());
    }

    @Override
    public ResponseEntity<QuoteWrapper> updateStatus(Integer id, QuoteStatusUpdateRequest request) {
        return ResponseEntity.ok(quoteService.updateStatus(id, request));
    }

    @Override
    public ResponseEntity<AdminStatsWrapper> adminStats() {
        return ResponseEntity.ok(quoteService.getAdminStats());
    }
}
