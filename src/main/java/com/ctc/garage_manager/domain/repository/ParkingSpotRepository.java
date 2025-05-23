package com.ctc.garage_manager.domain.repository;

import com.ctc.garage_manager.domain.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    Optional<ParkingSpot> findByLatAndLng(Double lat, Double lng);
}
