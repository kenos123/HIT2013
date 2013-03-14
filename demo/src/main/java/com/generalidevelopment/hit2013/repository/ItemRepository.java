package com.generalidevelopment.hit2013.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.generalidevelopment.hit2013.domain.Item;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

}
