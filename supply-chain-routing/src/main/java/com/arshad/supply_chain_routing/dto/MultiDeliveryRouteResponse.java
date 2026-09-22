package com.arshad.supply_chain_routing.dto;

import java.util.List;

public class MultiDeliveryRouteResponse {

    private Long vehicleId;
    private List<Long> deliveryIds;
    private double totalDistance;
    private List<RouteResponse> routes;

    public MultiDeliveryRouteResponse(
            Long vehicleId,
            List<Long> deliveryIds,
            double totalDistance,
            List<RouteResponse> routes) {

        this.vehicleId = vehicleId;
        this.deliveryIds = deliveryIds;
        this.totalDistance = totalDistance;
        this.routes = routes;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public List<Long> getDeliveryIds() {
        return deliveryIds;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public List<RouteResponse> getRoutes() {
        return routes;
    }
}