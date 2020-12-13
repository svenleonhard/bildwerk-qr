package de.bildwerk.qr.service.impl;

import de.bildwerk.qr.domain.QrRoute;
import de.bildwerk.qr.repository.QrRouteRepository;
import de.bildwerk.qr.service.QrRouteService;
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

    public QrRouteServiceImpl(QrRouteRepository qrRouteRepository) {
        this.qrRouteRepository = qrRouteRepository;
    }

    @Override
    public QrRoute save(QrRoute qrRoute) {
        log.debug("Request to save QrRoute : {}", qrRoute);
        return qrRouteRepository.save(qrRoute);
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
