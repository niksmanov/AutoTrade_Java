package com.autotrade.repository;

import com.autotrade.domain.VehicleModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleModelRepository extends JpaRepository<VehicleModel, Integer> {
    boolean existsByNameIgnoreCase(String name);

    List<VehicleModel> findByMakeIdOrderByNameAsc(Integer makeId);

    List<VehicleModel> findByMakeIdAndVehicleTypeIdOrderByNameAsc(Integer makeId, Integer vehicleTypeId);
}
