package de.bildwerk.qr.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link de.bildwerk.qr.domain.QrRoute} entity. This class is used
 * in {@link de.bildwerk.qr.web.rest.QrRouteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /qr-routes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QrRouteCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter code;

    private StringFilter url;

    private BooleanFilter enabled;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LongFilter userId;

    public QrRouteCriteria() {}

    public QrRouteCriteria(QrRouteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public QrRouteCriteria copy() {
        return new QrRouteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QrRouteCriteria that = (QrRouteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(code, that.code) &&
            Objects.equals(url, that.url) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, code, url, enabled, startDate, endDate, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QrRouteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
