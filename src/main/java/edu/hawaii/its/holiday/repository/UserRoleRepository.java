package edu.hawaii.its.holiday.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.hawaii.its.holiday.type.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    UserRole findById(Integer id);

}
