package com.kiwi.reactor.web.rest;

import com.kiwi.reactor.KiwiCellApp;

import com.kiwi.reactor.domain.SysParams;
import com.kiwi.reactor.repository.SysParamsRepository;
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
 * Test class for the SysParamsResource REST controller.
 *
 * @see SysParamsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KiwiCellApp.class)
public class SysParamsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_S_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_S_VALUE = "BBBBBBBBBB";

    private static final Double DEFAULT_N_VALUE = 1D;
    private static final Double UPDATED_N_VALUE = 2D;

    @Autowired
    private SysParamsRepository sysParamsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSysParamsMockMvc;

    private SysParams sysParams;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SysParamsResource sysParamsResource = new SysParamsResource(sysParamsRepository);
        this.restSysParamsMockMvc = MockMvcBuilders.standaloneSetup(sysParamsResource)
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
    public static SysParams createEntity(EntityManager em) {
        SysParams sysParams = new SysParams()
            .name(DEFAULT_NAME)
            .sValue(DEFAULT_S_VALUE)
            .nValue(DEFAULT_N_VALUE);
        return sysParams;
    }

    @Before
    public void initTest() {
        sysParams = createEntity(em);
    }

    @Test
    @Transactional
    public void createSysParams() throws Exception {
        int databaseSizeBeforeCreate = sysParamsRepository.findAll().size();

        // Create the SysParams
        restSysParamsMockMvc.perform(post("/api/sys-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sysParams)))
            .andExpect(status().isCreated());

        // Validate the SysParams in the database
        List<SysParams> sysParamsList = sysParamsRepository.findAll();
        assertThat(sysParamsList).hasSize(databaseSizeBeforeCreate + 1);
        SysParams testSysParams = sysParamsList.get(sysParamsList.size() - 1);
        assertThat(testSysParams.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSysParams.getsValue()).isEqualTo(DEFAULT_S_VALUE);
        assertThat(testSysParams.getnValue()).isEqualTo(DEFAULT_N_VALUE);
    }

    @Test
    @Transactional
    public void createSysParamsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sysParamsRepository.findAll().size();

        // Create the SysParams with an existing ID
        sysParams.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysParamsMockMvc.perform(post("/api/sys-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sysParams)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SysParams> sysParamsList = sysParamsRepository.findAll();
        assertThat(sysParamsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sysParamsRepository.findAll().size();
        // set the field null
        sysParams.setName(null);

        // Create the SysParams, which fails.

        restSysParamsMockMvc.perform(post("/api/sys-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sysParams)))
            .andExpect(status().isBadRequest());

        List<SysParams> sysParamsList = sysParamsRepository.findAll();
        assertThat(sysParamsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checksValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = sysParamsRepository.findAll().size();
        // set the field null
        sysParams.setsValue(null);

        // Create the SysParams, which fails.

        restSysParamsMockMvc.perform(post("/api/sys-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sysParams)))
            .andExpect(status().isBadRequest());

        List<SysParams> sysParamsList = sysParamsRepository.findAll();
        assertThat(sysParamsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checknValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = sysParamsRepository.findAll().size();
        // set the field null
        sysParams.setnValue(null);

        // Create the SysParams, which fails.

        restSysParamsMockMvc.perform(post("/api/sys-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sysParams)))
            .andExpect(status().isBadRequest());

        List<SysParams> sysParamsList = sysParamsRepository.findAll();
        assertThat(sysParamsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSysParams() throws Exception {
        // Initialize the database
        sysParamsRepository.saveAndFlush(sysParams);

        // Get all the sysParamsList
        restSysParamsMockMvc.perform(get("/api/sys-params?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysParams.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sValue").value(hasItem(DEFAULT_S_VALUE.toString())))
            .andExpect(jsonPath("$.[*].nValue").value(hasItem(DEFAULT_N_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    public void getSysParams() throws Exception {
        // Initialize the database
        sysParamsRepository.saveAndFlush(sysParams);

        // Get the sysParams
        restSysParamsMockMvc.perform(get("/api/sys-params/{id}", sysParams.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sysParams.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sValue").value(DEFAULT_S_VALUE.toString()))
            .andExpect(jsonPath("$.nValue").value(DEFAULT_N_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSysParams() throws Exception {
        // Get the sysParams
        restSysParamsMockMvc.perform(get("/api/sys-params/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSysParams() throws Exception {
        // Initialize the database
        sysParamsRepository.saveAndFlush(sysParams);
        int databaseSizeBeforeUpdate = sysParamsRepository.findAll().size();

        // Update the sysParams
        SysParams updatedSysParams = sysParamsRepository.findOne(sysParams.getId());
        updatedSysParams
            .name(UPDATED_NAME)
            .sValue(UPDATED_S_VALUE)
            .nValue(UPDATED_N_VALUE);

        restSysParamsMockMvc.perform(put("/api/sys-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSysParams)))
            .andExpect(status().isOk());

        // Validate the SysParams in the database
        List<SysParams> sysParamsList = sysParamsRepository.findAll();
        assertThat(sysParamsList).hasSize(databaseSizeBeforeUpdate);
        SysParams testSysParams = sysParamsList.get(sysParamsList.size() - 1);
        assertThat(testSysParams.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSysParams.getsValue()).isEqualTo(UPDATED_S_VALUE);
        assertThat(testSysParams.getnValue()).isEqualTo(UPDATED_N_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingSysParams() throws Exception {
        int databaseSizeBeforeUpdate = sysParamsRepository.findAll().size();

        // Create the SysParams

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSysParamsMockMvc.perform(put("/api/sys-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sysParams)))
            .andExpect(status().isCreated());

        // Validate the SysParams in the database
        List<SysParams> sysParamsList = sysParamsRepository.findAll();
        assertThat(sysParamsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSysParams() throws Exception {
        // Initialize the database
        sysParamsRepository.saveAndFlush(sysParams);
        int databaseSizeBeforeDelete = sysParamsRepository.findAll().size();

        // Get the sysParams
        restSysParamsMockMvc.perform(delete("/api/sys-params/{id}", sysParams.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SysParams> sysParamsList = sysParamsRepository.findAll();
        assertThat(sysParamsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysParams.class);
        SysParams sysParams1 = new SysParams();
        sysParams1.setId(1L);
        SysParams sysParams2 = new SysParams();
        sysParams2.setId(sysParams1.getId());
        assertThat(sysParams1).isEqualTo(sysParams2);
        sysParams2.setId(2L);
        assertThat(sysParams1).isNotEqualTo(sysParams2);
        sysParams1.setId(null);
        assertThat(sysParams1).isNotEqualTo(sysParams2);
    }
}
