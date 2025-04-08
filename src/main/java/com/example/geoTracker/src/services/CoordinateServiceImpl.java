package com.example.geoTracker.src.services;

import com.example.geoTracker.src.models.RouteCoordinate;
import com.example.geoTracker.src.repositories.CoordinateRepository;
import com.example.geoTracker.src.services.interfaces.ICoordinateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CoordinateServiceImpl implements ICoordinateService {

    private final CoordinateRepository coordinateRepository;

    public CoordinateServiceImpl(CoordinateRepository coordinateRepository) {
        this.coordinateRepository = coordinateRepository;
    }

    @Override
    public CompletableFuture<List<Long>> getRouteIdsAroundCoordinates(double latitude, double longitude, double distance) {
        return CompletableFuture.supplyAsync(() ->
                coordinateRepository.getCoordinateRouteIdsWithinDistance(latitude, longitude, distance)
        );
    }

    @Override
    public CompletableFuture<Void> saveCoordinates(List<RouteCoordinate> routeCoordinates) {
        return CompletableFuture.runAsync(() ->
                coordinateRepository.saveAll(routeCoordinates)
        );
    }

    @Override
    public CompletableFuture<List<Long>> getCoordinateIdsOfRoute(Long routeId) {
        return CompletableFuture.supplyAsync(() ->
                coordinateRepository.getCoordinateIdsOfRoute(routeId)
        );
    }

    @Override
    public CompletableFuture<Void> deleteCoordinatesByRouteId(Long routeId) {
        return CompletableFuture.runAsync(() ->
                coordinateRepository.deleteCoordinatesByRouteId(routeId)
        );
    }
}
