package com.example.technical_challenge.service.order;

import com.example.technical_challenge.constant.ResponseCode;
import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.Order;
import com.example.technical_challenge.db.model.StockMovement;
import com.example.technical_challenge.db.repository.OrderRepository;
import com.example.technical_challenge.db.repository.StockMovementRepository;
import com.example.technical_challenge.dto.ItemDto;
import com.example.technical_challenge.dto.OrderDto;
import com.example.technical_challenge.exception.SIBSRuntimeException;
import com.example.technical_challenge.service.email.IEmailService;
import com.example.technical_challenge.service.stockMovement.IStockMovementService;
import lombok.AllArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final StockMovementRepository stockMovementRepository;

    @Autowired
    private final IStockMovementService stockMovementService;

    @Autowired
    private final IEmailService emailService;

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);


    @Override
    public OrderDto getOrder(Integer orderId) {

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()){
            return OrderDto.fromEntity(optionalOrder.get());
        }
        logger.error("Could not find order with id: {}",orderId);
        throw new SIBSRuntimeException(ResponseCode.PROVIDED_ID_INCORRECT);
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
        }else{
            newOrder.setIsFulfilled(false);
        }

        newOrder.setCreationDate(new Date());
        newOrder =  orderRepository.save(newOrder);
        if(stockMovementsNeededToFulfilOrder.size() > 0) {
            stockMovementRepository.saveAll(stockMovementsNeededToFulfilOrder);
            traceStockMovements(newOrder.getId(), stockMovementsNeededToFulfilOrder);
        }
        if(newOrder.getIsFulfilled()){
            emailService.sendEmail(newOrder.getUserWhoCreatedOrder().getEmail(),String.format("Order %2d fulfilled",newOrder.getId()),String.format("Your order with id %2d was fulfilled", newOrder.getId()));
            logger.info("Email of order fulfilled sent to email {}",newOrder.getUserWhoCreatedOrder().getEmail());
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
            logger.error("Could not delete order with id: {}",orderId);
            throw new SIBSRuntimeException(ResponseCode.DELETION_FAILED);
        }
    }

    @Override
    public List<OrderDto> getAllOrders(){
        return OrderDto.fromEntityList(orderRepository.findAll());
    }

    private void traceStockMovements(Integer orderId,List<StockMovement> stockMovements){
        String idsOfStockMovements =  stockMovements.stream().map(StockMovement::getId).map(String::valueOf).collect(Collectors.joining(", "));
        logger.trace("Order {} was fulfilled using the following stock movements: {}",orderId, idsOfStockMovements);
    }

}
