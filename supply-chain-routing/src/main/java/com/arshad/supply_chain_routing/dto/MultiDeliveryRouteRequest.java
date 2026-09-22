package com.arshad.supply_chain_routing.dto;

import java.util.List;

public class MultiDeliveryRouteRequest {
    private Long vehicleId;
    private List<Long> deliveryIds;

    public MultiDeliveryRouteRequest(List<Long> deliveryIds, Long vehicleId) {
        this.deliveryIds = deliveryIds;
        this.vehicleId = vehicleId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public List<Long> getDeliveryIds() {
        return deliveryIds;
    }
}
