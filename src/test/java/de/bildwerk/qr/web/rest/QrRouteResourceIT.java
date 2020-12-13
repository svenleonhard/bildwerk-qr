package de.bildwerk.qr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.bildwerk.qr.BildwerkQrApp;
import de.bildwerk.qr.domain.QrRoute;
import de.bildwerk.qr.repository.QrRouteRepository;
import de.bildwerk.qr.service.QrRouteService;
import java.time.LocalDate;
import java.time.ZoneId;
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
@WithMockUser
public class QrRouteResourceIT {
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private QrRouteRepository qrRouteRepository;

    @Autowired
    private QrRouteService qrRouteService;

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
    public void getAllQrRoutes() throws Exception {
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
        assertThat(testQrRoute.getUrl()).isEqualTo(UPDATED_URL);
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
