package com.kh.test.user.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.test.user.model.dto.User;
import com.kh.test.user.model.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService service;
	
	
	// 이름 검색
	// Model : 값을 request scope로 전달하는 객체 
	
	@GetMapping("selectName")
	public String selectName(
		@RequestParam("inputName") String inputName,
		Model model) {
		
		
		// 이름으로 조회 -> 결과 행 여러 개 가능성 O
		// -> List로 결과 반환 받기
		List<User> userList = service.selectName(inputName);
		
		
		// User 1행 조회 시
		// 결과 없음 ->  null 인지 확인
		
		// User N행 조회 (List 조회)
		// 결과 없음 -> List.isEmpty() == true 인지 확인
		// (List는 조회 결과가 없어도 List 객체는 무조건 생성됨!!)
		//  --> null 아님!!!
		
		String path = null;
		
		if(userList.isEmpty()) { // 결과 없음
			path = "searchFail"; // forward (요청 위임)
		
		} else {
			path = "searchSuccess";
			model.addAttribute("userList", userList);
		}
		
		return path;
	}
	
	
	
	

}

