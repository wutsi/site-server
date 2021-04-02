INSERT INTO T_SITE(id, name, domain_name, display_name) VALUES
    (1, 'foo', 'foo.com', 'Foo Site');

INSERT INTO T_ATTRIBUTE(site_fk, urn, value) VALUES
    (1, 'urn:attribute:wutsi:attr1', 'value1'),
    (1, 'urn:attribute:wutsi:attr2', 'value2'),
    (1, 'urn:attribute:wutsi:attr3', 'value3');
