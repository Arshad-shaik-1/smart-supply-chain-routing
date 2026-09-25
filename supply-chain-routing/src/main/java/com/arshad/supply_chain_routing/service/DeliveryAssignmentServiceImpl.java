package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.dto.DeliveryAssignmentResponse;
import com.arshad.supply_chain_routing.dto.RouteResponse;
import com.arshad.supply_chain_routing.entity.Delivery;
import com.arshad.supply_chain_routing.entity.DeliveryStatus;
import com.arshad.supply_chain_routing.entity.Vehicle;
import com.arshad.supply_chain_routing.entity.VehicleStatus;
import com.arshad.supply_chain_routing.graph.DijkstraService;
import com.arshad.supply_chain_routing.repository.DeliveryRepository;
import com.arshad.supply_chain_routing.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryAssignmentServiceImpl implements DeliveryAssignmentService{

    private final DeliveryRepository deliveryRepository;
    private final VehicleRepository vehicleRepository;
    private final DijkstraService dijkstraService;

    public DeliveryAssignmentServiceImpl(DeliveryRepository deliveryRepository,
                                         VehicleRepository vehicleRepository,
                                         DijkstraService dijkstraService) {
        this.deliveryRepository = deliveryRepository;
        this.vehicleRepository = vehicleRepository;
        this.dijkstraService = dijkstraService;
    }

    @Override
    public DeliveryAssignmentResponse assignDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).
                orElseThrow(
                        () -> new RuntimeException("Delivery Not Found")
                );
        List<Vehicle> vehicles = vehicleRepository.findAll();

        Vehicle bestVehicle = null;
        double shortestDistance = Double.POSITIVE_INFINITY;

        for(Vehicle v : vehicles){
            if(v.getStatus() != VehicleStatus.AVAILABLE){
                continue;
            }
            if(v.getCapacity() < delivery.getWeight()){
                continue;
            }

            Long vehicleLocation = v.getCurrentLocation().getId();
            Long deliverLocation = delivery.getDestinationLocation().getId();

            try{
                RouteResponse route = dijkstraService.findShortestPath(vehicleLocation, deliverLocation);

                if(route.getDistance() < shortestDistance){
                    shortestDistance = route.getDistance();
                    bestVehicle = v;
                }
            } catch(IllegalArgumentException e){
                continue;
            }
        }
        if(bestVehicle == null){
            throw new IllegalArgumentException("No suitable vehicle found for delivery "
            +deliveryId);
        }

        delivery.setVehicle(bestVehicle);
        delivery.setStatus(DeliveryStatus.ASSIGNED);
        deliveryRepository.save(delivery);

        return new DeliveryAssignmentResponse(
                delivery.getId(),
                bestVehicle.getId(),
                shortestDistance
        );
    }
}
