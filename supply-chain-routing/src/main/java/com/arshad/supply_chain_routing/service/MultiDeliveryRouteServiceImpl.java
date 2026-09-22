package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.dto.MultiDeliveryRouteRequest;
import com.arshad.supply_chain_routing.dto.MultiDeliveryRouteResponse;
import com.arshad.supply_chain_routing.dto.RouteResponse;
import com.arshad.supply_chain_routing.entity.Delivery;
import com.arshad.supply_chain_routing.entity.Vehicle;
import com.arshad.supply_chain_routing.graph.DijkstraService;
import com.arshad.supply_chain_routing.repository.DeliveryRepository;
import com.arshad.supply_chain_routing.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MultiDeliveryRouteServiceImpl implements MultiDeliveryRouteService {
    private final VehicleRepository vehicleRepository;
    private final DeliveryRepository deliveryRepository;
    private final DijkstraService dijkstraService;

    public MultiDeliveryRouteServiceImpl(VehicleRepository vehicleRepository,DeliveryRepository deliveryRepository, DijkstraService dijkstraService) {
        this.vehicleRepository = vehicleRepository;
        this.deliveryRepository = deliveryRepository;
        this.dijkstraService = dijkstraService;
    }

    @Override
    public MultiDeliveryRouteResponse calculateRoute(MultiDeliveryRouteRequest request) {

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle Not Found"));

        Long currentLocationId = vehicle.getCurrentLocation().getId();

        List<RouteResponse> routes = new ArrayList<>();

        double totalDistance = 0.0;
        for(Long deliveryId : request.getDeliveryIds()) {

            Delivery delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow( () -> new RuntimeException("Delivery not found"));

            Long destinationId = delivery.getDestinationLocation().getId();

            RouteResponse route = dijkstraService.findShortestPath(currentLocationId , destinationId);

            routes.add(route);
//             double distance = (double) route.get("distance");
             totalDistance += route.getDistance();

             currentLocationId = destinationId;

        }


        return new MultiDeliveryRouteResponse(
                request.getVehicleId(),
                request.getDeliveryIds(),
                totalDistance,
                routes
        );
    }
}
