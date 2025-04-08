package com.example.geoTracker.src.services.interfaces;

import com.example.geoTracker.src.models.Route;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IRouteService {
    CompletableFuture<Optional<Route>> getRouteAsync(long routeId);
    CompletableFuture<Route> addRoute(Route route);
    CompletableFuture<Route> updateRoute(Long id, Route route);
    CompletableFuture<Void> deleteRoute(long routeId);
    CompletableFuture<List<Route>> getUserRoutes(UUID userID);
    CompletableFuture<List<Route>> getAllRoutes();
    CompletableFuture<List<Route>> getRoutesByIds(List<Long> ids);
    CompletableFuture<List<Route>> getRoutesCreatedAtDate(Date createdAt);
    CompletableFuture<List<Route>> getRoutesCreatedAtDateBetween(Date startDate, Date endDate);
}
