
CREATE TABLE offres (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        advertiser_id BIGINT NOT NULL,
                        title VARCHAR(200) NOT NULL,
                        description TEXT NOT NULL,
                        landing_url VARCHAR(2048) NOT NULL,
                        niche VARCHAR(100) NOT NULL,
                        created_at DATETIME(6) NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE tags (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      name VARCHAR(100) NOT NULL,
                      PRIMARY KEY (id),
                      CONSTRAINT uk_tag_name UNIQUE (name)
);

CREATE TABLE campaigns (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           name VARCHAR(200) NOT NULL,
                           start_date DATE NOT NULL,
                           end_date DATE NOT NULL,
                           budget DECIMAL(12,2) NOT NULL,
                           payout_model VARCHAR(30) NOT NULL,
                           payout_value DECIMAL(12,2) NOT NULL,
                           status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                           offre_id BIGINT NOT NULL,
                           PRIMARY KEY (id),
                           CONSTRAINT fk_campaign_offre
                               FOREIGN KEY (offre_id) REFERENCES offres(id)
);

CREATE TABLE offre_tags (
                            offre_id BIGINT NOT NULL,
                            tag_id BIGINT NOT NULL,
                            PRIMARY KEY (offre_id, tag_id),
                            CONSTRAINT fk_offre_tags_offre
                                FOREIGN KEY (offre_id) REFERENCES offres(id)
                                    ON DELETE CASCADE,
                            CONSTRAINT fk_offre_tags_tag
                                FOREIGN KEY (tag_id) REFERENCES tags(id)
                                    ON DELETE CASCADE
);
