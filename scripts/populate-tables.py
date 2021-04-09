import hashlib
import os
import sqlite3
import sys
import time

object_table = 'drs_object'
access_method_table = 'drs_object_access_method'
checksum_table = 'drs_object_checksum'
bundle_table = 'drs_object_bundle'

test_data_bucket = 'ga4gh-demo-data'


def insert_dir(parent_path, c):
    c.execute("INSERT INTO {} (created_time, name, updated_time) VALUES ('{}', '{}', '{}');".format(
        object_table,
        time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(os.path.getmtime(parent_path))),
        os.path.basename(parent_path),
        time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(os.path.getmtime(parent_path)))
    ))
    parent_id = c.lastrowid

    size = 0
    checksums = []

    for path in os.listdir(parent_path):
        child_path = os.path.join(parent_path, path)
        if os.path.isdir(child_path):
            child_id, child_size, child_checksum = insert_dir(child_path, c)
        else:
            child_id, child_size, child_checksum = insert_file(child_path, c)
        size += child_size
        checksums.append(child_checksum)
        insert_bundle_link(parent_id, child_id, c)

    insert_access_url(parent_id, 'file://' + parent_path, 'file', None, c)

    key = parent_path[len(data_directory):].strip('/')
    if key.startswith('phenopackets'):
        insert_access_url(parent_id, 's3://{}/drs/{}'.format(test_data_bucket, key), 's3', 'us-east-1', c)

    c.execute('UPDATE {} SET size = {} WHERE id = {};'.format(object_table, size, parent_id))

    checksum = hashlib.md5("".join(sorted(checksums)).encode('ascii')).hexdigest()
    insert_checksum(parent_id, checksum, 'md5', c)

    return parent_id, size, checksum


def insert_file(path, c):
    size = os.path.getsize(path)
    c.execute("INSERT INTO {} (created_time, name, size, updated_time) VALUES ('{}', '{}', '{}', '{}');".format(
        object_table,
        time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(os.path.getmtime(path))),
        os.path.basename(path),
        size,
        time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(os.path.getmtime(path)))
    ))
    id = c.lastrowid

    insert_access_url(id, 'file://' + path, 'file', None, c)

    key = path[len(data_directory):].strip('/')
    if key.startswith('phenopackets'):
        insert_access_url(id, 's3://{}/drs/{}'.format(test_data_bucket, key), 's3', 'us-east-1', c)

    with open(path, "rb") as f:
        file_checksum = hashlib.md5()
        chunk = f.read(8192)
        while chunk:
            file_checksum.update(chunk)
            chunk = f.read(8192)

    checksum = file_checksum.hexdigest()
    insert_checksum(id, checksum, 'md5', c)
    return id, size, checksum


def insert_bundle_link(parent, child, c):
    c.execute('INSERT INTO {} VALUES({}, {});'.format(bundle_table, parent, child))


def insert_access_url(id, url, type, region, c):
    c.execute("INSERT INTO {} (drs_object_id, access_url, region, type ) VALUES ('{}', '{}', '{}', '{}');".format(
        access_method_table, id, url, region if region is not None else 'NULL', type
    ))


def insert_checksum(id, checksum, type, c):
    c.execute("INSERT INTO {} (drs_object_id, checksum, type) VALUES ('{}', '{}', '{}');".format(
        checksum_table, id, checksum, type
    ))


if __name__ == '__main__':
    resource_directory = os.path.abspath(sys.argv[1])
    data_directory = os.path.join(resource_directory, 'data')
    db_path = os.path.abspath(sys.argv[2])

    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()

    insert_dir(resource_directory, cursor)

    cursor.close()
    conn.commit()
    conn.close()
