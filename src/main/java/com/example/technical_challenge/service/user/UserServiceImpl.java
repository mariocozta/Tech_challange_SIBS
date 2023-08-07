package com.example.technical_challenge.service.user;

import com.example.technical_challenge.constant.ResponseCode;
import com.example.technical_challenge.db.model.User;
import com.example.technical_challenge.db.repository.UserRepository;
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
public class UserServiceImpl implements IUserService{

    @Autowired
    private final UserRepository userRepository;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public UserDto getUser(Integer userId) {

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()){
            return UserDto.fromEntity(optionalUser.get());
        }
        logger.error("Could not find user with id: {}",userId);
        throw new SIBSRuntimeException(ResponseCode.PROVIDED_ID_INCORRECT);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        UserDto.toEntity(userDto);

        User newUser =  userRepository.save(UserDto.toEntity(userDto));

        return UserDto.fromEntity(newUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User userToSave = UserDto.toEntity(userDto);

        return UserDto.fromEntity(userRepository.save(userToSave));
    }

    @Override
    public void deleteUser(Integer userId) {

        userRepository.deleteById(userId);
        if(userRepository.existsById(userId)){
            logger.error("Could not delete user with id: {}",userId);
            throw new SIBSRuntimeException(ResponseCode.DELETION_FAILED);

        }
    }

    @Override
    public List<UserDto> getAllUsers(){
        return UserDto.fromEntityList(userRepository.findAll());
    }
}
