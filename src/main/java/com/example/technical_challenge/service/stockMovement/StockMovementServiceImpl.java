package com.example.technical_challenge.service.stockMovement;

import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.Order;
import com.example.technical_challenge.db.model.StockMovement;
import com.example.technical_challenge.db.repository.OrderRepository;
import com.example.technical_challenge.db.repository.StockMovementRepository;
import com.example.technical_challenge.dto.StockMovementDto;
import com.example.technical_challenge.service.order.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StockMovementServiceImpl implements IStockMovementService {

    @Autowired
    private final StockMovementRepository stockMovementRepository;

    @Autowired
    private final OrderRepository orderRepository;

    @Override
    public StockMovementDto getStockMovement(Integer stockMovementId) {

        Optional<StockMovement> optionalStockMovement = stockMovementRepository.findById(stockMovementId);
        if (optionalStockMovement.isPresent()){
            return StockMovementDto.fromEntity(optionalStockMovement.get());
        }
        //TROW ERROR
        return null;
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
            //TROW ERROR
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
}
