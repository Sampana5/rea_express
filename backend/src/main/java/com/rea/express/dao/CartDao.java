package com.rea.express.dao;

import com.rea.express.POJO.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartDao extends JpaRepository<Cart, Integer> {

    @Query("select distinct c from Cart c left join fetch c.items i left join fetch i.product where c.user.id = :userId")
    Optional<Cart> findByUserIdWithItems(@Param("userId") Integer userId);

    Optional<Cart> findByUserId(Integer userId);
}
