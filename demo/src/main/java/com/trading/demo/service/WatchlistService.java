package com.trading.demo.service;

import com.trading.demo.model.Coin;
import com.trading.demo.model.User;
import com.trading.demo.model.Watchlist;

public interface WatchlistService {

    Watchlist findUserWatchlist(Long userId) throws Exception;

    Watchlist createWatchList(User user);

    Watchlist findById(Long id) throws Exception;

    Coin addItemToWatchlist(Coin coin, User user) throws Exception;
}
