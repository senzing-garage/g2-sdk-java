package com.senzing.g2.engine;

/**
 * Enumerates all the flags used by the Senzing SDK.
 */
public interface SzFlags {
    /**
     * The value representing no flags are being passed.
     * Alternatively, a <code>null</code> value will indicate
     * no flags as well.
     */
    long SZ_NO_FLAGS = 0L;

    /**
     * The bitwise flag for indicating that the Senzing engine should
     * produce and return the INFO document describing the affected
     * entities from an operation that modifies record data in the
     * repository.
     */
    long SZ_WITH_INFO = 1L << 62;

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include "resolved" relationships
     */
    long SZ_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES 
        = NativeEngine.G2_EXPORT_INCLUDE_MULTI_RECORD_ENTITIES;

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include "possibly same" relationships
     */
    long SZ_EXPORT_INCLUDE_POSSIBLY_SAME 
        = NativeEngine.G2_EXPORT_INCLUDE_POSSIBLY_SAME;

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include "possibly related" relationships
     */
    long SZ_EXPORT_INCLUDE_POSSIBLY_RELATED 
        = NativeEngine.G2_EXPORT_INCLUDE_POSSIBLY_RELATED;

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include "name only" relationships
     */
    long SZ_EXPORT_INCLUDE_NAME_ONLY 
        = NativeEngine.G2_EXPORT_INCLUDE_NAME_ONLY;

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include "disclosed" relationships
     */
    long SZ_EXPORT_INCLUDE_DISCLOSED 
        = NativeEngine.G2_EXPORT_INCLUDE_DISCLOSED;

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include singleton entities
     */
    long SZ_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES 
        = NativeEngine.G2_EXPORT_INCLUDE_SINGLE_RECORD_ENTITIES;

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include all entities
     */
    long SZ_EXPORT_INCLUDE_ALL_ENTITIES 
        = NativeEngine.G2_EXPORT_INCLUDE_ALL_ENTITIES;

    /**
     * The bitwise flag for export functionality to indicate that
     * we should include all relationships
     */
    long SZ_EXPORT_INCLUDE_ALL_HAVING_RELATIONSHIPS 
        = NativeEngine.G2_EXPORT_INCLUDE_ALL_HAVING_RELATIONSHIPS;

    /**
     * The bitwise flag for including possibly-same relations for entities
     */
    long SZ_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS
        = NativeEngine.G2_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS;

    /**
     * The bitwise flag for including possibly-related relations for entities
     */
    long SZ_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS 
        = NativeEngine.G2_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS;

    /**
     * The bitwise flag for including name-only relations for entities
     */
    long SZ_ENTITY_INCLUDE_NAME_ONLY_RELATIONS 
        = NativeEngine.G2_ENTITY_INCLUDE_NAME_ONLY_RELATIONS;

    /**
     * The bitwise flag for including disclosed relations for entities
     */
    long SZ_ENTITY_INCLUDE_DISCLOSED_RELATIONS 
        = NativeEngine.G2_ENTITY_INCLUDE_DISCLOSED_RELATIONS;

    /**
     * The bitwise flag for including all relations for entities
     */
    long SZ_ENTITY_INCLUDE_ALL_RELATIONS 
        = NativeEngine.G2_ENTITY_INCLUDE_ALL_RELATIONS;

    /**
     * The bitwise flag for including all features for entities
     */
    long SZ_ENTITY_INCLUDE_ALL_FEATURES 
        = NativeEngine.G2_ENTITY_INCLUDE_ALL_FEATURES;
    
    /**
     * The bitwise flag for including representative features for entities
     */
    long SZ_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES 
        = NativeEngine.G2_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES;
    
    /**
     * The bitwise flag for including the name of the entity
     */
    long SZ_ENTITY_INCLUDE_ENTITY_NAME 
        = NativeEngine.G2_ENTITY_INCLUDE_ENTITY_NAME;
    
    /**
     * The bitwise flag for including the record summary of the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_SUMMARY 
        = NativeEngine.G2_ENTITY_INCLUDE_RECORD_SUMMARY;
    
    /**
     * The bitwise flag for including the record types of the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_TYPES 
        = NativeEngine.G2_ENTITY_INCLUDE_RECORD_TYPES;

    /**
     * The bitwise flag for including the basic record data for the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_DATA 
        = NativeEngine.G2_ENTITY_INCLUDE_RECORD_DATA;
    
    /**
     * The bitwise flag for including the record matching info for the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO 
        = NativeEngine.G2_ENTITY_INCLUDE_RECORD_MATCHING_INFO;

    /**
     * The bitwise flag for including the record json data for the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_JSON_DATA 
        = NativeEngine.G2_ENTITY_INCLUDE_RECORD_JSON_DATA;

    /**
     * The bitwise flag for including the record unmapped data for the entity
     */
    long SZ_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA 
        = NativeEngine.G2_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA;

    /**
     * The bitwise flag for the features identifiers for the records
     */
    long SZ_ENTITY_INCLUDE_RECORD_FEATURE_IDS 
        = NativeEngine.G2_ENTITY_INCLUDE_RECORD_FEATURE_IDS;

    /**
     * The bitwise flag for including the name of the related entities
     */
    long SZ_ENTITY_INCLUDE_RELATED_ENTITY_NAME
        = NativeEngine.G2_ENTITY_INCLUDE_RELATED_ENTITY_NAME;

    /**
     * The bitwise flag for including the record matching info of the related
     * entities
     */
    long SZ_ENTITY_INCLUDE_RELATED_MATCHING_INFO 
        = NativeEngine.G2_ENTITY_INCLUDE_RELATED_MATCHING_INFO;

    /**
     * The bitwise flag for including the record summary of the related entities
     */
    long SZ_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY 
        = NativeEngine.G2_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY;

    /**
     * The bitwise flag for including the record types of the related entities
     */
    long SZ_ENTITY_INCLUDE_RELATED_RECORD_TYPES
        = NativeEngine.G2_ENTITY_INCLUDE_RELATED_RECORD_TYPES;

    /**
     * The bitwise flag for including the basic record data of the related
     * entities.
     */
    long SZ_ENTITY_INCLUDE_RELATED_RECORD_DATA 
        = NativeEngine.G2_ENTITY_INCLUDE_RELATED_RECORD_DATA;

    /**
     * The bitwise flag for including internal features in entity output
     */
    long SZ_ENTITY_OPTION_INCLUDE_INTERNAL_FEATURES
        = NativeEngine.G2_ENTITY_OPTION_INCLUDE_INTERNAL_FEATURES;

    /**
     * The bitwise flag for including feature statistics in entity output.
     */
    long SZ_ENTITY_OPTION_INCLUDE_FEATURE_STATS 
        = NativeEngine.G2_ENTITY_OPTION_INCLUDE_FEATURE_STATS;

    /**
     * The bitwise flag for including feature elements.
     */
    long SZ_ENTITY_OPTION_INCLUDE_FEATURE_ELEMENTS 
        = NativeEngine.G2_ENTITY_OPTION_INCLUDE_FEATURE_ELEMENTS;

    /**
     * The bitwise flag for including internal features.
     */
    long SZ_ENTITY_OPTION_INCLUDE_MATCH_KEY_DETAILS 
        = NativeEngine.G2_ENTITY_OPTION_INCLUDE_MATCH_KEY_DETAILS;

    /**
     * The bitwise flag for find-path functionality to indicate that
     * excluded entities are still allowed, but not preferred
     */
    long SZ_FIND_PATH_PREFER_EXCLUDE 
        = NativeEngine.G2_FIND_PATH_PREFER_EXCLUDE;

    /**
     * The bitwise flag for find-path functionality to include
     * matching info on entity paths
     */
    long SZ_FIND_PATH_MATCHING_INFO 
        = NativeEngine.G2_FIND_PATH_MATCHING_INFO;

    /**
     * The bitwise flag for find-path functionality to include
     * matching info on entity paths
     */
    long SZ_FIND_NETWORK_MATCHING_INFO 
        = NativeEngine.G2_FIND_NETWORK_MATCHING_INFO;

    /**
     * The bitwise flag for including feature scores.
     */
    long SZ_INCLUDE_FEATURE_SCORES 
        = NativeEngine.G2_INCLUDE_FEATURE_SCORES;
    
    /**
     * The bitwise flag for including statistics from search results
     */
    long SZ_SEARCH_INCLUDE_STATS 
        = NativeEngine.G2_SEARCH_INCLUDE_STATS;

    /**
     * The bitwise flag for including feature scores from search results.
     */
    long SZ_SEARCH_INCLUDE_FEATURE_SCORES 
        = NativeEngine.G2_SEARCH_INCLUDE_FEATURE_SCORES;

    /**
     * The bitwise flag for including detailed match key in search results
     */
    long SZ_SEARCH_INCLUDE_MATCH_KEY_DETAILS 
        = NativeEngine.G2_SEARCH_INCLUDE_MATCH_KEY_DETAILS;

    /**
     * The bitwise flag for search functionality to indicate that
     * we should include "resolved" match level results
     *
     */
    long SZ_SEARCH_INCLUDE_RESOLVED 
        = NativeEngine.G2_SEARCH_INCLUDE_RESOLVED;

    /**
     * The bitwise flag for search functionality to indicate that
     * we should include "possibly same" match level results
     */
    long SZ_SEARCH_INCLUDE_POSSIBLY_SAME 
        = NativeEngine.G2_SEARCH_INCLUDE_POSSIBLY_SAME;

    /**
     * The bitwise flag for search functionality to indicate that
     * we should include "possibly related" match level results
     *
     */
    long SZ_SEARCH_INCLUDE_POSSIBLY_RELATED 
        = NativeEngine.G2_SEARCH_INCLUDE_POSSIBLY_RELATED;

    /**
     * The bitwise flag for search functionality to indicate that
     * we should include "name only" match level results
     *
     */
    long SZ_SEARCH_INCLUDE_NAME_ONLY 
        = NativeEngine.G2_SEARCH_INCLUDE_NAME_ONLY;

    /**
     * The bitwise flag to use when a repository-modifying operation 
     * is being invoked and the desired repsonse should contain an
     * "INFO" message describing how the repository was affected as
     * a result of the operation. 
     */
    long SZ_WITH_INFO_FLAGS = SZ_WITH_INFO;

    /**
     * The bitwise flag for search functionality to indicate that
     * we should include all match level results
     *
     */
    long SZ_SEARCH_INCLUDE_ALL_ENTITIES
        = NativeEngine.G2_SEARCH_INCLUDE_ALL_ENTITIES;

    /**
     * The default recommended bitwise flag values for getting records
     */
    long SZ_RECORD_DEFAULT_FLAGS 
        = NativeEngine.G2_RECORD_DEFAULT_FLAGS;

    /**
     * The default recommended bitwise flag values for getting entities
     */
    long SZ_ENTITY_DEFAULT_FLAGS 
        = NativeEngine.G2_ENTITY_DEFAULT_FLAGS;
    
    /**
     * The default recommended bitwise flag values for getting entities
     */
    long SZ_ENTITY_BRIEF_DEFAULT_FLAGS 
        = NativeEngine.G2_ENTITY_BRIEF_DEFAULT_FLAGS;

    /**
     * The default recommended bitwise flag values for exporting entities
     */
    long SZ_EXPORT_DEFAULT_FLAGS 
        = NativeEngine.G2_EXPORT_DEFAULT_FLAGS;

    /**
     * The default recommended bitwise flag values for finding entity paths
     */
    long SZ_FIND_PATH_DEFAULT_FLAGS 
        = NativeEngine.G2_FIND_PATH_DEFAULT_FLAGS;

    /**
     * The default recommended bitwise flag values for finding entity networks
     */
    long SZ_FIND_NETWORK_DEFAULT_FLAGS 
        = NativeEngine.G2_FIND_NETWORK_DEFAULT_FLAGS;

    /**
     * The default recommended bitwise flag values for why-entities analysis on
     * entities
     */
    long SZ_WHY_ENTITIES_DEFAULT_FLAGS 
        = NativeEngine.G2_WHY_ENTITIES_DEFAULT_FLAGS;

    /**
     * The default recommended bitwise flag values for why-records analysis on
     * entities
     */
    long SZ_WHY_RECORDS_DEFAULT_FLAGS 
        = NativeEngine.G2_WHY_RECORDS_DEFAULT_FLAGS;

    /**
     * The default recommended bitwise flag values for why-record-in analysis on
     * entities
     */
    long SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS 
        = NativeEngine.G2_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS;

    /**
     * The default recommended bitwise flag values for how-analysis on entities
     */
    long SZ_HOW_ENTITY_DEFAULT_FLAGS 
        = NativeEngine.G2_HOW_ENTITY_DEFAULT_FLAGS;

    /**
     * The default recommended bitwise flag values for virtual-entity-analysis on
     * entities
     */
    long SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS 
        = NativeEngine.G2_VIRTUAL_ENTITY_DEFAULT_FLAGS;

    /**
     * The default recommended bitwise flag values for searching by attributes,
     * returning all matching entities
     */
    long SZ_SEARCH_BY_ATTRIBUTES_ALL 
        = NativeEngine.G2_SEARCH_BY_ATTRIBUTES_ALL;

    /**
     * The default recommended bitwise flag values for searching by attributes,
     * returning only strongly matching entities
     */
    long SZ_SEARCH_BY_ATTRIBUTES_STRONG 
        = NativeEngine.G2_SEARCH_BY_ATTRIBUTES_STRONG;

    /**
     * The default recommended bitwise flag values for searching by attributes,
     * returning minimal data with all matches
     */
    long SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL 
        = NativeEngine.G2_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL;

    /**
     * The default recommended bitwise flag values for searching by attributes,
     * returning the minimal data, and returning only the strongest matches
     */
    long SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG 
        = NativeEngine.G2_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG;
        
    /**
     * The default recommended bitwise flag values for searching by attributes
     */
    long SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS 
        = NativeEngine.G2_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS;
}
