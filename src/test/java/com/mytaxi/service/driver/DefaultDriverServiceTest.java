package com.mytaxi.service.driver;

import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.exception.OfflineStatusException;
import com.mytaxi.service.car.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultDriverServiceTest
{

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarService carService;


    private DriverDO mockDriver = new DriverDO(10L,"driver", "password");

    private ManufacturerDO mockManufacturer = new ManufacturerDO(1, "Mercedes Benz");

    private CarDO mockCar = new CarDO("XYZ432", 4, EngineType.GAS, mockManufacturer);



    @Test
    public void shouldSetCarToNullWhenDriverDeselectIt() throws EntityNotFoundException
    {
        mockCar.setSelected(true);
        mockDriver.setCar(mockCar);

        when(driverRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(mockDriver));

        Long driverId = 1L;

        DriverService driverService = new DefaultDriverService(driverRepository, carService);
        DriverDO driver = driverService.deselectCar(driverId);
        assertNull(driver.getCar());

        mockCar.setSelected(false);
        verify(carService).save(mockCar);

    }

    @Test
    public void shouldThrowExceptionWhenDeselectingACarAndDriverIsNotFound()
    {
        try {
            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            driverService.deselectCar(1L);
            fail();
        } catch (EntityNotFoundException e) {
            assertEquals("Could not find entity with id: 1", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhenSelectingACarAndDriverIsNotFound() throws CarAlreadyInUseException, OfflineStatusException
    {
        try {
            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            driverService.selectCar(1L, 1L);
        } catch (EntityNotFoundException e) {
            assertEquals("Could not find entity with id: 1", e.getMessage());
        }
    }

    @Test
    public void shouldThrowsCarAlreadyInUseExceptionIfCarIsAlreadySelected() throws OfflineStatusException, EntityNotFoundException
    {
        mockCar.setSelected(true);

        mockDriver.setOnlineStatus(OnlineStatus.ONLINE);

        when(driverRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(mockDriver));

        try {
            when(carService.find(1L)).thenReturn(mockCar);

            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            driverService.selectCar(1L, 1L);
        } catch (CarAlreadyInUseException e) {
            assertEquals("Car already in use by another driver", e.getMessage());
        }
    }

    @Test
    public void shouldThrowsOfflineStatusExceptionIfDriverStatusIsOffline() throws CarAlreadyInUseException, EntityNotFoundException
    {
        when(driverRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(mockDriver));

        try {
            DriverService driverService = new DefaultDriverService(driverRepository, carService);
            driverService.selectCar(1L, 1L);
        } catch (OfflineStatusException e) {
            assertEquals("Only ONLINE drivers can select a car", e.getMessage());
        }
    }

}
