package com.trading.demo.service;

import com.trading.demo.model.Coin;
import com.trading.demo.model.Users;
import com.trading.demo.model.Watchlist;

public interface WatchlistService {

    Watchlist findUserWatchlist(Long userId) throws Exception;

    Watchlist createWatchList(Users user);

    Watchlist findById(Long id) throws Exception;

    Coin addItemToWatchlist(Coin coin, Users user) throws Exception;
}
