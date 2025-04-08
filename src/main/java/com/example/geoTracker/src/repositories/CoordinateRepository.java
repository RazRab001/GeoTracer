package com.example.geoTracker.src.repositories;

import com.example.geoTracker.src.models.RouteCoordinate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordinateRepository extends CrudRepository<RouteCoordinate, Long> {

    @Query(value = "SELECT c.route_id FROM coordinates c " +
            "WHERE ST_DWithin(c.geom, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :distance)",
            nativeQuery = true)
    List<Long> getCoordinateRouteIdsWithinDistance(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distance") double distance);

    @Query("SELECT rc.id FROM RouteCoordinate rc WHERE rc.routeId = :id")
    List<Long> getCoordinateIdsOfRoute(@Param("id") long routeId);

    void deleteCoordinatesByRouteId(long id);
}
