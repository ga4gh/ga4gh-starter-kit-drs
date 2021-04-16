CREATE TABLE drs_object (
    id TEXT PRIMARY KEY,
    description TEXT,
    created_time TEXT,
    mime_type TEXT,
    name TEXT,
    size UNSIGNED BIG INT,
    updated_time TEXT,
    version TEXT
);

CREATE TABLE file_access_object(
    drs_object_id TEXT,
    path TEXT,
    PRIMARY KEY (drs_object_id, path),
    FOREIGN KEY (drs_object_id) REFERENCES drs_object(id)
);

CREATE TABLE aws_s3_access_object(
    drs_object_id TEXT,
    region TEXT,
    bucket TEXT,
    key TEXT,
    PRIMARY KEY (drs_object_id, key),
    FOREIGN KEY (drs_object_id) REFERENCES drs_object(id)
);

CREATE TABLE drs_object_alias (
    drs_object_id TEXT,
    alias TEXT,
    PRIMARY KEY (drs_object_id, alias),
    FOREIGN KEY(drs_object_id) REFERENCES drs_object(id)
);

CREATE TABLE drs_object_checksum (
    drs_object_id TEXT,
    checksum TEXT,
    type TEXT CHECK (type IN ('md5', 'etag', 'crc32c', 'trunc512', 'sha1', 'sha256')),
    PRIMARY KEY (drs_object_id, type),
    FOREIGN KEY(drs_object_id) REFERENCES drs_object(id)
);

CREATE TABLE drs_object_bundle (
    parent_id TEXT,
    child_id TEXT,
    PRIMARY KEY (parent_id, child_id),
    FOREIGN KEY(parent_id) REFERENCES drs_object(id),
    FOREIGN KEY(child_id) REFERENCES drs_object(id)
);