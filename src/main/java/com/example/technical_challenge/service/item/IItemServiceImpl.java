package com.example.technical_challenge.service.item;

import com.example.technical_challenge.constant.ResponseCode;
import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.User;
import com.example.technical_challenge.db.repository.ItemRepository;
import com.example.technical_challenge.db.repository.UserRepository;
import com.example.technical_challenge.dto.ItemDto;
import com.example.technical_challenge.dto.UserDto;
import com.example.technical_challenge.exception.SIBSRuntimeException;
import com.example.technical_challenge.service.order.OrderServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IItemServiceImpl implements IItemService {

    @Autowired
    private final ItemRepository itemRepository;

    private static final Logger logger = LogManager.getLogger(IItemServiceImpl.class);

    @Override
    public ItemDto getItem(Integer itemId) {

        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()){
            return ItemDto.fromEntity(optionalItem.get());
        }
        logger.error("Could not find item with id: {}",itemId);
        throw new SIBSRuntimeException(ResponseCode.PROVIDED_ID_INCORRECT);
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
    public void deleteItem(Integer itemId) {

        itemRepository.deleteById(itemId);
        if(itemRepository.existsById(itemId)){
            logger.error("Could not delete item with id: {}",itemId);
            throw new SIBSRuntimeException(ResponseCode.DELETION_FAILED);
        }
    }

    @Override
    public List<ItemDto> getAllItems(){
        return ItemDto.fromEntityList(itemRepository.findAll());
    }
}
