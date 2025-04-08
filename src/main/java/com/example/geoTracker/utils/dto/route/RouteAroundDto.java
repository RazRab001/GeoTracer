package com.example.geoTracker.utils.dto.route;

import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
public class RouteAroundDto {
    @Range(min = -90, max = 90, message = "Latitude must be between -90 and 90")
    private double latitude;

    @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180")
    private double longitude;

    private float distance = 1.0f;
}
