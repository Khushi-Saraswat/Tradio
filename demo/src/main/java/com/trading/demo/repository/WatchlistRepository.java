package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    Watchlist findByUserId(Long userId);

}
