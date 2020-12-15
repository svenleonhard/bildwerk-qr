package de.bildwerk.qr.web.rest;

import de.bildwerk.qr.domain.UserQrCodeExposed;
import de.bildwerk.qr.service.UserQrCodeExposedQueryService;
import de.bildwerk.qr.service.UserQrCodeExposedService;
import de.bildwerk.qr.service.dto.UserQrCodeExposedCriteria;
import de.bildwerk.qr.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link de.bildwerk.qr.domain.UserQrCodeExposed}.
 */
@RestController
@RequestMapping("/api")
public class UserQrCodeExposedResource {
    private final Logger log = LoggerFactory.getLogger(UserQrCodeExposedResource.class);

    private static final String ENTITY_NAME = "userQrCodeExposed";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserQrCodeExposedService userQrCodeExposedService;

    private final UserQrCodeExposedQueryService userQrCodeExposedQueryService;

    public UserQrCodeExposedResource(
        UserQrCodeExposedService userQrCodeExposedService,
        UserQrCodeExposedQueryService userQrCodeExposedQueryService
    ) {
        this.userQrCodeExposedService = userQrCodeExposedService;
        this.userQrCodeExposedQueryService = userQrCodeExposedQueryService;
    }

    /**
     * {@code POST  /user-qr-code-exposeds} : Create a new userQrCodeExposed.
     *
     * @param userQrCodeExposed the userQrCodeExposed to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userQrCodeExposed, or with status {@code 400 (Bad Request)} if the userQrCodeExposed has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-qr-code-exposeds")
    public ResponseEntity<UserQrCodeExposed> createUserQrCodeExposed(@RequestBody UserQrCodeExposed userQrCodeExposed)
        throws URISyntaxException {
        log.debug("REST request to save UserQrCodeExposed : {}", userQrCodeExposed);
        if (userQrCodeExposed.getId() != null) {
            throw new BadRequestAlertException("A new userQrCodeExposed cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserQrCodeExposed result = userQrCodeExposedService.save(userQrCodeExposed);
        return ResponseEntity
            .created(new URI("/api/user-qr-code-exposeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-qr-code-exposeds} : Updates an existing userQrCodeExposed.
     *
     * @param userQrCodeExposed the userQrCodeExposed to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userQrCodeExposed,
     * or with status {@code 400 (Bad Request)} if the userQrCodeExposed is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userQrCodeExposed couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-qr-code-exposeds")
    public ResponseEntity<UserQrCodeExposed> updateUserQrCodeExposed(@RequestBody UserQrCodeExposed userQrCodeExposed)
        throws URISyntaxException {
        log.debug("REST request to update UserQrCodeExposed : {}", userQrCodeExposed);
        if (userQrCodeExposed.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserQrCodeExposed result = userQrCodeExposedService.save(userQrCodeExposed);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userQrCodeExposed.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-qr-code-exposeds} : get all the userQrCodeExposeds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userQrCodeExposeds in body.
     */
    @GetMapping("/user-qr-code-exposeds")
    public ResponseEntity<List<UserQrCodeExposed>> getAllUserQrCodeExposeds(UserQrCodeExposedCriteria criteria) {
        log.debug("REST request to get UserQrCodeExposeds by criteria: {}", criteria);
        List<UserQrCodeExposed> entityList = userQrCodeExposedQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /user-qr-code-exposeds/count} : count all the userQrCodeExposeds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-qr-code-exposeds/count")
    public ResponseEntity<Long> countUserQrCodeExposeds(UserQrCodeExposedCriteria criteria) {
        log.debug("REST request to count UserQrCodeExposeds by criteria: {}", criteria);
        return ResponseEntity.ok().body(userQrCodeExposedQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-qr-code-exposeds/:id} : get the "id" userQrCodeExposed.
     *
     * @param code the id of the userQrCodeExposed to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userQrCodeExposed, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-qr-code-exposeds/{code}")
    public ResponseEntity<UserQrCodeExposed> getUserQrCodeExposed(@PathVariable String code) {
        log.debug("REST request to get UserQrCodeExposed : {}", code);
        return ResponseUtil.wrapOrNotFound(userQrCodeExposedService.findByCode(code));
    }

    /**
     * {@code DELETE  /user-qr-code-exposeds/:id} : delete the "id" userQrCodeExposed.
     *
     * @param id the id of the userQrCodeExposed to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-qr-code-exposeds/{id}")
    public ResponseEntity<Void> deleteUserQrCodeExposed(@PathVariable Long id) {
        log.debug("REST request to delete UserQrCodeExposed : {}", id);
        userQrCodeExposedService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
