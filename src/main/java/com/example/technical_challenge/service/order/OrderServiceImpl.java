package com.example.technical_challenge.service.order;

import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.Order;
import com.example.technical_challenge.db.model.StockMovement;
import com.example.technical_challenge.db.repository.OrderRepository;
import com.example.technical_challenge.db.repository.StockMovementRepository;
import com.example.technical_challenge.dto.ItemDto;
import com.example.technical_challenge.dto.OrderDto;
import com.example.technical_challenge.service.stockMovement.IStockMovementService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final StockMovementRepository stockMovementRepository;

    @Autowired
    private final IStockMovementService stockMovementService;

    @Override
    public OrderDto getOrder(Integer orderId) {

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()){
            return OrderDto.fromEntity(optionalOrder.get());
        }
        //TROW ERROR
        return null;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {

        Item itemFromOrder = ItemDto.toEntity(orderDto.getItemDto());
        Order newOrder = OrderDto.toEntity(orderDto);

        List<StockMovement> stockMovementsNeededToFulfilOrder = new ArrayList<>();

        if(orderRepository.countUnfulfilledOrdersForItem(itemFromOrder) <= 0 ){

            stockMovementsNeededToFulfilOrder = stockMovementService.getAllStockMovementsNeededToFulfilOrder(newOrder);

            if(stockMovementsNeededToFulfilOrder.size() > 0 ){
                newOrder.setIsFulfilled(true);
            }else{
                newOrder.setIsFulfilled(false);
            }
        }

        newOrder.setCreationDate(new Date());
        newOrder =  orderRepository.save(newOrder);
        if(stockMovementsNeededToFulfilOrder.size() > 0) {
            stockMovementRepository.saveAll(stockMovementsNeededToFulfilOrder);
        }
        return OrderDto.fromEntity(newOrder);
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto) {
        Order orderToSave = OrderDto.toEntity(orderDto);

        return OrderDto.fromEntity(orderRepository.save(orderToSave));
    }

    @Override
    public void deleteOrder(Integer orderId) {

        orderRepository.deleteById(orderId);
        if(orderRepository.existsById(orderId)){
            //TROW ERROR
        }
    }

    @Override
    public List<OrderDto> getAllOrders(){
        return OrderDto.fromEntityList(orderRepository.findAll());
    }


}
