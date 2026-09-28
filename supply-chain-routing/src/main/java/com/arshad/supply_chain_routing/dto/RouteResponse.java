package com.arshad.supply_chain_routing.dto;

import java.util.List;

public class RouteResponse {
    private Long source , destination;
    private double distance;
    private List<Long> path;

    public RouteResponse(Long source, Long destination, double distance, List<Long> path) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.path = path;
    }

    public Long getDestination() {
        return destination;
    }

    public Long getSource() {
        return source;
    }

    public double getDistance() {
        return distance;
    }

    public List<Long> getPath() {
        return path;
    }
}
