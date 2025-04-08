package com.example.geoTracker.src.repositories;

import com.example.geoTracker.src.models.Route;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {
    Optional<Route> getRouteById(Long id);

    void removeById(Long id);

    List<Route> getRoutesByOwner_Id(UUID userID);

    List<Route> getRoutesByCreatedAt(Date createdAt);

    List<Route> getRoutesByCreatedAtBetween(Date from, Date to);

    @Query("SELECT r FROM Route r where r.id in :routeIds")
    List<Route> getRoutesByIds( @Param("routeIds") List<Long> ids);
}
