package com.kiwi.reactor.service.promotions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kiwi.reactor.repository.RechargesRepository;

@Component("DefaultPromotion")
public class DefaultPromotion implements IPromotion{
	
	@Autowired
	private RechargesRepository rechargesRepository;

	@Override
	public double calculatePromotion(String userId, double rechargeValue) {
		double returnValue = 0;
		Long rechargeAvg = rechargesRepository.findAverageByUserId(userId);
		//if current recharge is ahead of current promedium ertur .10;
		if(rechargeAvg == null){	
			returnValue = 0.10d;
		}else
		if(rechargeValue > rechargeAvg){
			returnValue = 0.10d;
		}else{
			//now we get the min average of latest week
			returnValue = 0.5d;
		}
		return returnValue;
	}

}
