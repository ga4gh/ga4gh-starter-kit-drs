package org.ga4gh.starterkit.drs.utils;

/**
 * Enables flexible serialization of different model attributes based on controller
 * function. Used with the @JsonView annotation on controller functions and
 * model properties to align serialized attributes with specific endpoints
 */
public class SerializeView {

    /**
     * The attribute will always be serialized
     */
    public static class Always {}

    /**
     * The attribute will only be serialized for controllers marked 'Public',
     * that is, pertaining to the public (non admin) API
     */
    public static class Public extends Always {}

    /**
     * The attribute will only be serialized for controllers marked 'Admin',
     * that is, pertaining to private, administrative API routes
     */
    public static class Admin extends Always {}

    /**
     * The attribute will never be serialized
     */
    public static class Never {}
}
