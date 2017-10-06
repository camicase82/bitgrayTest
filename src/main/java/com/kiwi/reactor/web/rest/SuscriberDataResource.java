package com.kiwi.reactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kiwi.reactor.domain.SuscriberData;

import com.kiwi.reactor.repository.SuscriberDataRepository;
import com.kiwi.reactor.service.firebase.FirebaseFacade;
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
 * REST controller for managing SuscriberData.
 */
@RestController
@RequestMapping("/api")
public class SuscriberDataResource {

    private final Logger log = LoggerFactory.getLogger(SuscriberDataResource.class);

    private static final String ENTITY_NAME = "suscriberData";
        
    private final SuscriberDataRepository suscriberDataRepository;
    private final FirebaseFacade firebaseFacade;

    public SuscriberDataResource(SuscriberDataRepository suscriberDataRepository, FirebaseFacade firebaseFacade) {
        this.suscriberDataRepository = suscriberDataRepository;
        this.firebaseFacade = firebaseFacade;
    }

    /**
     * POST  /suscriber-data : Create a new suscriberData.
     *
     * @param suscriberData the suscriberData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new suscriberData, or with status 400 (Bad Request) if the suscriberData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/suscriber-data")
    @Timed
    public ResponseEntity<SuscriberData> createSuscriberData(@Valid @RequestBody SuscriberData suscriberData) throws URISyntaxException {
        log.debug("REST request to save SuscriberData : {}", suscriberData);
        if (suscriberData.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new suscriberData cannot already have an ID")).body(null);
        }
        //Modified standard to add firebase storage
        String newSuscId = firebaseFacade.createSuscriber(suscriberData);
        suscriberData.setFirebReference(newSuscId);
        SuscriberData result = suscriberDataRepository.save(suscriberData);
        return ResponseEntity.created(new URI("/api/suscriber-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /suscriber-data : Updates an existing suscriberData.
     *
     * @param suscriberData the suscriberData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated suscriberData,
     * or with status 400 (Bad Request) if the suscriberData is not valid,
     * or with status 500 (Internal Server Error) if the suscriberData couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/suscriber-data")
    @Timed
    public ResponseEntity<SuscriberData> updateSuscriberData(@Valid @RequestBody SuscriberData suscriberData) throws URISyntaxException {
        log.debug("REST request to update SuscriberData : {}", suscriberData);
        if (suscriberData.getId() == null) {
            return createSuscriberData(suscriberData);
        }
        SuscriberData result = suscriberDataRepository.save(suscriberData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, suscriberData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /suscriber-data : get all the suscriberData.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of suscriberData in body
     */
    @GetMapping("/suscriber-data")
    @Timed
    public List<SuscriberData> getAllSuscriberData() {
        log.debug("REST request to get all SuscriberData");
        List<SuscriberData> suscriberData = suscriberDataRepository.findAll();
        return suscriberData;
    }

    /**
     * GET  /suscriber-data/:id : get the "id" suscriberData.
     *
     * @param id the id of the suscriberData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the suscriberData, or with status 404 (Not Found)
     */
    @GetMapping("/suscriber-data/{id}")
    @Timed
    public ResponseEntity<SuscriberData> getSuscriberData(@PathVariable Long id) {
        log.debug("REST request to get SuscriberData : {}", id);
        SuscriberData suscriberData = suscriberDataRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(suscriberData));
    }

    /**
     * DELETE  /suscriber-data/:id : delete the "id" suscriberData.
     *
     * @param id the id of the suscriberData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/suscriber-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteSuscriberData(@PathVariable Long id) {
        log.debug("REST request to delete SuscriberData : {}", id);
        suscriberDataRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
