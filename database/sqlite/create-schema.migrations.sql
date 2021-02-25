CREATE TABLE drs_object (
    id TEXT PRIMARY KEY,
    description TEXT,
    created_time DATETIME,
    mime_type TEXT,
    name TEXT,
    size UNSIGNED BIG INT,
    updated_time DATETIME,
    version TEXT
);

CREATE TABLE drs_object_access_method (
    drs_object_id TEXT,
    access_id TEXT,
    access_url TEXT,
    region TEXT,
    type TEXT CHECK (type IN ('s3', 'https', 'file'))
);

CREATE TABLE drs_object_aliases (
    drs_object_id TEXT,
    alias TEXT,
    PRIMARY KEY (drs_object_id, alias),
    FOREIGN KEY(drs_object_id) REFERENCES drs_object(id)
);

CREATE TABLE drs_object_checksum (
    drs_object_id TEXT,
    checksum TEXT,
    type TEXT CHECK (type IN ('md5', 'etag', 'crc32c', 'trunc512', 'sha1', 'sha-256')),
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
