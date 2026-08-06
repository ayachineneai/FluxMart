package org.ayachinene.infra.persistence.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.ayachinene.app.domain.file.FileResourceId;
import org.ayachinene.app.domain.product.CategoryCode;
import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.sku.SkuCode;
import org.ayachinene.app.domain.product.specification.SpecificationId;
import org.ayachinene.app.domain.product.specification.SpecificationValueId;
import org.ayachinene.app.uuid7.UUID7s;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DomainValueTypeHandlerTest {

    @Test
    void mapsProductCodeToBinary() throws Exception {
        var uuid7 = UUID7s.generate();
        var productCode = new ProductCode(uuid7);
        var statement = mock(PreparedStatement.class);
        var resultSet = mock(ResultSet.class);
        var handler = new ProductCodeTypeHandler();
        when(resultSet.getBytes("product_code")).thenReturn(UUID7s.toBytes(uuid7));

        handler.setNonNullParameter(statement, 1, productCode, JdbcType.BINARY);

        verify(statement).setBytes(1, UUID7s.toBytes(uuid7));
        assertEquals(productCode, handler.getNullableResult(resultSet, "product_code"));
    }

    @Test
    void mapsFileResourceIdToBinary() throws Exception {
        var uuid7 = UUID7s.generate();
        var fileId = new FileResourceId(uuid7);
        var resultSet = mock(ResultSet.class);
        when(resultSet.getBytes("file_id")).thenReturn(UUID7s.toBytes(uuid7));

        assertEquals(
                fileId,
                new FileResourceIdTypeHandler().getNullableResult(resultSet, "file_id")
        );
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
    void mapsSkuAndSpecificationIdentitiesToBinary() throws Exception {
        var uuid7 = UUID7s.generate();
        var bytes = UUID7s.toBytes(uuid7);
        var resultSet = mock(ResultSet.class);
        when(resultSet.getBytes("value")).thenReturn(bytes);

        assertEquals(
                new SkuCode(uuid7),
                new SkuCodeTypeHandler().getNullableResult(resultSet, "value")
        );
        assertEquals(
                new SpecificationId(uuid7),
                new SpecificationIdTypeHandler().getNullableResult(resultSet, "value")
        );
        assertEquals(
                new SpecificationValueId(uuid7),
                new SpecificationValueIdTypeHandler().getNullableResult(resultSet, "value")
        );
    }

}
