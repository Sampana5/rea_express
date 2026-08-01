package com.rea.express.dao;

import com.rea.express.POJO.Quote;
import com.rea.express.POJO.QuoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuoteDao extends JpaRepository<Quote, Integer> {

    @Query("select distinct q from Quote q left join fetch q.items left join fetch q.user where q.user.id = :userId order by q.createdAt desc")
    List<Quote> findByUserIdWithItems(@Param("userId") Integer userId);

    @Query("select distinct q from Quote q left join fetch q.items left join fetch q.user order by q.createdAt desc")
    List<Quote> findAllWithItems();

    @Query("select distinct q from Quote q left join fetch q.items left join fetch q.user where q.id = :id")
    Optional<Quote> findByIdWithItems(@Param("id") Integer id);

    long countByStatus(QuoteStatus status);

    boolean existsByReference(String reference);
}
