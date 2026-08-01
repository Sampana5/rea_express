package com.rea.express.rest;

import com.rea.express.dto.CartItemRequest;
import com.rea.express.wrapper.CartWrapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/cart")
public interface CartRest {

    @GetMapping
    ResponseEntity<CartWrapper> getMyCart();

    @PostMapping(path = "/items")
    ResponseEntity<CartWrapper> addItem(@Valid @RequestBody CartItemRequest request);

    @PutMapping(path = "/items/{productId}")
    ResponseEntity<CartWrapper> updateItem(@PathVariable Integer productId,
                                           @Valid @RequestBody CartItemRequest request);

    @DeleteMapping(path = "/items/{productId}")
    ResponseEntity<CartWrapper> removeItem(@PathVariable Integer productId);

    @DeleteMapping
    ResponseEntity<CartWrapper> clear();
}
