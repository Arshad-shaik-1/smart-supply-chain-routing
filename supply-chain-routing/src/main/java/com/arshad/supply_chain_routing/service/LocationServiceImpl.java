package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.entity.Location;
import com.arshad.supply_chain_routing.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LocationServiceImpl implements LocationService{

    private LocationRepository localRepo;
    public LocationServiceImpl(LocationRepository localRepo){
        this.localRepo = localRepo;
    }

    @Override
    public Location addLocation(Location location){
        return localRepo.save(location);
    }

    @Override
    public List<Location> getAllLocations() {
        return localRepo.findAll();
    }

    @Override
    public Location getLocationById(long id) {
        return localRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteLocationById(Long id) {
        localRepo.deleteById(id);
    }
}
