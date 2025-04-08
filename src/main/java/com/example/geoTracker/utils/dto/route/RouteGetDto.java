package com.example.geoTracker.utils.dto.route;

import com.example.geoTracker.src.models.Route;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
public class RouteGetDto {
    private Long id;
    private String title;
    private String description;
    private List<RouteCoordinateDto> coordinates;
    private Date createdAt;

    public RouteGetDto(Route route) {
        id = route.getId();
        title = route.getTitle();
        description = route.getDescription();
        coordinates = route.getRouteCoordinates().stream()
                .map(routeCoordinate ->
                        new RouteCoordinateDto(routeCoordinate.getGeom())).toList();
        createdAt = route.getCreatedAt();
    }
}
