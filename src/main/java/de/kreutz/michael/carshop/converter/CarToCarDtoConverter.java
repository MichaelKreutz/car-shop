package de.kreutz.michael.carshop.converter;

import de.kreutz.michael.carshop.dto.CarDto;
import de.kreutz.michael.carshop.model.Car;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a @see Car object to a @see CarDto object.
 *
 * If @see CarDto and @see Car diverge then a non-trivial mapping must be implemented here.
 */
@Component
public class CarToCarDtoConverter implements Converter<Car, CarDto> {

    @Override
    public CarDto convert(final Car car) {
        return CarDto.builder()
            .brand(car.getBrand())
            .color(car.getColor())
            .dateOfPurchase(car.getDateOfPurchase())
            .isReserved(car.isReserved())
            .price(car.getPrice())
            .vehicleIdentificationNumber(car.getVehicleIdentificationNumber())
            .build();
    }
}
