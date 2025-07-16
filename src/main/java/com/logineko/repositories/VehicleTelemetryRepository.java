package com.logineko.repositories;

import static com.logineko.utils.DateTimeUtils.EXTERNAL_SOURCE_DATE_FORMATTER;

import com.logineko.dto.pagination.PaginationQueryParam;
import com.logineko.dto.telemetry.TelemetryFilterDto;
import com.logineko.entities.CombineTelemetry;
import com.logineko.entities.TractorTelemetry;
import com.logineko.entities.Vehicle;
import com.logineko.entities.VehicleTelemetry;
import com.logineko.entities.VehicleTelemetry_;
import com.logineko.repositories.dto.PaginatedResult;
import com.logineko.services.TelemetryFieldRegistryService;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class VehicleTelemetryRepository implements PanacheRepositoryBase<VehicleTelemetry, UUID> {
  @Inject EntityManager em;

  @Inject TelemetryFieldRegistryService fieldRegistry;

  public PaginatedResult<VehicleTelemetry> searchTelemetry(
      List<TelemetryFilterDto> filters, PaginationQueryParam pagination) {
    List<VehicleTelemetry> result = search(filters, pagination);
    Long count = countSearch(filters);
    return new PaginatedResult<>(result, count);
  }

  private List<VehicleTelemetry> search(
      List<TelemetryFilterDto> filters, PaginationQueryParam pagination) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<VehicleTelemetry> cq = cb.createQuery(VehicleTelemetry.class);
    Root<VehicleTelemetry> root = cq.from(VehicleTelemetry.class);

    Predicate predicate = buildSearchPredicate(root, cb, filters);
    cq.where(predicate);

    TypedQuery<VehicleTelemetry> query = em.createQuery(cq);
    query.setFirstResult(pagination.getOffset());
    query.setMaxResults(pagination.getSize());

    return query.getResultList();
  }

  private Long countSearch(List<TelemetryFilterDto> filters) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<VehicleTelemetry> root = cq.from(VehicleTelemetry.class);

    Predicate finalPredicate = buildSearchPredicate(root, cb, filters);
    cq.select(cb.count(root)).where(finalPredicate);

    return em.createQuery(cq).getSingleResult();
  }

  @SuppressWarnings("unchecked")
  private Predicate buildSearchPredicate(
      Root<VehicleTelemetry> root, CriteriaBuilder cb, List<TelemetryFilterDto> filters) {
    List<Predicate> predicates = new ArrayList<>();

    for (TelemetryFilterDto filter : filters) {
      String fieldName = filter.field();
      Object value = prepareValue(fieldName, filter.value());
      Path<?> fieldPath = getPathForField(root, cb, fieldName);

      switch (filter.operation()) {
        case EQUALS -> predicates.add(cb.equal(fieldPath, value));
        case CONTAINS -> {
          Expression<String> fieldAsString = fieldPath.as(String.class);
          predicates.add(
              cb.like(cb.lower(fieldAsString), "%" + value.toString().toLowerCase() + "%"));
        }
        case GREATER_THAN ->
            predicates.add(cb.greaterThan((Expression<Comparable>) fieldPath, (Comparable) value));
        case LESS_THAN ->
            predicates.add(cb.lessThan((Expression<Comparable>) fieldPath, (Comparable) value));
      }
    }

    return cb.and(predicates.toArray(new Predicate[0]));
  }

  private Path<?> getPathForField(
      Root<VehicleTelemetry> root, CriteriaBuilder cb, String fieldName) {
    Class<?> declaringClass = fieldRegistry.getDeclaringClass(fieldName);

    if (Vehicle.class.equals(declaringClass)) {
      return root.join(VehicleTelemetry_.vehicle).get(fieldName);
    }
    if (CombineTelemetry.class.equals(declaringClass)) {
      return cb.treat(root, CombineTelemetry.class).get(fieldName);
    }
    if (TractorTelemetry.class.equals(declaringClass)) {
      return cb.treat(root, TractorTelemetry.class).get(fieldName);
    }

    // field is in the base VehicleTelemetry class
    return root.get(fieldName);
  }

  @SuppressWarnings("unchecked")
  private Object prepareValue(String fieldName, Object value) {
    Class<?> fieldType = fieldRegistry.getFieldType(fieldName);

    // the validator already confirmed the value is valid, but as a safeguard we will try/catch
    try {
      if (fieldType.isEnum() && value instanceof String stringValue) {
        return Enum.valueOf((Class<Enum>) fieldType, stringValue);
      }

      if (LocalDateTime.class.isAssignableFrom(fieldType) && value instanceof String stringValue) {
        return LocalDateTime.parse(stringValue, EXTERNAL_SOURCE_DATE_FORMATTER);
      }

      return value;
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Invalid value for enum field '" + fieldName + "': " + value, e);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException(
          "Invalid date/time format for field '" + fieldName + "': " + value, e);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Invalid value for field '" + fieldName + "': " + value, e);
    }
  }
}
