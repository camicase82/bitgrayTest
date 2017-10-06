package com.kiwi.reactor.web.rest;

import com.kiwi.reactor.KiwiCellApp;

import com.kiwi.reactor.domain.Promotions;
import com.kiwi.reactor.repository.PromotionsRepository;
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
 * Test class for the PromotionsResource REST controller.
 *
 * @see PromotionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KiwiCellApp.class)
public class PromotionsResourceIntTest {

    private static final LocalDate DEFAULT_CONFIGURATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CONFIGURATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_TEST = false;
    private static final Boolean UPDATED_TEST = true;

    private static final String DEFAULT_MODULE = "AAAAAAAAAA";
    private static final String UPDATED_MODULE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_APPLICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPLICATION = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PromotionsRepository promotionsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPromotionsMockMvc;

    private Promotions promotions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PromotionsResource promotionsResource = new PromotionsResource(promotionsRepository);
        this.restPromotionsMockMvc = MockMvcBuilders.standaloneSetup(promotionsResource)
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
    public static Promotions createEntity(EntityManager em) {
        Promotions promotions = new Promotions()
            .configurationDate(DEFAULT_CONFIGURATION_DATE)
            .test(DEFAULT_TEST)
            .module(DEFAULT_MODULE)
            .application(DEFAULT_APPLICATION);
        return promotions;
    }

    @Before
    public void initTest() {
        promotions = createEntity(em);
    }

    @Test
    @Transactional
    public void createPromotions() throws Exception {
        int databaseSizeBeforeCreate = promotionsRepository.findAll().size();

        // Create the Promotions
        restPromotionsMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotions)))
            .andExpect(status().isCreated());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeCreate + 1);
        Promotions testPromotions = promotionsList.get(promotionsList.size() - 1);
        assertThat(testPromotions.getConfigurationDate()).isEqualTo(DEFAULT_CONFIGURATION_DATE);
        assertThat(testPromotions.isTest()).isEqualTo(DEFAULT_TEST);
        assertThat(testPromotions.getModule()).isEqualTo(DEFAULT_MODULE);
        assertThat(testPromotions.getApplication()).isEqualTo(DEFAULT_APPLICATION);
    }

    @Test
    @Transactional
    public void createPromotionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = promotionsRepository.findAll().size();

        // Create the Promotions with an existing ID
        promotions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromotionsMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotions)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkConfigurationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionsRepository.findAll().size();
        // set the field null
        promotions.setConfigurationDate(null);

        // Create the Promotions, which fails.

        restPromotionsMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotions)))
            .andExpect(status().isBadRequest());

        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTestIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionsRepository.findAll().size();
        // set the field null
        promotions.setTest(null);

        // Create the Promotions, which fails.

        restPromotionsMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotions)))
            .andExpect(status().isBadRequest());

        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModuleIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionsRepository.findAll().size();
        // set the field null
        promotions.setModule(null);

        // Create the Promotions, which fails.

        restPromotionsMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotions)))
            .andExpect(status().isBadRequest());

        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApplicationIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionsRepository.findAll().size();
        // set the field null
        promotions.setApplication(null);

        // Create the Promotions, which fails.

        restPromotionsMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotions)))
            .andExpect(status().isBadRequest());

        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        // Get all the promotionsList
        restPromotionsMockMvc.perform(get("/api/promotions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotions.getId().intValue())))
            .andExpect(jsonPath("$.[*].configurationDate").value(hasItem(DEFAULT_CONFIGURATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].test").value(hasItem(DEFAULT_TEST.booleanValue())))
            .andExpect(jsonPath("$.[*].module").value(hasItem(DEFAULT_MODULE.toString())))
            .andExpect(jsonPath("$.[*].application").value(hasItem(DEFAULT_APPLICATION.toString())));
    }

    @Test
    @Transactional
    public void getPromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        // Get the promotions
        restPromotionsMockMvc.perform(get("/api/promotions/{id}", promotions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(promotions.getId().intValue()))
            .andExpect(jsonPath("$.configurationDate").value(DEFAULT_CONFIGURATION_DATE.toString()))
            .andExpect(jsonPath("$.test").value(DEFAULT_TEST.booleanValue()))
            .andExpect(jsonPath("$.module").value(DEFAULT_MODULE.toString()))
            .andExpect(jsonPath("$.application").value(DEFAULT_APPLICATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPromotions() throws Exception {
        // Get the promotions
        restPromotionsMockMvc.perform(get("/api/promotions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);
        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();

        // Update the promotions
        Promotions updatedPromotions = promotionsRepository.findOne(promotions.getId());
        updatedPromotions
            .configurationDate(UPDATED_CONFIGURATION_DATE)
            .test(UPDATED_TEST)
            .module(UPDATED_MODULE)
            .application(UPDATED_APPLICATION);

        restPromotionsMockMvc.perform(put("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPromotions)))
            .andExpect(status().isOk());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);
        Promotions testPromotions = promotionsList.get(promotionsList.size() - 1);
        assertThat(testPromotions.getConfigurationDate()).isEqualTo(UPDATED_CONFIGURATION_DATE);
        assertThat(testPromotions.isTest()).isEqualTo(UPDATED_TEST);
        assertThat(testPromotions.getModule()).isEqualTo(UPDATED_MODULE);
        assertThat(testPromotions.getApplication()).isEqualTo(UPDATED_APPLICATION);
    }

    @Test
    @Transactional
    public void updateNonExistingPromotions() throws Exception {
        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();

        // Create the Promotions

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPromotionsMockMvc.perform(put("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotions)))
            .andExpect(status().isCreated());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);
        int databaseSizeBeforeDelete = promotionsRepository.findAll().size();

        // Get the promotions
        restPromotionsMockMvc.perform(delete("/api/promotions/{id}", promotions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Promotions.class);
        Promotions promotions1 = new Promotions();
        promotions1.setId(1L);
        Promotions promotions2 = new Promotions();
        promotions2.setId(promotions1.getId());
        assertThat(promotions1).isEqualTo(promotions2);
        promotions2.setId(2L);
        assertThat(promotions1).isNotEqualTo(promotions2);
        promotions1.setId(null);
        assertThat(promotions1).isNotEqualTo(promotions2);
    }
}
