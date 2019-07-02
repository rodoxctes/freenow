package com.mytaxi.service.car;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.manufacturer.ManufacturerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultCarServiceTest
{

    @Mock
    private CarRepository carRepository;

    @Mock
    private ManufacturerService manufacturerService;

    @Mock
    private CarService carService;


    private DriverDO mockDriver = new DriverDO(10L,"driver", "password");

    private ManufacturerDO mockManufacturer = new ManufacturerDO(1, "Mercedes Benz");

    private CarDO mockCar = new CarDO("XYZ432", 4, EngineType.GAS, mockManufacturer);


    @Test
    public void shouldFindACarIfExists() throws EntityNotFoundException
    {
        when(carRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(mockCar));

        CarService carService = new DefaultCarService(carRepository, manufacturerService);

        CarDO result = carService.find(1L);
        assertEquals(mockCar, result);

    }

    @Test
    public void shouldCreateACar() throws EntityNotFoundException, ConstraintsViolationException
    {
        when(manufacturerService.find(anyLong())).thenReturn(mockManufacturer);
        when(carRepository.save(any(CarDO.class))).thenReturn(mockCar);

        CarService carService = new DefaultCarService(carRepository, manufacturerService);

        CarDO newCar = carService.create(mockCar);

        assertEquals(mockCar.getSeatCount(), newCar.getSeatCount());
        assertNull(newCar.getId());
        assertEquals(mockManufacturer, newCar.getManufacturer());
        assertEquals(1, newCar.getRating());
        assertEquals(mockCar.getEngineType(), newCar.getEngineType());
        assertEquals(mockCar.getLicensePlate(), newCar.getLicensePlate());
        assertEquals(mockCar.isConvertible(), newCar.isConvertible());

        verify(carRepository).save(mockCar);

    }

}
