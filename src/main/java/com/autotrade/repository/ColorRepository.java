package com.autotrade.repository;

import com.autotrade.domain.Color;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Integer> {
    boolean existsByNameIgnoreCase(String name);

    List<Color> findAllByOrderByNameAsc();
}
