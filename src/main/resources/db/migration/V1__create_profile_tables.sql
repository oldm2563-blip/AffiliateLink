
CREATE TABLE profile (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         email VARCHAR(255) NOT NULL,
                         status VARCHAR(255) NOT NULL,
                         created_at DATETIME(6) NOT NULL,
                         PRIMARY KEY (id),
                         CONSTRAINT uk_profile_email UNIQUE (email)
);

CREATE TABLE profile_advertisers (
                                     id BIGINT NOT NULL,
                                     company_name VARCHAR(255) NOT NULL,
                                     sector VARCHAR(255) NOT NULL,
                                     website VARCHAR(255) NOT NULL,
                                     PRIMARY KEY (id),
                                     CONSTRAINT uk_advertiser_company_name UNIQUE (company_name),
                                     CONSTRAINT fk_advertiser_profile
                                         FOREIGN KEY (id) REFERENCES profile(id)
);

CREATE TABLE profile_publishers (
                                    id BIGINT NOT NULL,
                                    first_name VARCHAR(255) NOT NULL,
                                    last_name VARCHAR(255) NOT NULL,
                                    niche VARCHAR(255) NOT NULL,
                                    bio VARCHAR(255) NOT NULL,
                                    country VARCHAR(255) NOT NULL,
                                    PRIMARY KEY (id),
                                    CONSTRAINT fk_publisher_profile
                                        FOREIGN KEY (id) REFERENCES profile(id)
);
