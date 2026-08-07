package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.ayachinene.app.domain.product.CategoryCode;
import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.sku.SkuCode;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DomainValueTypeHandlerTest {

    @Test
    void mapsProductCodeToVarchar() throws Exception {
        var productCode = new ProductCode("PRD_23456789ABCDEFGHJKMN");
        var statement = mock(PreparedStatement.class);
        var resultSet = mock(ResultSet.class);
        var handler = new ProductCodeTypeHandler();
        when(resultSet.getString("product_code")).thenReturn(productCode.value());

        handler.setNonNullParameter(statement, 1, productCode, JdbcType.VARCHAR);

        verify(statement).setString(1, productCode.value());
        assertEquals(productCode, handler.getNullableResult(resultSet, "product_code"));
    }

    @Test
    void mapsCategoryCodeToVarchar() throws Exception {
        var categoryCode = new CategoryCode("CATEGORY");
        var statement = mock(PreparedStatement.class);
        var resultSet = mock(ResultSet.class);
        var handler = new CategoryCodeTypeHandler();
        when(resultSet.getString("category_code")).thenReturn("CATEGORY");

        handler.setNonNullParameter(statement, 1, categoryCode, JdbcType.VARCHAR);

        verify(statement).setString(1, "CATEGORY");
        assertEquals(categoryCode, handler.getNullableResult(resultSet, "category_code"));
    }

    @Test
    void mapsSkuCodeToVarchar() throws Exception {
        var skuCode = new SkuCode("SKU_23456789ABCDEFGHJKMN");
        var resultSet = mock(ResultSet.class);
        when(resultSet.getString("value")).thenReturn(skuCode.value());

        assertEquals(
                skuCode,
                new SkuCodeTypeHandler().getNullableResult(resultSet, "value")
        );
    }

}
