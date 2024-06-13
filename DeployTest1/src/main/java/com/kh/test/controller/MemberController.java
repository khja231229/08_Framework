package com.kh.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

	@PostMapping("loginTest")
	public String loginTest(
		@RequestParam("id") String id,
		@RequestParam("pw") String pw,
		Model model) {
		
		// id가 "user01"
		// pw가 "pass01"
		
		String result = null;
		
		if(id.equals("user01") && pw.equals("pass01")) {
			result = "성공";
		}else {
			result = "실패";
		}
		
		// 타임리프 접두사 : classpath:/templates/
		// 타임리프 접미사 : .html
		
		model.addAttribute("result", result);
		
		return "loginResult";
	}
}
