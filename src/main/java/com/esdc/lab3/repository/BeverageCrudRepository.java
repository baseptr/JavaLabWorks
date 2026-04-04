package com.esdc.lab3.repository;

import com.esdc.lab3.entity.Beverage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeverageCrudRepository extends CrudRepository<Beverage, Long> {
}

