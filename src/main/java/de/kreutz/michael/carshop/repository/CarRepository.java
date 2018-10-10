package de.kreutz.michael.carshop.repository;

import de.kreutz.michael.carshop.model.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarRepository extends CrudRepository<Car, String> {

    List<Car> findAll();
}
