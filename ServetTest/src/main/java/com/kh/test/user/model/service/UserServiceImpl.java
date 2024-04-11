package com.kh.test.user.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.test.user.model.dto.User;
import com.kh.test.user.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service // bean 등록 
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	// 의존성 주입 (DI)
	private final UserMapper mapper;
	
	@Override
	public List<User> selectName(String inputName) {
		return mapper.selectName(inputName);
	}
	
	
	
	
	
}


