package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.utils.Values;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ProductCode.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public final class ProductCodeTypeHandler extends BaseTypeHandler<ProductCode> {

    @Override
    public void setNonNullParameter(PreparedStatement statement, int index, ProductCode parameter, JdbcType jdbcType) throws SQLException {
        statement.setString(index, parameter.value());
    }

    @Override
    public ProductCode getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return Values.map(resultSet.getString(columnName), ProductCode::new);
    }

    @Override
    public ProductCode getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return Values.map(resultSet.getString(columnIndex), ProductCode::new);
    }

    @Override
    public ProductCode getNullableResult(CallableStatement statement, int columnIndex) throws SQLException {
        return Values.map(statement.getString(columnIndex), ProductCode::new);
    }

}
