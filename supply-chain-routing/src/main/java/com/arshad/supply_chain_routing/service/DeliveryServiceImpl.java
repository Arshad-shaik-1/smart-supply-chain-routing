package com.arshad.supply_chain_routing.service;

import com.arshad.supply_chain_routing.entity.Delivery;
import com.arshad.supply_chain_routing.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public Delivery addDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Override
    public List<Delivery> getDeliveries() {
        return deliveryRepository.findAll();
    }

    @Override
    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Delivery with id " + id + " not found"));
    }

    @Override
    public Delivery updateDelivery(Long id, Delivery delivery) {

        Delivery existing = getDeliveryById(id);

        existing.setDeliveryAddress(delivery.getDeliveryAddress());
        existing.setWeight(delivery.getWeight());
        existing.setPriority(delivery.getPriority());
        existing.setStatus(delivery.getStatus());
        existing.setDestinationLocation(delivery.getDestinationLocation());

        return deliveryRepository.save(existing);
    }

    @Override
    public void deleteDeliveryById(Long id) {
        deliveryRepository.deleteById(id);
    }
}