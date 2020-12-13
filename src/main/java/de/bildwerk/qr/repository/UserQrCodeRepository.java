package de.bildwerk.qr.repository;

import de.bildwerk.qr.domain.UserQrCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the UserQrCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserQrCodeRepository extends JpaRepository<UserQrCode, Long>, JpaSpecificationExecutor<UserQrCode> {}
