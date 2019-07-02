package com.mytaxi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CarController.class, secure = false)
@AutoConfigureMockMvc
public class CarControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @MockBean
    private CarRepository carRepository;

    private ManufacturerDO mockManufacturer = new ManufacturerDO(1, "Mercedes Benz");

    private CarDO mockCar = new CarDO("XYZ432", 4, EngineType.GAS, mockManufacturer);

    private CarDO mockCarTwo = new CarDO("ACB432", 4, EngineType.ELECTRIC, mockManufacturer);

    private static final String EXAMPLE_CAR_JSON =
        "{  \"licensePlate\": \"ABC123\",  \"seatCount\": 4,  \"convertible\": false,  \"deleted\": false,  \"rating\": 5,  \"manufacturer\": {    \"id\": 1  },  \"selected\": false,  \"engineType\": \"GAS\"}";


    @Test
    public void shouldRetrieveDetailsForCarById() throws Exception
    {
        when(carService.find(anyLong())).thenReturn(mockCar);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
            "/v1/cars/{carId}", "3").accept(
            MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = getResponseWithOneCar();

        JSONAssert.assertEquals(expected, result.getResponse()
            .getContentAsString(), false);
    }

    @Test
    public void shouldGet400ErrorWhenIdDoesNotExist() throws Exception
    {
        String carId = "10";

        when(carService.find(anyLong())).thenThrow( new EntityNotFoundException("Could not find entity with id: " + carId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
            "/v1/cars/{carId}", carId).accept(
            MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }


    @Test
    public void shouldReturnAllCars() throws Exception
    {
        List<CarDO> carDOList = new ArrayList<>();
        carDOList.add(mockCar);
        carDOList.add(mockCarTwo);

        when(carService.find()).thenReturn(carDOList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
            "/v1/cars/");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();


        String expected = getResponseWithTwoCars();


        JSONAssert.assertEquals(expected, result.getResponse()
            .getContentAsString(), false);
    }

    @Test
    public void shouldCreateCar() throws Exception
    {
        when(carService.create(any(CarDO.class))).thenReturn(mockCar);

        RequestBuilder requestBuilder = post("/v1/cars")
            .accept(MediaType.APPLICATION_JSON).content(EXAMPLE_CAR_JSON)
            .contentType(MediaType.APPLICATION_JSON);


        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

    }

    @Test
    public void shouldGet400WhenCreateCarWithWrongValues() throws Exception
    {

        String incompleteBodyRequest = ""
            + "{  \"seatCount\": 4,  "
            + "\"convertible\": false,  \"deleted\": false,  \"rating\": 5, "
            + " \"manufacturer\": {    \"id\": 1  },  \"selected\": false,  \"engineType\": \"GAS\"}";


        RequestBuilder requestBuilder = post("/v1/cars")
            .accept(MediaType.APPLICATION_JSON).content(incompleteBodyRequest)
            .contentType(MediaType.APPLICATION_JSON);


        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    public void shouldUpdateCarRating() throws Exception
    {
        String carId = "10";

        String bodyRequest = "{\"rating\": 5}";

        when(carService.updateRating(anyLong(), anyInt())).thenReturn(mockCar);

        RequestBuilder requestBuilder = put("/v1/cars/{carId}", carId)
            .accept(MediaType.APPLICATION_JSON).content(bodyRequest)
            .contentType(MediaType.APPLICATION_JSON);


        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

    }


    @Test
    public void shouldDeleteCarById() throws Exception
    {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/v1/cars/{carId}", "1")
            .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

    }


    public static CarDTO asObject(String car) throws IOException
    {
        return new ObjectMapper().readValue(car, CarDTO.class);
    }

    private String getResponseWithOneCar()
    {
        return "{\n"
            + "  \"licensePlate\": \"XYZ432\",\n"
            + "  \"seatCount\": 4,\n"
            + "  \"convertible\": false,\n"
            + "  \"deleted\": false,\n"
            + "  \"rating\": 1,\n"
            + "  \"manufacturer\": {\n"
            + "    \"id\": 1\n"
            + "  },\n"
            + "  \"selected\": false,\n"
            + "  \"engineType\": \"GAS\"\n"
            + "}";
    }

    private String getResponseWithTwoCars()
    {
        return "[\n"
            + "  {\n"
            + "    \"licensePlate\": \"XYZ432\",\n"
            + "    \"seatCount\": 4,\n"
            + "    \"convertible\": false,\n"
            + "    \"deleted\": false,\n"
            + "    \"rating\": 1,\n"
            + "    \"manufacturer\": {\n"
            + "      \"id\": 1\n"
            + "    },\n"
            + "    \"selected\": false,\n"
            + "    \"engineType\": \"GAS\"\n"
            + "  },\n"
            + "  {\n"
            + "    \"licensePlate\": \"ACB432\",\n"
            + "    \"seatCount\": 4,\n"
            + "    \"convertible\": false,\n"
            + "    \"deleted\": false,\n"
            + "    \"rating\": 1,\n"
            + "    \"manufacturer\": {\n"
            + "      \"id\": 1\n"
            + "    },\n"
            + "    \"selected\": false,\n"
            + "    \"engineType\": \"ELECTRIC\"\n"
            + "  }\n"
            + "]";
    }
}
