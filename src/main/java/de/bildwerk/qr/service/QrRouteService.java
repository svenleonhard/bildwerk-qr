package de.bildwerk.qr.service;

import de.bildwerk.qr.domain.QrRoute;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link QrRoute}.
 */
public interface QrRouteService {
    /**
     * Save a qrRoute.
     *
     * @param qrRoute the entity to save.
     * @return the persisted entity.
     */
    QrRoute save(QrRoute qrRoute);

    /**
     * Get all the qrRoutes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QrRoute> findAll(Pageable pageable);

    /**
     * Get the "id" qrRoute.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QrRoute> findOne(Long id);

    /**
     * Delete the "id" qrRoute.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
