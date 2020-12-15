package de.bildwerk.qr.repository;

import de.bildwerk.qr.domain.UserQrCodeExposed;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the UserQrCodeExposed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserQrCodeExposedRepository extends JpaRepository<UserQrCodeExposed, Long>, JpaSpecificationExecutor<UserQrCodeExposed> {}
