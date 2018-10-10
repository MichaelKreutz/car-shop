package de.kreutz.michael.carshop.service;

import de.kreutz.michael.carshop.converter.CarDtoToCarConverter;
import de.kreutz.michael.carshop.converter.CarToCarDtoConverter;
import de.kreutz.michael.carshop.dto.CarDto;
import de.kreutz.michael.carshop.exception.CarAlreadyExistsException;
import de.kreutz.michael.carshop.model.Car;
import de.kreutz.michael.carshop.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarService {

    private final CarRepository carRepository;
    private final CarToCarDtoConverter carToCarDtoConverter;
    private final CarDtoToCarConverter carDtoToCarConverter;

    public List<CarDto> fetchCars(final Boolean isReserved, final BigDecimal minPrice, final BigDecimal maxPrice) {
        log.info("Fetch all cars from inventory");
        final List<CarDto> allCars = carRepository.findAll().stream()
            .filter(car -> isReserved == null || isReserved.equals(car.isReserved()))
            .filter(car -> minPrice == null || minPrice.compareTo(car.getPrice()) <= 0)
            .filter(car -> maxPrice == null || maxPrice.compareTo(car.getPrice()) >= 0)
            .map(carToCarDtoConverter::convert)
            .collect(toList());
        log.debug("Fetched [{}] cars from inventory after filtering", allCars.size());
        return allCars;
    }

    public void deleteCar(final String carId) {
        log.info("Delete car with id [{}]", carId);
        carRepository.deleteById(carId);
    }

    public void createCar(final CarDto carDto) {
        final String vehicleIdentificationNumber = carDto.getVehicleIdentificationNumber();
        log.info("Create car with id [{}]", vehicleIdentificationNumber);
        final Car car = carDtoToCarConverter.convert(carDto);
        if (carRepository.existsById(vehicleIdentificationNumber)) {
            throw new CarAlreadyExistsException(format("Car with id [%s] already exists.", vehicleIdentificationNumber));
        }
        carRepository.save(car);
    }

    public BigDecimal calcTotalValue(final List<CarDto> cars) {
        log.info("Calculating total value for cars [{}]", cars);
        return cars.stream()
            .map(CarDto::getPrice)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
