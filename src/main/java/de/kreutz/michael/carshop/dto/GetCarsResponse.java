package de.kreutz.michael.carshop.dto;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class GetCarsResponse {
    Metadata metadata;
    List<CarDto> cars;
}
