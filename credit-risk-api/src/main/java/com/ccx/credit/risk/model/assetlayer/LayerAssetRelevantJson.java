package com.ccx.credit.risk.model.assetlayer;


import java.util.List;

public class LayerAssetRelevantJson {

    private List<Parent> list;

    private List<Tags> tags;

    public LayerAssetRelevantJson() {
    }

    public LayerAssetRelevantJson(List<Parent> list, List<Tags> tags) {
        this.list = list;
        this.tags = tags;
    }

    public static class Parent {
        private Integer id;

        private Integer level;

        private String groupId = "4001";

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }
    }

    public static class Tags {
        private Integer id;

        private Data data;

        private Integer pId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public Integer getpId() {
            return pId;
        }

        public void setpId(Integer pId) {
            this.pId = pId;
        }

        public static class Data {
            private Integer id;

            private String name;

            private String content;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }

    public List<Parent> getList() {
        return list;
    }

    public void setList(List<Parent> list) {
        this.list = list;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }
}
