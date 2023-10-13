create table if not exists drs_object (
    id text primary key,
    description text,
    created_time timestamp without time zone,
    mime_type text,
    name text,
    size bigint,
    updated_time timestamp without time zone,
    version text,
    is_bundle boolean
);

create table if not exists file_access_object(
    id serial primary key,
    drs_object_id text,
    path text,
    foreign key (drs_object_id) references drs_object(id)
);

create table if not exists aws_s3_access_object(
    id serial primary key,
    drs_object_id text,
    region text,
    bucket text,
    key text,
    foreign key (drs_object_id) references drs_object(id)
);

create table if not exists drs_object_alias (
    drs_object_id text,
    alias text,
    primary key (drs_object_id, alias),
    foreign key(drs_object_id) references drs_object(id)
);

create type drs_checksum_type as enum ('md5', 'etag', 'crc32c', 'trunc512', 'sha1', 'sha256');

create table if not exists drs_object_checksum (
    id serial primary key,
    drs_object_id text,
    checksum text,
    type drs_checksum_type,
    foreign key(drs_object_id) references drs_object(id)
);

create table drs_object_bundle (
    parent_id text,
    child_id text,
    primary key (parent_id, child_id),
    foreign key(parent_id) references drs_object(id),
    foreign key(child_id) references drs_object(id)
);

create table passport_broker (
    url text PRIMARY KEY,
    secret text
);

create table passport_visa (
    id text primary key,
    name text,
    issuer text,
    secret text,
    passport_broker_url text,
    foreign key (passport_broker_url) references passport_broker(url)
);

CREATE TABLE drs_object_visa (
    drs_object_id text,
    visa_id text,
    foreign key(drs_object_id) references drs_object(id),
    foreign key(visa_id) references passport_visa(id)
);