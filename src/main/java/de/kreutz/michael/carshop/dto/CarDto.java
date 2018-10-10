package de.kreutz.michael.carshop.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Car dto, additional validation can be added. If less fields should be exposed to the outside
 * world, then this dto can diverge from @see Car.
 */
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = CarDto.CarDtoBuilder.class)
@Valid
public class CarDto {

    @NotBlank
    String vehicleIdentificationNumber;

    String brand;

    String color;

    BigDecimal price;

    OffsetDateTime dateOfPurchase;

    boolean isReserved;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class CarDtoBuilder {
        // needed for deserialization
    }
}
