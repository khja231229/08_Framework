package edu.kh.api.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kh.api.dto.Dong;
import edu.kh.api.dto.Dosi;
import edu.kh.api.dto.Sigungu;

@Controller
public class DosiController {
	private String serviceKey = "BTjX5Rqk5SZSwrW687YxxqoqoH7FbYV%2FBKqfde1PPn0jiIoOy6aYAUb1MuK7h9izWzM%2FYX6SVOjBBUMIuwRRIg%3D%3D";
	
	// 추출된 데이터를 저장할 List
    private List<Dosi> dosiList = new ArrayList<>();
    private List<Sigungu> sigunguList = new ArrayList<>();
    private List<Dong> dongList = new ArrayList<>();
    
    // pk번호
    private int dosiNo = 1;
    private int sigunguNo = 1;
    private int dongNo = 1;
	
	
	@GetMapping("dong")
	public String dong(
		@RequestParam("page") int page,
		@RequestParam("perPage") int perPage
		) throws IOException, JSONException {
		
		for(int i=1 ; i<=5; i++) {
			String resp = requestDong(i, perPage);
			
			JSONObject j1 = new JSONObject(resp);
			JSONArray data = new JSONArray(j1.getString("data"));
			
			convertData(data);
		}
		
		System.out.println("dosiList : " + dosiList.size());
		System.out.println("sigunguList : " + sigunguList.size());
		System.out.println("dongList : " + dongList.size());
        
		return "redirect:/";
	}
	
	
	
	
	private String requestDong(int page, int perPage) throws IOException {
		
		StringBuilder urlBuilder = new StringBuilder("http://api.odcloud.kr/api/15063424/v1/uddi:257e1510-0eeb-44de-8883-8295c94dadf7"); /*URL*/
		
		urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") 	+ "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("page","UTF-8") 		+ "=" + URLEncoder.encode(String.valueOf(page), "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("perPage","UTF-8") 		+ "=" + URLEncoder.encode(String.valueOf(perPage), "UTF-8")); /*한 페이지 결과 수*/
        
		
        URL url = new URL(urlBuilder.toString());
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("GET"); // 요청 메서드 GET
        
        conn.setRequestProperty("Content-type", "application/json");
        
        // 응답 상태 코드(200 : 성공, 404 : 페이지 찾을 수 없음 ...)
        System.out.println("Response code: " + conn.getResponseCode());
        
        
        // 입력 보조 스트림
        BufferedReader rd;
        
        // 응답 상태 코드가 200 ~ 300인 경우 (요청/응답 성공인 경우)
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            
        	// InputStreamReader : 바이트 기반 입력 스트림을 문자 기반으로 변경
        	
        	// 문자 형태로 빨리 입력 받기 위한 준비(응답 데이터 빨리 받기)
        	rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        
        
        StringBuilder sb = new StringBuilder();
        String line;
        
        // 데이터를 한 줄 씩 계속 읽어 들여서 sb에 추가
        // 더 이상 읽을 내용이 없으면 반복문 종료
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        
        rd.close(); // 다 쓴 스트림 자원 반환
        conn.disconnect(); // 외부 요청 연결 끊기
        
		
		return sb.toString();
	}
	
	
	
	
	private void convertData(JSONArray data) throws JsonMappingException, JsonProcessingException, JSONException {
        // 현재 조회된 시/구/동
        Dosi currentDosi = null;
        Sigungu currentSigungu = null;
        Dong currentDong = null;
        
        for(int i=0 ; i<data.length() ; i++) {
        	ObjectMapper om = new ObjectMapper();
        	Map<String, String> map = om.readValue(data.getString(i), Map.class);
        	
        	// 없으면  null
//        	System.out.println(map.get("시도명"));
//        	System.out.println(map.get("시군구명"));
//        	System.out.println(map.get("읍면동명"));
        	
        	// 조회된 데이터에서 도시가 바뀐 경우
        	if(currentDosi == null || !currentDosi.getDosiName().equals(map.get("시도명"))) {
        		currentDosi = new Dosi(dosiNo++, map.get("시도명"));
        		
        		// 필터링 로직 추가!!!
        		
        		
        		dosiList.add(currentDosi);
        		
        		currentSigungu = null;
                currentDong = null;
                continue;
        	}
        	
        	// 조회된 데이터에서 시군구가 바뀐 경우
    		if(currentSigungu == null || !currentSigungu.getSigunguName().equals(map.get("시군구명"))) {
    			
        		currentSigungu = new Sigungu(dosiNo, sigunguNo++, map.get("시군구명"));
        		
        		sigunguList.add(currentSigungu);
        		
        		currentDong = null;
        		
                continue;
        	}
        	
    		// 조회된 데이터에서 읍면동이 바뀐 경우
        	if(currentDong == null || !currentDong.getDongName().equals(map.get("읍면동명"))) {
        		currentDong = new Dong(dongNo++, sigunguNo, map.get("읍면동명"));
        		
        		dongList.add(currentDong);
        	}
        }
	}
	
	
	
}
