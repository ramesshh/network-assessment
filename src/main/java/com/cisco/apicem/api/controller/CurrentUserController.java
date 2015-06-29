package com.cisco.apicem.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cisco.apicem.Constants;
import com.cisco.apicem.domain.User;
import com.cisco.apicem.model.UserDetails;
import com.cisco.apicem.security.CurrentUser;
import com.cisco.apicem.service.UserService;

@RestController
@RequestMapping(value = Constants.URI_API + Constants.URI_SELF)
public class CurrentUserController {

	private static final Logger log = LoggerFactory.getLogger(CurrentUserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public UserDetails currentUser(@CurrentUser User user) {
		if (log.isDebugEnabled()) {
			log.debug("get current user info");
		}

		UserDetails details = userService.findByUsername(user.getUsername());

		if (log.isDebugEnabled()) {
			log.debug("current user value @" + details);
		}

		return details;
	}

}
