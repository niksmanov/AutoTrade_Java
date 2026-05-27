package com.autotrade.repository;

import com.autotrade.domain.VehicleType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Integer> {
    boolean existsByNameIgnoreCase(String name);

    List<VehicleType> findAllByOrderByNameAsc();
}
