package de.bildwerk.qr.web.rest;

import de.bildwerk.qr.domain.UserQrCode;
import de.bildwerk.qr.service.UserQrCodeQueryService;
import de.bildwerk.qr.service.UserQrCodeService;
import de.bildwerk.qr.service.dto.UserQrCodeCriteria;
import de.bildwerk.qr.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link de.bildwerk.qr.domain.UserQrCode}.
 */
@RestController
@RequestMapping("/api")
public class UserQrCodeResource {
    private final Logger log = LoggerFactory.getLogger(UserQrCodeResource.class);

    private static final String ENTITY_NAME = "userQrCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserQrCodeService userQrCodeService;

    private final UserQrCodeQueryService userQrCodeQueryService;

    public UserQrCodeResource(UserQrCodeService userQrCodeService, UserQrCodeQueryService userQrCodeQueryService) {
        this.userQrCodeService = userQrCodeService;
        this.userQrCodeQueryService = userQrCodeQueryService;
    }

    /**
     * {@code POST  /user-qr-codes} : Create a new userQrCode.
     *
     * @param userQrCode the userQrCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userQrCode, or with status {@code 400 (Bad Request)} if the userQrCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-qr-codes")
    public ResponseEntity<UserQrCode> createUserQrCode(@RequestBody UserQrCode userQrCode) throws URISyntaxException {
        log.debug("REST request to save UserQrCode : {}", userQrCode);
        if (userQrCode.getId() != null) {
            throw new BadRequestAlertException("A new userQrCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserQrCode result = userQrCodeService.save(userQrCode);
        return ResponseEntity
            .created(new URI("/api/user-qr-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-qr-codes} : Updates an existing userQrCode.
     *
     * @param userQrCode the userQrCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userQrCode,
     * or with status {@code 400 (Bad Request)} if the userQrCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userQrCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-qr-codes")
    public ResponseEntity<UserQrCode> updateUserQrCode(@RequestBody UserQrCode userQrCode) throws URISyntaxException {
        log.debug("REST request to update UserQrCode : {}", userQrCode);
        if (userQrCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserQrCode result = userQrCodeService.save(userQrCode);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userQrCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-qr-codes} : get all the userQrCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userQrCodes in body.
     */
    @GetMapping("/user-qr-codes")
    public ResponseEntity<List<UserQrCode>> getAllUserQrCodes(UserQrCodeCriteria criteria) {
        log.debug("REST request to get UserQrCodes by criteria: {}", criteria);
        List<UserQrCode> entityList = userQrCodeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /user-qr-codes/count} : count all the userQrCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-qr-codes/count")
    public ResponseEntity<Long> countUserQrCodes(UserQrCodeCriteria criteria) {
        log.debug("REST request to count UserQrCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(userQrCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-qr-codes/:id} : get the "id" userQrCode.
     *
     * @param id the id of the userQrCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userQrCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-qr-codes/{id}")
    public ResponseEntity<UserQrCode> getUserQrCode(@PathVariable Long id) {
        log.debug("REST request to get UserQrCode : {}", id);
        Optional<UserQrCode> userQrCode = userQrCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userQrCode);
    }

    /**
     * {@code DELETE  /user-qr-codes/:id} : delete the "id" userQrCode.
     *
     * @param id the id of the userQrCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-qr-codes/{id}")
    public ResponseEntity<Void> deleteUserQrCode(@PathVariable Long id) {
        log.debug("REST request to delete UserQrCode : {}", id);
        userQrCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
