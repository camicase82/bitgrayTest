package com.kiwi.reactor.repository;

import com.kiwi.reactor.domain.SysParams;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SysParams entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysParamsRepository extends JpaRepository<SysParams,Long> {
	
	public SysParams findByName(String name);

}
