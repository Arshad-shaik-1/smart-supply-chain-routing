package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.dto.DeliveryAssignmentResponse;

public interface DeliveryAssignmentService {
    DeliveryAssignmentResponse assignDelivery(Long deliveryId);
}
