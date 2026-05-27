package com.autotrade.repository;

import com.autotrade.domain.Image;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByVehicleId(UUID vehicleId);
}
