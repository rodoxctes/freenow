package com.mytaxi.dataaccessobject;

import com.mytaxi.domainobject.UserDO;
import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for user table.
 * <p/>
 */
public interface UserRepository extends CrudRepository<UserDO, Long>
{
    UserDO findByUsername(String username);

}
