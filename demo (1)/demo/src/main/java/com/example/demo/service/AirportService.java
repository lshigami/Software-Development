package com.example.demo.service;


import com.example.demo.cache.Airport;
import com.example.demo.cache.AirportCache;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {
    @Autowired
    private AirportCache airportCache;

    @PostConstruct
    public void init() {
        // Populate cache with some initial data
        airportCache.put("JFK", new Airport("JFK", "John F. Kennedy International Airport"));
        airportCache.put("LAX", new Airport("LAX", "Los Angeles International Airport"));
        airportCache.put("SFO", new Airport("SFO", "San Francisco International Airport"));
    }

    public List<Airport> getAllAirports() {
        // In a real application, this data would come from a database
        return List.of(
                new Airport("JFK", "John F. Kennedy International Airport"),
                new Airport("LAX", "Los Angeles International Airport"),
                new Airport("SFO", "San Francisco International Airport"),
                new Airport("ORD", "O'Hare International Airport")
        );
    }

    public Airport getAirportByCode(String code) {
        Airport airport = airportCache.get(code);;
        if (airport == null) {
            System.out.println("Cache miss for airport with code: " + code);
            // Simulate a database lookup
            airport = getAllAirports().stream()
                    .filter(a -> a.getCode().equals(code))
                    .findFirst()
                    .orElse(null);

            if (airport != null) {
                airportCache.put(code, airport);
            }
        }else{
            System.out.println("Cache hit for airport with code: " + code);
        }
        return airport;
    }
}
