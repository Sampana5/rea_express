package com.rea.express.rest;

import com.rea.express.dto.QuoteCreateRequest;
import com.rea.express.dto.QuoteStatusUpdateRequest;
import com.rea.express.wrapper.AdminStatsWrapper;
import com.rea.express.wrapper.QuoteWrapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/quotes")
public interface QuoteRest {

    @PostMapping
    ResponseEntity<QuoteWrapper> create(@Valid @RequestBody(required = false) QuoteCreateRequest request);

    @GetMapping(path = "/mine")
    ResponseEntity<List<QuoteWrapper>> mine();

    @GetMapping(path = "/{id}")
    ResponseEntity<QuoteWrapper> getOne(@PathVariable Integer id);

    @GetMapping
    ResponseEntity<List<QuoteWrapper>> all();

    @PutMapping(path = "/{id}/status")
    ResponseEntity<QuoteWrapper> updateStatus(@PathVariable Integer id,
                                              @Valid @RequestBody QuoteStatusUpdateRequest request);

    @GetMapping(path = "/admin/stats")
    ResponseEntity<AdminStatsWrapper> adminStats();
}
