package de.bildwerk.qr.service.impl;

import de.bildwerk.qr.domain.User;
import de.bildwerk.qr.domain.UserQrCode;
import de.bildwerk.qr.repository.UserQrCodeRepository;
import de.bildwerk.qr.security.SecurityUtils;
import de.bildwerk.qr.service.UserQrCodeService;
import de.bildwerk.qr.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final UserService userService;

    public UserQrCodeServiceImpl(UserQrCodeRepository userQrCodeRepository, UserService userService) {
        this.userQrCodeRepository = userQrCodeRepository;
        this.userService = userService;
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
        return userQrCodeRepository
            .findAll()
            .stream()
            .filter(
                userQrCode ->
                    userService.getUserWithAuthorities().isPresent() &&
                    userService.getUserWithAuthorities().get().equals(userQrCode.getUser())
            )
            .collect(Collectors.toList());
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
