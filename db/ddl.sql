CREATE DATABASE IF NOT EXISTS fluxmart
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE fluxmart;

DROP TABLE IF EXISTS file_resource;
CREATE TABLE IF NOT EXISTS file_resource
(
    id            BINARY(16)    COMMENT '主键',

    object_key    VARCHAR(1024)
                  CHARACTER SET ascii
                  COLLATE ascii_bin
                  COMMENT '系统生成的 OSS Object Key，区分大小写',
    original_name VARCHAR(255)  COMMENT '上传时的原始文件名',
    content_type  VARCHAR(128)  COMMENT '文件 MIME 类型',
    size          BIGINT UNSIGNED COMMENT '文件大小，单位字节',
    status        VARCHAR(20)   COMMENT '上传中-UPLOADING、可用-AVAILABLE、已删除-DELETED',

    created_at    DATETIME(3)   COMMENT '创建时间',
    updated_at    DATETIME(3)   COMMENT '最后修改时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_object_key (object_key)
) COMMENT = '文件资源';

DROP TABLE IF EXISTS product;
CREATE TABLE IF NOT EXISTS product
(
    id                BINARY(16)      COMMENT '主键',

    product_code      BINARY(16)      COMMENT '商品业务编号',

    status            VARCHAR(20)     COMMENT '草稿-DRAFT、在售-ON_SALE、已下架-OFF_SALE、已归档-ARCHIVED',
    title             VARCHAR(50)     COMMENT '商品标题',
    subtitle          VARCHAR(50)     COMMENT '商品副标题',
    description       TEXT            COMMENT '商品详细描述',
    category_code     VARCHAR(64)     COMMENT '商品类目业务编号',
    primary_image_file_id BINARY(16)  COMMENT '商品主图文件 ID',

    version           BIGINT UNSIGNED COMMENT '乐观锁版本',

    created_at        DATETIME(3)     COMMENT '创建时间',
    updated_at        DATETIME(3)     COMMENT '最后修改时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_product_code (product_code)
) COMMENT = '商品';

DROP TABLE IF EXISTS product_gallery_image;
CREATE TABLE IF NOT EXISTS product_gallery_image
(
    id         BINARY(16)      COMMENT 'UUIDv7 数据库内部主键',
    product_id BINARY(16)      COMMENT '商品内部主键',
    file_id    BINARY(16)      COMMENT '轮播图片文件 ID',
    sort_order INT UNSIGNED    COMMENT '从 0 开始的展示顺序',
    created_at DATETIME(3)     DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_product_id (product_id)
) COMMENT = '商品轮播图';


DROP TABLE IF EXISTS product_specification;
CREATE TABLE IF NOT EXISTS product_specification
(
    id                     BINARY(16)    COMMENT 'UUIDv7 数据库内部主键',
    product_id             BINARY(16)    COMMENT '所属商品主键',

    name                   VARCHAR(50)   COMMENT '规格名称，例如颜色、尺码',
    status                 VARCHAR(20)   COMMENT '启用-ENABLED、停用-DISABLED',
    sort_order             INT UNSIGNED  COMMENT '从 0 开始的展示顺序',

    created_at             DATETIME(3)   COMMENT '创建时间',
    updated_at             DATETIME(3)   COMMENT '最后修改时间',

    PRIMARY KEY (id),
    KEY idx_product_id (product_id)
) COMMENT = '商品销售规格';

DROP TABLE IF EXISTS product_specification_value;
CREATE TABLE IF NOT EXISTS product_specification_value
(
    id                       BINARY(16)    COMMENT 'UUIDv7 数据库内部主键',
    specification_id         BINARY(16)    COMMENT '所属销售规格内部主键',

    display_name             VARCHAR(50)   COMMENT '展示名称，例如黑色、白色、M、L',
    status                   VARCHAR(20)   COMMENT '启用-ENABLED、停用-DISABLED',
    sort_order               INT UNSIGNED  COMMENT '从 0 开始的展示顺序',

    created_at               DATETIME(3)   COMMENT '创建时间',
    updated_at               DATETIME(3)   COMMENT '最后修改时间',

    PRIMARY KEY (id),
    KEY idx_specification_id (specification_id)
) COMMENT = '商品销售规格值';

DROP TABLE IF EXISTS sku;
CREATE TABLE IF NOT EXISTS sku
(
    id                BINARY(16)      COMMENT 'UUIDv7 数据库内部主键',
    product_id        BINARY(16)      COMMENT '所属商品内部主键',

    sku_code          BINARY(16)      COMMENT '系统生成的 SKU 业务编号',
    merchant_sku_code VARCHAR(64)
                      CHARACTER SET utf8mb4
                      COLLATE utf8mb4_bin
                      COMMENT '商家自定义 SKU 货号，区分大小写',

    status            VARCHAR(20)     COMMENT '启用-ENABLED、停用-DISABLED',
    price_amount      BIGINT UNSIGNED COMMENT '价格，使用最小货币单位，例如人民币分',
    image_file_id     BINARY(16)      COMMENT 'SKU 专属图片文件 ID',

    version           BIGINT UNSIGNED COMMENT '乐观锁版本',
    created_at        DATETIME(3)     COMMENT '创建时间',
    updated_at        DATETIME(3)     COMMENT '最后修改时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_sku_code (sku_code),
    UNIQUE KEY uk_merchant_sku_code (merchant_sku_code),
    KEY idx_product_id (product_id)
) COMMENT = '商品 SKU';

DROP TABLE IF EXISTS sku_specification_selection;
CREATE TABLE IF NOT EXISTS sku_specification_selection
(
    id                     BINARY(16)   COMMENT 'UUIDv7 数据库内部主键',
    sku_id                 BINARY(16)   COMMENT 'SKU 内部主键',
    specification_id       BINARY(16)   COMMENT '商品销售规格内部主键',
    specification_value_id BINARY(16)   COMMENT '商品销售规格值内部主键',

    created_at             DATETIME(3)  COMMENT '创建时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_sku_specification (sku_id, specification_id),
    KEY idx_specification_id (specification_id),
    KEY idx_specification_value_id (specification_value_id)
) COMMENT = 'SKU 销售规格选择';
