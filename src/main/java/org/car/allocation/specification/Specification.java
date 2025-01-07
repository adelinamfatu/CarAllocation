package org.car.allocation.specification;
/**
 * A generic specification interface that defines methods for evaluating whether
 * an object satisfies certain criteria. It allows combining specifications using
 * logical operators like AND, OR, and NOT.
 *
 * @param <T> the type of object that the specification is evaluating.
 */
public interface Specification<T> {
    boolean isSatisfiedBy(T t);
    Specification<T> and(Specification<T> other);
    Specification<T> or(Specification<T> other);
    Specification<T> not();
}
