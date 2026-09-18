package com.arshad.supply_chain_routing.controller;

import com.arshad.supply_chain_routing.entity.Location;
import com.arshad.supply_chain_routing.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    public final LocationService locationService;
    public LocationController(LocationService locationService){
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody Location location){
        return ResponseEntity.ok(locationService.addLocation(location));
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations(){
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id){
        Location location = locationService.getLocationById(id);
        if(location == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(location);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocationById(@PathVariable Long id ){
        locationService.deleteLocationById(id);
        return ResponseEntity.noContent().build();
    }
//    @GetMapping("/test")
//    public String text(){
//        return "LocationController";
//    }

}
