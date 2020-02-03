package io.github.pascalgrimaud.toto.web.rest;

import io.github.pascalgrimaud.toto.TotoApp;
import io.github.pascalgrimaud.toto.domain.Alpha;
import io.github.pascalgrimaud.toto.repository.AlphaRepository;
import io.github.pascalgrimaud.toto.service.AlphaService;
import io.github.pascalgrimaud.toto.service.dto.AlphaDTO;
import io.github.pascalgrimaud.toto.service.mapper.AlphaMapper;
import io.github.pascalgrimaud.toto.web.rest.errors.ExceptionTranslator;
import io.github.pascalgrimaud.toto.service.dto.AlphaCriteria;
import io.github.pascalgrimaud.toto.service.AlphaQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static io.github.pascalgrimaud.toto.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AlphaResource} REST controller.
 */
@SpringBootTest(classes = TotoApp.class)
public class AlphaResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTHDAY = LocalDate.ofEpochDay(-1L);

    @Autowired
    private AlphaRepository alphaRepository;

    @Autowired
    private AlphaMapper alphaMapper;

    @Autowired
    private AlphaService alphaService;

    @Autowired
    private AlphaQueryService alphaQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restAlphaMockMvc;

    private Alpha alpha;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlphaResource alphaResource = new AlphaResource(alphaService, alphaQueryService);
        this.restAlphaMockMvc = MockMvcBuilders.standaloneSetup(alphaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alpha createEntity(EntityManager em) {
        Alpha alpha = new Alpha()
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .birthday(DEFAULT_BIRTHDAY);
        return alpha;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alpha createUpdatedEntity(EntityManager em) {
        Alpha alpha = new Alpha()
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .birthday(UPDATED_BIRTHDAY);
        return alpha;
    }

    @BeforeEach
    public void initTest() {
        alpha = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlpha() throws Exception {
        int databaseSizeBeforeCreate = alphaRepository.findAll().size();

        // Create the Alpha
        AlphaDTO alphaDTO = alphaMapper.toDto(alpha);
        restAlphaMockMvc.perform(post("/api/alphas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alphaDTO)))
            .andExpect(status().isCreated());

        // Validate the Alpha in the database
        List<Alpha> alphaList = alphaRepository.findAll();
        assertThat(alphaList).hasSize(databaseSizeBeforeCreate + 1);
        Alpha testAlpha = alphaList.get(alphaList.size() - 1);
        assertThat(testAlpha.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testAlpha.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testAlpha.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
    }

    @Test
    @Transactional
    public void createAlphaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alphaRepository.findAll().size();

        // Create the Alpha with an existing ID
        alpha.setId(1L);
        AlphaDTO alphaDTO = alphaMapper.toDto(alpha);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlphaMockMvc.perform(post("/api/alphas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alpha in the database
        List<Alpha> alphaList = alphaRepository.findAll();
        assertThat(alphaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = alphaRepository.findAll().size();
        // set the field null
        alpha.setFirstname(null);

        // Create the Alpha, which fails.
        AlphaDTO alphaDTO = alphaMapper.toDto(alpha);

        restAlphaMockMvc.perform(post("/api/alphas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alphaDTO)))
            .andExpect(status().isBadRequest());

        List<Alpha> alphaList = alphaRepository.findAll();
        assertThat(alphaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthdayIsRequired() throws Exception {
        int databaseSizeBeforeTest = alphaRepository.findAll().size();
        // set the field null
        alpha.setBirthday(null);

        // Create the Alpha, which fails.
        AlphaDTO alphaDTO = alphaMapper.toDto(alpha);

        restAlphaMockMvc.perform(post("/api/alphas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alphaDTO)))
            .andExpect(status().isBadRequest());

        List<Alpha> alphaList = alphaRepository.findAll();
        assertThat(alphaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAlphas() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList
        restAlphaMockMvc.perform(get("/api/alphas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())));
    }
    
    @Test
    @Transactional
    public void getAlpha() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get the alpha
        restAlphaMockMvc.perform(get("/api/alphas/{id}", alpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alpha.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()));
    }


    @Test
    @Transactional
    public void getAlphasByIdFiltering() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        Long id = alpha.getId();

        defaultAlphaShouldBeFound("id.equals=" + id);
        defaultAlphaShouldNotBeFound("id.notEquals=" + id);

        defaultAlphaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAlphaShouldNotBeFound("id.greaterThan=" + id);

        defaultAlphaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAlphaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAlphasByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where firstname equals to DEFAULT_FIRSTNAME
        defaultAlphaShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

        // Get all the alphaList where firstname equals to UPDATED_FIRSTNAME
        defaultAlphaShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllAlphasByFirstnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where firstname not equals to DEFAULT_FIRSTNAME
        defaultAlphaShouldNotBeFound("firstname.notEquals=" + DEFAULT_FIRSTNAME);

        // Get all the alphaList where firstname not equals to UPDATED_FIRSTNAME
        defaultAlphaShouldBeFound("firstname.notEquals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllAlphasByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where firstname in DEFAULT_FIRSTNAME or UPDATED_FIRSTNAME
        defaultAlphaShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

        // Get all the alphaList where firstname equals to UPDATED_FIRSTNAME
        defaultAlphaShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllAlphasByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where firstname is not null
        defaultAlphaShouldBeFound("firstname.specified=true");

        // Get all the alphaList where firstname is null
        defaultAlphaShouldNotBeFound("firstname.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlphasByFirstnameContainsSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where firstname contains DEFAULT_FIRSTNAME
        defaultAlphaShouldBeFound("firstname.contains=" + DEFAULT_FIRSTNAME);

        // Get all the alphaList where firstname contains UPDATED_FIRSTNAME
        defaultAlphaShouldNotBeFound("firstname.contains=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllAlphasByFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where firstname does not contain DEFAULT_FIRSTNAME
        defaultAlphaShouldNotBeFound("firstname.doesNotContain=" + DEFAULT_FIRSTNAME);

        // Get all the alphaList where firstname does not contain UPDATED_FIRSTNAME
        defaultAlphaShouldBeFound("firstname.doesNotContain=" + UPDATED_FIRSTNAME);
    }


    @Test
    @Transactional
    public void getAllAlphasByLastnameIsEqualToSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where lastname equals to DEFAULT_LASTNAME
        defaultAlphaShouldBeFound("lastname.equals=" + DEFAULT_LASTNAME);

        // Get all the alphaList where lastname equals to UPDATED_LASTNAME
        defaultAlphaShouldNotBeFound("lastname.equals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllAlphasByLastnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where lastname not equals to DEFAULT_LASTNAME
        defaultAlphaShouldNotBeFound("lastname.notEquals=" + DEFAULT_LASTNAME);

        // Get all the alphaList where lastname not equals to UPDATED_LASTNAME
        defaultAlphaShouldBeFound("lastname.notEquals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllAlphasByLastnameIsInShouldWork() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where lastname in DEFAULT_LASTNAME or UPDATED_LASTNAME
        defaultAlphaShouldBeFound("lastname.in=" + DEFAULT_LASTNAME + "," + UPDATED_LASTNAME);

        // Get all the alphaList where lastname equals to UPDATED_LASTNAME
        defaultAlphaShouldNotBeFound("lastname.in=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllAlphasByLastnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where lastname is not null
        defaultAlphaShouldBeFound("lastname.specified=true");

        // Get all the alphaList where lastname is null
        defaultAlphaShouldNotBeFound("lastname.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlphasByLastnameContainsSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where lastname contains DEFAULT_LASTNAME
        defaultAlphaShouldBeFound("lastname.contains=" + DEFAULT_LASTNAME);

        // Get all the alphaList where lastname contains UPDATED_LASTNAME
        defaultAlphaShouldNotBeFound("lastname.contains=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllAlphasByLastnameNotContainsSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where lastname does not contain DEFAULT_LASTNAME
        defaultAlphaShouldNotBeFound("lastname.doesNotContain=" + DEFAULT_LASTNAME);

        // Get all the alphaList where lastname does not contain UPDATED_LASTNAME
        defaultAlphaShouldBeFound("lastname.doesNotContain=" + UPDATED_LASTNAME);
    }


    @Test
    @Transactional
    public void getAllAlphasByBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where birthday equals to DEFAULT_BIRTHDAY
        defaultAlphaShouldBeFound("birthday.equals=" + DEFAULT_BIRTHDAY);

        // Get all the alphaList where birthday equals to UPDATED_BIRTHDAY
        defaultAlphaShouldNotBeFound("birthday.equals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllAlphasByBirthdayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where birthday not equals to DEFAULT_BIRTHDAY
        defaultAlphaShouldNotBeFound("birthday.notEquals=" + DEFAULT_BIRTHDAY);

        // Get all the alphaList where birthday not equals to UPDATED_BIRTHDAY
        defaultAlphaShouldBeFound("birthday.notEquals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllAlphasByBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where birthday in DEFAULT_BIRTHDAY or UPDATED_BIRTHDAY
        defaultAlphaShouldBeFound("birthday.in=" + DEFAULT_BIRTHDAY + "," + UPDATED_BIRTHDAY);

        // Get all the alphaList where birthday equals to UPDATED_BIRTHDAY
        defaultAlphaShouldNotBeFound("birthday.in=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllAlphasByBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where birthday is not null
        defaultAlphaShouldBeFound("birthday.specified=true");

        // Get all the alphaList where birthday is null
        defaultAlphaShouldNotBeFound("birthday.specified=false");
    }

    @Test
    @Transactional
    public void getAllAlphasByBirthdayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where birthday is greater than or equal to DEFAULT_BIRTHDAY
        defaultAlphaShouldBeFound("birthday.greaterThanOrEqual=" + DEFAULT_BIRTHDAY);

        // Get all the alphaList where birthday is greater than or equal to UPDATED_BIRTHDAY
        defaultAlphaShouldNotBeFound("birthday.greaterThanOrEqual=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllAlphasByBirthdayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where birthday is less than or equal to DEFAULT_BIRTHDAY
        defaultAlphaShouldBeFound("birthday.lessThanOrEqual=" + DEFAULT_BIRTHDAY);

        // Get all the alphaList where birthday is less than or equal to SMALLER_BIRTHDAY
        defaultAlphaShouldNotBeFound("birthday.lessThanOrEqual=" + SMALLER_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllAlphasByBirthdayIsLessThanSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where birthday is less than DEFAULT_BIRTHDAY
        defaultAlphaShouldNotBeFound("birthday.lessThan=" + DEFAULT_BIRTHDAY);

        // Get all the alphaList where birthday is less than UPDATED_BIRTHDAY
        defaultAlphaShouldBeFound("birthday.lessThan=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllAlphasByBirthdayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        // Get all the alphaList where birthday is greater than DEFAULT_BIRTHDAY
        defaultAlphaShouldNotBeFound("birthday.greaterThan=" + DEFAULT_BIRTHDAY);

        // Get all the alphaList where birthday is greater than SMALLER_BIRTHDAY
        defaultAlphaShouldBeFound("birthday.greaterThan=" + SMALLER_BIRTHDAY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlphaShouldBeFound(String filter) throws Exception {
        restAlphaMockMvc.perform(get("/api/alphas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())));

        // Check, that the count call also returns 1
        restAlphaMockMvc.perform(get("/api/alphas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlphaShouldNotBeFound(String filter) throws Exception {
        restAlphaMockMvc.perform(get("/api/alphas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlphaMockMvc.perform(get("/api/alphas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAlpha() throws Exception {
        // Get the alpha
        restAlphaMockMvc.perform(get("/api/alphas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlpha() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        int databaseSizeBeforeUpdate = alphaRepository.findAll().size();

        // Update the alpha
        Alpha updatedAlpha = alphaRepository.findById(alpha.getId()).get();
        // Disconnect from session so that the updates on updatedAlpha are not directly saved in db
        em.detach(updatedAlpha);
        updatedAlpha
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .birthday(UPDATED_BIRTHDAY);
        AlphaDTO alphaDTO = alphaMapper.toDto(updatedAlpha);

        restAlphaMockMvc.perform(put("/api/alphas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alphaDTO)))
            .andExpect(status().isOk());

        // Validate the Alpha in the database
        List<Alpha> alphaList = alphaRepository.findAll();
        assertThat(alphaList).hasSize(databaseSizeBeforeUpdate);
        Alpha testAlpha = alphaList.get(alphaList.size() - 1);
        assertThat(testAlpha.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testAlpha.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testAlpha.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void updateNonExistingAlpha() throws Exception {
        int databaseSizeBeforeUpdate = alphaRepository.findAll().size();

        // Create the Alpha
        AlphaDTO alphaDTO = alphaMapper.toDto(alpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlphaMockMvc.perform(put("/api/alphas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alpha in the database
        List<Alpha> alphaList = alphaRepository.findAll();
        assertThat(alphaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlpha() throws Exception {
        // Initialize the database
        alphaRepository.saveAndFlush(alpha);

        int databaseSizeBeforeDelete = alphaRepository.findAll().size();

        // Delete the alpha
        restAlphaMockMvc.perform(delete("/api/alphas/{id}", alpha.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alpha> alphaList = alphaRepository.findAll();
        assertThat(alphaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
