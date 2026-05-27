package com.autotrade.service;

import com.autotrade.domain.Color;
import com.autotrade.domain.FuelType;
import com.autotrade.domain.GearboxType;
import com.autotrade.domain.Image;
import com.autotrade.domain.Town;
import com.autotrade.domain.Vehicle;
import com.autotrade.domain.VehicleType;
import com.autotrade.dto.AllCommonsDto;
import com.autotrade.dto.CommonDto;
import com.autotrade.dto.ImageDto;
import com.autotrade.repository.ColorRepository;
import com.autotrade.repository.FuelTypeRepository;
import com.autotrade.repository.GearboxTypeRepository;
import com.autotrade.repository.ImageRepository;
import com.autotrade.repository.TownRepository;
import com.autotrade.repository.VehicleRepository;
import com.autotrade.repository.VehicleTypeRepository;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CommonService {
    private static final long MAX_SIZE_IN_BYTES = 1_000_000L;

    private final TownRepository towns;
    private final ColorRepository colors;
    private final VehicleTypeRepository vehicleTypes;
    private final FuelTypeRepository fuelTypes;
    private final GearboxTypeRepository gearboxTypes;
    private final ImageRepository images;
    private final VehicleRepository vehicles;
    private final MappingService mapper;

    @Transactional
    public boolean addTown(CommonDto model) {
        return addNamed(model, towns, towns::existsByNameIgnoreCase, Town::new, Town::setName);
    }

    @Transactional
    public boolean addColor(CommonDto model) {
        return addNamed(model, colors, colors::existsByNameIgnoreCase, Color::new, Color::setName);
    }

    @Transactional
    public boolean addVehicleType(CommonDto model) {
        return addNamed(model, vehicleTypes, vehicleTypes::existsByNameIgnoreCase, VehicleType::new, VehicleType::setName);
    }

    @Transactional
    public boolean addFuelType(CommonDto model) {
        return addNamed(model, fuelTypes, fuelTypes::existsByNameIgnoreCase, FuelType::new, FuelType::setName);
    }

    @Transactional
    public boolean addGearboxType(CommonDto model) {
        return addNamed(model, gearboxTypes, gearboxTypes::existsByNameIgnoreCase, GearboxType::new, GearboxType::setName);
    }

    @Transactional
    public boolean removeTown(Integer id) {
        return remove(towns, id);
    }

    @Transactional
    public boolean removeColor(Integer id) {
        return remove(colors, id);
    }

    @Transactional
    public boolean removeVehicleType(Integer id) {
        return remove(vehicleTypes, id);
    }

    @Transactional
    public boolean removeFuelType(Integer id) {
        return remove(fuelTypes, id);
    }

    @Transactional
    public boolean removeGearboxType(Integer id) {
        return remove(gearboxTypes, id);
    }

    @Transactional(readOnly = true)
    public List<CommonDto> getTowns() {
        return towns.findAllByOrderByNameAsc().stream().map(town -> new CommonDto(town.getId(), town.getName())).toList();
    }

    @Transactional(readOnly = true)
    public List<CommonDto> getColors() {
        return colors.findAllByOrderByNameAsc().stream().map(color -> new CommonDto(color.getId(), color.getName())).toList();
    }

    @Transactional(readOnly = true)
    public List<CommonDto> getVehicleTypes() {
        return vehicleTypes.findAllByOrderByNameAsc().stream().map(type -> new CommonDto(type.getId(), type.getName())).toList();
    }

    @Transactional(readOnly = true)
    public List<CommonDto> getFuelTypes() {
        return fuelTypes.findAllByOrderByNameAsc().stream().map(type -> new CommonDto(type.getId(), type.getName())).toList();
    }

    @Transactional(readOnly = true)
    public List<CommonDto> getGearboxTypes() {
        return gearboxTypes.findAllByOrderByNameAsc().stream().map(type -> new CommonDto(type.getId(), type.getName())).toList();
    }

    @Transactional(readOnly = true)
    public AllCommonsDto getAllCommons() {
        var commons = new AllCommonsDto();
        commons.setColors(getColors());
        commons.setTowns(getTowns());
        commons.setVehicleTypes(getVehicleTypes());
        commons.setFuelTypes(getFuelTypes());
        commons.setGearboxTypes(getGearboxTypes());
        return commons;
    }

    @Transactional(readOnly = true)
    public List<ImageDto> getImages(UUID vehicleId) {
        return images.findByVehicleId(vehicleId).stream().map(mapper::toImageDto).toList();
    }

    public List<Image> createImages(Vehicle vehicle, List<MultipartFile> uploadImages) {
        return uploadImages.stream()
                .filter(file -> "image/jpeg".equalsIgnoreCase(file.getContentType()) || "image/png".equalsIgnoreCase(file.getContentType()))
                .limit(10)
                .map(file -> createImage(vehicle, file))
                .toList();
    }

    private Image createImage(Vehicle vehicle, MultipartFile file) {
        var image = new Image();
        image.setVehicle(vehicle);
        image.setName(UUID.randomUUID() + ".jpg");
        image.setData(toJpegBytes(file));
        return image;
    }

    private byte[] toJpegBytes(MultipartFile file) {
        try {
            var source = ImageIO.read(file.getInputStream());
            if (source == null) {
                return file.getBytes();
            }

            float quality = Math.min(0.90f, Math.max(0.10f, (float) MAX_SIZE_IN_BYTES / Math.max(file.getSize(), 1L)));
            return writeJpeg(source, quality);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not process uploaded image.", ex);
        }
    }

    private byte[] writeJpeg(BufferedImage source, float quality) throws IOException {
        var rgb = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        rgb.getGraphics().drawImage(source, 0, 0, null);

        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        var params = writer.getDefaultWriteParam();
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(quality);

        try (var output = new ByteArrayOutputStream();
             var imageOutput = new MemoryCacheImageOutputStream(output)) {
            writer.setOutput(imageOutput);
            writer.write(null, new IIOImage(rgb, null, null), params);
            return output.toByteArray();
        } finally {
            writer.dispose();
        }
    }

    private <T> boolean addNamed(CommonDto model,
                                 JpaRepository<T, Integer> repository,
                                 Function<String, Boolean> exists,
                                 Supplier<T> factory,
                                 BiConsumer<T, String> setName) {
        var name = model.getName() == null ? "" : model.getName().trim();
        if (name.isEmpty() || exists.apply(name)) {
            return false;
        }

        var entity = factory.get();
        setName.accept(entity, name);
        repository.save(entity);
        return true;
    }

    private boolean remove(JpaRepository<?, Integer> repository, Integer id) {
        if (id == null || !repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean addImages(UUID vehicleId, List<MultipartFile> uploadImages) {
        Vehicle vehicle = vehicles.findById(vehicleId).orElseThrow();
        vehicle.getImages().addAll(createImages(vehicle, uploadImages));
        return true;
    }
}
