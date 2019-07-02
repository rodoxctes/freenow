package com.mytaxi.service.car;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.driver.DefaultDriverService;
import com.mytaxi.service.manufacturer.ManufacturerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some car specific things.
 * <p/>
 */
@Service
public class DefaultCarService implements CarService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private final CarRepository carRepository;

    private final ManufacturerService manufacturerService;

    public DefaultCarService(final CarRepository carRepository, final  ManufacturerService manufacturerService){
        this.carRepository = carRepository;
        this.manufacturerService = manufacturerService;
    }

    /**
     * Selects a car by id.
     *
     * @param carId
     * @return found car
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    @Override public CarDO find(Long carId) throws EntityNotFoundException
    {
        return findCarChecked(carId);
    }

    /**
     * Find all cars.
     */
    @Override public List<CarDO> find()
    {
        return (List<CarDO>) carRepository.findAll();
    }

    /**
     * Deletes an existing car by id.
     *
     * @param carId
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long carId) throws EntityNotFoundException
    {
        CarDO carDO = findCarChecked(carId);
        carDO.setDeleted(true);
    }


    @Override public CarDO updateRating(long carId, int rating) throws EntityNotFoundException
    {
        CarDO carDO = findCarChecked(carId);

        carDO.setRating(rating);

        return carRepository.save(carDO);
    }


    @Override
    public void save(CarDO car) {
        carRepository.save(car);
    }

    /**
     * Creates a new xar.
     *
     * @param carDO
     * @return
     * @throws ConstraintsViolationException if a car already exists with the given license plate, ... .
     * @throws EntityNotFoundException if a manufactures does not exists, ... .
     */
    @Override public CarDO create(CarDO carDO) throws ConstraintsViolationException, EntityNotFoundException
    {
        ManufacturerDO manufacturer = manufacturerService.find(carDO.getManufacturer().getId());

        carDO.setManufacturer(manufacturer);

        CarDO car;
        try
        {
            car = carRepository.save(carDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("ConstraintsViolationException while creating a car: {}", carDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }


    private CarDO findCarChecked(Long carId) throws EntityNotFoundException
    {
        return carRepository.findById(carId)
            .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + carId));
    }

}
