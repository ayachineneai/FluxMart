package org.ayachinene.app.uuid7;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UUID7Test {

    private static final String UUID7_TEXT =
            "018f6b5c-7c00-7000-8000-000000000001";

    @Test
    void parsesAUuidVersion7() {
        var result = UUID7s.fromString(UUID7_TEXT);

        assertTrue(result.isRight());
        assertEquals(7, result.get().getValue().version());
        assertEquals(2, result.get().getValue().variant());
    }

    @Test
    void rejectsOtherUuidVersionsFromEveryEntryPoint() {
        var uuid4 = UUID.randomUUID();

        assertTrue(UUID7s.fromString(uuid4.toString()).isLeft());
        assertTrue(UUID7s.fromBytes(UUIDUtilForTest.toBytes(uuid4)).isLeft());
        assertThrows(
                IllegalArgumentException.class,
                () -> UUID7s.fromStringUnsafe(uuid4.toString())
        );
    }

    @Test
    void convertsToAndFromBytes() {
        var uuid7 = UUID7s.fromStringUnsafe(UUID7_TEXT);

        assertEquals(uuid7, UUID7s.fromBytesUnsafe(UUID7s.toBytes(uuid7)));
    }

    @Test
    void generatesUuidVersion7WithJug() {
        var generated = UUID7s.generate();

        assertEquals(7, generated.getValue().version());
        assertEquals(2, generated.getValue().variant());
    }

    private static final class UUIDUtilForTest {
        private static byte[] toBytes(UUID uuid) {
            var bytes = new byte[16];
            var buffer = java.nio.ByteBuffer.wrap(bytes);
            buffer.putLong(uuid.getMostSignificantBits());
            buffer.putLong(uuid.getLeastSignificantBits());
            return bytes;
        }
    }
}
