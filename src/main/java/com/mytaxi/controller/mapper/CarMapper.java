package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper
{

    public static CarDO makeCarDO(CarDTO carDTO) {
        return new  CarDO(carDTO.getLicensePlate(),carDTO.getSeatCount(),carDTO.getEngineType(),carDTO.getManufacturer());
    }

    public static CarDTO makeCarDTO(CarDO carDO) {
        return CarDTO.builder()
            .licensePlate(carDO.getLicensePlate())
            .seatCount(carDO.getSeatCount())
            .convertible(carDO.isConvertible())
            .deleted(carDO.getDeleted())
            .rating(carDO.getRating())
            .engineType(carDO.getEngineType())
            .manufacturer(carDO.getManufacturer())
            .selected(carDO.isSelected())
            .build();
    }

    public static List<CarDTO> makeCarDtoList(Collection<CarDO> cars) {
        return cars.stream()
            .map(CarMapper::makeCarDTO)
            .collect(Collectors.toList());
    }

}
