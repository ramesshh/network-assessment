/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softql.apicem;

/**
 *
 * @author Ramesh
 */
public class ApiErrors {

	private static final String ERROR_PREFIX = "errors.";
	private static final String SUCCESS_PREFIX = "success.";
	public static final String GENERIC_ERROR = ERROR_PREFIX + "GENERIC";

	public static final String INVALID_REQUEST = ERROR_PREFIX + "INVALID_REQUEST";

	public static final String INVALID_IP = ERROR_PREFIX + "INVALID.IP";

	public static final String ONBOARD = SUCCESS_PREFIX + "ONBOARD.APIC";

	public static final String DELETE_APIC = SUCCESS_PREFIX + "DELETE.APIC";
	public static final String UPDATE_APIC = SUCCESS_PREFIX + "UPDATE.APIC";
	public static final String CONNECT_ERROR = ERROR_PREFIX + "NOTCONNECT.APIC";
	public static final String EXPORT_ERROR = ERROR_PREFIX + "EXPORT_APIC";

}
