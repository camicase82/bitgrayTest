package com.kiwi.reactor.repository;

import com.kiwi.reactor.domain.Promotions;
import com.kiwi.reactor.domain.Recharges;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Promotions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromotionsRepository extends JpaRepository<Promotions,Long> {
	
	
	@Query("SELECT p from Promotions p where p.test = false")
	/**
	 * Method to find the recharges average value for a given client since the beggining of time
	 * @param userId the fb user id being requested
	 * @return
	 */
	public List<Promotions> findDefaultPromotions();

}
