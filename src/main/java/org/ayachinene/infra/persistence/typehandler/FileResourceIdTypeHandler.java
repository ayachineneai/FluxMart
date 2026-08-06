package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.ayachinene.app.domain.file.FileResourceId;
import org.ayachinene.app.uuid7.UUID7s;
import org.ayachinene.utils.Values;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(FileResourceId.class)
@MappedJdbcTypes(JdbcType.BINARY)
public final class FileResourceIdTypeHandler extends BaseTypeHandler<FileResourceId> {

    @Override
    public void setNonNullParameter(PreparedStatement statement, int index, FileResourceId parameter, JdbcType jdbcType) throws SQLException {
        statement.setBytes(index, UUID7s.toBytes(parameter.value()));
    }

    @Override
    public FileResourceId getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return Values.map(resultSet.getBytes(columnName), bytes -> new FileResourceId(UUID7s.fromBytesUnsafe(bytes)));
    }

    @Override
    public FileResourceId getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return Values.map(resultSet.getBytes(columnIndex), bytes -> new FileResourceId(UUID7s.fromBytesUnsafe(bytes)));
    }

    @Override
    public FileResourceId getNullableResult(CallableStatement statement, int columnIndex) throws SQLException {
        return Values.map(statement.getBytes(columnIndex), bytes -> new FileResourceId(UUID7s.fromBytesUnsafe(bytes)));
    }

}
