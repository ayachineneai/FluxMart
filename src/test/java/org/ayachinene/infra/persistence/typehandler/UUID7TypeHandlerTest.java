package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.ayachinene.app.uuid7.UUID7s;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UUID7TypeHandlerTest {

    private final UUID7TypeHandler handler = new UUID7TypeHandler();

    @Test
    void writesUuid7AsSixteenBytes() throws Exception {
        var statement = mock(PreparedStatement.class);
        var uuid7 = UUID7s.generate();

        handler.setNonNullParameter(statement, 1, uuid7, JdbcType.BINARY);

        verify(statement).setBytes(1, UUID7s.toBytes(uuid7));
    }

    @Test
    void readsUuid7FromSixteenBytes() throws Exception {
        var resultSet = mock(ResultSet.class);
        var uuid7 = UUID7s.generate();
        when(resultSet.getBytes("id")).thenReturn(UUID7s.toBytes(uuid7));

        assertEquals(uuid7, handler.getNullableResult(resultSet, "id"));
    }

    @Test
    void preservesSqlNull() throws Exception {
        var resultSet = mock(ResultSet.class);
        when(resultSet.getBytes("id")).thenReturn(null);

        assertNull(handler.getNullableResult(resultSet, "id"));
    }
}
