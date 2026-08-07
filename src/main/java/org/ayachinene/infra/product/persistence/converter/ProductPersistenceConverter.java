package org.ayachinene.infra.product.persistence.converter;

import org.ayachinene.app.product.domain.Product;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.infra.product.persistence.ProductGalleryImagePO;
import org.ayachinene.infra.product.persistence.ProductPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ProductPersistenceConverter {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductPO toProductPo(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "fileId", source = "fileId")
    @Mapping(target = "sortOrder", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ProductGalleryImagePO toGalleryImagePo(UUID7 fileId);
}
