package com.autotrade.service;

import com.autotrade.domain.AppUser;
import com.autotrade.domain.Vehicle;
import com.autotrade.domain.VehicleMake;
import com.autotrade.domain.VehicleModel;
import com.autotrade.dto.SearchVehiclesDto;
import com.autotrade.dto.VehicleDto;
import com.autotrade.dto.VehicleMakeDto;
import com.autotrade.dto.VehicleModelDto;
import com.autotrade.repository.ColorRepository;
import com.autotrade.repository.FuelTypeRepository;
import com.autotrade.repository.GearboxTypeRepository;
import com.autotrade.repository.VehicleMakeRepository;
import com.autotrade.repository.VehicleModelRepository;
import com.autotrade.repository.VehicleRepository;
import com.autotrade.repository.VehicleTypeRepository;
import jakarta.persistence.criteria.JoinType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicles;
    private final VehicleMakeRepository makes;
    private final VehicleModelRepository models;
    private final VehicleTypeRepository vehicleTypes;
    private final ColorRepository colors;
    private final FuelTypeRepository fuelTypes;
    private final GearboxTypeRepository gearboxTypes;
    private final CommonService commonService;
    private final MappingService mapper;

    @Transactional
    public UUID addVehicle(VehicleDto model, AppUser user) {
        var vehicle = new Vehicle();
        vehicle.setUser(user);
        apply(model, vehicle);
        vehicle.setDateCreated(Instant.now());
        vehicle = vehicles.save(vehicle);
        vehicle.getImages().addAll(commonService.createImages(vehicle, model.getUploadImages()));
        return vehicle.getId();
    }

    @Transactional
    public UUID editVehicle(VehicleDto model) {
        var vehicle = vehicles.findById(model.getId()).orElseThrow();
        apply(model, vehicle);
        vehicle.getImages().clear();
        vehicle.getImages().addAll(commonService.createImages(vehicle, model.getUploadImages()));
        return vehicle.getId();
    }

    @Transactional
    public boolean removeVehicle(UUID id) {
        if (!vehicles.existsById(id)) {
            return false;
        }
        vehicles.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<VehicleDto> getVehicles(int page, int size, UUID userId, SearchVehiclesDto search) {
        int pageSize = size <= 0 ? 20 : size;
        Specification<Vehicle> spec = (root, query, cb) -> {
            if (!Long.class.equals(query.getResultType()) && !long.class.equals(query.getResultType())) {
                root.fetch("user", JoinType.LEFT).fetch("town", JoinType.LEFT);
                root.fetch("images", JoinType.LEFT);
                root.fetch("make", JoinType.LEFT);
                root.fetch("model", JoinType.LEFT);
                root.fetch("color", JoinType.LEFT);
                root.fetch("type", JoinType.LEFT);
                root.fetch("fuelType", JoinType.LEFT);
                root.fetch("gearboxType", JoinType.LEFT);
                query.distinct(true);
            }

            var predicate = cb.conjunction();
            if (userId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("user").get("id"), userId));
            }
            if (search == null) {
                return predicate;
            }

            if (search.getTownId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("user").get("town").get("id"), search.getTownId()));
            }
            if (search.getMakeId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("make").get("id"), search.getMakeId()));
            }
            if (search.getModelId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("model").get("id"), search.getModelId()));
            }
            if (search.getColorId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("color").get("id"), search.getColorId()));
            }
            if (search.getTypeId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("type").get("id"), search.getTypeId()));
            }
            if (search.getFuelTypeId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("fuelType").get("id"), search.getFuelTypeId()));
            }
            if (search.getGearboxTypeId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("gearboxType").get("id"), search.getGearboxTypeId()));
            }
            if (search.getAirbags() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("airbags"), search.getAirbags()));
            }
            if (search.getAbs() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("abs"), search.getAbs()));
            }
            if (search.getEsp() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("esp"), search.getEsp()));
            }
            if (search.getCentralLocking() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("centralLocking"), search.getCentralLocking()));
            }
            if (search.getAirConditioning() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("airConditioning"), search.getAirConditioning()));
            }
            if (search.getAutoPilot() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("autoPilot"), search.getAutoPilot()));
            }
            if (search.getFromCubicCapacity() != null) {
                predicate = cb.and(predicate, cb.ge(root.get("cubicCapacity"), search.getFromCubicCapacity()));
            }
            if (search.getToCubicCapacity() != null) {
                predicate = cb.and(predicate, cb.le(root.get("cubicCapacity"), search.getToCubicCapacity()));
            }
            if (search.getFromHorsePower() != null) {
                predicate = cb.and(predicate, cb.ge(root.get("horsePower"), search.getFromHorsePower()));
            }
            if (search.getToHorsePower() != null) {
                predicate = cb.and(predicate, cb.le(root.get("horsePower"), search.getToHorsePower()));
            }
            if (search.getFromPrice() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), search.getFromPrice()));
            }
            if (search.getToPrice() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), search.getToPrice()));
            }
            if (search.getFromProductionDate() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("productionDate"), search.getFromProductionDate()));
            }
            if (search.getToProductionDate() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("productionDate"), search.getToProductionDate()));
            }
            return predicate;
        };

        return vehicles.findAll(spec, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "dateCreated"))).stream()
                .map(mapper::toVehicleDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public VehicleDto getVehicle(UUID id) {
        return vehicles.findById(id).map(mapper::toVehicleDto).orElse(null);
    }

    @Transactional
    public boolean addMake(VehicleMakeDto model) {
        var name = cleanName(model.getName());
        if (!StringUtils.hasText(name) || makes.existsByNameIgnoreCase(name)) {
            return false;
        }

        var make = new VehicleMake();
        make.setName(name);
        makes.save(make);
        return true;
    }

    @Transactional
    public boolean removeMake(Integer id) {
        if (id == null || !makes.existsById(id)) {
            return false;
        }
        makes.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<VehicleMakeDto> getMakes() {
        return makes.findAllByOrderByNameAsc().stream().map(mapper::toMakeDto).toList();
    }

    @Transactional
    public boolean addModel(VehicleModelDto model) {
        var name = cleanName(model.getName());
        if (!StringUtils.hasText(name) || models.existsByNameIgnoreCase(name)) {
            return false;
        }

        var vehicleModel = new VehicleModel();
        vehicleModel.setName(name);
        vehicleModel.setMake(makes.findById(model.getMakeId()).orElseThrow());
        vehicleModel.setVehicleType(vehicleTypes.findById(model.getVehicleTypeId()).orElseThrow());
        models.save(vehicleModel);
        return true;
    }

    @Transactional
    public boolean removeModel(Integer id) {
        if (id == null || !models.existsById(id)) {
            return false;
        }
        models.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<VehicleModelDto> getModels(Integer makeId, Integer vehicleTypeId) {
        if (vehicleTypeId != null && vehicleTypeId > 0) {
            return models.findByMakeIdAndVehicleTypeIdOrderByNameAsc(makeId, vehicleTypeId).stream()
                    .map(mapper::toModelDto)
                    .toList();
        }
        return models.findByMakeIdOrderByNameAsc(makeId).stream().map(mapper::toModelDto).toList();
    }

    private void apply(VehicleDto model, Vehicle vehicle) {
        vehicle.setMake(makes.findById(model.getMakeId()).orElseThrow());
        vehicle.setModel(models.findById(model.getModelId()).orElseThrow());
        vehicle.setColor(colors.findById(model.getColorId()).orElseThrow());
        vehicle.setType(vehicleTypes.findById(model.getTypeId()).orElseThrow());
        vehicle.setFuelType(fuelTypes.findById(model.getFuelTypeId()).orElseThrow());
        vehicle.setGearboxType(gearboxTypes.findById(model.getGearboxTypeId()).orElseThrow());
        vehicle.setHorsePower(model.getHorsePower());
        vehicle.setPrice(model.getPrice());
        vehicle.setCubicCapacity(model.getCubicCapacity());
        vehicle.setAirbags(model.isAirbags());
        vehicle.setAbs(model.isAbs());
        vehicle.setEsp(model.isEsp());
        vehicle.setCentralLocking(model.isCentralLocking());
        vehicle.setAirConditioning(model.isAirConditioning());
        vehicle.setAutoPilot(model.isAutoPilot());
        vehicle.setProductionDate(model.getProductionDate());
    }

    private String cleanName(String value) {
        return value == null ? "" : value.trim();
    }
}
