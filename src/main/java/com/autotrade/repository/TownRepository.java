package com.autotrade.repository;

import com.autotrade.domain.Town;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepository extends JpaRepository<Town, Integer> {
    boolean existsByNameIgnoreCase(String name);

    List<Town> findAllByOrderByNameAsc();
}
