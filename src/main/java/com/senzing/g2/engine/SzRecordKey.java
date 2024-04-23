package com.senzing.g2.engine;

import java.util.Objects;
import java.lang.Comparable;

/**
 * Desribes a key for identifying a record as Java record class containing
 * a data source code and record ID.
 * 
 * @param dataSourceCode The non-null {@link String} data source code 
 *                       identifying the data source of the record.
 * @param recordId The non-null {@link String} record ID identifying 
 *                 the record within the data source.
 */
public record SzRecordKey(String dataSourceCode, String recordId) implements Comparable<SzRecordKey>
{
    public SzRecordKey(String dataSourceCode, String recordId) 
    {
        Objects.requireNonNull(dataSourceCode, "The data source code cannot be null");
        Objects.requireNonNull(recordId, "The record ID cannot be null");
        if (dataSourceCode.trim().length() == 0 || recordId.trim().length() == 0) {
            throw new IllegalArgumentException(
                "Empty string or all whitespace not allowed for either parameter.  "
                + "dataSourceCode=[ " + dataSourceCode + " ], recordId=[ " + recordId + " ]");
        }
        this.dataSourceCode = dataSourceCode.trim();
        this.recordId = recordId.trim();
    }

    /**
     * Implemented to sort {@link SzRecordKey} instances first on data source 
     * code and then on record ID.  This is a null-friendly comparison that sorts
     * <code>null</code> values firsdt.
     * 
     * @param recordKey The {@link SzRecordKey} to compare to.
     * 
     * @return A negative integer, zero (0) or a positive integer depending on
     *         whether this instance sorts less-than, equal-to or greater-than
     *         the specified instance, respectively.
     */
    public int compareTo(SzRecordKey recordKey) {
        if (recordKey == null) return 1; // null-friendly, null firsdt
        int diff = this.dataSourceCode().compareTo(recordKey.dataSourceCode());
        if (diff != 0) return diff;
        return this.recordId().compareTo(recordKey.recordId());
    }

    /**
     * Returns a brief {@link String} representation of this record class.
     * This formats the data source code and record ID with a colon 
     * character in between them.
     * 
     * @return A brief {@link String} representation of this class.
     */
    public String toString() {
        return this.dataSourceCode + ":" + this.recordId;
    }
}
