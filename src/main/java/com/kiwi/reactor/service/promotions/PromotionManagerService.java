package com.kiwi.reactor.service.promotions;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kiwi.reactor.domain.Promotions;
import com.kiwi.reactor.domain.Recharges;
import com.kiwi.reactor.repository.PromotionsRepository;
import com.kiwi.reactor.repository.RechargesRepository;

@Service
@Transactional
public class PromotionManagerService {
	
	@Autowired
	private RechargesRepository rechargesRepository;
	
	@Autowired
	private PromotionsRepository promotionsRepository;

    private final Logger log = LoggerFactory.getLogger(PromotionManagerService.class);
    
    public PromotionManagerService(){}
    
    /**
     * Method in charge of calculating the current discount for a given operation
     * this method validate the business rules to apply promotions, the using the
     * component name invokes the needed promotion
     * @param userId
     * @param rechargeValue
     * @return
     */
    public Promotions getPurchaseDisccount(String userId, double rechargeValue){
    	Promotions returnValue = null;
    	List<Recharges> appliedPromotions = rechargesRepository.findPromotionsAppliedOnLastDay(userId);
    	//First we check if promotions has been applied this day and calculate the discount price
    	if(appliedPromotions.size() == 0){
    		//We look if the user has a configured promotion if not, we get the global one
    		List<Promotions> enabledPromotions = promotionsRepository.findDefaultPromotions();
    		//more logic could be configured, here we only use the first valid pormotion
    		if(enabledPromotions.size() > 0){
    			returnValue = enabledPromotions.get(0);
    		}
    	}
    	return returnValue;
    }

}
