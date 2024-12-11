package com.bahadir.pos.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataFilterDto {
    private String searchText;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public LocalDate getStartOnlyDate(){
        return startDate.toLocalDate();
    }

    public LocalDate getEndOnlyDate(){
        return endDate.toLocalDate();
    }
}