package com.arshad.supply_chain_routing.controller;

import com.arshad.supply_chain_routing.dto.MultiDeliveryRouteRequest;
import com.arshad.supply_chain_routing.dto.MultiDeliveryRouteResponse;
import com.arshad.supply_chain_routing.service.MultiDeliveryRouteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "http://localhost:5173")
public class MultiDeliveryRouteController {
    private final MultiDeliveryRouteService multiDeliveryRouteService;

    public MultiDeliveryRouteController(MultiDeliveryRouteService multiDeliveryRouteService) {
        this.multiDeliveryRouteService = multiDeliveryRouteService;
    }

    @PostMapping("/multi-delivery")
    public MultiDeliveryRouteResponse calculateRoute(@RequestBody MultiDeliveryRouteRequest multiDeliveryRouteRequest) {
        return  multiDeliveryRouteService.calculateRoute(multiDeliveryRouteRequest);
    }
}
