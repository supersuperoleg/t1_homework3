package org.rakhimov.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rakhimov.application.dto.TaskDto;
import org.rakhimov.application.entity.Task;
import org.rakhimov.application.enums.TaskStatus;
import org.rakhimov.application.exception.InvalidTaskStatusException;
import org.rakhimov.application.exception.TaskCreationException;
import org.rakhimov.application.exception.TaskNotFoundException;
import org.rakhimov.application.repository.TaskRepository;
import org.rakhimov.application.util.TaskMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskDto createTask(TaskDto taskDto) {
        try {
            Task task = taskMapper.toEntity(taskDto);
            Task createdTask = taskRepository.save(task);
            return taskMapper.toDto(createdTask);
        } catch (Exception e) {
            String error = String.format("Failed to create task by dto: %s", taskDto);
            throw new TaskCreationException(error, e);
        }
    }

    public Optional<TaskDto> getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto);
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new TaskNotFoundException("Task not found with id " + id);
        }
        Task taskToUpdate = optionalTask.get();

        TaskStatus statusByDto;
        try {
            statusByDto = TaskStatus.valueOf(taskDto.getStatus());
        } catch (IllegalArgumentException e) {
            throw new InvalidTaskStatusException("Invalid status: " + taskDto.getStatus());
        }

        taskToUpdate.setTitle(taskDto.getTitle());
        taskToUpdate.setDescription(taskDto.getDescription());
        taskToUpdate.setUserId(taskDto.getUserId());
        taskToUpdate.setStatus(statusByDto);

        Task updatedTask = taskRepository.save(taskToUpdate);
        return taskMapper.toDto(updatedTask);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id " + id);
        }
        taskRepository.deleteById(id);
    }

    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }
}