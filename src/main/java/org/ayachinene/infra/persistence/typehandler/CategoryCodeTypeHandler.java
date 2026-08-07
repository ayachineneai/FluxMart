package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.utils.Values;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(CategoryCode.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public final class CategoryCodeTypeHandler extends BaseTypeHandler<CategoryCode> {

    @Override
    public void setNonNullParameter(PreparedStatement statement, int index, CategoryCode parameter, JdbcType jdbcType) throws SQLException {
        statement.setString(index, parameter.value());
    }

    @Override
    public CategoryCode getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return Values.map(resultSet.getString(columnName), CategoryCode::new);
    }

    @Override
    public CategoryCode getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return Values.map(resultSet.getString(columnIndex), CategoryCode::new);
    }

    @Override
    public CategoryCode getNullableResult(CallableStatement statement, int columnIndex) throws SQLException {
        return Values.map(statement.getString(columnIndex), CategoryCode::new);
    }
}
