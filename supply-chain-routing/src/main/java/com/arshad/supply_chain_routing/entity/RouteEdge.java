package com.arshad.supply_chain_routing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "route_edges")
public class RouteEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_location_id")
    private Location sourceLocation;

    @ManyToOne
    @JoinColumn(name = "destination_location_id")
    private Location destinationLocation;

    private double distance;

    public RouteEdge() {
    }

    public RouteEdge(Long id, Location sourceLocation, Location destinationLocation, double distance) {
        this.id = id;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(Location sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
