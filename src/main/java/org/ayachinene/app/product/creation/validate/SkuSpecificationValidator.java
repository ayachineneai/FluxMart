package org.ayachinene.app.product.creation.validate;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.utils.Streams;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.ayachinene.utils.Validates.require;

public final class SkuSpecificationValidator {

    private SkuSpecificationValidator() {
    }

    /**
     * 限制 SKU 与商品规格之间的关系：
     * 无规格商品只能有一个 SKU；有规格商品的每个 SKU 必须选择全部规格，
     * 选择值必须属于对应规格，并且不同 SKU 的规格组合不能重复。
     */
    public static void validate(
        LinkedHashMap<String, List<String>> specifications,
        List<CreateProductRequest.SkuRequest> skus
    ) {
        if (specifications.isEmpty()) {
            validateWithoutSpecifications(skus);
            return;
        }

        var combinations = new HashSet<Map<String, String>>();
        Streams.withIndex(skus).forEach(sku ->
            validateSku(
                specifications,
                combinations,
                sku.value(),
                sku.index()
            )
        );
    }

    private static void validateWithoutSpecifications(
        List<CreateProductRequest.SkuRequest> skus
    ) {
        // 无规格商品只有一种可售形态，因此只能创建一个 SKU。
        require(
            skus.size() == 1,
            "a product without specifications must have one SKU"
        );
    }

    private static void validateSku(
        LinkedHashMap<String, List<String>> specifications,
        Set<Map<String, String>> combinations,
        CreateProductRequest.SkuRequest sku,
        int index
    ) {
        validateSelections(specifications, sku.selections(), index);
        // 每个规格组合只能对应一个 SKU，selections 的提交顺序不影响组合判定。
        require(
            combinations.add(sku.selections()),
            "skus[" + index + "] has a duplicated specification combination"
        );
    }

    private static void validateSelections(
        LinkedHashMap<String, List<String>> specifications,
        Map<String, String> selections,
        int skuIndex
    ) {
        // 两侧规格名称必须完全一致：既不能漏选，也不能选择未定义的规格。
        require(
            selections.keySet().equals(specifications.keySet()),
            "skus[" + skuIndex + "] must select every specification"
        );
        // 每个选择值必须属于对应规格预先定义的值集合。
        selections.forEach((specification, value) ->
            require(
                specifications.get(specification).contains(value),
                "skus[" + skuIndex + "].selections[" + specification
                    + "] does not exist"
            )
        );
    }
}
