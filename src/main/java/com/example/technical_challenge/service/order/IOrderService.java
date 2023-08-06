package com.example.technical_challenge.service.order;

import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.Order;
import com.example.technical_challenge.dto.ItemDto;
import com.example.technical_challenge.dto.OrderDto;
import com.example.technical_challenge.dto.UserDto;

import java.util.List;

public interface IOrderService {

    OrderDto getOrder (Integer orderId);

    OrderDto createOrder (OrderDto orderDto);

    OrderDto updateOrder(OrderDto orderDto);

    void deleteOrder(Integer orderId);

    List<OrderDto> getAllOrders();

}
