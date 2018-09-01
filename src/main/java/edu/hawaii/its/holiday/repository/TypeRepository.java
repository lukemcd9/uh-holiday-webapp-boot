package edu.hawaii.its.holiday.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.hawaii.its.holiday.type.Type;

public interface TypeRepository extends JpaRepository<Type, Integer> {

    Type findById(Integer id);

}
