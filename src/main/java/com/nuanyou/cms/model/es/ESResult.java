package com.nuanyou.cms.model.es;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nuanyou.cms.model.LogVO;

import java.util.List;

/**
 * Created by Felix on 2017/2/15.
 */
public class ESResult {
    private String scrollId;
    private Integer took;
    private HitsSum hits;

    private Integer number;
    private Integer pageSize;


    public static class HitsSum {

        private Integer total;

        private List<Hit> hits;


        public static class Hit {

            @JsonProperty("_index")
            private String index;

            @JsonProperty("_type")
            private String type;

            @JsonProperty("_id")
            private String id;

            @JsonProperty("_source")
            private LogVO source;

            public String getIndex() {
                return index;
            }

            public void setIndex(String index) {
                this.index = index;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public LogVO getSource() {
                return source;
            }

            public void setSource(LogVO source) {
                this.source = source;
            }

        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }


        public List<Hit> getHits() {
            return hits;
        }

        public void setHits(List<Hit> hits) {
            this.hits = hits;
        }
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }


    public HitsSum getHits() {
        return hits;
    }

    public void setHits(HitsSum hits) {
        this.hits = hits;
    }


    public Integer getTook() {
        return took;
    }


    public void setTook(Integer took) {
        this.took = took;
    }

    public int getTotalPages() {
        return (this.hits.getTotal() - 1) / this.pageSize + 1;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
