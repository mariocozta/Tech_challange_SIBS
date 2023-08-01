package com.example.technical_challenge.service.item;

import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.User;
import com.example.technical_challenge.db.repository.ItemRepository;
import com.example.technical_challenge.db.repository.UserRepository;
import com.example.technical_challenge.dto.ItemDto;
import com.example.technical_challenge.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IItemServiceImpl implements IItemService {

    @Autowired
    private final ItemRepository itemRepository;

    @Override
    public ItemDto getItem(Integer userId) {

        Optional<Item> optionalItem = itemRepository.findById(userId);
        if (optionalItem.isPresent()){
            return ItemDto.fromEntity(optionalItem.get());
        }
        //TROW ERROR
        return null;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto) {
        ItemDto.toEntity(itemDto);

        Item item =  itemRepository.save(ItemDto.toEntity(itemDto));

        return ItemDto.fromEntity(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        Item itemToSave = ItemDto.toEntity(itemDto);

        return ItemDto.fromEntity(itemRepository.save(itemToSave));
    }

    @Override
    public void deleteItem(Integer userId) {

        itemRepository.deleteById(userId);
        if(itemRepository.existsById(userId)){
            //TROW ERROR
        }
    }

    @Override
    public List<ItemDto> getAllItems(){
        return ItemDto.fromEntityList(itemRepository.findAll());
    }
}
