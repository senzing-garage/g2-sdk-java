package com.senzing.g2.engine;

import java.util.Set;
import java.util.Collections;
import java.util.TreeSet;

/**
 * Contains constants used as placeholders to aid in the 
 * initialization of the {@link SzFlag} class.
 */
class SzFlagHelpers {
    /**
     * Private contructor.
     */
    private SzFlagHelpers() {
        // do nothing
    }

    /**
     * The package-private <b>unmodifiable</b> {@link Set} to use for {@link SzFlag}
     * instances that can be used for all usage groups.
     */
    static final Set<SzFlagUsageGroup> SZ_ALL_GROUPS_SET 
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> {@link Set} to use for {@link SzFlag}
     * instances that can only be used for "modify" operations.
     */
    static final Set<SzFlagUsageGroup> SZ_MODIFY_SET 
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to apply to {@link SzFlag} instances that retrieve entity data since
     * they are used by most operations.
     */
    static final Set<SzFlagUsageGroup> SZ_ENTITY_SET
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to apply to {@link SzFlag} instances that retrieve record data since
     * they are used by most other groups and can be specifically used for retrieving
     * a single record.
     */
    static final Set<SzFlagUsageGroup> SZ_ENTITY_RECORD_SET
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to use for {@link SzFlag} instances that can be used only for
     * "how analysis" and "why analysis" operations.
     */
    static final Set<SzFlagUsageGroup> SZ_HOW_WHY_SET 
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to use for {@link SzFlag} instances that can only be used for "search"
     * operations.
     */
    static final Set<SzFlagUsageGroup> SZ_SEARCH_SET 
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to use for {@link SzFlag} instances that can only be used for 
     * "export" operations.
     */
    static final Set<SzFlagUsageGroup> SZ_EXPORT_SET 
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to use for {@link SzFlag} instances that can only be used for 
     * "find path" operations.
     */
    static final Set<SzFlagUsageGroup> SZ_FIND_PATH_SET
        = Collections.unmodifiableSet(new TreeSet<>());
        
    /**
     * The package-private <b>unmodifiable</b> {@link Set} of {@link SzFlagUsageGroup}
     * instances to use for {@link SzFlag} instances that can only be used for 
     * "find network" operations.
     */
    static final Set<SzFlagUsageGroup> SZ_FIND_NETWORK_SET
        = Collections.unmodifiableSet(new TreeSet<>());
    
    /**
     * Formats a <code>long</code> integer value as hexadecimal with 
     * spaces between each group of for hex digits.
     * 
     * @param value The value to format.
     * 
     * @return The value formatted as a {@link String}.
     */
    static String hexFormat(long value) {
        StringBuilder   sb      = new StringBuilder(20);
        long            mask    = 0xFFFF << 48;
        String          prefix  = "";

        for (int index = 0; index < 4; index++) {
            long masked = value & mask;
            mask = mask >>> 16;
            masked = masked >>> ((3 - index) * 16);
            sb.append(prefix);
            String hex = Long.toUnsignedString(masked, 16);
            for (int zero = hex.length(); zero < 4; zero++) {
                sb.append("0");
            }
            sb.append(hex);
            prefix = " ";
        }

        return sb.toString();
    }

}
