package com.example.technical_challenge.dto;

import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.StockMovement;
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
public class StockMovementDto {

    private int id;

    private Date creationDate;

    private Item item;

    private int originalQuantity;

    private int currentQuantity;

    public static StockMovement toEntity(StockMovementDto stockMovementDto){

        return StockMovement.builder()
                .id(stockMovementDto.getId())
                .creationDate(stockMovementDto.getCreationDate())
                .item(stockMovementDto.getItem())
                .originalQuantity(stockMovementDto.getOriginalQuantity())
                .currentQuantity(stockMovementDto.getCurrentQuantity())
                .build();
    }

    public static StockMovementDto fromEntity(StockMovement stockMovement){
        return StockMovementDto.builder()
                .id(stockMovement.getId())
                .creationDate(stockMovement.getCreationDate())
                .item(stockMovement.getItem())
                .originalQuantity(stockMovement.getOriginalQuantity())
                .currentQuantity(stockMovement.getCurrentQuantity())
                .build();
    }

    public static List<StockMovementDto> fromEntityList(List<StockMovement> stockMovements) {
        return stockMovements.stream()
                .map(StockMovementDto::fromEntity)
                .collect(Collectors.toList());
    }
}
