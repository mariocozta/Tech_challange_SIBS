package com.example.technical_challenge.controller;


import com.example.technical_challenge.dto.ItemDto;
import com.example.technical_challenge.dto.StockMovementDto;
import com.example.technical_challenge.service.item.IItemServiceImpl;
import com.example.technical_challenge.service.stockMovement.IStockMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stockMovement")
public class StockMovementController {

    @Autowired
    private IStockMovementService stockMovementService;

    @GetMapping("/getStockMovement")
    public StockMovementDto getStockMovement(@RequestBody Integer stockMovementId)
    {
        return stockMovementService.getStockMovement(stockMovementId);
    }

    @PostMapping("/createStockMovement")
    public StockMovementDto createStockMovement(@RequestBody StockMovementDto stockMovementDto)
    {
        return stockMovementService.createStockMovement(stockMovementDto);
    }

    @PutMapping("/updateStockMovement")
    public StockMovementDto updateStockMovement(@RequestBody StockMovementDto stockMovementDto)
    {
        return stockMovementService.updateStockMovement(stockMovementDto);
    }

    @DeleteMapping("/deleteStockMovement")
    public boolean deleteUser(@RequestBody Integer stockMovementId){
        stockMovementService.deleteStockMovement(stockMovementId);
        return true;
    }

    @GetMapping("/getAllStockMovements")
    public List<StockMovementDto> getAllStockMovements(){
        return stockMovementService.getAllStockMovements();
    }
}
