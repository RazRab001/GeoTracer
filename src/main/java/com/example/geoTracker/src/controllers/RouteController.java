package com.example.geoTracker.src.controllers;

import com.example.geoTracker.src.models.Route;
import com.example.geoTracker.src.models.RouteCoordinate;
import com.example.geoTracker.src.services.interfaces.ICoordinateService;
import com.example.geoTracker.src.services.interfaces.IRouteService;
import com.example.geoTracker.utils.dto.route.RouteAroundDto;
import com.example.geoTracker.utils.dto.route.RouteCreateDto;
import com.example.geoTracker.utils.dto.route.RouteGetDto;
import com.example.geoTracker.utils.dto.route.RouteUpdateDto;
import jakarta.validation.Valid;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/route")
public class RouteController {

    private final IRouteService routeService;
    private final ICoordinateService coordinateService;

    public RouteController(IRouteService routeService, ICoordinateService coordinateService) {
        this.routeService = routeService;
        this.coordinateService = coordinateService;
    }

    @PostMapping
    public DeferredResult<RouteGetDto> createRoute(@Valid @RequestBody RouteCreateDto routeDto) {
        DeferredResult<RouteGetDto> deferredResult = new DeferredResult<>();

        GeometryFactory geometryFactory = new GeometryFactory();
        CompletableFuture<Route> futureRoute = routeService.addRoute(routeDto.toRoute());

        futureRoute.thenCompose(route -> {
            List<RouteCoordinate> coordinates = routeDto.getCoordinates().stream()
                    .map(coordDto -> coordDto.toCoordinate(geometryFactory, route.getId()))
                    .collect(Collectors.toList());

            return coordinateService.saveCoordinates(coordinates)
                    .thenApply(saved -> {
                        route.setRouteCoordinates(coordinates);
                        return new RouteGetDto(route);
                    });
        }).whenComplete((result, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(new RuntimeException("Failed to process route or coordinates", throwable));
            } else {
                deferredResult.setResult(result);
            }
        });

        return deferredResult;
    }

    @PutMapping("/{id:long}")
    public DeferredResult<RouteGetDto> updateRoute(@PathVariable Long id, @Valid @RequestBody RouteUpdateDto updateDto) {
        DeferredResult<RouteGetDto> deferredResult = new DeferredResult<>();

        GeometryFactory geometryFactory = new GeometryFactory();
        CompletableFuture<Route> futureRoute = routeService.updateRoute(id, updateDto.toRoute());

        futureRoute.thenCompose(route -> {
            if (updateDto.getCoordinates() == null) {
                return CompletableFuture.completedFuture(null); // Возвращаем пустой CompletableFuture
            }

            coordinateService.deleteCoordinatesByRouteId(route.getId());

            List<RouteCoordinate> coordinates = updateDto.getCoordinates().stream()
                    .map(coordDto -> coordDto.toCoordinate(geometryFactory, route.getId()))
                    .collect(Collectors.toList());

            return coordinateService.saveCoordinates(coordinates)
                    .thenApply(saved -> {
                        route.setRouteCoordinates(coordinates);
                        return new RouteGetDto(route);
                    });
        }).whenComplete((result, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(new RuntimeException("Failed to process route or coordinates", throwable));
            } else {
                deferredResult.setResult(result);
            }
        });
        return deferredResult;
    }

    @DeleteMapping("/{id:long}")
    public DeferredResult<Void> deleteRoute(@PathVariable Long id){
        DeferredResult<Void> deferredResult = new DeferredResult<>();
        CompletableFuture<Void> routeFuture = routeService.deleteRoute(id);

        routeFuture.whenComplete((result, throwable) -> {
            try{
                deferredResult.setResult(result);
            } catch (Exception e) {
                deferredResult.setErrorResult(new RuntimeException("Failed to get routes", e));
            }
        });
        return deferredResult;
    }

    @GetMapping("/user/{userId:UUID}")
    public DeferredResult<List<RouteGetDto>> getRoutes(@PathVariable UUID userId) {
        DeferredResult<List<RouteGetDto>> deferredListResult = new DeferredResult<>();

        CompletableFuture<List<Route>> routesFuture = routeService.getUserRoutes(userId);

        routesFuture.whenComplete((routes, throwable) -> {
            if (throwable != null) {
                deferredListResult.setErrorResult(new RuntimeException("Failed to get routes", throwable));
            } else {
                try{
                    deferredListResult.setResult(routes.stream().map(RouteGetDto::new).collect(Collectors.toList()));
                } catch (Exception e) {
                    deferredListResult.setErrorResult(new RuntimeException("Failed to get routes", e));
                }
            }
        });

        return deferredListResult;
    }

    @GetMapping()
    public DeferredResult<List<RouteGetDto>> getAllRoutes() {
        DeferredResult<List<RouteGetDto>> deferredListResult = new DeferredResult<>();

        CompletableFuture<List<Route>> routesFuture = routeService.getAllRoutes();
        routesFuture.whenComplete((routes, throwable) -> {
            if (throwable != null) {
                deferredListResult.setErrorResult(new RuntimeException("Failed to get routes", throwable));
            } else {
                try{
                    deferredListResult.setResult(routes.stream().map(RouteGetDto::new).collect(Collectors.toList()));
                } catch (Exception e) {
                    deferredListResult.setErrorResult(new RuntimeException("Failed to get routes", e));
                }
            }
        });

        return deferredListResult;
    }

    @GetMapping("/around")
    public DeferredResult<List<RouteGetDto>> getRoutesAround(@Valid @RequestBody RouteAroundDto aroundDto) {
        DeferredResult<List<RouteGetDto>> deferredListResult = new DeferredResult<>();

        CompletableFuture<List<Long>> routeIdsFuture =
                coordinateService.getRouteIdsAroundCoordinates(aroundDto.getLatitude(), aroundDto.getLongitude(), aroundDto.getDistance());

        routeIdsFuture.thenCompose(routeService::getRoutesByIds)
                .whenComplete((routes, throwable) -> {
            if (throwable != null) {
                deferredListResult.setErrorResult(new RuntimeException("Failed to get routes", throwable));
            } else {
                try{
                    deferredListResult.setResult(routes.stream().map(RouteGetDto::new).collect(Collectors.toList()));
                } catch (Exception e) {
                    deferredListResult.setErrorResult(new RuntimeException("Failed to get routes", e));
                }
            }
        });

        return deferredListResult;
    }

    @GetMapping("/{id}")
    public DeferredResult<RouteGetDto> getRouteById(@PathVariable Long id) {
        DeferredResult<RouteGetDto> deferredResult = new DeferredResult<>();

        CompletableFuture<Optional<Route>> routeFuture = routeService.getRouteAsync(id);

        routeFuture.whenComplete((route, throwable) -> {
            if (throwable != null || route.isEmpty()) {
                deferredResult.setErrorResult(new RuntimeException("Failed to get route", throwable));
            } else {
                if (throwable != null) {
                    deferredResult.setErrorResult(new RuntimeException("Failed to process route or coordinates", throwable));
                } else {
                    deferredResult.setResult(new RouteGetDto(route.get()));
                }
            }
        });

        return deferredResult;
    }

    @GetMapping("/date")
    public DeferredResult<List<RouteGetDto>> getRoutesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date lastDate) {

        DeferredResult<List<RouteGetDto>> deferredListResult = new DeferredResult<>();

        CompletableFuture<List<Route>> routesFuture = lastDate != null
                ? routeService.getRoutesCreatedAtDateBetween(date, lastDate)
                : routeService.getRoutesCreatedAtDate(date);

        routesFuture.whenComplete((routes, throwable) -> {
            if (throwable != null) {
                deferredListResult.setErrorResult(new RuntimeException("Failed to get routes", throwable));
            } else {
                try {
                    deferredListResult.setResult(routes.stream()
                            .map(RouteGetDto::new)
                            .collect(Collectors.toList()));
                } catch (Exception e) {
                    deferredListResult.setErrorResult(new RuntimeException("Failed to get routes", e));
                }
            }
        });

        return deferredListResult;
    }
}
