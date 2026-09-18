package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle addVehicle(Vehicle vehicle);
    List<Vehicle> getVehicles();
    Vehicle getVehicleById(Long id);
    Vehicle updateVehicle(Long id , Vehicle vehicle);
    void deleteVehicleById(Long id);
}
