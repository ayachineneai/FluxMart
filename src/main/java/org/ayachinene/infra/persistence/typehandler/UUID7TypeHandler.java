package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.utils.Values;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(UUID7.class)
@MappedJdbcTypes(JdbcType.BINARY)
public final class UUID7TypeHandler extends BaseTypeHandler<UUID7> {

    @Override
    public void setNonNullParameter(
            PreparedStatement statement,
            int index,
            UUID7 parameter,
            JdbcType jdbcType
    ) throws SQLException {
        statement.setBytes(index, UUID7s.toBytes(parameter));
    }

    @Override
    public UUID7 getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return Values.map(resultSet.getBytes(columnName), UUID7s::fromBytesUnsafe);
    }

    @Override
    public UUID7 getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return Values.map(resultSet.getBytes(columnIndex), UUID7s::fromBytesUnsafe);
    }

    @Override
    public UUID7 getNullableResult(CallableStatement statement, int columnIndex) throws SQLException {
        return Values.map(statement.getBytes(columnIndex), UUID7s::fromBytesUnsafe);
    }

}
