package com.arshad.supply_chain_routing.dto;

public class DeliveryAssignmentResponse {
    private Long deliveryId , vehicleId;
    private double distance;

    public DeliveryAssignmentResponse(Long deliveryId, Long vehicleId, double distance) {
        this.deliveryId = deliveryId;
        this.vehicleId = vehicleId;
        this.distance = distance;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public double getDistance() {
        return distance;
    }
}
