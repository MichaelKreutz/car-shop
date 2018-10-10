package de.kreutz.michael.carshop.converter;

import de.kreutz.michael.carshop.dto.CarDto;
import de.kreutz.michael.carshop.model.Car;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a @see CarDto object to a @see Car object.
 *
 * If @see CarDto and @see Car diverge then a non-trivial mapping must be implemented here.
 */
@Component
public class CarDtoToCarConverter implements Converter<CarDto, Car> {

    @Override
    public Car convert(final CarDto source) {
        final Car car = new Car();
        car.setBrand(source.getBrand());
        car.setColor(source.getColor());
        car.setDateOfPurchase(source.getDateOfPurchase());
        car.setPrice(source.getPrice());
        car.setReserved(source.isReserved());
        car.setVehicleIdentificationNumber(source.getVehicleIdentificationNumber());
        return car;
    }
}
