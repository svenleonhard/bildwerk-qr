package de.bildwerk.qr.service;

import de.bildwerk.qr.domain.*; // for static metamodels
import de.bildwerk.qr.domain.UserQrCode;
import de.bildwerk.qr.repository.UserQrCodeRepository;
import de.bildwerk.qr.security.AuthoritiesConstants;
import de.bildwerk.qr.security.SecurityUtils;
import de.bildwerk.qr.service.dto.UserQrCodeCriteria;
import io.github.jhipster.service.QueryService;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link UserQrCode} entities in the database.
 * The main input is a {@link UserQrCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserQrCode} or a {@link Page} of {@link UserQrCode} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserQrCodeQueryService extends QueryService<UserQrCode> {
    private final Logger log = LoggerFactory.getLogger(UserQrCodeQueryService.class);

    private final UserQrCodeRepository userQrCodeRepository;
    private final UserService userService;

    public UserQrCodeQueryService(UserQrCodeRepository userQrCodeRepository, UserService userService) {
        this.userQrCodeRepository = userQrCodeRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link UserQrCode} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserQrCode> findByCriteria(UserQrCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserQrCode> specification = createSpecification(criteria);
        return userQrCodeRepository
            .findAll(specification)
            .stream()
            .filter(
                userQrCode ->
                    userService.getUserWithAuthorities().isPresent() &&
                    (
                        SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
                        userService.getUserWithAuthorities().get().equals(userQrCode.getUser())
                    )
            )
            .collect(Collectors.toList());
    }

    /**
     * Return a {@link Page} of {@link UserQrCode} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserQrCode> findByCriteria(UserQrCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserQrCode> specification = createSpecification(criteria);
        return userQrCodeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserQrCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserQrCode> specification = createSpecification(criteria);
        return userQrCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link UserQrCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserQrCode> createSpecification(UserQrCodeCriteria criteria) {
        Specification<UserQrCode> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserQrCode_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), UserQrCode_.code));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(UserQrCode_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
