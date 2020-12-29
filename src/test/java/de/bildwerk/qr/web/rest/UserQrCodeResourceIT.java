package de.bildwerk.qr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.bildwerk.qr.BildwerkQrApp;
import de.bildwerk.qr.domain.User;
import de.bildwerk.qr.domain.UserQrCode;
import de.bildwerk.qr.repository.UserQrCodeRepository;
import de.bildwerk.qr.service.UserQrCodeQueryService;
import de.bildwerk.qr.service.UserQrCodeService;
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
 * Integration tests for the {@link UserQrCodeResource} REST controller.
 */
@SpringBootTest(classes = BildwerkQrApp.class)
@AutoConfigureMockMvc
@WithMockUser(value = "admin", roles = { "ADMIN", "USER" })
public class UserQrCodeResourceIT {
    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private UserQrCodeRepository userQrCodeRepository;

    @Autowired
    private UserQrCodeService userQrCodeService;

    @Autowired
    private UserQrCodeQueryService userQrCodeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserQrCodeMockMvc;

    private UserQrCode userQrCode;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserQrCode createEntity(EntityManager em) {
        UserQrCode userQrCode = new UserQrCode().code(DEFAULT_CODE);
        return userQrCode;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserQrCode createUpdatedEntity(EntityManager em) {
        UserQrCode userQrCode = new UserQrCode().code(UPDATED_CODE);
        return userQrCode;
    }

    @BeforeEach
    public void initTest() {
        userQrCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserQrCode() throws Exception {
        int databaseSizeBeforeCreate = userQrCodeRepository.findAll().size();
        // Create the UserQrCode
        restUserQrCodeMockMvc
            .perform(
                post("/api/user-qr-codes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userQrCode))
            )
            .andExpect(status().isCreated());

        // Validate the UserQrCode in the database
        List<UserQrCode> userQrCodeList = userQrCodeRepository.findAll();
        assertThat(userQrCodeList).hasSize(databaseSizeBeforeCreate + 1);
        UserQrCode testUserQrCode = userQrCodeList.get(userQrCodeList.size() - 1);
        assertThat(testUserQrCode.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createUserQrCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userQrCodeRepository.findAll().size();

        // Create the UserQrCode with an existing ID
        userQrCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserQrCodeMockMvc
            .perform(
                post("/api/user-qr-codes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userQrCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserQrCode in the database
        List<UserQrCode> userQrCodeList = userQrCodeRepository.findAll();
        assertThat(userQrCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserQrCodes() throws Exception {
        // Initialize the database
        userQrCodeRepository.saveAndFlush(userQrCode);

        // Get all the userQrCodeList
        restUserQrCodeMockMvc
            .perform(get("/api/user-qr-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userQrCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    public void getUserQrCode() throws Exception {
        // Initialize the database
        userQrCodeRepository.saveAndFlush(userQrCode);

        // Get the userQrCode
        restUserQrCodeMockMvc
            .perform(get("/api/user-qr-codes/{id}", userQrCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userQrCode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    public void getUserQrCodesByIdFiltering() throws Exception {
        // Initialize the database
        userQrCodeRepository.saveAndFlush(userQrCode);

        Long id = userQrCode.getId();

        defaultUserQrCodeShouldBeFound("id.equals=" + id);
        defaultUserQrCodeShouldNotBeFound("id.notEquals=" + id);

        defaultUserQrCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserQrCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultUserQrCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserQrCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllUserQrCodesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        userQrCodeRepository.saveAndFlush(userQrCode);

        // Get all the userQrCodeList where code equals to DEFAULT_CODE
        defaultUserQrCodeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the userQrCodeList where code equals to UPDATED_CODE
        defaultUserQrCodeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllUserQrCodesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userQrCodeRepository.saveAndFlush(userQrCode);

        // Get all the userQrCodeList where code not equals to DEFAULT_CODE
        defaultUserQrCodeShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the userQrCodeList where code not equals to UPDATED_CODE
        defaultUserQrCodeShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllUserQrCodesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        userQrCodeRepository.saveAndFlush(userQrCode);

        // Get all the userQrCodeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultUserQrCodeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the userQrCodeList where code equals to UPDATED_CODE
        defaultUserQrCodeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllUserQrCodesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userQrCodeRepository.saveAndFlush(userQrCode);

        // Get all the userQrCodeList where code is not null
        defaultUserQrCodeShouldBeFound("code.specified=true");

        // Get all the userQrCodeList where code is null
        defaultUserQrCodeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserQrCodesByCodeContainsSomething() throws Exception {
        // Initialize the database
        userQrCodeRepository.saveAndFlush(userQrCode);

        // Get all the userQrCodeList where code contains DEFAULT_CODE
        defaultUserQrCodeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the userQrCodeList where code contains UPDATED_CODE
        defaultUserQrCodeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllUserQrCodesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        userQrCodeRepository.saveAndFlush(userQrCode);

        // Get all the userQrCodeList where code does not contain DEFAULT_CODE
        defaultUserQrCodeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the userQrCodeList where code does not contain UPDATED_CODE
        defaultUserQrCodeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllUserQrCodesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        userQrCodeRepository.saveAndFlush(userQrCode);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userQrCode.setUser(user);
        userQrCodeRepository.saveAndFlush(userQrCode);
        Long userId = user.getId();

        // Get all the userQrCodeList where user equals to userId
        defaultUserQrCodeShouldBeFound("userId.equals=" + userId);

        // Get all the userQrCodeList where user equals to userId + 1
        defaultUserQrCodeShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserQrCodeShouldBeFound(String filter) throws Exception {
        restUserQrCodeMockMvc
            .perform(get("/api/user-qr-codes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userQrCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restUserQrCodeMockMvc
            .perform(get("/api/user-qr-codes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserQrCodeShouldNotBeFound(String filter) throws Exception {
        restUserQrCodeMockMvc
            .perform(get("/api/user-qr-codes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserQrCodeMockMvc
            .perform(get("/api/user-qr-codes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingUserQrCode() throws Exception {
        // Get the userQrCode
        restUserQrCodeMockMvc.perform(get("/api/user-qr-codes/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserQrCode() throws Exception {
        // Initialize the database
        userQrCodeService.save(userQrCode);

        int databaseSizeBeforeUpdate = userQrCodeRepository.findAll().size();

        // Update the userQrCode
        UserQrCode updatedUserQrCode = userQrCodeRepository.findById(userQrCode.getId()).get();
        // Disconnect from session so that the updates on updatedUserQrCode are not directly saved in db
        em.detach(updatedUserQrCode);
        updatedUserQrCode.code(UPDATED_CODE);

        restUserQrCodeMockMvc
            .perform(
                put("/api/user-qr-codes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserQrCode))
            )
            .andExpect(status().isOk());

        // Validate the UserQrCode in the database
        List<UserQrCode> userQrCodeList = userQrCodeRepository.findAll();
        assertThat(userQrCodeList).hasSize(databaseSizeBeforeUpdate);
        UserQrCode testUserQrCode = userQrCodeList.get(userQrCodeList.size() - 1);
        assertThat(testUserQrCode.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserQrCode() throws Exception {
        int databaseSizeBeforeUpdate = userQrCodeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserQrCodeMockMvc
            .perform(
                put("/api/user-qr-codes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userQrCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserQrCode in the database
        List<UserQrCode> userQrCodeList = userQrCodeRepository.findAll();
        assertThat(userQrCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserQrCode() throws Exception {
        // Initialize the database
        userQrCodeService.save(userQrCode);

        int databaseSizeBeforeDelete = userQrCodeRepository.findAll().size();

        // Delete the userQrCode
        restUserQrCodeMockMvc
            .perform(delete("/api/user-qr-codes/{id}", userQrCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserQrCode> userQrCodeList = userQrCodeRepository.findAll();
        assertThat(userQrCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
