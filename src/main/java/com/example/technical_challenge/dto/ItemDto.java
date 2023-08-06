package com.example.technical_challenge.dto;

import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private int id;

    private String name;

    public static Item toEntity(ItemDto itemDto){

        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())

                .build();
    }

    public static ItemDto fromEntity(Item item){
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

    public static List<ItemDto> fromEntityList(List<Item> items) {
        return items.stream()
                .map(ItemDto::fromEntity)
                .collect(Collectors.toList());
    }
}
