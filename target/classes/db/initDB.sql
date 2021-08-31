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

INSERT INTO properties (propName, propValue)
VALUES ('dailyEdiDir', 'C:\Shared\10. IT\02.EDI\Daily');