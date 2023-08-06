package com.example.technical_challenge.controller;

import com.example.technical_challenge.dto.OrderDto;
import com.example.technical_challenge.dto.StockMovementDto;
import com.example.technical_challenge.service.order.IOrderService;
import com.example.technical_challenge.service.stockMovement.IStockMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("/getOrder")
    public OrderDto getOrdert(@RequestBody Integer orderId)
    {
        return orderService.getOrder(orderId);
    }

    @PostMapping("/createOrder")
    public OrderDto createOrder(@RequestBody OrderDto orderDto)
    {
        return orderService.createOrder(orderDto);
    }

    @PutMapping("/updateOrder")
    public OrderDto updateOrder(@RequestBody OrderDto orderDto)
    {
        return orderService.updateOrder(orderDto);
    }

    @DeleteMapping("/deleteOrder")
    public boolean deleteUser(@RequestBody Integer orderId){
        orderService.deleteOrder(orderId);
        return true;
    }

    @GetMapping("/getAllOrders")
    public List<OrderDto> getAllOrders(){
        return orderService.getAllOrders();
    }
}
