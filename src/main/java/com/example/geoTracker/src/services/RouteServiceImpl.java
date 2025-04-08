package com.example.geoTracker.src.services;

import com.example.geoTracker.src.models.Route;
import com.example.geoTracker.src.repositories.RouteRepository;
import com.example.geoTracker.src.services.interfaces.IRouteService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class RouteServiceImpl implements IRouteService {

    private final RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    @Async
    public CompletableFuture<Optional<Route>> getRouteAsync(long routeId) {
        return CompletableFuture.supplyAsync(() -> routeRepository.getRouteById(routeId))
                .exceptionally(ex -> {
                    System.err.println("Error fetching route: " + ex.getMessage());
                    return Optional.empty();
                });
    }

    @Override
    @Async
    public CompletableFuture<Route> addRoute(Route route) {
        return CompletableFuture.supplyAsync(() -> routeRepository.save(route));
    }

    @Override
    @Async
    public CompletableFuture<Route> updateRoute(Long id, Route route) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Route> currentRoute = getRouteAsync(id).join();
            if (currentRoute.isPresent()) {
                throw new IllegalArgumentException("Route not found");
            }
            route.setId(id);
            return routeRepository.save(route);
        });
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteRoute(long routeId) {
        return CompletableFuture.runAsync(() -> routeRepository.removeById(routeId));
    }

    @Override
    @Async
    public CompletableFuture<List<Route>> getUserRoutes(UUID userID) {
        return CompletableFuture.supplyAsync(() ->
                routeRepository.getRoutesByOwner_Id(userID)
        );
    }

    @Override
    @Async
    public CompletableFuture<List<Route>> getAllRoutes() {
        return CompletableFuture.supplyAsync(() ->
                (List<Route>) routeRepository.findAll()
        );
    }

    @Override
    @Async
    public CompletableFuture<List<Route>> getRoutesByIds(List<Long> ids) {
        return CompletableFuture.supplyAsync(() -> {
            if (ids.isEmpty()) {
                return Collections.emptyList();
            }
            return routeRepository.getRoutesByIds(ids);

        });
    }

    @Override
    @Async
    public CompletableFuture<List<Route>> getRoutesCreatedAtDate(Date createdAt) {
        return CompletableFuture.supplyAsync(() ->
                routeRepository.getRoutesByCreatedAt(createdAt)
        );
    }

    @Override
    @Async
    public CompletableFuture<List<Route>> getRoutesCreatedAtDateBetween(Date startDate, Date endDate) {
        return CompletableFuture.supplyAsync(() ->
                routeRepository.getRoutesByCreatedAtBetween(startDate, endDate)
        );
    }
}
