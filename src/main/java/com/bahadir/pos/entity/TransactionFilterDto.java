package com.bahadir.pos.entity;

import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

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