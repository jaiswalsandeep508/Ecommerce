CREATE TABLE categories (
                            category_id BIGINT NOT NULL AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            description VARCHAR(255),
                            parent_category_id BIGINT,
                            image_url VARCHAR(255),
                            active BIT NOT NULL,
                            created_by BIGINT NOT NULL,
                            updated_by BIGINT NOT NULL,
                            created_at TIMESTAMP(6) NOT NULL,
                            updated_at TIMESTAMP(6) NOT NULL,

                            CONSTRAINT pk_categories
                                PRIMARY KEY (category_id),

                            CONSTRAINT uk_categories_name
                                UNIQUE (name),

                            CONSTRAINT fk_categories_parent
                                FOREIGN KEY (parent_category_id)
                                    REFERENCES categories(category_id)
);


CREATE TABLE brands (
                        brand_id BIGINT NOT NULL AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        description VARCHAR(255),
                        logo_url VARCHAR(255),
                        website VARCHAR(255),
                        active BIT NOT NULL,
                        created_at TIMESTAMP(6) NOT NULL,
                        updated_at TIMESTAMP(6) NOT NULL,
                        created_by BIGINT NOT NULL,
                        updated_by BIGINT NOT NULL,

                        CONSTRAINT pk_brands
                            PRIMARY KEY (brand_id),

                        CONSTRAINT uk_brands_name
                            UNIQUE (name)
);


CREATE TABLE products (
                          product_id BIGINT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(200) NOT NULL,
                          description VARCHAR(2000),
                          sku VARCHAR(100) NOT NULL,
                          price DECIMAL(19,2) NOT NULL,
                          discount DECIMAL(19,2) NOT NULL,
                          special_price DECIMAL(19,2) NOT NULL,
                          category_id BIGINT NOT NULL,
                          brand_id BIGINT NOT NULL,
                          seller_id BIGINT NOT NULL,
                          average_rating DOUBLE NOT NULL,
                          review_count INTEGER NOT NULL,
                          status VARCHAR(30) NOT NULL,
                          created_at TIMESTAMP(6) NOT NULL,
                          updated_at TIMESTAMP(6) NOT NULL,
                          created_by BIGINT NOT NULL,
                          updated_by BIGINT NOT NULL,

                          CONSTRAINT pk_products
                              PRIMARY KEY (product_id),

                          CONSTRAINT uk_products_sku
                              UNIQUE (sku),

                          CONSTRAINT fk_products_category
                              FOREIGN KEY (category_id)
                                  REFERENCES categories(category_id),

                          CONSTRAINT fk_products_brand
                              FOREIGN KEY (brand_id)
                                  REFERENCES brands(brand_id)
);


CREATE TABLE product_images (
                                product_image_id BIGINT NOT NULL AUTO_INCREMENT,
                                product_id BIGINT NOT NULL,
                                image_url VARCHAR(255) NOT NULL,
                                display_order INTEGER,
                                primary_image BIT NOT NULL,
                                created_at TIMESTAMP(6) NOT NULL,
                                updated_at TIMESTAMP(6) NOT NULL,
                                created_by BIGINT NOT NULL,
                                updated_by BIGINT NOT NULL,

                                CONSTRAINT pk_product_images
                                    PRIMARY KEY (product_image_id),

                                CONSTRAINT fk_product_images_product
                                    FOREIGN KEY (product_id)
                                        REFERENCES products(product_id)
);


CREATE TABLE reviews (
                         review_id BIGINT NOT NULL AUTO_INCREMENT,
                         product_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         rating INTEGER NOT NULL,
                         title VARCHAR(255) NOT NULL,
                         comment VARCHAR(2000),
                         created_at TIMESTAMP(6) NOT NULL,
                         updated_at TIMESTAMP(6) NOT NULL,

                         CONSTRAINT pk_reviews
                             PRIMARY KEY (review_id),

                         CONSTRAINT fk_reviews_product
                             FOREIGN KEY (product_id)
                                 REFERENCES products(product_id),

                         CONSTRAINT uk_reviews_product_user
                             UNIQUE (product_id, user_id)
);


CREATE INDEX idx_categories_parent_category_id
    ON categories(parent_category_id);


CREATE INDEX idx_products_category_id
    ON products(category_id);


CREATE INDEX idx_products_brand_id
    ON products(brand_id);


CREATE INDEX idx_products_seller_id
    ON products(seller_id);


CREATE INDEX idx_product_images_product_id
    ON product_images(product_id);


CREATE INDEX idx_reviews_product_id
    ON reviews(product_id);


CREATE INDEX idx_reviews_user_id
    ON reviews(user_id);