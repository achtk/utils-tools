package com.chua.utils.netx.elasticsearch.version;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author CH
 */
@NoArgsConstructor
@Data
@Getter
@Setter
public class ElasticSearchVersion {


    /**
     * name : 3KAwdVy
     * cluster_name : elasticsearch
     * cluster_uuid : 2nmKZN-wQ2KZ1BVatX_n7w
     * version : {"number":"6.2.4","build_hash":"ccec39f","build_date":"2018-04-12T20:37:28.497551Z","build_snapshot":false,"lucene_version":"7.2.1","minimum_wire_compatibility_version":"5.6.0","minimum_index_compatibility_version":"5.0.0"}
     * tagline : You Know, for Search
     */

    private String name;
    private String cluster_name;
    private String cluster_uuid;
    private VersionBean version;
    private String tagline;

    @NoArgsConstructor
    @Data
    public static class VersionBean {
        /**
         * number : 6.2.4
         * build_hash : ccec39f
         * build_date : 2018-04-12T20:37:28.497551Z
         * build_snapshot : false
         * lucene_version : 7.2.1
         * minimum_wire_compatibility_version : 5.6.0
         * minimum_index_compatibility_version : 5.0.0
         */

        private String number;
        private String build_hash;
        private String build_date;
        private boolean build_snapshot;
        private String lucene_version;
        private String minimum_wire_compatibility_version;
        private String minimum_index_compatibility_version;
    }
}
