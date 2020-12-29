package de.bildwerk.qr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.bildwerk.qr.BildwerkQrApp;
import de.bildwerk.qr.domain.QrRoute;
import de.bildwerk.qr.domain.User;
import de.bildwerk.qr.repository.QrRouteRepository;
import de.bildwerk.qr.service.QrRouteQueryService;
import de.bildwerk.qr.service.QrRouteService;
import de.bildwerk.qr.service.dto.QrRouteCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QrRouteResource} REST controller.
 */
@SpringBootTest(classes = BildwerkQrApp.class)
@AutoConfigureMockMvc
@WithMockUser(value = "admin", roles = { "ADMIN", "USER" })
public class QrRouteResourceIT {
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "https://AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";
    private static final String UPDATED_URL_EXPECTED = "https://BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private QrRouteRepository qrRouteRepository;

    @Autowired
    private QrRouteService qrRouteService;

    @Autowired
    private QrRouteQueryService qrRouteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQrRouteMockMvc;

    private QrRoute qrRoute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QrRoute createEntity(EntityManager em) {
        QrRoute qrRoute = new QrRoute()
            .description(DEFAULT_DESCRIPTION)
            .code(DEFAULT_CODE)
            .url(DEFAULT_URL)
            .enabled(DEFAULT_ENABLED)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return qrRoute;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QrRoute createUpdatedEntity(EntityManager em) {
        QrRoute qrRoute = new QrRoute()
            .description(UPDATED_DESCRIPTION)
            .code(UPDATED_CODE)
            .url(UPDATED_URL)
            .enabled(UPDATED_ENABLED)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return qrRoute;
    }

    @BeforeEach
    public void initTest() {
        qrRoute = createEntity(em);
    }

    @Test
    @Transactional
    public void createQrRoute() throws Exception {
        int databaseSizeBeforeCreate = qrRouteRepository.findAll().size();
        // Create the QrRoute
        restQrRouteMockMvc
            .perform(post("/api/qr-routes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrRoute)))
            .andExpect(status().isCreated());

        // Validate the QrRoute in the database
        List<QrRoute> qrRouteList = qrRouteRepository.findAll();
        assertThat(qrRouteList).hasSize(databaseSizeBeforeCreate + 1);
        QrRoute testQrRoute = qrRouteList.get(qrRouteList.size() - 1);
        assertThat(testQrRoute.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQrRoute.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testQrRoute.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testQrRoute.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testQrRoute.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testQrRoute.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createQrRouteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = qrRouteRepository.findAll().size();

        // Create the QrRoute with an existing ID
        qrRoute.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQrRouteMockMvc
            .perform(post("/api/qr-routes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrRoute)))
            .andExpect(status().isBadRequest());

        // Validate the QrRoute in the database
        List<QrRoute> qrRouteList = qrRouteRepository.findAll();
        assertThat(qrRouteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllQrRoutesWithRoleAdmin() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList
        restQrRouteMockMvc
            .perform(get("/api/qr-routes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qrRoute.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    @WithMockUser(value = "user", roles = { "USER" })
    public void getAllQrRoutes() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList
        restQrRouteMockMvc
            .perform(get("/api/qr-routes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getQrRoute() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get the qrRoute
        restQrRouteMockMvc
            .perform(get("/api/qr-routes/{id}", qrRoute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(qrRoute.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getQrRoutesByIdFiltering() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        Long id = qrRoute.getId();

        defaultQrRouteShouldBeFound("id.equals=" + id);
        defaultQrRouteShouldNotBeFound("id.notEquals=" + id);

        defaultQrRouteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQrRouteShouldNotBeFound("id.greaterThan=" + id);

        defaultQrRouteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQrRouteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where description equals to DEFAULT_DESCRIPTION
        defaultQrRouteShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the qrRouteList where description equals to UPDATED_DESCRIPTION
        defaultQrRouteShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where description not equals to DEFAULT_DESCRIPTION
        defaultQrRouteShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the qrRouteList where description not equals to UPDATED_DESCRIPTION
        defaultQrRouteShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultQrRouteShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the qrRouteList where description equals to UPDATED_DESCRIPTION
        defaultQrRouteShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where description is not null
        defaultQrRouteShouldBeFound("description.specified=true");

        // Get all the qrRouteList where description is null
        defaultQrRouteShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllQrRoutesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where description contains DEFAULT_DESCRIPTION
        defaultQrRouteShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the qrRouteList where description contains UPDATED_DESCRIPTION
        defaultQrRouteShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where description does not contain DEFAULT_DESCRIPTION
        defaultQrRouteShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the qrRouteList where description does not contain UPDATED_DESCRIPTION
        defaultQrRouteShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where code equals to DEFAULT_CODE
        defaultQrRouteShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the qrRouteList where code equals to UPDATED_CODE
        defaultQrRouteShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where code not equals to DEFAULT_CODE
        defaultQrRouteShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the qrRouteList where code not equals to UPDATED_CODE
        defaultQrRouteShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where code in DEFAULT_CODE or UPDATED_CODE
        defaultQrRouteShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the qrRouteList where code equals to UPDATED_CODE
        defaultQrRouteShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where code is not null
        defaultQrRouteShouldBeFound("code.specified=true");

        // Get all the qrRouteList where code is null
        defaultQrRouteShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllQrRoutesByCodeContainsSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where code contains DEFAULT_CODE
        defaultQrRouteShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the qrRouteList where code contains UPDATED_CODE
        defaultQrRouteShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where code does not contain DEFAULT_CODE
        defaultQrRouteShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the qrRouteList where code does not contain UPDATED_CODE
        defaultQrRouteShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where url equals to DEFAULT_URL
        defaultQrRouteShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the qrRouteList where url equals to UPDATED_URL
        defaultQrRouteShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where url not equals to DEFAULT_URL
        defaultQrRouteShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the qrRouteList where url not equals to UPDATED_URL
        defaultQrRouteShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where url in DEFAULT_URL or UPDATED_URL
        defaultQrRouteShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the qrRouteList where url equals to UPDATED_URL
        defaultQrRouteShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where url is not null
        defaultQrRouteShouldBeFound("url.specified=true");

        // Get all the qrRouteList where url is null
        defaultQrRouteShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllQrRoutesByUrlContainsSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where url contains DEFAULT_URL
        defaultQrRouteShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the qrRouteList where url contains UPDATED_URL
        defaultQrRouteShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where url does not contain DEFAULT_URL
        defaultQrRouteShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the qrRouteList where url does not contain UPDATED_URL
        defaultQrRouteShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where enabled equals to DEFAULT_ENABLED
        defaultQrRouteShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the qrRouteList where enabled equals to UPDATED_ENABLED
        defaultQrRouteShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where enabled not equals to DEFAULT_ENABLED
        defaultQrRouteShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the qrRouteList where enabled not equals to UPDATED_ENABLED
        defaultQrRouteShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultQrRouteShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the qrRouteList where enabled equals to UPDATED_ENABLED
        defaultQrRouteShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where enabled is not null
        defaultQrRouteShouldBeFound("enabled.specified=true");

        // Get all the qrRouteList where enabled is null
        defaultQrRouteShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllQrRoutesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where startDate equals to DEFAULT_START_DATE
        defaultQrRouteShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the qrRouteList where startDate equals to UPDATED_START_DATE
        defaultQrRouteShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where startDate not equals to DEFAULT_START_DATE
        defaultQrRouteShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the qrRouteList where startDate not equals to UPDATED_START_DATE
        defaultQrRouteShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultQrRouteShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the qrRouteList where startDate equals to UPDATED_START_DATE
        defaultQrRouteShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where startDate is not null
        defaultQrRouteShouldBeFound("startDate.specified=true");

        // Get all the qrRouteList where startDate is null
        defaultQrRouteShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQrRoutesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultQrRouteShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the qrRouteList where startDate is greater than or equal to UPDATED_START_DATE
        defaultQrRouteShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where startDate is less than or equal to DEFAULT_START_DATE
        defaultQrRouteShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the qrRouteList where startDate is less than or equal to SMALLER_START_DATE
        defaultQrRouteShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where startDate is less than DEFAULT_START_DATE
        defaultQrRouteShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the qrRouteList where startDate is less than UPDATED_START_DATE
        defaultQrRouteShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where startDate is greater than DEFAULT_START_DATE
        defaultQrRouteShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the qrRouteList where startDate is greater than SMALLER_START_DATE
        defaultQrRouteShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where endDate equals to DEFAULT_END_DATE
        defaultQrRouteShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the qrRouteList where endDate equals to UPDATED_END_DATE
        defaultQrRouteShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where endDate not equals to DEFAULT_END_DATE
        defaultQrRouteShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the qrRouteList where endDate not equals to UPDATED_END_DATE
        defaultQrRouteShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultQrRouteShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the qrRouteList where endDate equals to UPDATED_END_DATE
        defaultQrRouteShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where endDate is not null
        defaultQrRouteShouldBeFound("endDate.specified=true");

        // Get all the qrRouteList where endDate is null
        defaultQrRouteShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultQrRouteShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the qrRouteList where endDate is greater than or equal to UPDATED_END_DATE
        defaultQrRouteShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where endDate is less than or equal to DEFAULT_END_DATE
        defaultQrRouteShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the qrRouteList where endDate is less than or equal to SMALLER_END_DATE
        defaultQrRouteShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where endDate is less than DEFAULT_END_DATE
        defaultQrRouteShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the qrRouteList where endDate is less than UPDATED_END_DATE
        defaultQrRouteShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);

        // Get all the qrRouteList where endDate is greater than DEFAULT_END_DATE
        defaultQrRouteShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the qrRouteList where endDate is greater than SMALLER_END_DATE
        defaultQrRouteShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllQrRoutesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        qrRouteRepository.saveAndFlush(qrRoute);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        qrRoute.setUser(user);
        qrRouteRepository.saveAndFlush(qrRoute);
        Long userId = user.getId();

        // Get all the qrRouteList where user equals to userId
        defaultQrRouteShouldBeFound("userId.equals=" + userId);

        // Get all the qrRouteList where user equals to userId + 1
        defaultQrRouteShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQrRouteShouldBeFound(String filter) throws Exception {
        restQrRouteMockMvc
            .perform(get("/api/qr-routes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qrRoute.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));

        // Check, that the count call also returns 1
        restQrRouteMockMvc
            .perform(get("/api/qr-routes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQrRouteShouldNotBeFound(String filter) throws Exception {
        restQrRouteMockMvc
            .perform(get("/api/qr-routes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQrRouteMockMvc
            .perform(get("/api/qr-routes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingQrRoute() throws Exception {
        // Get the qrRoute
        restQrRouteMockMvc.perform(get("/api/qr-routes/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQrRoute() throws Exception {
        // Initialize the database
        qrRouteService.save(qrRoute);

        int databaseSizeBeforeUpdate = qrRouteRepository.findAll().size();

        // Update the qrRoute
        QrRoute updatedQrRoute = qrRouteRepository.findById(qrRoute.getId()).get();
        // Disconnect from session so that the updates on updatedQrRoute are not directly saved in db
        em.detach(updatedQrRoute);
        updatedQrRoute
            .description(UPDATED_DESCRIPTION)
            .code(UPDATED_CODE)
            .url(UPDATED_URL)
            .enabled(UPDATED_ENABLED)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restQrRouteMockMvc
            .perform(
                put("/api/qr-routes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedQrRoute))
            )
            .andExpect(status().isOk());

        // Validate the QrRoute in the database
        List<QrRoute> qrRouteList = qrRouteRepository.findAll();
        assertThat(qrRouteList).hasSize(databaseSizeBeforeUpdate);
        QrRoute testQrRoute = qrRouteList.get(qrRouteList.size() - 1);
        assertThat(testQrRoute.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQrRoute.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testQrRoute.getUrl()).isEqualTo(UPDATED_URL_EXPECTED);
        assertThat(testQrRoute.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testQrRoute.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testQrRoute.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingQrRoute() throws Exception {
        int databaseSizeBeforeUpdate = qrRouteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQrRouteMockMvc
            .perform(put("/api/qr-routes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrRoute)))
            .andExpect(status().isBadRequest());

        // Validate the QrRoute in the database
        List<QrRoute> qrRouteList = qrRouteRepository.findAll();
        assertThat(qrRouteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQrRoute() throws Exception {
        // Initialize the database
        qrRouteService.save(qrRoute);

        int databaseSizeBeforeDelete = qrRouteRepository.findAll().size();

        // Delete the qrRoute
        restQrRouteMockMvc
            .perform(delete("/api/qr-routes/{id}", qrRoute.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QrRoute> qrRouteList = qrRouteRepository.findAll();
        assertThat(qrRouteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
