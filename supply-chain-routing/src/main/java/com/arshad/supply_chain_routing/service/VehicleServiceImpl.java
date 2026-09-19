package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.entity.Vehicle;
import com.arshad.supply_chain_routing.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).
                orElseThrow(
                        () -> new RuntimeException("Vehicle with id " + id + " not found")
                );
    }

    @Override
    public Vehicle updateVehicle(Long id , Vehicle vehicle) {
        Vehicle existing = getVehicleById(id);

        existing.setVehicleNo(vehicle.getVehicleNo());
        existing.setCapacity(vehicle.getCapacity());
        existing.setCurrentLocation(vehicle.getCurrentLocation());
        existing.setStatus(vehicle.getStatus());

        return vehicleRepository.save(existing);
    }

    @Override
    public void deleteVehicleById(Long id) {
        vehicleRepository.deleteById(id);
    }
}
