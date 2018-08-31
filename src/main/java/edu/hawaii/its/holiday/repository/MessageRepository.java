package edu.hawaii.its.holiday.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.hawaii.its.holiday.type.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    Message findById(Integer id);

}
