package org.car.allocation.specification;

/**
 * A composite specification that combines two specifications with a logical AND operation.
 * This means an object satisfies this specification only if it satisfies both of the combined specifications.
 *
 * @param <T> the type of object being tested by the specification.
 */
public class AndSpecification<T> implements Specification<T> {
    private final Specification<T> spec1;
    private final Specification<T> spec2;

    public AndSpecification(Specification<T> spec1, Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    @Override
    public boolean isSatisfiedBy(T t) {
        return spec1.isSatisfiedBy(t) && spec2.isSatisfiedBy(t);
    }

    /**
     * Combines this specification with another using a logical AND operation.
     *
     * @param other the other specification to combine.
     * @return a new AndSpecification representing the combination.
     */
    @Override
    public Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }

    /**
     * Combines this specification with another using a logical OR operation.
     *
     * @param other the other specification to combine.
     * @return a new OrSpecification representing the combination.
     */
    @Override
    public Specification<T> or(Specification<T> other) {
        return new OrSpecification<>(this, other);
    }

    /**
     * Negates this specification using a logical NOT operation.
     *
     * @return a new NotSpecification representing the negation of this specification.
     */
    @Override
    public Specification<T> not() {
        return new NotSpecification<>(this);
    }
}
