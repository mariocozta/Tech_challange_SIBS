package com.example.technical_challenge.service.item;

import com.example.technical_challenge.dto.ItemDto;
import com.example.technical_challenge.dto.UserDto;

import java.util.List;

public interface IItemService {

    ItemDto getItem (Integer itemId);

    ItemDto createItem (ItemDto itemDto);

    ItemDto updateItem(ItemDto itemDto);

    void deleteItem(Integer itemId);

    List<ItemDto> getAllItems();
}
