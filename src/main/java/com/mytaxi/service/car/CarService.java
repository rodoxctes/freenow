package com.mytaxi.service.car;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

import java.util.List;

public interface CarService
{

    CarDO find(Long id) throws EntityNotFoundException;

    List<CarDO> find();

    void delete(Long id) throws EntityNotFoundException;

    CarDO updateRating(long id, int rating) throws EntityNotFoundException;

    void save(CarDO c);

    CarDO create(CarDO carDO) throws ConstraintsViolationException, EntityNotFoundException;
}
