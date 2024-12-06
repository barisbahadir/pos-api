package com.bahadir.pos.entity;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateItemDto {
    private Long id;
    private Integer orderValue;
}