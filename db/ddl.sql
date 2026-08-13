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
    purpose       VARCHAR(30)   COMMENT '文件用途：商品图片-PRODUCT_IMAGE',
    status        VARCHAR(20)   COMMENT '上传中-UPLOADING、可用-AVAILABLE、已删除-DELETED',
    upload_expires_at DATETIME(3) COMMENT '上传凭证过期时间',

    created_at    DATETIME(3)   COMMENT '创建时间',
    updated_at    DATETIME(3)   COMMENT '最后修改时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_object_key (object_key),
    KEY idx_status_upload_expires_at (status, upload_expires_at)
) COMMENT = '文件资源';

DROP TABLE IF EXISTS user_account;
CREATE TABLE IF NOT EXISTS user_account
(
    id         BINARY(16)    COMMENT 'UUIDv7 数据库内部主键',

    username   VARCHAR(50)
               CHARACTER SET utf8mb4
               COLLATE utf8mb4_bin
               COMMENT '用户账户名，区分大小写',
    email      VARCHAR(320)
               CHARACTER SET ascii
               COLLATE ascii_bin
               COMMENT '应用层规范化为小写后的登录邮箱',
    status     VARCHAR(20)   COMMENT '正常-ACTIVE、已禁用-DISABLED',

    created_at DATETIME(3)   COMMENT '创建时间',
    updated_at DATETIME(3)   COMMENT '最后修改时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
) COMMENT = '用户账户';

DROP TABLE IF EXISTS product;
CREATE TABLE IF NOT EXISTS product
(
    id                BINARY(16)      COMMENT '主键',

    product_code      VARCHAR(24)
                      CHARACTER SET ascii
                      COLLATE ascii_bin
                      COMMENT '系统生成的商品业务编号，区分大小写',

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

    specification_code     VARCHAR(24)
                           CHARACTER SET ascii
                           COLLATE ascii_bin
                           COMMENT '系统生成的规格业务编号，区分大小写',

    name                   VARCHAR(50)   COMMENT '规格名称，例如颜色、尺码',
    status                 VARCHAR(20)   COMMENT '启用-ENABLED、停用-DISABLED',
    sort_order             INT UNSIGNED  COMMENT '从 0 开始的展示顺序',

    created_at             DATETIME(3)   COMMENT '创建时间',
    updated_at             DATETIME(3)   COMMENT '最后修改时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_specification_code (specification_code),
    KEY idx_product_id (product_id)
) COMMENT = '商品销售规格';

DROP TABLE IF EXISTS product_specification_value;
CREATE TABLE IF NOT EXISTS product_specification_value
(
    id                       BINARY(16)    COMMENT 'UUIDv7 数据库内部主键',
    specification_id         BINARY(16)    COMMENT '所属销售规格内部主键',

    specification_value_code VARCHAR(24)
                             CHARACTER SET ascii
                             COLLATE ascii_bin
                             COMMENT '系统生成的规格值业务编号，区分大小写',

    display_name             VARCHAR(50)   COMMENT '展示名称，例如黑色、白色、M、L',
    status                   VARCHAR(20)   COMMENT '启用-ENABLED、停用-DISABLED',
    sort_order               INT UNSIGNED  COMMENT '从 0 开始的展示顺序',

    created_at               DATETIME(3)   COMMENT '创建时间',
    updated_at               DATETIME(3)   COMMENT '最后修改时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_specification_value_code (specification_value_code),
    KEY idx_specification_id (specification_id)
) COMMENT = '商品销售规格值';

DROP TABLE IF EXISTS sku;
CREATE TABLE IF NOT EXISTS sku
(
    id                BINARY(16)      COMMENT 'UUIDv7 数据库内部主键',
    product_id        BINARY(16)      COMMENT '所属商品内部主键',

    sku_code          VARCHAR(24)
                      CHARACTER SET ascii
                      COLLATE ascii_bin
                      COMMENT '系统生成的 SKU 业务编号，区分大小写',
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

DROP TABLE IF EXISTS stock;
CREATE TABLE IF NOT EXISTS stock
(
    id                 BINARY(16)      COMMENT 'UUIDv7 数据库内部主键',
    sku_id             BINARY(16)      COMMENT 'SKU 内部主键',

    available_quantity BIGINT UNSIGNED COMMENT '当前可用于下单的库存数量',
    reserved_quantity  BIGINT UNSIGNED COMMENT '已被订单预占的库存数量',

    version            BIGINT UNSIGNED COMMENT '乐观锁版本',
    created_at         DATETIME(3)     COMMENT '创建时间',
    updated_at         DATETIME(3)     COMMENT '最后修改时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_sku_id (sku_id)
) COMMENT = 'SKU 库存';

DROP TABLE IF EXISTS customer_order;
CREATE TABLE IF NOT EXISTS customer_order
(
    id                 BINARY(16)      COMMENT 'UUIDv7 数据库内部主键',

    order_code         VARCHAR(24)
                       CHARACTER SET ascii
                       COLLATE ascii_bin
                       COMMENT '系统生成的订单业务编号，区分大小写',
    user_id            BINARY(16)      COMMENT '下单用户内部主键',
    request_key        VARCHAR(64)
                       CHARACTER SET ascii
                       COLLATE ascii_bin
                       COMMENT '创建订单请求幂等键',

    status             VARCHAR(30)     COMMENT '待支付-PENDING_PAYMENT、已支付-PAID、已取消-CANCELLED、已关闭-CLOSED',
    total_amount       BIGINT UNSIGNED COMMENT '订单总金额，单位为人民币分',
    payment_expires_at DATETIME(3)     COMMENT '支付截止时间，超时后关闭订单并释放库存',

    version            BIGINT UNSIGNED COMMENT '乐观锁版本',
    created_at         DATETIME(3)     COMMENT '创建时间',
    updated_at         DATETIME(3)     COMMENT '最后修改时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_order_code (order_code),
    UNIQUE KEY uk_user_request_key (user_id, request_key),
    KEY idx_user_id_created_at (user_id, created_at),
    KEY idx_status_payment_expires_at (status, payment_expires_at)
) COMMENT = '用户订单';

DROP TABLE IF EXISTS payment;
CREATE TABLE IF NOT EXISTS payment
(
    id                BINARY(16)      COMMENT 'UUIDv7 数据库内部主键',

    payment_code      VARCHAR(24)
                      CHARACTER SET ascii
                      COLLATE ascii_bin
                      COMMENT '系统生成的支付单业务编号，区分大小写',
    order_id          BINARY(16)      COMMENT '所属订单内部主键',
    channel           VARCHAR(20)     COMMENT '支付渠道，当前仅支持支付宝-ALIPAY',

    status            VARCHAR(20)     COMMENT '待支付-PENDING、支付成功-SUCCEEDED、已关闭-CLOSED',
    amount            BIGINT UNSIGNED COMMENT '支付金额，单位为人民币分',
    provider_trade_no VARCHAR(64)
                      CHARACTER SET ascii
                      COLLATE ascii_bin
                      COMMENT '支付渠道生成的交易编号',

    paid_at           DATETIME(3)     COMMENT '支付成功时间',
    closed_at         DATETIME(3)     COMMENT '支付关闭时间',

    version           BIGINT UNSIGNED COMMENT '乐观锁版本',
    created_at        DATETIME(3)     COMMENT '创建时间',
    updated_at        DATETIME(3)     COMMENT '最后修改时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_payment_code (payment_code),
    UNIQUE KEY uk_order_id (order_id),
    UNIQUE KEY uk_channel_provider_trade_no (channel, provider_trade_no),
    KEY idx_status_updated_at (status, updated_at)
) COMMENT = '支付单';

DROP TABLE IF EXISTS order_item;
CREATE TABLE IF NOT EXISTS order_item
(
    id                     BINARY(16)      COMMENT 'UUIDv7 数据库内部主键',
    order_id               BINARY(16)      COMMENT '所属订单内部主键',

    product_code           VARCHAR(24)
                           CHARACTER SET ascii
                           COLLATE ascii_bin
                           COMMENT '下单时的商品业务编号',
    sku_code               VARCHAR(24)
                           CHARACTER SET ascii
                           COLLATE ascii_bin
                           COMMENT '下单时的 SKU 业务编号',

    product_title          VARCHAR(50)     COMMENT '下单时的商品标题快照',
    specification_snapshot JSON            COMMENT '下单时有序的规格身份与展示文本快照',
    image_file_id          BINARY(16)      COMMENT '下单时采用的商品或 SKU 图片文件 ID',

    unit_price_amount      BIGINT UNSIGNED COMMENT '下单时的 SKU 单价，单位为人民币分',
    quantity               INT UNSIGNED    COMMENT '购买数量',
    total_amount           BIGINT UNSIGNED COMMENT '订单项总金额，等于单价乘以数量',
    sort_order             INT UNSIGNED    COMMENT '订单内从 0 开始的展示顺序',

    created_at             DATETIME(3)     COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_order_id (order_id)
) COMMENT = '订单商品项';

DROP TABLE IF EXISTS stock_reservation;
CREATE TABLE IF NOT EXISTS stock_reservation
(
    id            BINARY(16)      COMMENT 'UUIDv7 数据库内部主键',
    order_id      BINARY(16)      COMMENT '所属订单内部主键',
    order_item_id BINARY(16)      COMMENT '对应订单商品项内部主键',
    sku_id        BINARY(16)      COMMENT '被预占库存的 SKU 内部主键',

    quantity      BIGINT UNSIGNED COMMENT '预占数量',
    status        VARCHAR(20)     COMMENT '已预占-RESERVED、已确认-CONFIRMED、已释放-RELEASED',
    expires_at    DATETIME(3)     COMMENT '预占过期时间，通常与订单支付截止时间一致',

    created_at    DATETIME(3)     COMMENT '创建时间',
    updated_at    DATETIME(3)     COMMENT '最后修改时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_order_item_id (order_item_id),
    KEY idx_order_id_status (order_id, status),
    KEY idx_status_expires_at (status, expires_at)
) COMMENT = '库存预占记录';
