package com.softql.apicem.api.user;

import com.softql.apicem.Constants;
import com.softql.apicem.domain.User;
import com.softql.apicem.model.PasswordForm;
import com.softql.apicem.model.ProfileForm;
import com.softql.apicem.model.UserDetails;
import com.softql.apicem.security.CurrentUser;
import com.softql.apicem.service.UserService;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.URI_API + Constants.URI_SELF)
public class CurrentUserController {

    private static final Logger log = LoggerFactory
            .getLogger(CurrentUserController.class);

    @Inject
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public UserDetails currentUser(@CurrentUser User user) {
        if (log.isDebugEnabled()) {
            log.debug("get current user info");
        }

        UserDetails details = userService.findUserById(user.getId());

        if (log.isDebugEnabled()) {
            log.debug("current user value @" + details);
        }

        return details;
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, params = "aciton=CHANGE_PWD")
    @ResponseBody
    public ResponseEntity<Void> changePassword(
            @CurrentUser User user,
            @RequestBody PasswordForm fm) {
        if (log.isDebugEnabled()) {
            log.debug("change password of user@" + fm);
        }

        userService.updatePassword(user.getId(), fm);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, params = "action=UPDATE_PROFILE")
    @ResponseBody
    public ResponseEntity<Void> updateProfile(
            @CurrentUser User user,
            @RequestBody ProfileForm fm) {
        if (log.isDebugEnabled()) {
            log.debug("update user profile data @" + fm);
        }

        userService.updateProfile(user.getId(), fm);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
