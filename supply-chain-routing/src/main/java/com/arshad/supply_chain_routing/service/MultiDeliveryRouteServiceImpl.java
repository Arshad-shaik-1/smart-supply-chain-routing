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
public class MultiDeliveryRouteServiceImpl
        implements MultiDeliveryRouteService {

    private final VehicleRepository vehicleRepository;
    private final DeliveryRepository deliveryRepository;
    private final DijkstraService dijkstraService;

    public MultiDeliveryRouteServiceImpl(
            VehicleRepository vehicleRepository,
            DeliveryRepository deliveryRepository,
            DijkstraService dijkstraService) {

        this.vehicleRepository = vehicleRepository;
        this.deliveryRepository = deliveryRepository;
        this.dijkstraService = dijkstraService;
    }

    @Override
    public MultiDeliveryRouteResponse calculateRoute(
            MultiDeliveryRouteRequest request) {

        // 1. Find vehicle
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() ->
                        new RuntimeException("Vehicle Not Found"));

        // 2. Check total delivery weight
        double totalWeight = 0.0;

        for (Long deliveryId : request.getDeliveryIds()) {

            Delivery delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow(() ->
                            new RuntimeException("Delivery Not Found"));

            totalWeight += delivery.getWeight();
        }

        // 3. Check vehicle capacity
        if (totalWeight > vehicle.getCapacity()) {

            throw new IllegalArgumentException(
                    "Vehicle Capacity Exceeded. vehicle capacity is : "
                            + vehicle.getCapacity()
                            + ", required capacity : "
                            + totalWeight
            );
        }

        // 4. Starting location of vehicle
        Long currentLocationId =
                vehicle.getCurrentLocation().getId();

        // 5. Keep track of deliveries that still need to be visited
        List<Long> remainingDeliveries =
                new ArrayList<>(request.getDeliveryIds());

        // 6. Store deliveries in the newly calculated order
        List<Long> orderedDeliveryIds =
                new ArrayList<>();

        // 7. Store routes
        List<RouteResponse> routes =
                new ArrayList<>();

        double totalDistance = 0.0;

        // 8. Find the nearest delivery repeatedly
        while (!remainingDeliveries.isEmpty()) {

            Long nearestDeliveryId = null;
            RouteResponse nearestRoute = null;

            double shortestDistance =
                    Double.POSITIVE_INFINITY;

            // Check every remaining delivery
            for (Long deliveryId : remainingDeliveries) {

                Delivery delivery =
                        deliveryRepository.findById(deliveryId)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Delivery Not Found"));

                Long destinationId =
                        delivery.getDestinationLocation().getId();

                try {

                    // Find shortest route from current location
                    // to this delivery
                    RouteResponse route =
                            dijkstraService.findShortestPath(
                                    currentLocationId,
                                    destinationId
                            );

                    // Check whether this is the nearest delivery
                    if (route.getDistance() < shortestDistance) {

                        shortestDistance =
                                route.getDistance();

                        nearestDeliveryId =
                                deliveryId;

                        nearestRoute =
                                route;
                    }

                } catch (IllegalArgumentException e) {

                    // No route to this delivery
                    continue;
                }
            }

            // No reachable delivery remains
            if (nearestDeliveryId == null) {

                throw new IllegalArgumentException(
                        "No reachable delivery found from location "
                                + currentLocationId
                );
            }

            // Add selected delivery to the final order
            orderedDeliveryIds.add(nearestDeliveryId);

            // Add selected route
            routes.add(nearestRoute);

            // Add distance
            totalDistance += nearestRoute.getDistance();

            // Vehicle is now at the selected delivery location
            currentLocationId =
                    nearestRoute.getDestination();

            // Remove delivery from remaining list
            remainingDeliveries.remove(nearestDeliveryId);
        }

        // 9. Return the optimized delivery order
        return new MultiDeliveryRouteResponse(
                request.getVehicleId(),
                orderedDeliveryIds,
                totalDistance,
                routes
        );
    }
}