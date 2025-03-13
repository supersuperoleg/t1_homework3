package org.rakhimov.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rakhimov.application.TestContainer;
import org.rakhimov.application.dto.TaskDto;
import org.rakhimov.application.entity.Task;
import org.rakhimov.application.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskServiceIntegrationTest extends TestContainer {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    @Transactional
    void cleanUp() {
        taskRepository.deleteAll();
    }

    @Test
    void createTask_Integration_Success() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Task");
        taskDto.setDescription("Description");
        taskDto.setStatus("NEW");
        taskDto.setUserId(1L);

        TaskDto createdTask = taskService.createTask(taskDto);

        assertNotNull(createdTask.getId());
        assertEquals("Task", createdTask.getTitle());
        assertEquals("NEW", createdTask.getStatus());
    }

    @Test
    void getTaskById_Integration_Success() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Task");
        taskDto.setDescription("Test");
        taskDto.setStatus("NEW");
        taskDto.setUserId(1L);
        TaskDto createdTask = taskService.createTask(taskDto);

        Optional<TaskDto> result = taskService.getTaskById(createdTask.getId());

        assertTrue(result.isPresent());
        assertEquals("Task", result.get().getTitle());
    }

    @Test
    void updateTask_Integration_Success() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Task1");
        taskDto.setDescription("Description1");
        taskDto.setStatus("NEW");
        taskDto.setUserId(1L);
        TaskDto createdTask = taskService.createTask(taskDto);

        TaskDto updateDto = new TaskDto();
        updateDto.setTitle("Task2");
        updateDto.setDescription("Description2");
        updateDto.setStatus("IN_WORK");
        updateDto.setUserId(2L);

        TaskDto updatedTask = taskService.updateTask(createdTask.getId(), updateDto);

        assertEquals("Task2", updatedTask.getTitle());
        assertEquals("IN_WORK", updatedTask.getStatus());
    }

    @Test
    void deleteTask_Integration_Success() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Task");
        taskDto.setStatus("NEW");
        taskDto.setUserId(1L);
        TaskDto createdTask = taskService.createTask(taskDto);

        taskService.deleteTask(createdTask.getId());

        Optional<Task> deletedTask = taskRepository.findById(createdTask.getId());
        assertFalse(deletedTask.isPresent());
    }

    @Test
    void getAllTasks_Integration_Success() {
        TaskDto taskDto1 = new TaskDto();
        taskDto1.setTitle("Task1");
        taskDto1.setStatus("NEW");
        taskDto1.setUserId(1L);
        taskService.createTask(taskDto1);

        TaskDto taskDto2 = new TaskDto();
        taskDto2.setTitle("Task2");
        taskDto2.setStatus("IN_WORK");
        taskDto2.setUserId(2L);
        taskService.createTask(taskDto2);

        List<TaskDto> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Task1")));
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Task2")));
    }
}
