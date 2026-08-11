package org.ayachinene.api.product.data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>{@code
 * {
 *   "title": "纯棉 T 恤",
 *   "subtitle": "柔软透气",
 *   "description": "100% 纯棉",
 *   "categoryCode": "TSHIRT",
 *   "primaryImageFileId": "0195d7d2-6380-7a5c-8b35-3a23b8df1f01",
 *   "galleryImageFileIds": [
 *     "0195d7d2-6380-7a5c-8b35-3a23b8df1f02"
 *   ],
 *   "specifications": [
 *     {"name": "颜色", "values": ["黑色", "白色"]},
 *     {"name": "尺码", "values": ["M", "L"]}
 *   ],
 *   "skus": [
 *     {
 *       "merchantSkuCode": "TSHIRT-BLACK-M",
 *       "price": 99.00,
 *       "imageFileId": "0195d7d2-6380-7a5c-8b35-3a23b8df1f03",
 *       "selections": [
 *         {"specification": "颜色", "value": "黑色"},
 *         {"specification": "尺码", "value": "M"}
 *       ]
 *     }
 *   ]
 * }
 * }</pre>
 */
public record CreateProductRequest(
        String title,
        String subtitle,
        String description,
        String categoryCode,
        String primaryImageFileId,
        List<String> galleryImageFileIds,
        List<SpecificationRequest> specifications,
        List<SkuRequest> skus
) {

    public record SpecificationRequest(
            String name,
            List<String> values
    ) {
    }

    public record SkuRequest(
            String merchantSkuCode,
            BigDecimal price,
            String imageFileId,
            List<SelectionRequest> selections
    ) {
    }

    public record SelectionRequest(
            String specification,
            String value
    ) {
    }
}
