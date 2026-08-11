package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.utils.Values;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(SpecificationValueCode.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public final class SpecificationValueCodeTypeHandler
        extends BaseTypeHandler<SpecificationValueCode> {

    @Override
    public void setNonNullParameter(
            PreparedStatement statement,
            int index,
            SpecificationValueCode parameter,
            JdbcType jdbcType
    ) throws SQLException {
        statement.setString(index, parameter.value());
    }

    @Override
    public SpecificationValueCode getNullableResult(
            ResultSet resultSet,
            String columnName
    ) throws SQLException {
        return Values.map(resultSet.getString(columnName), SpecificationValueCode::new);
    }

    @Override
    public SpecificationValueCode getNullableResult(
            ResultSet resultSet,
            int columnIndex
    ) throws SQLException {
        return Values.map(resultSet.getString(columnIndex), SpecificationValueCode::new);
    }

    @Override
    public SpecificationValueCode getNullableResult(
            CallableStatement statement,
            int columnIndex
    ) throws SQLException {
        return Values.map(statement.getString(columnIndex), SpecificationValueCode::new);
    }
}
