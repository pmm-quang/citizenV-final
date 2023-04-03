package com.citizenv.app.service;

import com.citizenv.app.entity.User;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.payload.UserDto;
import com.citizenv.app.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository repository;

    Logger logger = LogManager.getRootLogger();

    public List<UserDto> getAll() {
        List<User> entities = repository.findAll();
        return entities.stream().map(l-> mapper.map(l, UserDto.class)).collect(Collectors.toList());
    }

    public UserDto getById(String userId) {
        User province = repository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", Long.parseLong(userId)));
        return mapper.map(province, UserDto.class);
    }

    public UserDto createUser(Map<String, Object> JSONInfoAsMap) {
        return null;
    }

    public UserDto updateUser(Map<String, Object> JSONInfoAsMap) {

        return null;
    }

    public String deleteById(String userId) {
        return "Deleted";
    }
}
