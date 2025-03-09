package org.rakhimov.application.repository;


import org.rakhimov.application.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
