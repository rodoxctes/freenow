package com.mytaxi.service.driver;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.GeoCoordinate;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.exception.OfflineStatusException;
import com.mytaxi.service.car.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultDriverService implements DriverService
{

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private final DriverRepository driverRepository;

    private final CarService carService;


    public DefaultDriverService(final DriverRepository driverRepository, CarService carService)
    {
        this.driverRepository = driverRepository;
        this.carService = carService;
    }


    /**
     * Selects a driver by id.
     *
     * @param driverId
     * @return found driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    public DriverDO find(Long driverId) throws EntityNotFoundException
    {
        return findDriverChecked(driverId);
    }


    /**
     * Creates a new driver.
     *
     * @param driverDO
     * @return
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException
    {
        DriverDO driver;
        try
        {
            driver = driverRepository.save(driverDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("ConstraintsViolationException while creating a driver: {}", driverDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return driver;
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long driverId) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setDeleted(true);
    }


    /**
     * Update the location for a driver.
     *
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
    }


    /**
     * Find all drivers by online state.
     *
     * @param onlineStatus
     */
    @Override
    public List<DriverDO> find(OnlineStatus onlineStatus)
    {
        return driverRepository.findByOnlineStatus(onlineStatus);
    }

    /**
     * Select the car for a driver
     *
     * @param driverId
     * @param carId
     */
    @Override public DriverDO selectCar(Long driverId, Long carId) throws EntityNotFoundException, OfflineStatusException, CarAlreadyInUseException
    {
        DriverDO driver = findDriverChecked(driverId);

        if(driver.getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
            throw new OfflineStatusException("Only ONLINE drivers can select a car");
        }

        CarDO car = carService.find(carId);

        if(car.isSelected()) {
            throw new CarAlreadyInUseException("Car already in use by another driver");
        }

        updateCarStatus(driver, car);

        driver.setCar(car);
        driverRepository.save(driver);

        return driver;
    }


    /**
     * deselect the car for a driver
     *
     * @param driverId
     * @return
     */
    @Override public DriverDO deselectCar(Long driverId) throws EntityNotFoundException
    {
        DriverDO driver = findDriverChecked(driverId);

        Optional.ofNullable(driver.getCar()).ifPresent(c -> {
            c.setSelected(false);
            carService.save(c);
        });

        driver.setCar(null);
        driverRepository.save(driver);
        return driver;
    }


    private DriverDO findDriverChecked(Long driverId) throws EntityNotFoundException
    {
        return driverRepository.findById(driverId)
            .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + driverId));
    }


    private void updateCarStatus(DriverDO driver, CarDO car)
    {
        car.setSelected(true);
        carService.save(car);

        Optional<CarDO> currentCar = Optional.ofNullable(driver.getCar());
        currentCar.ifPresent(c -> {
            c.setSelected(false);
            carService.save(c);
        });
    }

}
