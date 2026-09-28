package com.arshad.supply_chain_routing.repository;

import com.arshad.supply_chain_routing.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
