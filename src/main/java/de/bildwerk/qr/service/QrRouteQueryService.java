package de.bildwerk.qr.service;

import de.bildwerk.qr.domain.*; // for static metamodels
import de.bildwerk.qr.domain.QrRoute;
import de.bildwerk.qr.repository.QrRouteRepository;
import de.bildwerk.qr.security.AuthoritiesConstants;
import de.bildwerk.qr.security.SecurityUtils;
import de.bildwerk.qr.service.dto.QrRouteCriteria;
import io.github.jhipster.service.QueryService;
import io.github.jhipster.service.filter.LongFilter;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link QrRoute} entities in the database.
 * The main input is a {@link QrRouteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QrRoute} or a {@link Page} of {@link QrRoute} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QrRouteQueryService extends QueryService<QrRoute> {
    private final Logger log = LoggerFactory.getLogger(QrRouteQueryService.class);

    private final QrRouteRepository qrRouteRepository;
    private final UserService userService;

    public QrRouteQueryService(QrRouteRepository qrRouteRepository, UserService userService) {
        this.qrRouteRepository = qrRouteRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link QrRoute} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QrRoute> findByCriteria(QrRouteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<QrRoute> specification = createSpecification(criteria);
        return qrRouteRepository
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
     * Return a {@link Page} of {@link QrRoute} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QrRoute> findByCriteria(QrRouteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            return qrRouteRepository.findAll(createSpecification(criteria), page);
        }
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(userService.getUserWithAuthorities().get().getId());
        criteria.setUserId(longFilter);
        return qrRouteRepository.findAll(createSpecification(criteria), page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QrRouteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<QrRoute> specification = createSpecification(criteria);
        return qrRouteRepository.count(specification);
    }

    /**
     * Function to convert {@link QrRouteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QrRoute> createSpecification(QrRouteCriteria criteria) {
        Specification<QrRoute> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), QrRoute_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), QrRoute_.description));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), QrRoute_.code));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), QrRoute_.url));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), QrRoute_.enabled));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), QrRoute_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), QrRoute_.endDate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(QrRoute_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
