package com.mytaxi.controller;

import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.service.driver.DriverService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DriverControllerTest
{

    private DriverDO mockDriver = new DriverDO(10L,"driver", "password");

    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    @Before
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DriverController(driverService)).build();
    }

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

    @Test
    public void shouldDeselectCar() throws Exception
    {
        String driverId = "10";

        when(driverService.deselectCar(anyLong())).thenReturn(mockDriver);

        RequestBuilder requestBuilder = delete("/v1/drivers/{driverId}/car", driverId)
            .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

    }
}
