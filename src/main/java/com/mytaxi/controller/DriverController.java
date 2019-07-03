package com.mytaxi.controller;

import com.mytaxi.controller.mapper.DriverMapper;
import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.exception.OfflineStatusException;
import com.mytaxi.request.SearchRequest;
import com.mytaxi.service.driver.DriverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/drivers")
@Api(
    value = "Drivers",
    tags = {"Drivers"}
)
public class DriverController
{

    private final DriverService driverService;


    @Autowired
    public DriverController(final DriverService driverService)
    {
        this.driverService = driverService;
    }


    @GetMapping("/{driverId}")
    public DriverDTO getDriver(@PathVariable long driverId) throws EntityNotFoundException
    {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException
    {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }


    @DeleteMapping("/{driverId}")
    public void deleteDriver(@PathVariable long driverId) throws EntityNotFoundException
    {
        driverService.delete(driverId);
    }


    @PutMapping("/{driverId}")
    public void updateLocation(
        @PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude)
        throws EntityNotFoundException
    {
        driverService.updateLocation(driverId, longitude, latitude);
    }


    @GetMapping
    public List<DriverDTO> findDrivers(@RequestParam OnlineStatus onlineStatus)
    {
        return DriverMapper.makeDriverDTOList(driverService.find(onlineStatus));
    }


    @PutMapping("/{driverId}/car/{carId}")
    @ApiOperation(
        value = "Select a car"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void selectCar(@Valid @PathVariable long driverId, @PathVariable long carId)
        throws EntityNotFoundException, CarAlreadyInUseException, OfflineStatusException
    {
        driverService.selectCar(driverId, carId);
    }


    @DeleteMapping("/{driverId}/car")
    @ApiOperation(
        value = "Deselect a car"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deselectCar(@Valid @PathVariable long driverId) throws EntityNotFoundException {
        driverService.deselectCar(driverId);
    }

    @GetMapping("/search/car/")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(
        value = "Search for drivers by their attributes (username, online_status) as well as car characteristics (license plate, rating, etc)."
    )
    public List<DriverDTO> findDriverByCar(
        @ApiParam(value = "The driver Name")
        @RequestParam(required = false) String name,
        @ApiParam(value = "The driver's status")
        @RequestParam(required = false) OnlineStatus driverOnlineStatus,
        @ApiParam(value = "the car's license plate")
        @RequestParam(required = false) String licensePlate,
        @ApiParam(value = "the car's status")
        @RequestParam(required = false) Boolean selected,
        @ApiParam(value = "Number of seats of the car")
        @RequestParam(required = false) Integer seatCount,
        @ApiParam(value = "The car's rating")
        @RequestParam(required = false) Integer carRating)
    {
        SearchRequest.SearchCarRequest searchCarRequest = new SearchRequest.SearchCarRequest(licensePlate, selected, seatCount, carRating);

        SearchRequest.SearchDriverRequest searchDriverRequest = new SearchRequest.SearchDriverRequest(name, driverOnlineStatus);

        SearchRequest searchRequest = new SearchRequest(searchDriverRequest, searchCarRequest );

        return DriverMapper.makeDriverDTOList(driverService.findByQuery(searchRequest));
    }
}
