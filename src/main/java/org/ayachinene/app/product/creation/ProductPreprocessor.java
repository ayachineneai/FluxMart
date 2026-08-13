package org.ayachinene.app.product.creation;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.shared.exception.ValidationException;
import org.ayachinene.utils.Streams;
import org.ayachinene.utils.Strings;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.ayachinene.utils.Strings.strip;

public final class ProductPreprocessor {

    private ProductPreprocessor() {
    }

    public static CreateProductRequest preprocess(CreateProductRequest request) {
        if (request == null) return null;
        var specifications = specifications(request.specifications());
        return new CreateProductRequest(
            strip(request.title()),
            strip(request.subtitle()),
            strip(request.description()),
            strip(request.categoryCode()),
            request.primaryImageFileId(),
            Streams.of(request.galleryImageFileIds())
                .filter(Objects::nonNull)
                .distinct()
                .toList(),
            specifications,
            skus(request.skus(), specifications.isEmpty())
        );
    }

    /**
     * 生成有序的规格定义：忽略 null 项、清理文本并去除重复规格值。
     */
    private static LinkedHashMap<String, List<String>> specifications(
        LinkedHashMap<String, List<String>> specifications
    ) {
        var preprocessed = new LinkedHashMap<String, List<String>>();
        if (specifications == null) return preprocessed;

        for (var entry : specifications.entrySet()) {
            if (entry.getKey() == null) continue;

            var name = strip(entry.getKey());
            var values = Streams.of(entry.getValue())
                .filter(Objects::nonNull)
                .map(Strings::strip)
                .distinct()
                .toList();

            // 清理后的规格名称不能冲突，否则后一个规格会覆盖前一个规格。
            if (preprocessed.containsKey(name)) {
                throw new ValidationException(
                    "specification names must be unique"
                );
            }
            preprocessed.put(name, values);
        }
        return preprocessed;
    }

    private static List<CreateProductRequest.SkuRequest> skus(
        List<CreateProductRequest.SkuRequest> skus,
        boolean withoutSpecifications
    ) {
        return Streams.of(skus)
            .filter(Objects::nonNull)
            .map(sku -> sku(sku, withoutSpecifications))
            .toList();
    }

    private static CreateProductRequest.SkuRequest sku(
        CreateProductRequest.SkuRequest sku,
        boolean withoutSpecifications
    ) {
        return new CreateProductRequest.SkuRequest(
            strip(sku.merchantSkuCode()),
            sku.price(),
            sku.imageFileId(),
            withoutSpecifications
                ? new LinkedHashMap<>()
                : selections(sku.selections())
        );
    }

    /**
     * 生成规格名称到所选规格值的映射：忽略不完整的选择并清理文本。
     */
    private static Map<String, String> selections(Map<String, String> selections) {
        var preprocessed = new LinkedHashMap<String, String>();
        if (selections == null) return preprocessed;

        for (var entry : selections.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) continue;

            var specification = strip(entry.getKey());
            var value = strip(entry.getValue());

            // 清理后的规格名称不能冲突，否则一个 SKU 会为同一规格选择多个值。
            if (preprocessed.containsKey(specification)) {
                throw new ValidationException(
                    "selection specification is duplicated"
                );
            }

            preprocessed.put(specification, value);
        }
        return preprocessed;
    }

}
