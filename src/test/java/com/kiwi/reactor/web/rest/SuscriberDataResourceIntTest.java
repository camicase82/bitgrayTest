package com.kiwi.reactor.web.rest;

import com.google.firebase.database.FirebaseDatabase;
import com.kiwi.reactor.KiwiCellApp;

import com.kiwi.reactor.domain.SuscriberData;
import com.kiwi.reactor.repository.SuscriberDataRepository;
import com.kiwi.reactor.service.firebase.FirebaseFacade;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SuscriberDataResource REST controller.
 *
 * @see SuscriberDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KiwiCellApp.class)
public class SuscriberDataResourceIntTest {

    private static final String DEFAULT_FB_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_FB_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Long DEFAULT_BALANCE = 1L;
    private static final Long UPDATED_BALANCE = 2L;

    @Autowired
    private SuscriberDataRepository suscriberDataRepository;
    
    @Autowired
    private FirebaseFacade firebaseFacade;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSuscriberDataMockMvc;

    private SuscriberData suscriberData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SuscriberDataResource suscriberDataResource = new SuscriberDataResource(suscriberDataRepository, firebaseFacade);
        this.restSuscriberDataMockMvc = MockMvcBuilders.standaloneSetup(suscriberDataResource)
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
    public static SuscriberData createEntity(EntityManager em) {
        SuscriberData suscriberData = new SuscriberData()
            .firebReference(DEFAULT_FB_REFERENCE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .balance(DEFAULT_BALANCE);
        return suscriberData;
    }

    @Before
    public void initTest() {
        suscriberData = createEntity(em);
    }

    @Test
    @Transactional
    public void createSuscriberData() throws Exception {
        int databaseSizeBeforeCreate = suscriberDataRepository.findAll().size();

        // Create the SuscriberData
        restSuscriberDataMockMvc.perform(post("/api/suscriber-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suscriberData)))
            .andExpect(status().isCreated());

        // Validate the SuscriberData in the database
        List<SuscriberData> suscriberDataList = suscriberDataRepository.findAll();
        assertThat(suscriberDataList).hasSize(databaseSizeBeforeCreate + 1);
        SuscriberData testSuscriberData = suscriberDataList.get(suscriberDataList.size() - 1);
        assertThat(testSuscriberData.getFirebReference()).isEqualTo(DEFAULT_FB_REFERENCE);
        assertThat(testSuscriberData.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSuscriberData.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createSuscriberDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = suscriberDataRepository.findAll().size();

        // Create the SuscriberData with an existing ID
        suscriberData.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuscriberDataMockMvc.perform(post("/api/suscriber-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suscriberData)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SuscriberData> suscriberDataList = suscriberDataRepository.findAll();
        assertThat(suscriberDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFbReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = suscriberDataRepository.findAll().size();
        // set the field null
        suscriberData.setFirebReference(null);

        // Create the SuscriberData, which fails.

        restSuscriberDataMockMvc.perform(post("/api/suscriber-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suscriberData)))
            .andExpect(status().isBadRequest());

        List<SuscriberData> suscriberDataList = suscriberDataRepository.findAll();
        assertThat(suscriberDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = suscriberDataRepository.findAll().size();
        // set the field null
        suscriberData.setPhoneNumber(null);

        // Create the SuscriberData, which fails.

        restSuscriberDataMockMvc.perform(post("/api/suscriber-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suscriberData)))
            .andExpect(status().isBadRequest());

        List<SuscriberData> suscriberDataList = suscriberDataRepository.findAll();
        assertThat(suscriberDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSuscriberData() throws Exception {
        // Initialize the database
        suscriberDataRepository.saveAndFlush(suscriberData);

        // Get all the suscriberDataList
        restSuscriberDataMockMvc.perform(get("/api/suscriber-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suscriberData.getId().intValue())))
            .andExpect(jsonPath("$.[*].fbReference").value(hasItem(DEFAULT_FB_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }

    @Test
    @Transactional
    public void getSuscriberData() throws Exception {
        // Initialize the database
        suscriberDataRepository.saveAndFlush(suscriberData);

        // Get the suscriberData
        restSuscriberDataMockMvc.perform(get("/api/suscriber-data/{id}", suscriberData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(suscriberData.getId().intValue()))
            .andExpect(jsonPath("$.fbReference").value(DEFAULT_FB_REFERENCE.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSuscriberData() throws Exception {
        // Get the suscriberData
        restSuscriberDataMockMvc.perform(get("/api/suscriber-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuscriberData() throws Exception {
        // Initialize the database
        suscriberDataRepository.saveAndFlush(suscriberData);
        int databaseSizeBeforeUpdate = suscriberDataRepository.findAll().size();

        // Update the suscriberData
        SuscriberData updatedSuscriberData = suscriberDataRepository.findOne(suscriberData.getId());
        updatedSuscriberData
            .firebReference(UPDATED_FB_REFERENCE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .balance(UPDATED_BALANCE);

        restSuscriberDataMockMvc.perform(put("/api/suscriber-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSuscriberData)))
            .andExpect(status().isOk());

        // Validate the SuscriberData in the database
        List<SuscriberData> suscriberDataList = suscriberDataRepository.findAll();
        assertThat(suscriberDataList).hasSize(databaseSizeBeforeUpdate);
        SuscriberData testSuscriberData = suscriberDataList.get(suscriberDataList.size() - 1);
        assertThat(testSuscriberData.getFirebReference()).isEqualTo(UPDATED_FB_REFERENCE);
        assertThat(testSuscriberData.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSuscriberData.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingSuscriberData() throws Exception {
        int databaseSizeBeforeUpdate = suscriberDataRepository.findAll().size();

        // Create the SuscriberData

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSuscriberDataMockMvc.perform(put("/api/suscriber-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suscriberData)))
            .andExpect(status().isCreated());

        // Validate the SuscriberData in the database
        List<SuscriberData> suscriberDataList = suscriberDataRepository.findAll();
        assertThat(suscriberDataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSuscriberData() throws Exception {
        // Initialize the database
        suscriberDataRepository.saveAndFlush(suscriberData);
        int databaseSizeBeforeDelete = suscriberDataRepository.findAll().size();

        // Get the suscriberData
        restSuscriberDataMockMvc.perform(delete("/api/suscriber-data/{id}", suscriberData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SuscriberData> suscriberDataList = suscriberDataRepository.findAll();
        assertThat(suscriberDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuscriberData.class);
        SuscriberData suscriberData1 = new SuscriberData();
        suscriberData1.setId(1L);
        SuscriberData suscriberData2 = new SuscriberData();
        suscriberData2.setId(suscriberData1.getId());
        assertThat(suscriberData1).isEqualTo(suscriberData2);
        suscriberData2.setId(2L);
        assertThat(suscriberData1).isNotEqualTo(suscriberData2);
        suscriberData1.setId(null);
        assertThat(suscriberData1).isNotEqualTo(suscriberData2);
    }
}
