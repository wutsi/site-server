CREATE TABLE T_SITE(
    id                      SERIAL NOT NULL PRIMARY KEY,
    name                    VARCHAR(100) NOT NULL,
    domain_name             VARCHAR(255) NOT NULL,
    display_name            VARCHAR(100) NOT NULL,

    UNIQUE(name),
    UNIQUE(domain_name)
);

CREATE TABLE T_ATTRIBUTE(
    id              SERIAL NOT NULL PRIMARY KEY,
    urn             VARCHAR(255) NOT NULL,
    value           TEXT NOT NULL,

    site_fk         BIGINT NOT NULL,

    UNIQUE(site_fk, urn),
    CONSTRAINT fk_site FOREIGN KEY(site_fk) REFERENCES T_SITE(id)
);
