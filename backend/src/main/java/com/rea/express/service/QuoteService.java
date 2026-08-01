package com.rea.express.service;

import com.rea.express.dto.QuoteCreateRequest;
import com.rea.express.dto.QuoteStatusUpdateRequest;
import com.rea.express.wrapper.AdminStatsWrapper;
import com.rea.express.wrapper.QuoteWrapper;

import java.util.List;

public interface QuoteService {

    QuoteWrapper createFromCart(QuoteCreateRequest request);

    List<QuoteWrapper> getMyQuotes();

    QuoteWrapper getQuote(Integer id);

    List<QuoteWrapper> getAllQuotes();

    QuoteWrapper updateStatus(Integer id, QuoteStatusUpdateRequest request);

    AdminStatsWrapper getAdminStats();
}
