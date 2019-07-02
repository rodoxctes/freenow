package com.mytaxi.controller;

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.request.UpdateCarRatingRequest;
import com.mytaxi.service.car.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * All operations with a car will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/cars")
@Api(
    value = "cars",
    tags = {"Cars"}
)
public class CarController
{

    private CarService carService;

    @Autowired
    public CarController(final CarService carService){this.carService = carService;}


    @GetMapping(path = "/{carId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
        value = "Finds a car by its id",
        produces = "application/json"
    )
    public CarDTO getCar(@PathVariable long carId) throws EntityNotFoundException {
        return CarMapper.makeCarDTO(carService.find(carId));
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
        value = "Retrieves a list of cars",
        produces = "application/json"
    )
    public List<CarDTO> getCars() {
        return carService.find().stream()
            .map(CarMapper::makeCarDTO)
            .collect(Collectors.toList());
    }


    @PostMapping
    @ApiOperation(
        value = "Creates a new car"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@Valid @RequestBody CarDTO carDTO) throws ConstraintsViolationException, EntityNotFoundException
    {
        CarDO carDO = CarMapper.makeCarDO(carDTO);
        return CarMapper.makeCarDTO(carService.create(carDO));

    }


    @DeleteMapping("/{carId}")
    @ApiOperation(
        value = "Deletes an existing car"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable long carId) throws EntityNotFoundException
    {
        carService.delete(carId);

    }


    @PutMapping("/{carId}")
    @ApiOperation(
        value = "Updates the rating to an existing car, value between 1 and 5"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CarDTO update(@PathVariable long carId, @Valid @RequestBody UpdateCarRatingRequest updateCarRatingRequest) throws EntityNotFoundException {
        return CarMapper.makeCarDTO(carService.updateRating(carId, updateCarRatingRequest.getRating()));
    }

}
