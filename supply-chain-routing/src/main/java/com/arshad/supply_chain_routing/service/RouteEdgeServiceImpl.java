package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.entity.RouteEdge;
import com.arshad.supply_chain_routing.repository.RouteEdgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteEdgeServiceImpl implements RouteEdgeService {

    private final RouteEdgeRepository routeEdgeRepository;

    public RouteEdgeServiceImpl(RouteEdgeRepository routeEdgeRepository) {
        this.routeEdgeRepository = routeEdgeRepository;
    }
    @Override
    public RouteEdge addRouteEdge(RouteEdge routeEdge) {
        return routeEdgeRepository.save(routeEdge);
    }

    @Override
    public List<RouteEdge> getAllRoutes() {
        return routeEdgeRepository.findAll();
    }

    @Override
    public RouteEdge getRouteById(Long id) {
        return routeEdgeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Route edge with id " + id + " not found!")
        );
    }

    @Override
    public void deleteRouteById(Long id) {
        routeEdgeRepository.deleteById(id);
    }
}
