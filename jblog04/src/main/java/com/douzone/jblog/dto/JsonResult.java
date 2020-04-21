package com.douzone.jblog.dto;

public class JsonResult {
	private String result;  /* "success" or "fail" */
	private Object data;    /* if success, Data set */
	private String message; /* if fail, Error Message set */
	
	// 생성자를 private로 묶어 강제한다 => 싱글톤 패턴
	private JsonResult(Object data) {
		result = "success"; // 데이터가 들어 온다면 성공...
		this.data = data;   // Json 데이터 세팅
	}

	private JsonResult(String message) {
		result = "fail";  // 메세시가 들어 온다면 실패...
		this.message = message; // 오류메세지 세팅
	}

	public static JsonResult success(Object data) {
		return new JsonResult(data); // 생성자에 데이터를 인자로 넘겨줌
	}

	public static JsonResult fail(String message) {
		return new JsonResult(message); // 생성자에 메세지를 인자로 넘겨줌
	}

	public String getResult() {
		return result;
	}
	public Object getData() {
		return data;
	}
	public String getMessage() {
		return message;
	}
}
