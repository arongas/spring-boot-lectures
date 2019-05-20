CREATE TABLE jpa_resource
(
    tracking_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    author character varying(255) COLLATE pg_catalog."default",
    description character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    type character varying(255) COLLATE pg_catalog."default",
    url character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT jpa_resource_pkey PRIMARY KEY (tracking_id)
);

INSERT INTO jpa_resource(tracking_id, author, description, name, type, url) VALUES ('R-0001', 'PUBLIC AUTHOR', 'MY FIRST BOOK IN SPRING BOOT', 'SPRING BOOT', 'BOOK', 'http://mybook.com');