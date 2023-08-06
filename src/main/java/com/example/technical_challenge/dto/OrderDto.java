package com.example.technical_challenge.dto;

import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.Order;
import com.example.technical_challenge.db.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private int id;

    private Date creationDate;

    private ItemDto itemDto;

    private Integer quantity;

    private UserDto userWhoCreatedOrderDto;

    private Boolean isFulfilled;

    public static Order toEntity(OrderDto orderDto){

        return Order.builder()
                .id(orderDto.getId())
                .creationDate(orderDto.getCreationDate())
                .item(ItemDto.toEntity(orderDto.getItemDto()))
                .quantity(orderDto.getQuantity())
                .userWhoCreatedOrder(UserDto.toEntity(orderDto.getUserWhoCreatedOrderDto()))
                .isFulfilled(orderDto.getIsFulfilled())
                .build();
    }

    public static OrderDto fromEntity(Order order){
        return OrderDto.builder()
                .id(order.getId())
                .creationDate(order.getCreationDate())
                .itemDto(ItemDto.fromEntity(order.getItem()))
                .quantity(order.getQuantity())
                .userWhoCreatedOrderDto(UserDto.fromEntity(order.getUserWhoCreatedOrder()))
                .isFulfilled(order.getIsFulfilled())
                .build();
    }

    public static List<OrderDto> fromEntityList(List<Order> orders) {
        return orders.stream()
                .map(OrderDto::fromEntity)
                .collect(Collectors.toList());
    }
}
