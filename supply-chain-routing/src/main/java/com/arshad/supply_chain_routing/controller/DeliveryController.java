package com.arshad.supply_chain_routing.controller;

import com.arshad.supply_chain_routing.entity.Delivery;
import com.arshad.supply_chain_routing.service.DeliveryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public Delivery addDelivery(@RequestBody Delivery delivery) {
        return deliveryService.addDelivery(delivery);
    }

    @GetMapping
    public List<Delivery> getDeliveries() {
        return deliveryService.getDeliveries();
    }

    @GetMapping("/{id}")
    public Delivery getDeliveryById(@PathVariable Long id) {
        return deliveryService.getDeliveryById(id);
    }

    @PutMapping("/{id}")
    public Delivery updateDelivery(
            @PathVariable Long id,
            @RequestBody Delivery delivery) {

        return deliveryService.updateDelivery(id, delivery);
    }

    @DeleteMapping("/{id}")
    public String deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDeliveryById(id);
        return "Delivery deleted successfully";
    }
}