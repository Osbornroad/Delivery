/*DROP TABLE IF EXISTS finishParts CASCADE;

CREATE TABLE finishParts
(
    id                     SERIAL PRIMARY KEY,
    finishPartNumber       VARCHAR UNIQUE NOT NULL,
    sortNum                INTEGER NOT NULL
);*/

/*DROP TABLE IF EXISTS kits CASCADE ;

CREATE TABLE kits
(
    id                     SERIAL PRIMARY KEY,
    kitName                VARCHAR UNIQUE NOT NULL,
    wib224                 VARCHAR UNIQUE NOT NULL,
    series                 VARCHAR NOT NULL,
    sortNum                INTEGER NOT NULL,
    current                BOOLEAN DEFAULT TRUE
);*/

-- DROP INDEX IF EXISTS wib224_idx;

-- CREATE UNIQUE INDEX wib224_idx ON kits (wib224);

/*DROP TABLE IF EXISTS kits_finish_parts CASCADE;

CREATE TABLE kits_finish_parts
(
    kits_id          INTEGER NOT NULL,
    finish_parts_id  INTEGER NOT NULL,
    PRIMARY KEY (kits_id, finish_parts_id),
    CONSTRAINT fk_variants_finish_parts_1 FOREIGN KEY (kits_id) REFERENCES kits (id),
    CONSTRAINT fk_variants_finish_parts_2 FOREIGN KEY (finish_parts_id) REFERENCES finishParts (id)
);*/

/*DROP TABLE IF EXISTS notes;

CREATE TABLE notes
(
  id                 SERIAL PRIMARY KEY,
  fieldKey           INTEGER,
  aPoint             VARCHAR,
  sequence           VARCHAR,
  modelVariant       VARCHAR,
  series             VARCHAR,
  number             INTEGER,
  planned            TIMESTAMP,
  wib225             VARCHAR,
  wib224             VARCHAR,
  aPointDateTime     TIMESTAMP
);

ALTER TABLE notes ADD CONSTRAINT notesUnique UNIQUE (aPoint, series, number);*/

/*DELETE FROM notes WHERE fieldKey > 0;*/

/*DROP TABLE IF EXISTS matching224;

CREATE TABLE matching224
(
  id                 SERIAL PRIMARY KEY,
  concat             VARCHAR,
  wib224             VARCHAR
);*/

/*CREATE TABLE properties
(
    id                  SERIAL PRIMARY KEY,
    propName            VARCHAR,
    propValue           VARCHAR
)
*/

/*INSERT INTO properties (propName, propValue)
VALUES ('weeklyEdiDir', 'C:\Shared\10. IT\02.EDI\Weekly');*/

/*INSERT INTO properties (propName, propValue)
VALUES ('dailyEdiDir', 'C:\Shared\10. IT\02.EDI\Daily');*/

/*INSERT INTO properties (propName, propValue)
VALUES ('orderDir', 'C:\Shared\10. IT\03.Order');*/



/*CREATE TABLE kits_finish_parts
(
    kits_id          kits_id          INTEGER NOT NULL,
    finish_parts_id  INTEGER NOT NULL,
    PRIMARY KEY (kits_id, finish_parts_id),
    CONSTRAINT fk_variants_finish_parts_1 FOREIGN KEY (kits_id) REFERENCES kits (id),
    CONSTRAINT fk_variants_finish_parts_2 FOREIGN KEY (finish_parts_id) REFERENCES finishParts (id)
);*/



/*DROP TABLE IF EXISTS parts CASCADE;

CREATE TABLE parts
(
    id                      SERIAL PRIMARY KEY,
    partNumber              VARCHAR UNIQUE NOT NULL,
    sortNum                 INTEGER NOT NULL,
    partType                VARCHAR NOT NULL,
    snp                     INTEGER NOT NULL
);*/

/*
CREATE TABLE part_qty
(
    part_id          INTEGER NOT NULL,
    finishPart_id    INTEGER NOT NULL,
    qty              INTEGER,
    PRIMARY KEY (part_id, finishPart_id),
    FOREIGN KEY (part_id) REFERENCES parts (id),
    FOREIGN KEY (finishPart_id) REFERENCES finishParts (id)

);*/

/*INSERT INTO part_qty VALUES
(
    1, 1, 5
);

INSERT INTO part_qty VALUES
(
    2, 1, 3
)*/

/*ALTER TABLE part_qty ALTER COLUMN qty TYPE numeric;*/

/*DROP TABLE shipping;

CREATE TABLE shipping
(
    id                   SERIAL PRIMARY KEY,
    noteId              INTEGER NOT NULL,
    dateTime            TIMESTAMP NOT NULL,
    userId               VARCHAR,
    FOREIGN KEY (noteId) REFERENCES notes (id),
    CONSTRAINT shippingUnique UNIQUE (noteId)
);*/

/*ALTER TABLE shipping ADD COLUMN fk_shipping INTEGER;*/

/*UPDATE shipping SET fk_shipping = 0;*/

/*ALTER TABLE shipping RENAME COLUMN fk_shipping TO fkShipping;*/