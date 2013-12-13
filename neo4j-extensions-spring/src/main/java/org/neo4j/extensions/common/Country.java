package org.neo4j.extensions.common;

/**
 * Country.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public enum Country {

    US("us");

    private String code;

    private Country(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * Determines whether the codes match.
     * 
     * @param code The code to check.
     * 
     * @return Whether to the codes are equal.
     */
    public Boolean hasSameCode(String code) {
        // check for null case
        if (this.code == null || code == null) {
            return false;
        }

        String toUpper = code.toUpperCase();
        String toLower = code.toLowerCase();
        return (this.code.equals(code) || this.code.equals(toUpper) || this.code.equals(toLower) || this.code.toUpperCase().equals(toUpper) || this.code
                .toLowerCase().equals(toLower));
    }

    /**
     * Determines the appropriate type from a code.
     * 
     * @param code The code to use.
     * 
     * @return The representing type; always null if both input null.
     */
    public static Country fromCode(String code) {
        // check for null case
        if (code == null) {
            return null;
        }

        // iterate through all types checking code
        for (Country type : Country.values()) {
            if (type.hasSameCode(code)) {
                return type;
            }
        }

        return null;
    }

}
