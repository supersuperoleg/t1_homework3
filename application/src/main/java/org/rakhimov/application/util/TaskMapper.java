package org.rakhimov.application.util;


import org.rakhimov.application.dto.TaskDto;
import org.rakhimov.application.entity.Task;
import org.rakhimov.application.enums.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskDto dto) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(TaskStatus.fromString(dto.getStatus()))
                .userId(dto.getUserId())
                .build();
    }

    public TaskDto toDto(Task entity) {
        TaskStatus status = entity.getStatus();
        return TaskDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(status.toString())
                .userId(entity.getUserId())
                .build();
    }
}