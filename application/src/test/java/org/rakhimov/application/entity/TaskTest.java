package org.rakhimov.application.entity;

import org.junit.jupiter.api.Test;
import org.rakhimov.application.enums.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    void getStatus_shouldReturnUndefined_whenStatusIsNull() {
        // Arrange
        Task task = new Task();
        task.setStatus(null);

        // Act
        TaskStatus result = task.getStatus();

        // Assert
        assertEquals(TaskStatus.UNDEFINED, result, "Статус будет UNDEFINED когда сэтим null");
    }

    @Test
    void getStatus_shouldReturnActualStatus_whenStatusIsSet() {
        // Arrange
        Task task = new Task();
        task.setStatus(TaskStatus.IN_WORK);

        // Act
        TaskStatus result = task.getStatus();

        // Assert
        assertEquals(TaskStatus.IN_WORK, result, "Статус должен быть равен тому, что мы задаем сэттером");
    }
}