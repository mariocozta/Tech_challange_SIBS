package com.example.technical_challenge.controller;


import com.example.technical_challenge.dto.ItemDto;
import com.example.technical_challenge.dto.UserDto;
import com.example.technical_challenge.service.item.IItemServiceImpl;
import com.example.technical_challenge.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private IItemServiceImpl itemService;

    @GetMapping("/getItem")
    public ItemDto getUser(@RequestBody Integer itemId)
    {
        return itemService.getItem(itemId);
    }

    @PostMapping("/createItem")
    public ItemDto createUser(@RequestBody ItemDto itemDto)
    {
        return itemService.createItem(itemDto);
    }

    @PutMapping("/updateItem")
    public ItemDto updateUser(@RequestBody ItemDto itemDto)
    {
        return itemService.updateItem(itemDto);
    }

    @DeleteMapping("/deleteItem")
    public boolean deleteUser(@RequestBody Integer itemId){
        itemService.deleteItem(itemId);
        return true;
    }

    @GetMapping("/getAllItems")
    public List<ItemDto> getAllUsers(){
        return itemService.getAllItems();
    }
}
