package com.kiwi.reactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kiwi.reactor.domain.Promotions;

import com.kiwi.reactor.repository.PromotionsRepository;
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
 * REST controller for managing Promotions.
 */
@RestController
@RequestMapping("/api")
public class PromotionsResource {

    private final Logger log = LoggerFactory.getLogger(PromotionsResource.class);

    private static final String ENTITY_NAME = "promotions";
        
    private final PromotionsRepository promotionsRepository;

    public PromotionsResource(PromotionsRepository promotionsRepository) {
        this.promotionsRepository = promotionsRepository;
    }

    /**
     * POST  /promotions : Create a new promotions.
     *
     * @param promotions the promotions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new promotions, or with status 400 (Bad Request) if the promotions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/promotions")
    @Timed
    public ResponseEntity<Promotions> createPromotions(@Valid @RequestBody Promotions promotions) throws URISyntaxException {
        log.debug("REST request to save Promotions : {}", promotions);
        if (promotions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new promotions cannot already have an ID")).body(null);
        }
        Promotions result = promotionsRepository.save(promotions);
        return ResponseEntity.created(new URI("/api/promotions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /promotions : Updates an existing promotions.
     *
     * @param promotions the promotions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated promotions,
     * or with status 400 (Bad Request) if the promotions is not valid,
     * or with status 500 (Internal Server Error) if the promotions couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/promotions")
    @Timed
    public ResponseEntity<Promotions> updatePromotions(@Valid @RequestBody Promotions promotions) throws URISyntaxException {
        log.debug("REST request to update Promotions : {}", promotions);
        if (promotions.getId() == null) {
            return createPromotions(promotions);
        }
        Promotions result = promotionsRepository.save(promotions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, promotions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /promotions : get all the promotions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of promotions in body
     */
    @GetMapping("/promotions")
    @Timed
    public List<Promotions> getAllPromotions() {
        log.debug("REST request to get all Promotions");
        List<Promotions> promotions = promotionsRepository.findAll();
        return promotions;
    }

    /**
     * GET  /promotions/:id : get the "id" promotions.
     *
     * @param id the id of the promotions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the promotions, or with status 404 (Not Found)
     */
    @GetMapping("/promotions/{id}")
    @Timed
    public ResponseEntity<Promotions> getPromotions(@PathVariable Long id) {
        log.debug("REST request to get Promotions : {}", id);
        Promotions promotions = promotionsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(promotions));
    }

    /**
     * DELETE  /promotions/:id : delete the "id" promotions.
     *
     * @param id the id of the promotions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/promotions/{id}")
    @Timed
    public ResponseEntity<Void> deletePromotions(@PathVariable Long id) {
        log.debug("REST request to delete Promotions : {}", id);
        promotionsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
