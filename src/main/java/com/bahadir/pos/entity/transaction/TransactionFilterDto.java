package com.bahadir.pos.entity.transaction;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFilterDto {
    private String searchText;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}