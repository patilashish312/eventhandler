package com.cs.assignment.eventhandler.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs.assignment.eventhandler.io.DBEvent;

public interface DBEventRepository extends JpaRepository<DBEvent, String> {

	Optional<DBEvent> findById(String id);
}
