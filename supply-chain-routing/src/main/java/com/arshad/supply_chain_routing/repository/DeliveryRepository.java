package com.arshad.supply_chain_routing.repository;

import com.arshad.supply_chain_routing.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}