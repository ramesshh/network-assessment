/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cisco.apicem.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cisco.apicem.Constants;
import com.cisco.apicem.exception.InvalidRequestException;
import com.cisco.apicem.model.ApicEmLoginForm;
import com.cisco.apicem.security.SecurityUtil;
import com.cisco.apicem.service.ApicEmService;
import com.cisco.apicem.service.UserService;
import com.cisco.apicem.util.ServiceURLS;
import com.cisco.apicem.util.URLUtil;

/**
 *
 * @author Ramesh
 */
@RequestMapping(value = Constants.URI_API)
@RestController
public class SignupController {

	private static final Logger log = LoggerFactory.getLogger(SignupController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ApicEmService apicEmService;

	@RequestMapping(value = { "/token" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> getToken(@RequestBody ApicEmLoginForm form, BindingResult errors,
			HttpServletRequest req) {
		if (log.isDebugEnabled()) {
			log.debug("signup data@" + form);
		}

		if (errors.hasErrors()) {
			throw new InvalidRequestException(errors);
		}

		String url = URLUtil
				.constructUrl(form.getApicemIP(), null, form.getVersion(), ServiceURLS.TICKET.value(), null);
		String token = "";
		boolean hasErrors = false;
		try {
			token = apicEmService.getToken(form, url);
		} catch (Exception e) {
			hasErrors = true;
			e.printStackTrace();
		}

		if (StringUtils.isBlank(token) || hasErrors) {
			if (!StringUtils.equalsIgnoreCase(form.getApicemIP(), "sandboxapic.cisco.com")
					&& !StringUtils.equalsIgnoreCase(form.getApicemIP(), "64.103.26.55")) {
				return new ResponseEntity<>(token, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<>(token, HttpStatus.CREATED);
	}

	@RequestMapping(value = { "/apicem" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> onboardApicEm(@RequestBody ApicEmLoginForm form, BindingResult errors,
			HttpServletRequest req) {

		if (errors.hasErrors()) {
			throw new InvalidRequestException(errors);
		}

		if (InetAddressValidator.getInstance().isValidInet4Address(form.getApicemIP())) {
			String userName = SecurityUtil.currentUser().getUsername();
			form.setUserId(userName);
			apicEmService.onBoardApicem(form);
		} else {
			return new ResponseEntity<>("Invalid IP address", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Success", HttpStatus.CREATED);
	}

	@RequestMapping(value = { "/apicem" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ApicEmLoginForm>> getAllApicEMs(HttpServletRequest req) {

		String userName = SecurityUtil.currentUser().getUsername();
		List<ApicEmLoginForm> apicEMList = apicEmService.getApicEms(userName);

		return new ResponseEntity<>(apicEMList, HttpStatus.OK);
	}
}
