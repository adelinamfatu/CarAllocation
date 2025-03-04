package org.car.allocation.specification;

/**
 * A specification that evaluates the logical OR operation between two specifications.
 *
 * @param <T> the type of objects that this specification applies to.
 */
public class OrSpecification<T> implements Specification<T> {
    private final Specification<T> spec1;
    private final Specification<T> spec2;

    public OrSpecification(Specification<T> spec1, Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    @Override
    public boolean isSatisfiedBy(T t) {
        return spec1.isSatisfiedBy(t) || spec2.isSatisfiedBy(t);
    }

    @Override
    public Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }

    @Override
    public Specification<T> or(Specification<T> other) {
        return new OrSpecification<>(this, other);
    }

    @Override
    public Specification<T> not() {
        return new NotSpecification<>(this);
    }
}
