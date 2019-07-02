package com.mytaxi.service.manufacturer;

import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.driver.DefaultDriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultManufacturerService implements ManufacturerService
{

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private final ManufacturerRepository manufacturerRepository;

    public DefaultManufacturerService(final ManufacturerRepository manufacturerRepository)
    {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public ManufacturerDO find(Long manufacturerId) throws EntityNotFoundException
    {
        return findManufacturerChecked(manufacturerId);
    }

    private ManufacturerDO findManufacturerChecked(Long manufacturerId) throws EntityNotFoundException
    {
        return manufacturerRepository.findById(manufacturerId)
            .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + manufacturerId));
    }
}
