package com.autotrade.repository;

import com.autotrade.domain.GearboxType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GearboxTypeRepository extends JpaRepository<GearboxType, Integer> {
    boolean existsByNameIgnoreCase(String name);

    List<GearboxType> findAllByOrderByNameAsc();
}
