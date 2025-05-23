package com.ctc.garage_manager.domain.repository;

import com.ctc.garage_manager.domain.entity.Sector;
import com.ctc.garage_manager.domain.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    long countByParkingSpotSector(Sector sector);
}
