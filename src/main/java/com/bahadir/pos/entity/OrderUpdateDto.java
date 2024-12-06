package com.bahadir.pos.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDto {
    private Long categoryId;
    private List<OrderUpdateItemDto> orderedValues;
}