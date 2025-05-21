package com.ctc.garage_manager.domain.repository;

import com.ctc.garage_manager.domain.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

}
