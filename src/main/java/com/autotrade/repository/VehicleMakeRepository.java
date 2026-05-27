package com.autotrade.repository;

import com.autotrade.domain.VehicleMake;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleMakeRepository extends JpaRepository<VehicleMake, Integer> {
    boolean existsByNameIgnoreCase(String name);

    List<VehicleMake> findAllByOrderByNameAsc();
}
