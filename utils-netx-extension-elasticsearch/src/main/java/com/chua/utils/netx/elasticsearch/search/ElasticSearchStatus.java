package com.chua.utils.netx.elasticsearch.search;

import java.util.List;

/**
 * 状态
 * @author CHTK
 */
public class ElasticSearchStatus {
    /**
     * took : 6
     * timed_out : false
     * _shards : {"total":5,"successful":5,"failed":0}
     * hits : {"total":8,"max_score":0,"hits":[{"_index":"test","_type":"doc","_id":"0","_score":0,"_source":{"id":0}},{"_index":"test","_type":"doc","_id":"5","_score":0,"_source":{"id":5}},{"_index":"test","_type":"doc","_id":"2","_score":0,"_source":{"id":2}},{"_index":"test","_type":"doc","_id":"4","_score":0,"_source":{"id":4}},{"_index":"test","_type":"doc","_id":"6","_score":0,"_source":{"id":6}},{"_index":"test","_type":"doc","_id":"1","_score":0,"_source":{"id":1}},{"_index":"test","_type":"doc","_id":"7","_score":0,"_source":{"id":7}},{"_index":"test","_type":"doc","_id":"3","_score":0,"_source":{"id":3}}]}
     */

    private int took;
    private boolean timed_out;
    private ShardsBean _shards;
    private HitsBeanX hits;

    public int getTook() {
        return took;
    }

    public void setTook(int took) {
        this.took = took;
    }

    public boolean isTimed_out() {
        return timed_out;
    }

    public void setTimed_out(boolean timed_out) {
        this.timed_out = timed_out;
    }

    public ShardsBean get_shards() {
        return _shards;
    }

    public void set_shards(ShardsBean _shards) {
        this._shards = _shards;
    }

    public HitsBeanX getHits() {
        return hits;
    }

    public void setHits(HitsBeanX hits) {
        this.hits = hits;
    }

    public static class ShardsBean {
        /**
         * total : 5
         * successful : 5
         * failed : 0
         */

        private int total;
        private int successful;
        private int failed;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSuccessful() {
            return successful;
        }

        public void setSuccessful(int successful) {
            this.successful = successful;
        }

        public int getFailed() {
            return failed;
        }

        public void setFailed(int failed) {
            this.failed = failed;
        }
    }

    public static class HitsBeanX {
        /**
         * total : 8
         * max_score : 0
         * hits : [{"_index":"test","_type":"doc","_id":"0","_score":0,"_source":{"id":0}},{"_index":"test","_type":"doc","_id":"5","_score":0,"_source":{"id":5}},{"_index":"test","_type":"doc","_id":"2","_score":0,"_source":{"id":2}},{"_index":"test","_type":"doc","_id":"4","_score":0,"_source":{"id":4}},{"_index":"test","_type":"doc","_id":"6","_score":0,"_source":{"id":6}},{"_index":"test","_type":"doc","_id":"1","_score":0,"_source":{"id":1}},{"_index":"test","_type":"doc","_id":"7","_score":0,"_source":{"id":7}},{"_index":"test","_type":"doc","_id":"3","_score":0,"_source":{"id":3}}]
         */

        private int total;
        private int max_score;
        private List<HitsBean> hits;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getMax_score() {
            return max_score;
        }

        public void setMax_score(int max_score) {
            this.max_score = max_score;
        }

        public List<HitsBean> getHits() {
            return hits;
        }

        public void setHits(List<HitsBean> hits) {
            this.hits = hits;
        }

        public static class HitsBean {
            /**
             * _index : test
             * _type : doc
             * _id : 0
             * _score : 0
             * _source : {"id":0}
             */

            private String _index;
            private String _type;
            private String _id;
            private int _score;
            private Object _source;

            public String get_index() {
                return _index;
            }

            public void set_index(String _index) {
                this._index = _index;
            }

            public String get_type() {
                return _type;
            }

            public void set_type(String _type) {
                this._type = _type;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public int get_score() {
                return _score;
            }

            public void set_score(int _score) {
                this._score = _score;
            }

            public Object get_source() {
                return _source;
            }

            public void set_source(Object _source) {
                this._source = _source;
            }

        }
    }
}
