package com.kiwi.reactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kiwi.reactor.domain.Recharges;

import com.kiwi.reactor.repository.RechargesRepository;
import com.kiwi.reactor.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Recharges.
 */
@RestController
@RequestMapping("/api")
public class RechargesResource {

    private final Logger log = LoggerFactory.getLogger(RechargesResource.class);

    private static final String ENTITY_NAME = "recharges";
        
    private final RechargesRepository rechargesRepository;

    public RechargesResource(RechargesRepository rechargesRepository) {
        this.rechargesRepository = rechargesRepository;
    }

    /**
     * POST  /recharges : Create a new recharges.
     *
     * @param recharges the recharges to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recharges, or with status 400 (Bad Request) if the recharges has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recharges")
    @Timed
    public ResponseEntity<Recharges> createRecharges(@Valid @RequestBody Recharges recharges) throws URISyntaxException {
        log.debug("REST request to save Recharges : {}", recharges);
        if (recharges.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new recharges cannot already have an ID")).body(null);
        }
        Recharges result = rechargesRepository.save(recharges);
        return ResponseEntity.created(new URI("/api/recharges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recharges : Updates an existing recharges.
     *
     * @param recharges the recharges to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recharges,
     * or with status 400 (Bad Request) if the recharges is not valid,
     * or with status 500 (Internal Server Error) if the recharges couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recharges")
    @Timed
    public ResponseEntity<Recharges> updateRecharges(@Valid @RequestBody Recharges recharges) throws URISyntaxException {
        log.debug("REST request to update Recharges : {}", recharges);
        if (recharges.getId() == null) {
            return createRecharges(recharges);
        }
        Recharges result = rechargesRepository.save(recharges);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recharges.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recharges : get all the recharges.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of recharges in body
     */
    @GetMapping("/recharges")
    @Timed
    public List<Recharges> getAllRecharges() {
        log.debug("REST request to get all Recharges");
        List<Recharges> recharges = rechargesRepository.findAll();
        return recharges;
    }

    /**
     * GET  /recharges/:id : get the "id" recharges.
     *
     * @param id the id of the recharges to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recharges, or with status 404 (Not Found)
     */
    @GetMapping("/recharges/{id}")
    @Timed
    public ResponseEntity<Recharges> getRecharges(@PathVariable Long id) {
        log.debug("REST request to get Recharges : {}", id);
        Recharges recharges = rechargesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(recharges));
    }

    /**
     * DELETE  /recharges/:id : delete the "id" recharges.
     *
     * @param id the id of the recharges to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recharges/{id}")
    @Timed
    public ResponseEntity<Void> deleteRecharges(@PathVariable Long id) {
        log.debug("REST request to delete Recharges : {}", id);
        rechargesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
