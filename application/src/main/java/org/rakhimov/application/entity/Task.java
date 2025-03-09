package org.rakhimov.application.entity;

import jakarta.persistence.*;
import lombok.*;
import org.rakhimov.application.enums.TaskStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Long userId;

    public TaskStatus getStatus() {
        return status == null ? TaskStatus.UNDEFINED : status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                '}';
    }
}
