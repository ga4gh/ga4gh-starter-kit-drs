/* ##################################################
    TEST DATASET: PHENOPACKETS
   ################################################## */

/*
    An open dataset of 384 phenopackets acts as our test dataset. We must
    first load up the dev database with these objects. First create a root
    dataset DRS object, which will act as the top-level bundle for all
    phenopacket files
*/

INSERT INTO drs_object (
    id,
    description,
    name,
    version
) VALUES (
    "b8cd0667-2c33-4c9f-967b-161b905932c9",
    "Open dataset of 384 phenopackets",
    "phenopackets.test.dataset",
    "1.0.0"
);

/*
    Next, create a DRS object bundle representing all phenopacket files for
    a given "family", or last name. Creates the family bundle. Creates DRS
    Objects for each patient/file in the family. Associate the patients with
    the family. Associate the family with the root phenopacket bundle
*/

/* ##################################################
    PHENOPACKETS TEST DATASET: CAO FAMILY
   ################################################## */

INSERT INTO drs_object (
    id,
    description,
    name,
    version
) VALUES (
    "a1dd4ae2-8d26-43b0-a199-342b64c7dff6",
    "Synthetic Phenopacket dataset: Cao family",
    "phenopackets.cao.family",
    "1.0.0"
);

INSERT INTO drs_object (
    id,
    description,
    mime_type,
    name,
    size,
    version
) VALUES
    (
        "1a570e4e-2489-4218-9333-f65549495872",
        "Phenopackets, Cao family, Patient 1",
        "application/json",
        "phenopackets.cao.1",
        4257,
        "1.0.0"
    ),
    (
        "4d83ba3f-a476-4c7c-868f-3d1fcf77fe29",
        "Phenopackets, Cao family, Patient 2",
        "application/json",
        "phenopackets.cao.2",
        7401,
        "1.0.0"
    ),
    (
        "924901d5-6d31-4c33-b443-7931eadfac4b",
        "Phenopackets, Cao family, Patient 3",
        "application/json",
        "phenopackets.cao.3",
        4251,
        "1.0.0"
    ),
    (
        "0f8abce3-e161-4bdf-981f-86257d505d69",
        "Phenopackets, Cao family, Patient 4",
        "application/json",
        "phenopackets.cao.4",
        9264,
        "1.0.0"
    );

INSERT INTO drs_object_bundle VALUES
    ("b8cd0667-2c33-4c9f-967b-161b905932c9", "a1dd4ae2-8d26-43b0-a199-342b64c7dff6"),
    ("a1dd4ae2-8d26-43b0-a199-342b64c7dff6", "1a570e4e-2489-4218-9333-f65549495872"),
    ("a1dd4ae2-8d26-43b0-a199-342b64c7dff6", "4d83ba3f-a476-4c7c-868f-3d1fcf77fe29"),
    ("a1dd4ae2-8d26-43b0-a199-342b64c7dff6", "924901d5-6d31-4c33-b443-7931eadfac4b"),
    ("a1dd4ae2-8d26-43b0-a199-342b64c7dff6", "0f8abce3-e161-4bdf-981f-86257d505d69");

/* ##################################################
    PHENOPACKETS TEST DATASET: ZHANG FAMILY
   ################################################## */

INSERT INTO drs_object (
    id,
    description,
    name,
    version
) VALUES (
    "355a74bd-6571-4d4a-8602-a9989936717f",
    "Synthetic Phenopacket dataset: Zhang family",
    "phenopackets.zhang.family",
    "1.0.0"
);

INSERT INTO drs_object (
    id,
    description,
    mime_type,
    name,
    size,
    version
) VALUES
    (
        "697907bf-d5bd-433e-aac2-1747f1faf366",
        "Phenopackets, Zhang family, 2009, proband",
        "application/json",
        "phenopackets.zhang.2009.proband",
        6471,
        "1.0.0"
    ),
    (
        "3a45fab2-f350-445d-a137-4b1f946bf184",
        "Phenopackets, Zhang family, 2011, Patient 2",
        "application/json",
        "phenopackets.zhang.2011.2",
        4001,
        "1.0.0"
    ),
    (
        "ac53ca59-46f3-4f61-b1e7-bb75a49bfbfe",
        "Phenopackets, Zhang family, 2016, Patient 1",
        "application/json",
        "phenopackets.zhang.2016.1",
        4709,
        "1.0.0"
    ),
    (
        "1275f896-4c8f-47e1-99a1-873a6b2ef5fb",
        "Phenopackets, Zhang family, 2017, Patient 1",
        "application/json",
        "phenopackets.zhang.2017.1",
        7555,
        "1.0.0"
    ),
    (
        "8f40acc0-0c54-45c5-8c85-a6f5fb32a1a7",
        "Phenopackets, Zhang family, 2017, Patient 2",
        "application/json",
        "phenopackets.zhang.2017.2",
        7114,
        "1.0.0"
    ),
    (
        "41898242-62a9-4129-9a2c-5a4e8f5f0afb",
        "Phenopackets, Zhang family, 2017, Patient 3",
        "application/json",
        "phenopackets.zhang.2017.3",
        6262,
        "1.0.0"
    ),
    (
        "6b994f18-6189-4233-bb83-139686490d68",
        "Phenopackets, Zhang family, 2017, Patient 4",
        "application/json",
        "phenopackets.zhang.2017.4",
        6289,
        "1.0.0"
    );

INSERT INTO drs_object_bundle VALUES
    ("b8cd0667-2c33-4c9f-967b-161b905932c9", "355a74bd-6571-4d4a-8602-a9989936717f"),
    ("355a74bd-6571-4d4a-8602-a9989936717f", "697907bf-d5bd-433e-aac2-1747f1faf366"),
    ("355a74bd-6571-4d4a-8602-a9989936717f", "3a45fab2-f350-445d-a137-4b1f946bf184"),
    ("355a74bd-6571-4d4a-8602-a9989936717f", "ac53ca59-46f3-4f61-b1e7-bb75a49bfbfe"),
    ("355a74bd-6571-4d4a-8602-a9989936717f", "1275f896-4c8f-47e1-99a1-873a6b2ef5fb"),
    ("355a74bd-6571-4d4a-8602-a9989936717f", "8f40acc0-0c54-45c5-8c85-a6f5fb32a1a7"),
    ("355a74bd-6571-4d4a-8602-a9989936717f", "41898242-62a9-4129-9a2c-5a4e8f5f0afb"),
    ("355a74bd-6571-4d4a-8602-a9989936717f", "6b994f18-6189-4233-bb83-139686490d68");
