package org.ayachinene.infra.product.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.ayachinene.app.product.creation.ProductCreationPos;
import org.ayachinene.app.product.repository.ProductRepository;
import org.ayachinene.utils.Values;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductMapper productMapper;
    private final ProductGalleryImageMapper productGalleryImageMapper;
    private final ProductSpecificationMapper productSpecificationMapper;
    private final ProductSpecificationValueMapper productSpecificationValueMapper;
    private final SkuMapper skuMapper;
    private final SkuSpecificationSelectionMapper skuSpecificationSelectionMapper;
    private final StockMapper stockMapper;

    public ProductRepositoryImpl(
        ProductMapper productMapper,
        ProductGalleryImageMapper productGalleryImageMapper,
        ProductSpecificationMapper productSpecificationMapper,
        ProductSpecificationValueMapper productSpecificationValueMapper,
        SkuMapper skuMapper,
        SkuSpecificationSelectionMapper skuSpecificationSelectionMapper,
        StockMapper stockMapper
    ) {
        this.productMapper = productMapper;
        this.productGalleryImageMapper = productGalleryImageMapper;
        this.productSpecificationMapper = productSpecificationMapper;
        this.productSpecificationValueMapper = productSpecificationValueMapper;
        this.skuMapper = skuMapper;
        this.skuSpecificationSelectionMapper = skuSpecificationSelectionMapper;
        this.stockMapper = stockMapper;
    }

    @Override
    public void insert(ProductCreationPos pos) {
        productMapper.insert(pos.product());
        insertBatch(productGalleryImageMapper, pos.productGalleryImages());
        insertBatch(productSpecificationMapper, pos.productSpecifications());
        insertBatch(productSpecificationValueMapper, pos.productSpecificationValues());
        insertBatch(skuMapper, pos.skus());
        insertBatch(skuSpecificationSelectionMapper, pos.skuSpecificationSelections());
        insertBatch(stockMapper, pos.stocks());
    }

    private <T> void insertBatch(BaseMapper<T> mapper, List<T> values) {
        Values.guard(!values.isEmpty(), () -> mapper.insert(values));
    }
}
