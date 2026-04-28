package com.kraken.cube.userService.repository;

import org.springframework.data.repository.*;

import com.kraken.cube.userService.model.User;


public interface UserRepository extends CrudRepository<User, Long> {

}
 