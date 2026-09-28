package com.arshad.supply_chain_routing.controller;

import com.arshad.supply_chain_routing.dto.DeliveryAssignmentResponse;
import com.arshad.supply_chain_routing.service.DeliveryAssignmentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assignments")
public class DeliveryAssignmentController {

    private final DeliveryAssignmentService deliveryAssignmentService;

    public DeliveryAssignmentController(DeliveryAssignmentService deliveryAssignmentService) {
        this.deliveryAssignmentService = deliveryAssignmentService;
    }

    @PostMapping("/delivery/{deliveryId}")
    public DeliveryAssignmentResponse assignDelivery (@PathVariable("deliveryId") Long deliveryId) {
        return deliveryAssignmentService.assignDelivery(deliveryId);
    }

}
