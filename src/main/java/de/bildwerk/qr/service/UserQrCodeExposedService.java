package de.bildwerk.qr.service;

import de.bildwerk.qr.domain.UserQrCodeExposed;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link UserQrCodeExposed}.
 */
public interface UserQrCodeExposedService {
    /**
     * Save a userQrCodeExposed.
     *
     * @param userQrCodeExposed the entity to save.
     * @return the persisted entity.
     */
    UserQrCodeExposed save(UserQrCodeExposed userQrCodeExposed);

    /**
     * Get all the userQrCodeExposeds.
     *
     * @return the list of entities.
     */
    List<UserQrCodeExposed> findAll();

    /**
     * Get the "id" userQrCodeExposed.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserQrCodeExposed> findOne(Long id);

    /**
     * Get the "code" userQrCodeExposed.
     *
     * @param code the id of the entity.
     * @return the entity.
     */
    Optional<UserQrCodeExposed> findByCode(String code);

    /**
     * Delete the "id" userQrCodeExposed.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
