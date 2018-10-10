package de.kreutz.michael.carshop.dto;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;

@Value
@Builder(toBuilder = true)
public class Metadata {
    BigDecimal totalValue;
}
