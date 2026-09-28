package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.dto.MultiDeliveryRouteRequest;
import com.arshad.supply_chain_routing.dto.MultiDeliveryRouteResponse;
import com.arshad.supply_chain_routing.dto.RouteResponse;

import java.util.Map;


public interface MultiDeliveryRouteService {
    MultiDeliveryRouteResponse calculateRoute(MultiDeliveryRouteRequest request);
}
