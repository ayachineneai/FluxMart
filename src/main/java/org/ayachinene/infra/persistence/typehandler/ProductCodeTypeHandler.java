package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.uuid7.UUID7s;
import org.ayachinene.utils.Values;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ProductCode.class)
@MappedJdbcTypes(JdbcType.BINARY)
public final class ProductCodeTypeHandler extends BaseTypeHandler<ProductCode> {

    @Override
    public void setNonNullParameter(PreparedStatement statement, int index, ProductCode parameter, JdbcType jdbcType) throws SQLException {
        statement.setBytes(index, UUID7s.toBytes(parameter.value()));
    }

    @Override
    public ProductCode getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return Values.map(resultSet.getBytes(columnName), bytes -> new ProductCode(UUID7s.fromBytesUnsafe(bytes)));
    }

    @Override
    public ProductCode getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return Values.map(resultSet.getBytes(columnIndex), bytes -> new ProductCode(UUID7s.fromBytesUnsafe(bytes)));
    }

    @Override
    public ProductCode getNullableResult(CallableStatement statement, int columnIndex) throws SQLException {
        return Values.map(statement.getBytes(columnIndex), bytes -> new ProductCode(UUID7s.fromBytesUnsafe(bytes)));
    }

}
