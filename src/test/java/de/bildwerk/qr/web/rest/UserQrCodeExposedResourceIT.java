package de.bildwerk.qr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.bildwerk.qr.BildwerkQrApp;
import de.bildwerk.qr.domain.UserQrCodeExposed;
import de.bildwerk.qr.repository.UserQrCodeExposedRepository;
import de.bildwerk.qr.service.UserQrCodeExposedQueryService;
import de.bildwerk.qr.service.UserQrCodeExposedService;
import de.bildwerk.qr.service.dto.UserQrCodeExposedCriteria;
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
 * Integration tests for the {@link UserQrCodeExposedResource} REST controller.
 */
@SpringBootTest(classes = BildwerkQrApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class UserQrCodeExposedResourceIT {
    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private UserQrCodeExposedRepository userQrCodeExposedRepository;

    @Autowired
    private UserQrCodeExposedService userQrCodeExposedService;

    @Autowired
    private UserQrCodeExposedQueryService userQrCodeExposedQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserQrCodeExposedMockMvc;

    private UserQrCodeExposed userQrCodeExposed;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserQrCodeExposed createEntity(EntityManager em) {
        UserQrCodeExposed userQrCodeExposed = new UserQrCodeExposed().code(DEFAULT_CODE).url(DEFAULT_URL);
        return userQrCodeExposed;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserQrCodeExposed createUpdatedEntity(EntityManager em) {
        UserQrCodeExposed userQrCodeExposed = new UserQrCodeExposed().code(UPDATED_CODE).url(UPDATED_URL);
        return userQrCodeExposed;
    }

    @BeforeEach
    public void initTest() {
        userQrCodeExposed = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserQrCodeExposed() throws Exception {
        int databaseSizeBeforeCreate = userQrCodeExposedRepository.findAll().size();
        // Create the UserQrCodeExposed
        restUserQrCodeExposedMockMvc
            .perform(
                post("/api/user-qr-code-exposeds")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userQrCodeExposed))
            )
            .andExpect(status().isCreated());

        // Validate the UserQrCodeExposed in the database
        List<UserQrCodeExposed> userQrCodeExposedList = userQrCodeExposedRepository.findAll();
        assertThat(userQrCodeExposedList).hasSize(databaseSizeBeforeCreate + 1);
        UserQrCodeExposed testUserQrCodeExposed = userQrCodeExposedList.get(userQrCodeExposedList.size() - 1);
        assertThat(testUserQrCodeExposed.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUserQrCodeExposed.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createUserQrCodeExposedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userQrCodeExposedRepository.findAll().size();

        // Create the UserQrCodeExposed with an existing ID
        userQrCodeExposed.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserQrCodeExposedMockMvc
            .perform(
                post("/api/user-qr-code-exposeds")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userQrCodeExposed))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserQrCodeExposed in the database
        List<UserQrCodeExposed> userQrCodeExposedList = userQrCodeExposedRepository.findAll();
        assertThat(userQrCodeExposedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposeds() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList
        restUserQrCodeExposedMockMvc
            .perform(get("/api/user-qr-code-exposeds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userQrCodeExposed.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    public void getUserQrCodeExposed() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get the userQrCodeExposed
        restUserQrCodeExposedMockMvc
            .perform(get("/api/user-qr-code-exposeds/{id}", userQrCodeExposed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userQrCodeExposed.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    public void getUserQrCodeExposedsByIdFiltering() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        Long id = userQrCodeExposed.getId();

        defaultUserQrCodeExposedShouldBeFound("id.equals=" + id);
        defaultUserQrCodeExposedShouldNotBeFound("id.notEquals=" + id);

        defaultUserQrCodeExposedShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserQrCodeExposedShouldNotBeFound("id.greaterThan=" + id);

        defaultUserQrCodeExposedShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserQrCodeExposedShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where code equals to DEFAULT_CODE
        defaultUserQrCodeExposedShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the userQrCodeExposedList where code equals to UPDATED_CODE
        defaultUserQrCodeExposedShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where code not equals to DEFAULT_CODE
        defaultUserQrCodeExposedShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the userQrCodeExposedList where code not equals to UPDATED_CODE
        defaultUserQrCodeExposedShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where code in DEFAULT_CODE or UPDATED_CODE
        defaultUserQrCodeExposedShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the userQrCodeExposedList where code equals to UPDATED_CODE
        defaultUserQrCodeExposedShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where code is not null
        defaultUserQrCodeExposedShouldBeFound("code.specified=true");

        // Get all the userQrCodeExposedList where code is null
        defaultUserQrCodeExposedShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByCodeContainsSomething() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where code contains DEFAULT_CODE
        defaultUserQrCodeExposedShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the userQrCodeExposedList where code contains UPDATED_CODE
        defaultUserQrCodeExposedShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where code does not contain DEFAULT_CODE
        defaultUserQrCodeExposedShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the userQrCodeExposedList where code does not contain UPDATED_CODE
        defaultUserQrCodeExposedShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where url equals to DEFAULT_URL
        defaultUserQrCodeExposedShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the userQrCodeExposedList where url equals to UPDATED_URL
        defaultUserQrCodeExposedShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where url not equals to DEFAULT_URL
        defaultUserQrCodeExposedShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the userQrCodeExposedList where url not equals to UPDATED_URL
        defaultUserQrCodeExposedShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where url in DEFAULT_URL or UPDATED_URL
        defaultUserQrCodeExposedShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the userQrCodeExposedList where url equals to UPDATED_URL
        defaultUserQrCodeExposedShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where url is not null
        defaultUserQrCodeExposedShouldBeFound("url.specified=true");

        // Get all the userQrCodeExposedList where url is null
        defaultUserQrCodeExposedShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByUrlContainsSomething() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where url contains DEFAULT_URL
        defaultUserQrCodeExposedShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the userQrCodeExposedList where url contains UPDATED_URL
        defaultUserQrCodeExposedShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllUserQrCodeExposedsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        userQrCodeExposedRepository.saveAndFlush(userQrCodeExposed);

        // Get all the userQrCodeExposedList where url does not contain DEFAULT_URL
        defaultUserQrCodeExposedShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the userQrCodeExposedList where url does not contain UPDATED_URL
        defaultUserQrCodeExposedShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserQrCodeExposedShouldBeFound(String filter) throws Exception {
        restUserQrCodeExposedMockMvc
            .perform(get("/api/user-qr-code-exposeds?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userQrCodeExposed.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restUserQrCodeExposedMockMvc
            .perform(get("/api/user-qr-code-exposeds/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserQrCodeExposedShouldNotBeFound(String filter) throws Exception {
        restUserQrCodeExposedMockMvc
            .perform(get("/api/user-qr-code-exposeds?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserQrCodeExposedMockMvc
            .perform(get("/api/user-qr-code-exposeds/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingUserQrCodeExposed() throws Exception {
        // Get the userQrCodeExposed
        restUserQrCodeExposedMockMvc.perform(get("/api/user-qr-code-exposeds/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserQrCodeExposed() throws Exception {
        // Initialize the database
        userQrCodeExposedService.save(userQrCodeExposed);

        int databaseSizeBeforeUpdate = userQrCodeExposedRepository.findAll().size();

        // Update the userQrCodeExposed
        UserQrCodeExposed updatedUserQrCodeExposed = userQrCodeExposedRepository.findById(userQrCodeExposed.getId()).get();
        // Disconnect from session so that the updates on updatedUserQrCodeExposed are not directly saved in db
        em.detach(updatedUserQrCodeExposed);
        updatedUserQrCodeExposed.code(UPDATED_CODE).url(UPDATED_URL);

        restUserQrCodeExposedMockMvc
            .perform(
                put("/api/user-qr-code-exposeds")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserQrCodeExposed))
            )
            .andExpect(status().isOk());

        // Validate the UserQrCodeExposed in the database
        List<UserQrCodeExposed> userQrCodeExposedList = userQrCodeExposedRepository.findAll();
        assertThat(userQrCodeExposedList).hasSize(databaseSizeBeforeUpdate);
        UserQrCodeExposed testUserQrCodeExposed = userQrCodeExposedList.get(userQrCodeExposedList.size() - 1);
        assertThat(testUserQrCodeExposed.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUserQrCodeExposed.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingUserQrCodeExposed() throws Exception {
        int databaseSizeBeforeUpdate = userQrCodeExposedRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserQrCodeExposedMockMvc
            .perform(
                put("/api/user-qr-code-exposeds")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userQrCodeExposed))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserQrCodeExposed in the database
        List<UserQrCodeExposed> userQrCodeExposedList = userQrCodeExposedRepository.findAll();
        assertThat(userQrCodeExposedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserQrCodeExposed() throws Exception {
        // Initialize the database
        userQrCodeExposedService.save(userQrCodeExposed);

        int databaseSizeBeforeDelete = userQrCodeExposedRepository.findAll().size();

        // Delete the userQrCodeExposed
        restUserQrCodeExposedMockMvc
            .perform(delete("/api/user-qr-code-exposeds/{id}", userQrCodeExposed.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserQrCodeExposed> userQrCodeExposedList = userQrCodeExposedRepository.findAll();
        assertThat(userQrCodeExposedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
