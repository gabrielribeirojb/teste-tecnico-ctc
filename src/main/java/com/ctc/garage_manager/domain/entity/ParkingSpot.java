package com.ctc.garage_manager.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "parking_spots")
public class ParkingSpot {

    @Id
    private Long id;

    @EqualsAndHashCode.Include
    private Double lat;

    @EqualsAndHashCode.Include
    private Double lng;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;
}
