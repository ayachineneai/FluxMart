package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.app.order.domain.OrderCode;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
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

    @Test
    void mapsOrderCodeToVarchar() throws Exception {
        var orderCode = new OrderCode("ORD_23456789ABCDEFGHJKMN");
        var resultSet = mock(ResultSet.class);
        when(resultSet.getString("value")).thenReturn(orderCode.value());

        assertEquals(
                orderCode,
                new OrderCodeTypeHandler().getNullableResult(resultSet, "value")
        );
    }

    @Test
    void mapsSpecificationCodesToVarchar() throws Exception {
        var specificationCode = SpecificationCode.generate();
        var valueCode = SpecificationValueCode.generate();
        var resultSet = mock(ResultSet.class);
        when(resultSet.getString("specification_code"))
                .thenReturn(specificationCode.value());
        when(resultSet.getString("specification_value_code"))
                .thenReturn(valueCode.value());

        assertEquals(
                specificationCode,
                new SpecificationCodeTypeHandler().getNullableResult(
                        resultSet,
                        "specification_code"
                )
        );
        assertEquals(
                valueCode,
                new SpecificationValueCodeTypeHandler().getNullableResult(
                        resultSet,
                        "specification_value_code"
                )
        );
    }

}
