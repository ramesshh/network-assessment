/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cisco.apicem.exception;

/**
 *
 * @author Ramesh
 */
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String userName;

	public ResourceNotFoundException(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
