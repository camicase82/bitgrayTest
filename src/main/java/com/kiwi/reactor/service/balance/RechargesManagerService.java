package com.kiwi.reactor.service.balance;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kiwi.reactor.domain.Promotions;
import com.kiwi.reactor.domain.Recharges;
import com.kiwi.reactor.domain.SuscriberData;
import com.kiwi.reactor.domain.SysParams;
import com.kiwi.reactor.repository.RechargesRepository;
import com.kiwi.reactor.repository.SuscriberDataRepository;
import com.kiwi.reactor.repository.SysParamsRepository;
import com.kiwi.reactor.service.firebase.FirebaseFacade;
import com.kiwi.reactor.service.promotions.IPromotion;
import com.kiwi.reactor.service.promotions.PromotionManagerService;

@Service
@Transactional
public class RechargesManagerService {

	public static enum PARAMS {SEC_PRICE};
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired 
	private SysParamsRepository sysParamsRepository;
	
	@Autowired
	private PromotionManagerService promotionManagerService;
	
	@Autowired
	private SuscriberDataRepository suscriberDataRepository;
	
	@Autowired
	private RechargesRepository rechargesRepository;
	
	@Autowired
	private BalanceManagerService balanceManagerService;

    private final Logger log = LoggerFactory.getLogger(RechargesManagerService.class);
    
    /**
     * Method in charge of processing a recharge request, here, the base repositories are used
     * and inner balance manager and promotions manager are used
     * @param userId
     * @param value
     */
    public boolean rechargeLine(String userId, double value){
    	boolean returnValue = false;
    	SysParams priceParam = sysParamsRepository.findByName(PARAMS.SEC_PRICE.toString());
    	SuscriberData suscriber = suscriberDataRepository.findByFirebReference(userId);
    	double promotionValue = 0;
    	
    	if(priceParam != null && suscriber != null){
    		//if we got the parameter, next step is to call the promotion service and get current discount
    		Promotions discount = promotionManagerService.getPurchaseDisccount(userId, value);
    		//with the discount we make the change on the database
    		if(discount != null){
    			//value = (value * discount) + discount;
    			String componentName = discount.getModule();
    			IPromotion currentPromotion = (IPromotion)context.getBean(componentName);
    			promotionValue = currentPromotion.calculatePromotion(userId, value);
    			if(promotionValue > 0){
    				value += value * promotionValue;
    			}
    		}
    		//now we calculate the ammount of seconds to buy
    		long awardedSecs = Math.round(value / priceParam.getnValue());
    		//with the awarded secs, we register the recharge and update the account balance
    		Recharges recharge = new Recharges();
    		recharge.setAwardedSecs(awardedSecs);
    		recharge.setDisccount(Double.valueOf(promotionValue));
    		recharge.setDate(LocalDate.now());
    		recharge.setPromotions(discount);
    		recharge.setValue( Math.round(value));
    		recharge.setSuscriber(suscriber);
    		rechargesRepository.save(recharge);
    		returnValue = true;
    		//once the recharge is recorded, we call the balance service to update FB data
    		balanceManagerService.addBalance(userId, awardedSecs);
    		
    		
    		
    	}else{
    		log.error("Basic parameter not configured in the system: price =" 
    									+ priceParam + " Suscriber= "+ suscriber);
    		throw new RuntimeException("Basic parameter not configured in the system: price =" 
    									+ priceParam + " Suscriber= "+ suscriber);
    	}
    	return returnValue;
    }

}
