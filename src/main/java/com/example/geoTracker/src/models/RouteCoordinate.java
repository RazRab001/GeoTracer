package com.example.geoTracker.src.models;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "coordinates")

public class RouteCoordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "GEOMETRY(Point, 4326)")
    private Point geom;

    @Column(name = "route_id")
    private Long routeId;
}
