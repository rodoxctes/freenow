package com.mytaxi.controller;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.service.driver.DriverService;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@WebMvcTest(value = DriverController.class, secure = false)
@AutoConfigureMockMvc
public class DriverControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    private ManufacturerDO mockManufacturer = new ManufacturerDO(1, "Mercedes Benz");

    private CarDO mockCar = new CarDO("XYZ432", 4, EngineType.GAS, mockManufacturer);

    private CarDO mockCarTwo = new CarDO("ACB432", 4, EngineType.ELECTRIC, mockManufacturer);

    private DriverDO mockDriver = new DriverDO(10L,"driver", "password");



    @Test
    public void shouldSelectCar() throws Exception
    {
        String driverId = "10";

        String carId = "2";

        when(driverService.selectCar(anyLong(), anyLong())).thenReturn(mockDriver);

        RequestBuilder requestBuilder = put("/v1/drivers/{driverId}/car/{carId}", driverId, carId)
            .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

    }
}
