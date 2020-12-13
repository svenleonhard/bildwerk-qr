package de.bildwerk.qr.service.impl;

import de.bildwerk.qr.domain.UserQrCode;
import de.bildwerk.qr.repository.UserQrCodeRepository;
import de.bildwerk.qr.service.UserQrCodeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserQrCode}.
 */
@Service
@Transactional
public class UserQrCodeServiceImpl implements UserQrCodeService {
    private final Logger log = LoggerFactory.getLogger(UserQrCodeServiceImpl.class);

    private final UserQrCodeRepository userQrCodeRepository;

    public UserQrCodeServiceImpl(UserQrCodeRepository userQrCodeRepository) {
        this.userQrCodeRepository = userQrCodeRepository;
    }

    @Override
    public UserQrCode save(UserQrCode userQrCode) {
        log.debug("Request to save UserQrCode : {}", userQrCode);
        return userQrCodeRepository.save(userQrCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserQrCode> findAll() {
        log.debug("Request to get all UserQrCodes");
        return userQrCodeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserQrCode> findOne(Long id) {
        log.debug("Request to get UserQrCode : {}", id);
        return userQrCodeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserQrCode : {}", id);
        userQrCodeRepository.deleteById(id);
    }
}
