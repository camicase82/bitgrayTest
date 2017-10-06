package com.kiwi.reactor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kiwi.reactor.domain.SysParams;

import com.kiwi.reactor.repository.SysParamsRepository;
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
 * REST controller for managing SysParams.
 */
@RestController
@RequestMapping("/api")
public class SysParamsResource {

    private final Logger log = LoggerFactory.getLogger(SysParamsResource.class);

    private static final String ENTITY_NAME = "sysParams";
        
    private final SysParamsRepository sysParamsRepository;

    public SysParamsResource(SysParamsRepository sysParamsRepository) {
        this.sysParamsRepository = sysParamsRepository;
    }

    /**
     * POST  /sys-params : Create a new sysParams.
     *
     * @param sysParams the sysParams to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sysParams, or with status 400 (Bad Request) if the sysParams has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sys-params")
    @Timed
    public ResponseEntity<SysParams> createSysParams(@Valid @RequestBody SysParams sysParams) throws URISyntaxException {
        log.debug("REST request to save SysParams : {}", sysParams);
        if (sysParams.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sysParams cannot already have an ID")).body(null);
        }
        SysParams result = sysParamsRepository.save(sysParams);
        return ResponseEntity.created(new URI("/api/sys-params/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sys-params : Updates an existing sysParams.
     *
     * @param sysParams the sysParams to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sysParams,
     * or with status 400 (Bad Request) if the sysParams is not valid,
     * or with status 500 (Internal Server Error) if the sysParams couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sys-params")
    @Timed
    public ResponseEntity<SysParams> updateSysParams(@Valid @RequestBody SysParams sysParams) throws URISyntaxException {
        log.debug("REST request to update SysParams : {}", sysParams);
        if (sysParams.getId() == null) {
            return createSysParams(sysParams);
        }
        SysParams result = sysParamsRepository.save(sysParams);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sysParams.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sys-params : get all the sysParams.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sysParams in body
     */
    @GetMapping("/sys-params")
    @Timed
    public List<SysParams> getAllSysParams() {
        log.debug("REST request to get all SysParams");
        List<SysParams> sysParams = sysParamsRepository.findAll();
        return sysParams;
    }

    /**
     * GET  /sys-params/:id : get the "id" sysParams.
     *
     * @param id the id of the sysParams to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sysParams, or with status 404 (Not Found)
     */
    @GetMapping("/sys-params/{id}")
    @Timed
    public ResponseEntity<SysParams> getSysParams(@PathVariable Long id) {
        log.debug("REST request to get SysParams : {}", id);
        SysParams sysParams = sysParamsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sysParams));
    }

    /**
     * DELETE  /sys-params/:id : delete the "id" sysParams.
     *
     * @param id the id of the sysParams to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sys-params/{id}")
    @Timed
    public ResponseEntity<Void> deleteSysParams(@PathVariable Long id) {
        log.debug("REST request to delete SysParams : {}", id);
        sysParamsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
