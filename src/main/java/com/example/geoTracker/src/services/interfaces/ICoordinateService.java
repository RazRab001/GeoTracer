package com.example.geoTracker.src.services.interfaces;

import com.example.geoTracker.src.models.RouteCoordinate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ICoordinateService {
    CompletableFuture<List<Long>> getRouteIdsAroundCoordinates(double latitude, double longitude, double distance);
    CompletableFuture<Void> saveCoordinates(List<RouteCoordinate> routeCoordinates);
    CompletableFuture<List<Long>> getCoordinateIdsOfRoute(Long routeId);
    CompletableFuture<Void> deleteCoordinatesByRouteId(Long routeId);
}
