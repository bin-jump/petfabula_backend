package com.petfabula.domain.common.domain;

import java.io.Serializable;

public abstract class ValueObject implements Serializable {

    protected abstract Object[] getCompareValues();

    @Override
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(this.getClass())) {
            return false;
        }
        if (o == this) {
            return true;
        }

        @SuppressWarnings("unchecked")
        final ValueObject other = (ValueObject) o;
        Object[] myVals = getCompareValues();
        Object[] otherVals = other.getCompareValues();

        for (int i = 0; i < myVals.length; i++) {
            if (myVals[i] == null ||
                    otherVals[i] == null ||
                    !myVals[i].equals(otherVals[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        Object[] myVals = getCompareValues();
        for (int i = 0; i < myVals.length; i++) {
            int v = myVals[i] == null ? 0 : myVals[i].hashCode();
            hash = 31 * hash + v;
        }
        return hash;
    }

}
