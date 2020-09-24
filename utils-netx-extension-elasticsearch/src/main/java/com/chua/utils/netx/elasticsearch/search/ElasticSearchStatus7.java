package com.chua.utils.netx.elasticsearch.search;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * es7状态
 * @author CHTK
 */
@NoArgsConstructor
@Data
public class ElasticSearchStatus7 {


    /**
     * took : 1
     * timed_out : false
     * _shards : {"total":1,"successful":1,"skipped":0,"failed":0}
     * hits : {"total":{"value":1,"relation":"eq"},"max_score":1,"hits":[{"_index":"test","_type":"doc","_id":"20","_score":1,"_source":{"date":1565574980,"test":"uuid-20","id":20,"test1":"uuid-20"}}]}
     */

    private int took;
    private boolean timed_out;
    private ShardsBean _shards;
    private HitsBeanX hits;

    @NoArgsConstructor
    @Data
    public static class ShardsBean {
        /**
         * total : 1
         * successful : 1
         * skipped : 0
         * failed : 0
         */

        private int total;
        private int successful;
        private int skipped;
        private int failed;
    }

    @NoArgsConstructor
    @Data
    public static class HitsBeanX {
        /**
         * total : {"value":1,"relation":"eq"}
         * max_score : 1
         * hits : [{"_index":"test","_type":"doc","_id":"20","_score":1,"_source":{"date":1565574980,"test":"uuid-20","id":20,"test1":"uuid-20"}}]
         */

        private TotalBean total;
        private int max_score;
        private List<HitsBean> hits;

        @NoArgsConstructor
        @Data
        public static class TotalBean {
            /**
             * value : 1
             * relation : eq
             */

            private int value;
            private String relation;
        }

        @NoArgsConstructor
        @Data
        public static class HitsBean {
            /**
             * _index : test
             * _type : doc
             * _id : 20
             * _score : 1
             * _source : {"date":1565574980,"test":"uuid-20","id":20,"test1":"uuid-20"}
             */

            private String _index;
            private String _type;
            private String _id;
            private int _score;
            private Object _source;

        }
    }
}
