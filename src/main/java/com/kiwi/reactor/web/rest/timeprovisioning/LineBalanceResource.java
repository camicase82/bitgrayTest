package com.kiwi.reactor.web.rest.timeprovisioning;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.kiwi.reactor.security.AuthoritiesConstants;
import com.kiwi.reactor.service.balance.BalanceManagerService;
import com.kiwi.reactor.service.balance.RechargesManagerService;
import com.kiwi.reactor.web.rest.UserResource;
import com.kiwi.reactor.web.rest.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class LineBalanceResource {
	
	private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private BalanceManagerService balanceManagerService;
    
    @GetMapping("/balance")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity getBalance(@RequestParam String userId) throws URISyntaxException {
        log.debug("REST request to getBalance : {}, {}", userId);
        
        long balance = balanceManagerService.getSecsLeft(userId);
        
        return ResponseEntity.ok()
			.headers(HeaderUtil.createAlert( "Balance retrieved", "" + balance))
			.body(balance);
    }
    
    @DeleteMapping("/balance")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity removeBalance(@RequestParam String userId, @RequestParam int secs) throws URISyntaxException {
        log.debug("REST request to getBalance : {}, {}", userId);
        //first we check if the operation ca be made
        long secsLeft = balanceManagerService.getSecsLeft(userId);
        if(secsLeft > secs){
        	balanceManagerService.removeBalance(userId, secs);
            
            return ResponseEntity.ok()
    			.headers(HeaderUtil.createAlert( "Balance retrieved", userId))
    			.body(userId);
        }else{
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        			.headers(HeaderUtil.createAlert( "Remove balance failed", "Ammount requested is higher than current balance " + secsLeft))
        			.body("Remove balance failed" + "Ammount requested is higher than current balance " + secsLeft);
        }
        
    }

}
