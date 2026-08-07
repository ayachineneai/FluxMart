package org.ayachinene.shared.uuid7;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import com.fasterxml.uuid.impl.UUIDUtil;
import io.vavr.control.Either;

import java.util.function.Supplier;

public final class UUID7s {

    private static final TimeBasedEpochGenerator GENERATOR =
            Generators.timeBasedEpochGenerator();

    private UUID7s() {
    }

    public static UUID7 generate() {
        return UUID7.of(GENERATOR.generate());
    }

    public static Either<IllegalArgumentException, UUID7> fromString(String value) {
        return safely(() -> fromStringUnsafe(value));
    }

    public static UUID7 fromStringUnsafe(String value) {
        return UUID7.of(UUIDUtil.uuid(value));
    }

    public static byte[] toBytes(UUID7 uuid7) {
        return UUIDUtil.asByteArray(uuid7.getValue());
    }

    public static Either<IllegalArgumentException, UUID7> fromBytes(byte[] bytes) {
        return safely(() -> fromBytesUnsafe(bytes));
    }

    public static UUID7 fromBytesUnsafe(byte[] bytes) {
        return UUID7.of(UUIDUtil.uuid(bytes));
    }

    private static Either<IllegalArgumentException, UUID7> safely(
            Supplier<UUID7> operation
    ) {
        try {
            return Either.right(operation.get());
        } catch (RuntimeException exception) {
            return Either.left(new IllegalArgumentException("Invalid UUID7", exception));
        }
    }
}
