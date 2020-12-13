package de.bildwerk.qr.service;

import de.bildwerk.qr.domain.UserQrCode;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link UserQrCode}.
 */
public interface UserQrCodeService {
    /**
     * Save a userQrCode.
     *
     * @param userQrCode the entity to save.
     * @return the persisted entity.
     */
    UserQrCode save(UserQrCode userQrCode);

    /**
     * Get all the userQrCodes.
     *
     * @return the list of entities.
     */
    List<UserQrCode> findAll();

    /**
     * Get the "id" userQrCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserQrCode> findOne(Long id);

    /**
     * Delete the "id" userQrCode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
