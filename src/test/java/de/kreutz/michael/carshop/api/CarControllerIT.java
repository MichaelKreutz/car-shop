package de.kreutz.michael.carshop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.kreutz.michael.carshop.dto.CarDto;
import de.kreutz.michael.carshop.service.CarService;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static java.util.Arrays.asList;
import java.math.BigDecimal;
import java.util.List;
import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CarController.class)
public class CarControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Test
    public void shouldGetAllCars() throws Exception {
        final List<CarDto> cars = asList(random(CarDto.class), random(CarDto.class));
        doReturn(cars).when(carService).fetchCars(null, null, null);
        final double totalValue = 123.45;
        final BigDecimal totalValueBigDecimal = new BigDecimal("" + totalValue);
        doReturn(totalValueBigDecimal).when(carService).calcTotalValue(cars);

        mockMvc.perform(get("/cars").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cars", hasSize(2)))
            .andExpect(jsonPath("$.metadata.totalValue", equalTo(totalValue)));
    }

    @Test
    public void shouldReturn404InDeleteCarByIdIfIdNotExists() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(carService).deleteCar("123");

        mockMvc.perform(delete("/cars/123"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteCarById() throws Exception {
        doNothing().when(carService).deleteCar("123");

        mockMvc.perform(delete("/cars/123"))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldFailToCreateCarIfBodyIsMissing() throws Exception {
        mockMvc.perform(post("/cars"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCreateCar() throws Exception {
        final CarDto carDto = EnhancedRandom.random(CarDto.class);
        final String serializedCar = objectMapper.writeValueAsString(carDto);
        mockMvc.perform(post("/cars").contentType(APPLICATION_JSON_VALUE).content(serializedCar))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldFailOnValidationCreateCar() throws Exception {
        final CarDto carDto = EnhancedRandom.random(CarDto.class).toBuilder()
            .vehicleIdentificationNumber(null)
            .build();
        final String serializedCar = objectMapper.writeValueAsString(carDto);
        mockMvc.perform(post("/cars").contentType(APPLICATION_JSON_VALUE).content(serializedCar))
            .andExpect(status().isBadRequest());
    }
}
