package com.example.technical_challenge.controller;


import com.example.technical_challenge.dto.ItemDto;
import com.example.technical_challenge.dto.UserDto;
import com.example.technical_challenge.service.item.IItemServiceImpl;
import com.example.technical_challenge.service.user.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    private IItemServiceImpl itemService;

    @GetMapping("/getItem")
    public ItemDto getUser(@RequestBody Integer itemId)
    {
        return itemService.getItem(itemId);
    }

    @PostMapping("/createItem")
    public ItemDto createUser(@RequestBody ItemDto userDto)
    {
        return itemService.createItem(userDto);
    }

    @PutMapping("/updateItem")
    public ItemDto updateUser(@RequestBody ItemDto userDto)
    {
        return itemService.updateItem(userDto);
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
