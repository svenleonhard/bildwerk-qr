package de.bildwerk.qr.repository;

import de.bildwerk.qr.domain.QrRoute;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the QrRoute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QrRouteRepository extends JpaRepository<QrRoute, Long>, JpaSpecificationExecutor<QrRoute> {
    @Query("select qrRoute from QrRoute qrRoute where qrRoute.user.login = ?#{principal.username}")
    List<QrRoute> findByUserIsCurrentUser();
}
