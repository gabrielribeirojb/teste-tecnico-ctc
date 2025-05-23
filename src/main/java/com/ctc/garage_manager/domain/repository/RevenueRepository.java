package com.ctc.garage_manager.domain.repository;

import com.ctc.garage_manager.domain.entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    Optional<Revenue> findBySectorAndTimestampBetween(String sector, LocalDate startOfDay, LocalDate endOfDay);
}
