package de.bildwerk.qr.service;

import de.bildwerk.qr.domain.*; // for static metamodels
import de.bildwerk.qr.domain.UserQrCodeExposed;
import de.bildwerk.qr.repository.UserQrCodeExposedRepository;
import de.bildwerk.qr.service.dto.UserQrCodeExposedCriteria;
import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link UserQrCodeExposed} entities in the database.
 * The main input is a {@link UserQrCodeExposedCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserQrCodeExposed} or a {@link Page} of {@link UserQrCodeExposed} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserQrCodeExposedQueryService extends QueryService<UserQrCodeExposed> {
    private final Logger log = LoggerFactory.getLogger(UserQrCodeExposedQueryService.class);

    private final UserQrCodeExposedRepository userQrCodeExposedRepository;

    public UserQrCodeExposedQueryService(UserQrCodeExposedRepository userQrCodeExposedRepository) {
        this.userQrCodeExposedRepository = userQrCodeExposedRepository;
    }

    /**
     * Return a {@link List} of {@link UserQrCodeExposed} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserQrCodeExposed> findByCriteria(UserQrCodeExposedCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserQrCodeExposed> specification = createSpecification(criteria);
        return userQrCodeExposedRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserQrCodeExposed} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserQrCodeExposed> findByCriteria(UserQrCodeExposedCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserQrCodeExposed> specification = createSpecification(criteria);
        return userQrCodeExposedRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserQrCodeExposedCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserQrCodeExposed> specification = createSpecification(criteria);
        return userQrCodeExposedRepository.count(specification);
    }

    /**
     * Function to convert {@link UserQrCodeExposedCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserQrCodeExposed> createSpecification(UserQrCodeExposedCriteria criteria) {
        Specification<UserQrCodeExposed> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserQrCodeExposed_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), UserQrCodeExposed_.code));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), UserQrCodeExposed_.url));
            }
        }
        return specification;
    }
}
