package org.rakhimov.application.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskStatusTest {
    @Test
    void fromString_shouldReturnCorrectStatus_whenValidString() {
        assertEquals(TaskStatus.NEW, TaskStatus.fromString("NEW"), "Должен вернуть NEW для 'NEW'");
        assertEquals(TaskStatus.IN_WORK, TaskStatus.fromString("IN_WORK"), "Должен вернуть IN_WORK для 'IN_WORK'");
        assertEquals(TaskStatus.CANCELLED, TaskStatus.fromString("CANCELLED"), "Должен вернуть CANCELLED для 'CANCELLED'");
        assertEquals(TaskStatus.CLOSED, TaskStatus.fromString("CLOSED"), "Должен вернуть CLOSED для 'CLOSED'");
        assertEquals(TaskStatus.UNDEFINED, TaskStatus.fromString("UNDEFINED"), "Должен вернуть UNDEFINED для 'UNDEFINED'");
    }

    @Test
    void fromString_shouldHandleCaseInsensitivity_whenLowerCaseString() {
        assertEquals(TaskStatus.NEW, TaskStatus.fromString("new"), "Должен вернуть NEW для 'new'");
        assertEquals(TaskStatus.IN_WORK, TaskStatus.fromString("in_work"), "Должен вернуть IN_WORK для 'in_work'");
        assertEquals(TaskStatus.CANCELLED, TaskStatus.fromString("cancelled"), "Должен вернуть CANCELLED для 'cancelled'");
        assertEquals(TaskStatus.CLOSED, TaskStatus.fromString("closed"), "Должен вернуть CLOSED для 'closed'");
        assertEquals(TaskStatus.UNDEFINED, TaskStatus.fromString("undefined"), "Должен вернуть UNDEFINED для 'undefined'");
    }

    @Test
    void fromString_shouldReturnUndefined_whenNull() {
        // Arrange & Act
        TaskStatus result = TaskStatus.fromString(null);

        // Assert
        assertEquals(TaskStatus.UNDEFINED, result, "Должен вернуть UNDEFINED для null");
    }

    @Test
    void fromString_shouldThrowIllegalArgumentException_whenInvalidString() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> TaskStatus.fromString("qwerty"),
                "Должен выбросить исключение IllegalArgumentException для неизвестных значений"
        );
        assertEquals("Unknown status: qwerty", exception.getMessage(), "Какое-то исключение");
    }

    @Test
    void fromString_shouldThrowIllegalArgumentException_whenEmptyString() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> TaskStatus.fromString(""),
                "Должен выбросить исключение IllegalArgumentException для пустой строки"
        );
        assertEquals("Unknown status: ", exception.getMessage(), "Какое-то исключение");
    }
}