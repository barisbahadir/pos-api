package com.bahadir.pos.entity;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDto {
    private Long id;
    private Integer orderValue;
}