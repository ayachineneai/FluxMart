package org.ayachinene.utils.data;

public record WithIndex<T>(
        int index,
        T value
) {
}
