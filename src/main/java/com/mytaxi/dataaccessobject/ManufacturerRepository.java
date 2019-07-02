package com.mytaxi.dataaccessobject;

import com.mytaxi.domainobject.ManufacturerDO;
import org.springframework.data.repository.CrudRepository;

public interface ManufacturerRepository extends CrudRepository<ManufacturerDO, Long>
{
}
