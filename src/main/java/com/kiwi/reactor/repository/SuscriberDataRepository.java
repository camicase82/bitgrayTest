package com.kiwi.reactor.repository;

import com.kiwi.reactor.domain.SuscriberData;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SuscriberData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuscriberDataRepository extends JpaRepository<SuscriberData,Long> {

	public SuscriberData findByFirebReference(String firebReference);
}
