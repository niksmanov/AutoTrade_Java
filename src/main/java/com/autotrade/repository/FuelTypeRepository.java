package com.autotrade.repository;

import com.autotrade.domain.FuelType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuelTypeRepository extends JpaRepository<FuelType, Integer> {
    boolean existsByNameIgnoreCase(String name);

    List<FuelType> findAllByOrderByNameAsc();
}
