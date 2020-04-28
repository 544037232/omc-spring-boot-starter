package com.pricess.omc.filter;

import com.pricess.omc.api.Filter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class FilterComparator implements Comparator<Filter>, Serializable {

    private static final int INITIAL_ORDER = 100;
    private static final int ORDER_STEP = 100;
    private final Map<String, Integer> filterToOrder = new HashMap<>();

    public FilterComparator() {
        Step order = new Step(INITIAL_ORDER, ORDER_STEP);
        put(DebugFilter.class, order.next());
        put(ParamsAdapterFilter.class, order.next());
        put(ResultBuilderFilter.class, order.next());
        put(ServiceProviderFilter.class, order.next());
        put(StoreProviderFilter.class, order.next());
    }

    public int compare(Filter lhs, Filter rhs) {
        Integer left = getOrder(lhs.getClass());
        Integer right = getOrder(rhs.getClass());
        return left.compareTo(right);
    }

    /**
     * Determines if a particular {@link Filter} is registered to be sorted
     *
     * @param filter
     * @return
     */
    public boolean isRegistered(Class<? extends Filter> filter) {
        return getOrder(filter) != null;
    }

    /**
     * Registers a {@link Filter} to exist after a particular {@link Filter} that is
     * already registered.
     * @param filter the {@link Filter} to register
     * @param afterFilter the {@link Filter} that is already registered and that
     * {@code filter} should be placed after.
     */
    public void registerAfter(Class<? extends Filter> filter,
                              Class<? extends Filter> afterFilter) {
        Integer position = getOrder(afterFilter);
        if (position == null) {
            throw new IllegalArgumentException(
                    "Cannot register after unregistered Filter " + afterFilter);
        }

        put(filter, position + 1);
    }

    /**
     * Registers a {@link Filter} to exist before a particular {@link Filter} that is
     * already registered.
     * @param filter the {@link Filter} to register
     * @param beforeFilter the {@link Filter} that is already registered and that
     * {@code filter} should be placed before.
     */
    public void registerBefore(Class<? extends Filter> filter,
                               Class<? extends Filter> beforeFilter) {
        Integer position = getOrder(beforeFilter);
        if (position == null) {
            throw new IllegalArgumentException(
                    "Cannot register after unregistered Filter " + beforeFilter);
        }

        put(filter, position - 1);
    }

    private void put(Class<? extends Filter> filter, int position) {
        String className = filter.getName();
        filterToOrder.put(className, position);
    }

    /**
     * Gets the order of a particular {@link Filter} class taking into consideration
     * superclasses.
     *
     * @param clazz the {@link Filter} class to determine the sort order
     * @return the sort order or null if not defined
     */
    public Integer getOrder(Class<?> clazz) {
        while (clazz != null) {
            Integer result = filterToOrder.get(clazz.getName());
            if (result != null) {
                return result;
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private static class Step {

        private int value;
        private final int stepSize;

        Step(int initialValue, int stepSize) {
            this.value = initialValue;
            this.stepSize = stepSize;
        }

        int next() {
            int value = this.value;
            this.value += this.stepSize;
            return value;
        }

    }

}
