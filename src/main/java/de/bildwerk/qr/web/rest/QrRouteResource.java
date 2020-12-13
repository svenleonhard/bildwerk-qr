package de.bildwerk.qr.web.rest;

import de.bildwerk.qr.domain.QrRoute;
import de.bildwerk.qr.service.QrRouteService;
import de.bildwerk.qr.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link de.bildwerk.qr.domain.QrRoute}.
 */
@RestController
@RequestMapping("/api")
public class QrRouteResource {
    private final Logger log = LoggerFactory.getLogger(QrRouteResource.class);

    private static final String ENTITY_NAME = "qrRoute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QrRouteService qrRouteService;

    public QrRouteResource(QrRouteService qrRouteService) {
        this.qrRouteService = qrRouteService;
    }

    /**
     * {@code POST  /qr-routes} : Create a new qrRoute.
     *
     * @param qrRoute the qrRoute to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qrRoute, or with status {@code 400 (Bad Request)} if the qrRoute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/qr-routes")
    public ResponseEntity<QrRoute> createQrRoute(@RequestBody QrRoute qrRoute) throws URISyntaxException {
        log.debug("REST request to save QrRoute : {}", qrRoute);
        if (qrRoute.getId() != null) {
            throw new BadRequestAlertException("A new qrRoute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QrRoute result = qrRouteService.save(qrRoute);
        return ResponseEntity
            .created(new URI("/api/qr-routes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /qr-routes} : Updates an existing qrRoute.
     *
     * @param qrRoute the qrRoute to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qrRoute,
     * or with status {@code 400 (Bad Request)} if the qrRoute is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qrRoute couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/qr-routes")
    public ResponseEntity<QrRoute> updateQrRoute(@RequestBody QrRoute qrRoute) throws URISyntaxException {
        log.debug("REST request to update QrRoute : {}", qrRoute);
        if (qrRoute.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QrRoute result = qrRouteService.save(qrRoute);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qrRoute.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /qr-routes} : get all the qrRoutes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qrRoutes in body.
     */
    @GetMapping("/qr-routes")
    public ResponseEntity<List<QrRoute>> getAllQrRoutes(Pageable pageable) {
        log.debug("REST request to get a page of QrRoutes");
        Page<QrRoute> page = qrRouteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /qr-routes/:id} : get the "id" qrRoute.
     *
     * @param id the id of the qrRoute to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qrRoute, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/qr-routes/{id}")
    public ResponseEntity<QrRoute> getQrRoute(@PathVariable Long id) {
        log.debug("REST request to get QrRoute : {}", id);
        Optional<QrRoute> qrRoute = qrRouteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(qrRoute);
    }

    /**
     * {@code DELETE  /qr-routes/:id} : delete the "id" qrRoute.
     *
     * @param id the id of the qrRoute to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/qr-routes/{id}")
    public ResponseEntity<Void> deleteQrRoute(@PathVariable Long id) {
        log.debug("REST request to delete QrRoute : {}", id);
        qrRouteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
