package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.entity.RouteEdge;
import org.springframework.stereotype.Service;
import org.springframework.util.RouteMatcher;

import java.util.List;

public interface RouteEdgeService {
    RouteEdge addRouteEdge(RouteEdge routeEdge);

    List<RouteEdge> getAllRoutes();

    RouteEdge getRouteById(Long id);

    void deleteRouteById(Long id);
}
