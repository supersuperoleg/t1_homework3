package org.rakhimov.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rakhimov.application.dto.TaskDto;
import org.rakhimov.application.entity.Task;
import org.rakhimov.application.enums.TaskStatus;
import org.rakhimov.application.exception.InvalidTaskStatusException;
import org.rakhimov.application.exception.TaskCreationException;
import org.rakhimov.application.exception.TaskNotFoundException;
import org.rakhimov.application.repository.TaskRepository;
import org.rakhimov.application.util.TaskMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private TaskDto taskDto;
    private Task task;

    @BeforeEach
    void setUp() {
        taskDto = new TaskDto();
        taskDto.setTitle("Test");
        taskDto.setDescription("Description");
        taskDto.setStatus("NEW");
        taskDto.setUserId(1L);

        task = new Task();
        task.setId(1L);
        task.setTitle("Test");
        task.setDescription("Description");
        task.setStatus(TaskStatus.NEW);
        task.setUserId(1L);
    }

    @Test
    void createTask_Success() {
        // Arrange
        when(taskMapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        // Act
        TaskDto result = taskService.createTask(taskDto);

        // Assert
        assertNotNull(result);
        assertEquals(taskDto.getTitle(), result.getTitle());
        assertEquals(taskDto.getStatus(), result.getStatus());
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).toEntity(taskDto);
        verify(taskMapper, times(1)).toDto(task);
    }

    @Test
    void createTask_Failure_ThrowsTaskCreationException() {
        // Arrange
        when(taskMapper.toEntity(taskDto)).thenThrow(new RuntimeException("Ошибка!"));

        // Act & Assert
        TaskCreationException exception = assertThrows(TaskCreationException.class, () -> taskService.createTask(taskDto));
        assertTrue(exception.getMessage().contains("Failed to create task by dto"));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getTaskById_Success_ReturnsTaskDto() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        Optional<TaskDto> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals(taskDto.getTitle(), result.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_NotFound_ReturnsEmpty() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<TaskDto> result = taskService.getTaskById(1L);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskMapper, never()).toDto(any(Task.class));
    }

    @Test
    void updateTask_Success() {
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(TaskStatus.IN_WORK);
        updatedTask.setUserId(2L);

        TaskDto updatedTaskDto = new TaskDto();
        updatedTaskDto.setTitle("Updated Task");
        updatedTaskDto.setDescription("Updated Description");
        updatedTaskDto.setStatus("IN_WORK");
        updatedTaskDto.setUserId(2L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(updatedTaskDto);

        TaskDto result = taskService.updateTask(1L, updatedTaskDto);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("IN_WORK", result.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTask_TaskNotFound_ThrowsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(1L, taskDto));
        assertEquals("Task not found with id 1", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTask_InvalidStatus_ThrowsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        taskDto.setStatus("INVALID_STATUS");

        InvalidTaskStatusException exception = assertThrows(InvalidTaskStatusException.class, () ->
                taskService.updateTask(1L, taskDto));
        assertTrue(exception.getMessage().contains("Invalid status: INVALID_STATUS"));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_Success() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_NotFound_ThrowsException() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () ->
                taskService.deleteTask(1L));
        assertEquals("Task not found with id 1", exception.getMessage());
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAllTasks_Success() {
        List<Task> tasks = Collections.singletonList(task);
        List<TaskDto> taskDtoList = Collections.singletonList(taskDto);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        List<TaskDto> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals(taskDto.getTitle(), result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getAllTasks_EmptyList() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        List<TaskDto> result = taskService.getAllTasks();

        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findAll();
        verify(taskMapper, never()).toDto(any(Task.class));
    }
}