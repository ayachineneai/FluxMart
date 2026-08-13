package org.ayachinene.api.product.data;

import org.ayachinene.shared.uuid7.UUID7;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建商品请求。
 *
 * <pre>{@code
 * {
 *   "title": "纯棉 T 恤",
 *   "subtitle": "柔软透气",
 *   "description": "100% 纯棉",
 *   "categoryCode": "TSHIRT",
 *   "primaryImageFileId": "0195d7d2-6380-7a5c-8b35-3a23b8df1f01",
 *   "galleryImageFileIds": [
 *     "0195d7d2-6380-7a5c-8b35-3a23b8df1f02",
 *     "0195d7d2-6380-7a5c-8b35-3a23b8df1f03"
 *   ],
 *   "specifications": {
 *     "颜色": ["黑色", "白色"],
 *     "尺码": ["M", "L"]
 *   },
 *   "skus": [
 *     {
 *       "merchantSkuCode": "TSHIRT-BLACK-M",
 *       "price": 99.00,
 *       "imageFileId": "0195d7d2-6380-7a5c-8b35-3a23b8df1f04",
 *       "selections": {
 *         "颜色": "黑色",
 *         "尺码": "M"
 *       }
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
    UUID7 primaryImageFileId,
    List<UUID7> galleryImageFileIds,
    LinkedHashMap<String, List<String>> specifications,
    List<SkuRequest> skus
) {

    public record SkuRequest(
        String merchantSkuCode,
        BigDecimal price,
        UUID7 imageFileId,
        Map<String, String> selections
    ) {
    }
}
