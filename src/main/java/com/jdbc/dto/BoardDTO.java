package com.jdbc.dto;

import lombok.Data;

//data == Getters and Setters
@Data
public class BoardDTO {

//COMMIT TEST

	private int num;

	private String name;
	private String pwd;
	private String email;
	private String subject;
	private String content;
	private String ipAddr;
	private String created;
	private int hitCount;
	
	
}
