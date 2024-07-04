package com.example.demo.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

@Component
public class AirportCache {
    private static final long TTL = 30000;
    private Map<String, CacheItem> cache = new ConcurrentHashMap<>();
    private ScheduledExecutorService cleaner = Executors.newScheduledThreadPool(1);

    public AirportCache() {
        cleaner.scheduleAtFixedRate(this::cleanUp, TTL, TTL, TimeUnit.MILLISECONDS);
    }

    public void put(String key, Airport airport) {
        cache.put(key, new CacheItem(airport, System.currentTimeMillis()));
    }

    public Airport get(String key) {
        CacheItem cacheItem = cache.get(key);
        if (cacheItem != null && (System.currentTimeMillis() - cacheItem.getTimestamp()) < TTL) {
            return cacheItem.getAirport();
        } else {
            cache.remove(key);
            return null;
        }
    }

    private void cleanUp() {
        long now = System.currentTimeMillis();
        cache.entrySet().removeIf(entry -> (now - entry.getValue().getTimestamp()) >= TTL);
    }
    @Getter
    @Setter
    @AllArgsConstructor
    private static class CacheItem {
        private Airport airport;
        private long timestamp;
    }
}