package de.kreutz.michael.carshop.service;

import de.kreutz.michael.carshop.converter.CarToCarDtoConverter;
import de.kreutz.michael.carshop.dto.CarDto;
import de.kreutz.michael.carshop.exception.CarAlreadyExistsException;
import de.kreutz.michael.carshop.model.Car;
import de.kreutz.michael.carshop.repository.CarRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarServiceIT {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarToCarDtoConverter carToCarDtoConverter;

    @Before
    public void setup() {
        carRepository.deleteAll();
    }

    @Test
    public void shouldFetchAllCars() {
        final Car car = new Car();
        final String brand = "brand";
        final String color = "color";
        final OffsetDateTime dateOfPurchase = OffsetDateTime.now();
        final BigDecimal price = new BigDecimal("1.23");
        final boolean isReserved = true;
        final String vehicleIdentificationNumber = "unique vehicle identification number";
        car.setBrand(brand);
        car.setColor(color);
        car.setDateOfPurchase(dateOfPurchase);
        car.setPrice(price);
        car.setReserved(isReserved);
        car.setVehicleIdentificationNumber(vehicleIdentificationNumber);
        carRepository.save(car);

        final List<CarDto> carDtos = carService.fetchCars(null, null, null);

        assertThat(carDtos).hasSize(1);
        final CarDto carDto = carDtos.get(0);
        assertThat(carDto.getBrand()).isEqualTo(brand);
        assertThat(carDto.getColor()).isEqualTo(color);
        assertThat(carDto.getDateOfPurchase()).isEqualTo(dateOfPurchase);
        assertThat(carDto.getPrice()).isEqualTo(price);
        assertThat(carDto.isReserved()).isEqualTo(isReserved);
        assertThat(carDto.getVehicleIdentificationNumber()).isEqualTo(vehicleIdentificationNumber);
    }

    @Test
    public void shouldFetchAllReservedCars() {
        final Car car1 = random(Car.class);
        car1.setReserved(true);
        car1.setPrice(new BigDecimal("4200.50"));
        final Car car2 = random(Car.class);
        car2.setReserved(false);
        carRepository.save(car1);
        carRepository.save(car2);
        assertThat(carRepository.findAll()).hasSize(2);

        final List<CarDto> carDtos = carService.fetchCars(true, null, null);

        assertThat(carDtos).hasSize(1);
        final CarDto carDto = carDtos.get(0);
        assertThat(carDto.getBrand()).isEqualTo(car1.getBrand());
        assertThat(carDto.getColor()).isEqualTo(car1.getColor());
        assertThat(carDto.getPrice()).isEqualTo(car1.getPrice());
        assertThat(carDto.isReserved()).isEqualTo(true);
        assertThat(carDto.getVehicleIdentificationNumber()).isEqualTo(car1.getVehicleIdentificationNumber());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void shouldFailToDeleteCarIfIdDoesNotExist() {
        carService.deleteCar("1243");
    }

    @Test
    public void shouldDeleteCar() {
        final Car car = random(Car.class);
        carRepository.save(car);
        assertThat(carRepository.findAll()).hasSize(1);

        carService.deleteCar(car.getVehicleIdentificationNumber());

        assertThat(carRepository.findAll()).hasSize(0);
    }

    @Test(expected = CarAlreadyExistsException.class)
    public void shouldFailToCreateCarIfIdAlreadyExists() {
        final Car car = random(Car.class);
        carRepository.save(car);
        assertThat(carRepository.findAll()).hasSize(1);

        carService.createCar(carToCarDtoConverter.convert(car));
    }

    @Test
    public void shouldCreateCar() {
        final Car car = random(Car.class);
        assertThat(carRepository.findAll()).hasSize(0);

        carService.createCar(carToCarDtoConverter.convert(car));

        assertThat(carRepository.findAll()).hasSize(1);
        assertThat(carRepository.findById(car.getVehicleIdentificationNumber())).isPresent();
    }
}
