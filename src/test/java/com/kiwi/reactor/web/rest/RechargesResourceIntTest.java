package com.kiwi.reactor.web.rest;

import com.kiwi.reactor.KiwiCellApp;

import com.kiwi.reactor.domain.Recharges;
import com.kiwi.reactor.repository.RechargesRepository;
import com.kiwi.reactor.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RechargesResource REST controller.
 *
 * @see RechargesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KiwiCellApp.class)
public class RechargesResourceIntTest {

    private static final Long DEFAULT_VALUE = 1L;
    private static final Long UPDATED_VALUE = 2L;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_DISCCOUNT = 1D;
    private static final Double UPDATED_DISCCOUNT = 2D;

    private static final Long DEFAULT_AWARDED_SECS = 1L;
    private static final Long UPDATED_AWARDED_SECS = 2L;

    @Autowired
    private RechargesRepository rechargesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRechargesMockMvc;

    private Recharges recharges;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RechargesResource rechargesResource = new RechargesResource(rechargesRepository);
        this.restRechargesMockMvc = MockMvcBuilders.standaloneSetup(rechargesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recharges createEntity(EntityManager em) {
        Recharges recharges = new Recharges()
            .value(DEFAULT_VALUE)
            .date(DEFAULT_DATE)
            .disccount(DEFAULT_DISCCOUNT)
            .awardedSecs(DEFAULT_AWARDED_SECS);
        return recharges;
    }

    @Before
    public void initTest() {
        recharges = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecharges() throws Exception {
        int databaseSizeBeforeCreate = rechargesRepository.findAll().size();

        // Create the Recharges
        restRechargesMockMvc.perform(post("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recharges)))
            .andExpect(status().isCreated());

        // Validate the Recharges in the database
        List<Recharges> rechargesList = rechargesRepository.findAll();
        assertThat(rechargesList).hasSize(databaseSizeBeforeCreate + 1);
        Recharges testRecharges = rechargesList.get(rechargesList.size() - 1);
        assertThat(testRecharges.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testRecharges.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRecharges.getDisccount()).isEqualTo(DEFAULT_DISCCOUNT);
        assertThat(testRecharges.getAwardedSecs()).isEqualTo(DEFAULT_AWARDED_SECS);
    }

    @Test
    @Transactional
    public void createRechargesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rechargesRepository.findAll().size();

        // Create the Recharges with an existing ID
        recharges.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRechargesMockMvc.perform(post("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recharges)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Recharges> rechargesList = rechargesRepository.findAll();
        assertThat(rechargesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = rechargesRepository.findAll().size();
        // set the field null
        recharges.setValue(null);

        // Create the Recharges, which fails.

        restRechargesMockMvc.perform(post("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recharges)))
            .andExpect(status().isBadRequest());

        List<Recharges> rechargesList = rechargesRepository.findAll();
        assertThat(rechargesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rechargesRepository.findAll().size();
        // set the field null
        recharges.setDate(null);

        // Create the Recharges, which fails.

        restRechargesMockMvc.perform(post("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recharges)))
            .andExpect(status().isBadRequest());

        List<Recharges> rechargesList = rechargesRepository.findAll();
        assertThat(rechargesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDisccountIsRequired() throws Exception {
        int databaseSizeBeforeTest = rechargesRepository.findAll().size();
        // set the field null
        recharges.setDisccount(null);

        // Create the Recharges, which fails.

        restRechargesMockMvc.perform(post("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recharges)))
            .andExpect(status().isBadRequest());

        List<Recharges> rechargesList = rechargesRepository.findAll();
        assertThat(rechargesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAwardedSecsIsRequired() throws Exception {
        int databaseSizeBeforeTest = rechargesRepository.findAll().size();
        // set the field null
        recharges.setAwardedSecs(null);

        // Create the Recharges, which fails.

        restRechargesMockMvc.perform(post("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recharges)))
            .andExpect(status().isBadRequest());

        List<Recharges> rechargesList = rechargesRepository.findAll();
        assertThat(rechargesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecharges() throws Exception {
        // Initialize the database
        rechargesRepository.saveAndFlush(recharges);

        // Get all the rechargesList
        restRechargesMockMvc.perform(get("/api/recharges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recharges.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].disccount").value(hasItem(DEFAULT_DISCCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].awardedSecs").value(hasItem(DEFAULT_AWARDED_SECS.intValue())));
    }

    @Test
    @Transactional
    public void getRecharges() throws Exception {
        // Initialize the database
        rechargesRepository.saveAndFlush(recharges);

        // Get the recharges
        restRechargesMockMvc.perform(get("/api/recharges/{id}", recharges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recharges.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.disccount").value(DEFAULT_DISCCOUNT.doubleValue()))
            .andExpect(jsonPath("$.awardedSecs").value(DEFAULT_AWARDED_SECS.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRecharges() throws Exception {
        // Get the recharges
        restRechargesMockMvc.perform(get("/api/recharges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecharges() throws Exception {
        // Initialize the database
        rechargesRepository.saveAndFlush(recharges);
        int databaseSizeBeforeUpdate = rechargesRepository.findAll().size();

        // Update the recharges
        Recharges updatedRecharges = rechargesRepository.findOne(recharges.getId());
        updatedRecharges
            .value(UPDATED_VALUE)
            .date(UPDATED_DATE)
            .disccount(UPDATED_DISCCOUNT)
            .awardedSecs(UPDATED_AWARDED_SECS);

        restRechargesMockMvc.perform(put("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecharges)))
            .andExpect(status().isOk());

        // Validate the Recharges in the database
        List<Recharges> rechargesList = rechargesRepository.findAll();
        assertThat(rechargesList).hasSize(databaseSizeBeforeUpdate);
        Recharges testRecharges = rechargesList.get(rechargesList.size() - 1);
        assertThat(testRecharges.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testRecharges.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRecharges.getDisccount()).isEqualTo(UPDATED_DISCCOUNT);
        assertThat(testRecharges.getAwardedSecs()).isEqualTo(UPDATED_AWARDED_SECS);
    }

    @Test
    @Transactional
    public void updateNonExistingRecharges() throws Exception {
        int databaseSizeBeforeUpdate = rechargesRepository.findAll().size();

        // Create the Recharges

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRechargesMockMvc.perform(put("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recharges)))
            .andExpect(status().isCreated());

        // Validate the Recharges in the database
        List<Recharges> rechargesList = rechargesRepository.findAll();
        assertThat(rechargesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRecharges() throws Exception {
        // Initialize the database
        rechargesRepository.saveAndFlush(recharges);
        int databaseSizeBeforeDelete = rechargesRepository.findAll().size();

        // Get the recharges
        restRechargesMockMvc.perform(delete("/api/recharges/{id}", recharges.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Recharges> rechargesList = rechargesRepository.findAll();
        assertThat(rechargesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recharges.class);
        Recharges recharges1 = new Recharges();
        recharges1.setId(1L);
        Recharges recharges2 = new Recharges();
        recharges2.setId(recharges1.getId());
        assertThat(recharges1).isEqualTo(recharges2);
        recharges2.setId(2L);
        assertThat(recharges1).isNotEqualTo(recharges2);
        recharges1.setId(null);
        assertThat(recharges1).isNotEqualTo(recharges2);
    }
}
