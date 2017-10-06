package com.kiwi.reactor.repository;

import com.kiwi.reactor.domain.Recharges;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Recharges entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RechargesRepository extends JpaRepository<Recharges,Long> {
	
	@Query("SELECT avg(r.value) from Recharges r where r.suscriber.firebReference =:userId")
	/**
	 * Method to find the recharges average value for a given client since the beggining of time
	 * @param userId the fb user id being requested
	 * @return
	 */
	public Long findAverageByUserId(@Param("userId") String userId);
	
	
	@Query("SELECT r FROM Recharges r where r.date = current_date and r.disccount > 0 and r.suscriber.firebReference =:userId")
	/**
	 * Method to find the recharges average value for a given client since the beggining of time
	 * @param userId the fb user id being requested
	 * @return
	 */
	public List<Recharges> findPromotionsAppliedOnLastDay(@Param("userId") String userId);

}
