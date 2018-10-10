package de.kreutz.michael.carshop.api;

import de.kreutz.michael.carshop.dto.CarDto;
import de.kreutz.michael.carshop.dto.GetCarsResponse;
import de.kreutz.michael.carshop.dto.Metadata;
import de.kreutz.michael.carshop.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/cars")
    public GetCarsResponse getAllCars(
        @RequestParam(value = "reserved", required = false) final Boolean isReserved,
        @RequestParam(value = "min_price", required = false) final BigDecimal minPrice,
        @RequestParam(value = "max_price", required = false) final BigDecimal maxPrice
    ) {
        final List<CarDto> allCars = carService.fetchCars(isReserved, minPrice, maxPrice);
        final BigDecimal totalValue = carService.calcTotalValue(allCars);
        return GetCarsResponse.builder()
            .metadata(Metadata.builder().totalValue(totalValue).build())
            .cars(allCars)
            .build();
    }

    /**
     * This endpoint is called if the car shop sells a car since it then leaves the inventory.
     */
    @DeleteMapping("/cars/{car-id}")
    public void deleteCar(@PathVariable("car-id") final String carId) {
        carService.deleteCar(carId);
    }

    /**
     * This endpoint is called if the car shop buys a car since it then enters the inventory.
     */
    @PostMapping("/cars")
    public void createCar(@Valid @RequestBody final CarDto carDto) {
        carService.createCar(carDto);
    }
}
