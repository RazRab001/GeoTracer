package com.example.geoTracker.utils.dto.route;

import com.example.geoTracker.src.models.Route;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class RouteCreateDto {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Nullable
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private List<RouteCoordinateDto> coordinates;

    public Route toRoute() {
        Route route = new Route();
        route.setTitle(title);
        route.setDescription(description);
        return route;
    }
}
