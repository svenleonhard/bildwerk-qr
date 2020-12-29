package de.bildwerk.qr.service.impl;

import de.bildwerk.qr.domain.QrRoute;
import de.bildwerk.qr.domain.UserQrCode;
import de.bildwerk.qr.domain.UserQrCodeExposed;
import de.bildwerk.qr.repository.QrRouteRepository;
import de.bildwerk.qr.service.QrRouteService;
import de.bildwerk.qr.service.UserQrCodeExposedService;
import de.bildwerk.qr.service.UserQrCodeQueryService;
import de.bildwerk.qr.service.UserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QrRoute}.
 */
@Service
@Transactional
public class QrRouteServiceImpl implements QrRouteService {
    private final Logger log = LoggerFactory.getLogger(QrRouteServiceImpl.class);

    private final QrRouteRepository qrRouteRepository;
    private final UserQrCodeExposedService userQrCodeExposedService;
    private final UserQrCodeQueryService userQrCodeQueryService;
    private final UserService userService;

    public QrRouteServiceImpl(
        QrRouteRepository qrRouteRepository,
        UserQrCodeExposedService userQrCodeExposedService,
        UserQrCodeQueryService userQrCodeQueryService,
        UserService userService
    ) {
        this.qrRouteRepository = qrRouteRepository;
        this.userQrCodeExposedService = userQrCodeExposedService;
        this.userQrCodeQueryService = userQrCodeQueryService;
        this.userService = userService;
    }

    @Override
    public QrRoute save(QrRoute qrRoute) {
        log.debug("Request to save QrRoute : {}", qrRoute);

        if (!qrRoute.getUrl().matches("^https?://.*")) {
            qrRoute.setUrl("https://" + qrRoute.getUrl());
        }
        QrRoute savedQrRoute = qrRouteRepository.save(qrRoute);
        try {
            UserQrCode userQrCode = userQrCodeQueryService
                .findByUser(userService.getUserWithAuthorities().orElseThrow(() -> new Exception("No user found")))
                .orElseThrow(() -> new Exception("No user qr code found"));
            UserQrCodeExposed userQrCodeExposed = userQrCodeExposedService
                .findByCode(userQrCode.getCode())
                .orElseThrow(() -> new Exception("No exposed qr code found"));
            userQrCodeExposed.setUrl(qrRoute.getUrl());
            userQrCodeExposedService.save(userQrCodeExposed);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return savedQrRoute;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QrRoute> findAll(Pageable pageable) {
        log.debug("Request to get all QrRoutes");
        return qrRouteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QrRoute> findOne(Long id) {
        log.debug("Request to get QrRoute : {}", id);
        return qrRouteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QrRoute : {}", id);
        qrRouteRepository.deleteById(id);
    }
}
