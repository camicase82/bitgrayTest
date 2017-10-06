package com.kiwi.reactor.web.rest.timeprovisioning;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.kiwi.reactor.domain.User;
import com.kiwi.reactor.security.AuthoritiesConstants;
import com.kiwi.reactor.service.balance.RechargesManagerService;
import com.kiwi.reactor.service.dto.UserDTO;
import com.kiwi.reactor.web.rest.UserResource;
import com.kiwi.reactor.web.rest.util.HeaderUtil;
import com.kiwi.reactor.web.rest.vm.ManagedUserVM;

import io.github.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class LineRechargesResource {

	private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private RechargesManagerService rechargesManagerService;
    
    @PostMapping("/lineRecharges")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity rechargeLine(@RequestParam String userId, @RequestParam long value) throws URISyntaxException {
        log.debug("REST request to rechargeLine : {}, {}", userId, value);
        
        boolean recharged = rechargesManagerService.rechargeLine(userId, value);
        if(recharged){
        	return ResponseEntity.ok()
        			.headers(HeaderUtil.createAlert( "Line reacharged", userId))
        			.body(userId);
        }else{
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        			.headers(HeaderUtil.createAlert( "Recharge failed", userId))
        			.body(userId);
        }
        
    }
}
