package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.entity.Delivery;

import java.util.List;

public interface DeliveryService {

    Delivery addDelivery(Delivery delivery);

    List<Delivery> getDeliveries();

    Delivery getDeliveryById(Long id);

    Delivery updateDelivery(Long id, Delivery delivery);

    void deleteDeliveryById(Long id);
}