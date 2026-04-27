package org.example.btnl_2504.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "todos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "{todo.validation.content.notBlank}")
    private String content;

    @FutureOrPresent(message = "{todo.validation.dueDate.futureOrPresent}")
    private LocalDate dueDate;

    private String status;
    private String priority;
}
