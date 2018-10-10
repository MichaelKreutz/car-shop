package de.kreutz.michael.carshop.model;

import lombok.Data;
import org.springframework.data.jpa.repository.Temporal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
public class Car {

    @Id
    private String vehicleIdentificationNumber;

    private String brand;

    private String color;

    private BigDecimal price;

    private OffsetDateTime dateOfPurchase;

    private boolean isReserved;
}
