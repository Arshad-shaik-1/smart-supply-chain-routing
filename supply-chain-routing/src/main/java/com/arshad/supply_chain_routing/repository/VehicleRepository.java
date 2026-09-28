package com.arshad.supply_chain_routing.repository;

import com.arshad.supply_chain_routing.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
