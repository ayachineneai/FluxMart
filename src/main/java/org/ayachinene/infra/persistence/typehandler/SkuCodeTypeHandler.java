package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.utils.Values;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(SkuCode.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public final class SkuCodeTypeHandler extends BaseTypeHandler<SkuCode> {

    @Override
    public void setNonNullParameter(
            PreparedStatement statement,
            int index,
            SkuCode parameter,
            JdbcType jdbcType
    ) throws SQLException {
        statement.setString(index, parameter.value());
    }

    @Override
    public SkuCode getNullableResult(ResultSet resultSet, String columnName)
            throws SQLException {
        return Values.map(
                resultSet.getString(columnName),
                SkuCode::new
        );
    }

    @Override
    public SkuCode getNullableResult(ResultSet resultSet, int columnIndex)
            throws SQLException {
        return Values.map(
                resultSet.getString(columnIndex),
                SkuCode::new
        );
    }

    @Override
    public SkuCode getNullableResult(CallableStatement statement, int columnIndex)
            throws SQLException {
        return Values.map(
                statement.getString(columnIndex),
                SkuCode::new
        );
    }
}
