package de.kreutz.michael.carshop.service;

import de.kreutz.michael.carshop.converter.CarDtoToCarConverter;
import de.kreutz.michael.carshop.converter.CarToCarDtoConverter;
import de.kreutz.michael.carshop.dto.CarDto;
import de.kreutz.michael.carshop.exception.CarAlreadyExistsException;
import de.kreutz.michael.carshop.model.Car;
import de.kreutz.michael.carshop.repository.CarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import java.math.BigDecimal;
import java.util.List;
import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarToCarDtoConverter carToCarDtoConverter;

    @Mock
    private CarDtoToCarConverter carDtoToCarConverter;

    @InjectMocks
    private CarService carService;

    @Test
    public void shouldFetchCars() {
        final Car car1 = random(Car.class);
        car1.setReserved(true);
        final Car car2 = random(Car.class);
        car2.setReserved(false);
        final List<Car> cars = asList(car1, car2);
        when(carRepository.findAll()).thenReturn(cars);

        final List<CarDto> foundUnfilteredCarDtos = carService.fetchCars(null, null, null);
        final List<CarDto> foundReservedCarDtos = carService.fetchCars(true, null, null);

        assertThat(foundUnfilteredCarDtos).hasSize(cars.size());
        assertThat(foundReservedCarDtos).hasSize(1);
    }

    @Test
    public void shouldDeleteCar() {
        final String carId = "car id";

        carService.deleteCar(carId);

        verify(carRepository).deleteById(carId);
        verify(carDtoToCarConverter, never()).convert(any());
        verify(carToCarDtoConverter, never()).convert(any());
    }

    @Test
    public void shouldCreateCar() {
        final CarDto carDto = random(CarDto.class);
        when(carRepository.existsById(any())).thenReturn(false);

        carService.createCar(carDto);

        verify(carRepository).save(any());
        verify(carRepository).existsById(any());
        verify(carDtoToCarConverter).convert(carDto);
        verify(carToCarDtoConverter, never()).convert(any());
    }

    @Test(expected = CarAlreadyExistsException.class)
    public void shouldNotCreateCarBecauseIdAlreadyExists() {
        final CarDto carDto = random(CarDto.class);
        when(carRepository.existsById(any())).thenReturn(true);

        carService.createCar(carDto);

        verify(carRepository,never()).save(any());
        verify(carRepository).existsById(any());
        verify(carDtoToCarConverter).convert(carDto);
        verify(carToCarDtoConverter, never()).convert(any());
    }

    @Test
    public void shouldCalcTotalValueForZeroPrice() {
        final CarDto carDto = random(CarDto.class).toBuilder()
            .price(null)
            .build();
        final List<CarDto> carDtos = singletonList(carDto);

        final BigDecimal totalValue = carService.calcTotalValue(carDtos);

        assertThat(totalValue).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void shouldCalcTotalValue() {
        final CarDto carDto1 = random(CarDto.class);
        final CarDto carDto2 = random(CarDto.class);
        final List<CarDto> carDtos = asList(carDto1, carDto2);

        final BigDecimal totalValue = carService.calcTotalValue(carDtos);

        assertThat(totalValue).isEqualTo(carDto1.getPrice().add(carDto2.getPrice()));
    }
}
