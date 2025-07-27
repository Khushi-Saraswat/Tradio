package com.trading.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.demo.model.Coin;
import com.trading.demo.model.User;
import com.trading.demo.model.Watchlist;
import com.trading.demo.repository.WatchlistRepository;

@Service
public class WatchlistServiceImpl implements WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;

    @Override
    public Watchlist findUserWatchlist(Long userId) throws Exception {
        Watchlist watchlist = watchlistRepository.findByUserId(userId);
        if (watchlist == null) {
            throw new Exception("watch not found");
        }
        System.out.println("watchList for users" + watchlist);
        return watchlist;
    }

    @Override
    public Watchlist createWatchList(User user) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist findById(Long id) throws Exception {
        Optional<Watchlist> optionalWatchlist = watchlistRepository.findById(id);
        if (optionalWatchlist.isEmpty()) {
            throw new Exception("watch list not found");
        }
        return optionalWatchlist.get();
    }

    @Override
    public Coin addItemToWatchlist(Coin coin, User user) throws Exception {
        Watchlist watchlist = findUserWatchlist(user.getId());

        if (watchlist.getCoins().contains(coin)) {
            watchlist.getCoins().remove(coin);
        } else
            watchlist.getCoins().add(coin);
        watchlistRepository.save(watchlist);
        return coin;
    }
}
