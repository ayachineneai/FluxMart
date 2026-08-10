package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.ayachinene.app.order.domain.OrderCode;
import org.ayachinene.utils.Values;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(OrderCode.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public final class OrderCodeTypeHandler extends BaseTypeHandler<OrderCode> {

    @Override
    public void setNonNullParameter(
            PreparedStatement statement,
            int index,
            OrderCode parameter,
            JdbcType jdbcType
    ) throws SQLException {
        statement.setString(index, parameter.value());
    }

    @Override
    public OrderCode getNullableResult(ResultSet resultSet, String columnName)
            throws SQLException {
        return Values.map(resultSet.getString(columnName), OrderCode::new);
    }

    @Override
    public OrderCode getNullableResult(ResultSet resultSet, int columnIndex)
            throws SQLException {
        return Values.map(resultSet.getString(columnIndex), OrderCode::new);
    }

    @Override
    public OrderCode getNullableResult(CallableStatement statement, int columnIndex)
            throws SQLException {
        return Values.map(statement.getString(columnIndex), OrderCode::new);
    }
}
