package org.rakhimov.application.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rakhimov.application.dto.TaskDto;
import org.rakhimov.application.entity.Task;
import org.rakhimov.application.enums.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskMapperTest {

    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
    }

    @Test
    void toEntity_shouldMapAllFieldsCorrectly_whenDtoIsValid() {
        // Arrange
        TaskDto dto = TaskDto.builder()
                .title("Test")
                .description("Description")
                .status("IN_WORK")
                .userId(1L)
                .build();

        // Act
        Task result = taskMapper.toEntity(dto);

        // Assert
        assertEquals("Test", result.getTitle(), "Title должен совпадать");
        assertEquals("Description", result.getDescription(), "Description должен совпадать");
        assertEquals(TaskStatus.IN_WORK, result.getStatus(), "Статус должен быть IN_WORK");
        assertEquals(1L, result.getUserId(), "UserId должен совпадать");
    }

    @Test
    void toEntity_shouldHandleNullFields_whenDtoHasNulls() {
        // Arrange
        TaskDto dto = TaskDto.builder()
                .title(null)
                .description(null)
                .status(null)
                .userId(null)
                .build();

        // Act
        Task result = taskMapper.toEntity(dto);

        // Assert
        assertNull(result.getTitle(), "Title должен совпадать");
        assertNull(result.getDescription(), "Description должен совпадать");
        assertEquals(TaskStatus.UNDEFINED, result.getStatus(), "Статус должен быть UNDEFINED когда сэтим null");
        assertNull(result.getUserId(), "UserId должен совпадать");
    }


    @Test
    void toDto_shouldMapAllFieldsCorrectly_whenEntityIsValid() {
        // Arrange
        Task entity = Task.builder()
                .title("Test")
                .description("Description")
                .status(TaskStatus.CLOSED)
                .userId(2L)
                .build();

        // Act
        TaskDto result = taskMapper.toDto(entity);

        // Assert
        assertEquals("Test", result.getTitle(), "Title должен совпадать");
        assertEquals("Description", result.getDescription(), "Description должен совпадать");
        assertEquals("CLOSED", result.getStatus(), "Статус должен быть \"CLOSED\"");
        assertEquals(2L, result.getUserId(), "UserId должен совпадать");
    }

    @Test
    void toDto_shouldHandleNullFields_whenEntityHasNulls() {
        // Arrange
        Task entity = Task.builder()
                .title(null)
                .description(null)
                .status(null)
                .userId(null)
                .build();

        // Act
        TaskDto result = taskMapper.toDto(entity);

        // Assert
        assertNull(result.getTitle(), "Title должен совпадать");
        assertNull(result.getDescription(), "Description должен совпадать");
        assertEquals("UNDEFINED", result.getStatus(), "Статус должен быть UNDEFINED когда сэтим null");
        assertNull(result.getUserId(), "UserId должен совпадать");
    }

}