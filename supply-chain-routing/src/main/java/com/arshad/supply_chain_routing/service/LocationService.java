package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.entity.Location;

import java.util.List;

public interface LocationService {
    Location addLocation(Location location);

    List<Location> getAllLocations();

    Location getLocationById(long id);

    void deleteLocationById(Long id);
}
