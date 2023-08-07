package com.example.technical_challenge.service.stockMovement;

import com.example.technical_challenge.constant.ResponseCode;
import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.Order;
import com.example.technical_challenge.db.model.StockMovement;
import com.example.technical_challenge.db.repository.OrderRepository;
import com.example.technical_challenge.db.repository.StockMovementRepository;
import com.example.technical_challenge.dto.StockMovementDto;
import com.example.technical_challenge.exception.SIBSRuntimeException;
import com.example.technical_challenge.service.email.IEmailService;
import com.example.technical_challenge.service.order.IOrderService;
import com.example.technical_challenge.service.order.OrderServiceImpl;
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
public class StockMovementServiceImpl implements IStockMovementService {

    @Autowired
    private final StockMovementRepository stockMovementRepository;

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final IEmailService emailService;

    private static final Logger logger = LogManager.getLogger(StockMovementServiceImpl.class);

    @Override
    public StockMovementDto getStockMovement(Integer stockMovementId) {

        Optional<StockMovement> optionalStockMovement = stockMovementRepository.findById(stockMovementId);
        if (optionalStockMovement.isPresent()){
            return StockMovementDto.fromEntity(optionalStockMovement.get());
        }
        logger.error("Could not find stock movement with id: {}",stockMovementId);
        throw new SIBSRuntimeException(ResponseCode.PROVIDED_ID_INCORRECT);
    }

    @Override
    public StockMovementDto createStockMovement(StockMovementDto stockMovementDto) {
        StockMovement stockMovement = StockMovementDto.toEntity(stockMovementDto);
        stockMovement.setCreationDate(new Date());
        stockMovement.setCurrentQuantity(stockMovement.getOriginalQuantity());
        StockMovement newStockMovement =  stockMovementRepository.save(stockMovement);

        this.tryToFulfilOrdersForGivenItem(stockMovement.getItem());

        return StockMovementDto.fromEntity(newStockMovement);
    }

    @Override
    public List<StockMovement> getAllStockMovementsNeededToFulfilOrder (Order order){

        List<StockMovement> stockMovementsWithPositiveStock = stockMovementRepository.findByItemWhereCurrentQuantityBiggerThanZero(order.getItem().getId());
        Integer orderItemQuantity = order.getQuantity();
        Integer currentOrderItemQuantity = orderItemQuantity;
        Integer currentStockOfItem = stockMovementsWithPositiveStock.stream().mapToInt(StockMovement::getCurrentQuantity).sum();

        List<StockMovement> stockMovementsUsedForFulfilment = new ArrayList<>();

        if(currentStockOfItem > orderItemQuantity){
            for (StockMovement stockMovement : stockMovementsWithPositiveStock) {
                if(stockMovement.getCurrentQuantity() < currentOrderItemQuantity){
                    currentOrderItemQuantity = currentOrderItemQuantity - stockMovement.getCurrentQuantity();
                    stockMovement.setCurrentQuantity(0);
                    stockMovementsUsedForFulfilment.add(stockMovement);
                }else{
                    stockMovement.setCurrentQuantity(stockMovement.getCurrentQuantity()- currentOrderItemQuantity);
                    stockMovementsUsedForFulfilment.add(stockMovement);
                    break;
                }
            }
        }
        return stockMovementsUsedForFulfilment;
    }

    @Override
    public void tryToFulfilOrdersForGivenItem(Item item){
        List<Order> unfulfilledOrders = orderRepository.getAllByItemAndIsFulfilledIsFalseOrderByCreationDateAsc(item);

        for ( Order unfulfilledOrder : unfulfilledOrders){
            if(unfulfilledOrder.getQuantity() < this.getCurrentStockOfItem(item)) {
                List<StockMovement> stockMovements = this.getAllStockMovementsNeededToFulfilOrder(unfulfilledOrder);
                if (stockMovements.size() > 0) {
                    unfulfilledOrder.setIsFulfilled(true);
                    orderRepository.save(unfulfilledOrder);
                    stockMovementRepository.saveAll(stockMovements);
                    traceStockMovements(unfulfilledOrder.getId(),stockMovements);
                    emailService.sendEmail(unfulfilledOrder.getUserWhoCreatedOrder().getEmail(),String.format("Order %2d fulfilled",unfulfilledOrder.getId()),String.format("Your order with id %2d was fulfilled", unfulfilledOrder.getId()));
                    logger.info("Email of order fulfilled sent to email {}",unfulfilledOrder.getUserWhoCreatedOrder().getEmail());
                }
            }else{
                break;
            }
        }
    }

    @Override
    public StockMovementDto updateStockMovement(StockMovementDto stockMovementDto) {
        StockMovement stockMovementToSave = StockMovementDto.toEntity(stockMovementDto);

        return StockMovementDto.fromEntity(stockMovementRepository.save(stockMovementToSave));
    }

    @Override
    public void deleteStockMovement(Integer stockMovementId) {

        stockMovementRepository.deleteById(stockMovementId);
        if(stockMovementRepository.existsById(stockMovementId)){
            logger.error("Could not delete stock movement with id: {}",stockMovementId);
            throw new SIBSRuntimeException(ResponseCode.DELETION_FAILED);
        }
    }

    @Override
    public List<StockMovementDto> getAllStockMovements(){
        return StockMovementDto.fromEntityList(stockMovementRepository.findAll());
    }


    @Override
    public Integer getCurrentStockOfItem(Item item){
        List<StockMovement> stockMovementsWithPositiveStock = stockMovementRepository.findByItemWhereCurrentQuantityBiggerThanZero(item.getId());
        return stockMovementsWithPositiveStock.stream().mapToInt(StockMovement::getCurrentQuantity).sum();
    }
    private void traceStockMovements(Integer orderId,List<StockMovement> stockMovements){
        String idsOfStockMovements =  stockMovements.stream().map(StockMovement::getId).map(String::valueOf).collect(Collectors.joining(", "));
        logger.info("Order {} was fulfilled using the following stock movements: {}",orderId, idsOfStockMovements);
    }
}
