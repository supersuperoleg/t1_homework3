package org.rakhimov.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rakhimov.application.dto.TaskDto;
import org.rakhimov.application.exception.TaskCreationException;
import org.rakhimov.application.exception.TaskNotFoundException;
import org.rakhimov.application.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("Test Task");
        taskDto.setDescription("Test Description");
        taskDto.setStatus("NEW");
        taskDto.setUserId(1L);
    }

    // Тесты для createTask
    @Test
    void createTask_Success() throws Exception {
        when(taskService.createTask(any(TaskDto.class))).thenReturn(taskDto);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task created successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Test Task"))
                .andExpect(jsonPath("$.errorDetails").isEmpty());

        verify(taskService, times(1)).createTask(any(TaskDto.class));
    }

    @Test
    void createTask_TaskCreationException() throws Exception {
        String errorMessage = "Creation failed due to invalid data";
        Throwable cause = new IllegalArgumentException("Invalid input");
        when(taskService.createTask(any(TaskDto.class)))
                .thenThrow(new TaskCreationException(errorMessage, cause));

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Failed to create task"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errorDetails").value(errorMessage)); // Проверяем только message

        verify(taskService, times(1)).createTask(any(TaskDto.class));
    }

    // Тесты для getTaskById
    @Test
    void getTaskById_Success() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(taskDto));

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task found successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Test Task"))
                .andExpect(jsonPath("$.errorDetails").isEmpty());

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void getTaskById_NotFound() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errorDetails").value("No task found with ID 1"));

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void getTaskById_InvalidIdZero() throws Exception {
        mockMvc.perform(get("/tasks/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Invalid Task ID"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errorDetails").value("Task ID cannot be 0"));

        verify(taskService, never()).getTaskById(anyLong());
    }

    // Тесты для updateTask
    @Test
    void updateTask_Success() throws Exception {
        when(taskService.updateTask(eq(1L), any(TaskDto.class))).thenReturn(taskDto);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Test Task"))
                .andExpect(jsonPath("$.errorDetails").isEmpty());

        verify(taskService, times(1)).updateTask(eq(1L), any(TaskDto.class));
    }

    @Test
    void updateTask_NotFound() throws Exception {
        when(taskService.updateTask(eq(1L), any(TaskDto.class)))
                .thenThrow(new TaskNotFoundException("Task not found with id 1"));

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errorDetails").value("Task not found with id 1"));

        verify(taskService, times(1)).updateTask(eq(1L), any(TaskDto.class));
    }

    @Test
    void updateTask_InvalidIdZero() throws Exception {
        mockMvc.perform(put("/tasks/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Invalid Task ID"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errorDetails").value("Task ID cannot be 0"));

        verify(taskService, never()).updateTask(anyLong(), any(TaskDto.class));
    }

    // Тесты для deleteTask
    @Test
    void deleteTask_Success() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task deleted successfully"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errorDetails").isEmpty());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void deleteTask_NotFound() throws Exception {
        doThrow(new TaskNotFoundException("Task not found with id 1")).when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errorDetails").value("Task not found with id 1"));

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void deleteTask_InvalidIdZero() throws Exception {
        mockMvc.perform(delete("/tasks/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Invalid Task ID"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errorDetails").value("Task ID cannot be 0"));

        verify(taskService, never()).deleteTask(anyLong());
    }

    // Тесты для getAllTasks
    @Test
    void getAllTasks_Success() throws Exception {
        List<TaskDto> tasks = Collections.singletonList(taskDto);
        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tasks retrieved successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Test Task"))
                .andExpect(jsonPath("$.errorDetails").isEmpty());

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getAllTasks_EmptyList() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No tasks found"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errorDetails").value("There are no tasks available"));

        verify(taskService, times(1)).getAllTasks();
    }
}