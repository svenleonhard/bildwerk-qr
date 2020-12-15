package de.bildwerk.qr.service.impl;

import de.bildwerk.qr.domain.UserQrCodeExposed;
import de.bildwerk.qr.repository.UserQrCodeExposedRepository;
import de.bildwerk.qr.service.UserQrCodeExposedQueryService;
import de.bildwerk.qr.service.UserQrCodeExposedService;
import de.bildwerk.qr.service.dto.UserQrCodeExposedCriteria;
import io.github.jhipster.service.filter.StringFilter;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserQrCodeExposed}.
 */
@Service
@Transactional
public class UserQrCodeExposedServiceImpl implements UserQrCodeExposedService {
    private final Logger log = LoggerFactory.getLogger(UserQrCodeExposedServiceImpl.class);

    private final UserQrCodeExposedRepository userQrCodeExposedRepository;
    private final UserQrCodeExposedQueryService userQrCodeExposedQueryService;

    public UserQrCodeExposedServiceImpl(
        UserQrCodeExposedRepository userQrCodeExposedRepository,
        UserQrCodeExposedQueryService userQrCodeExposedQueryService
    ) {
        this.userQrCodeExposedRepository = userQrCodeExposedRepository;
        this.userQrCodeExposedQueryService = userQrCodeExposedQueryService;
    }

    @Override
    public UserQrCodeExposed save(UserQrCodeExposed userQrCodeExposed) {
        log.debug("Request to save UserQrCodeExposed : {}", userQrCodeExposed);
        return userQrCodeExposedRepository.save(userQrCodeExposed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserQrCodeExposed> findAll() {
        log.debug("Request to get all UserQrCodeExposeds");
        return userQrCodeExposedRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserQrCodeExposed> findOne(Long id) {
        log.debug("Request to get UserQrCodeExposed : {}", id);
        return userQrCodeExposedRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserQrCodeExposed> findByCode(String code) {
        log.debug("Request to get UserQrCodeExposed : {}", code);
        UserQrCodeExposedCriteria criteria = new UserQrCodeExposedCriteria();
        StringFilter stringFilter = new StringFilter();
        stringFilter.setEquals(code);
        criteria.setCode(stringFilter);
        List<UserQrCodeExposed> entityList = userQrCodeExposedQueryService.findByCriteria(criteria);
        return entityList.stream().findFirst();
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserQrCodeExposed : {}", id);
        userQrCodeExposedRepository.deleteById(id);
    }
}
