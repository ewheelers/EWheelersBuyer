package com.ewheelers.ewheelersbuyer.ModelClass;

import java.io.Serializable;

public class HomeColletions implements Serializable {
    private String collection_id;
    private String collection_name;
    private String collection_description;
    private String collection_link_caption;
    private String collection_link_url;
    private String collection_layout_type;
    private String collection_type;
    private String collection_criteria;
    private String collection_child_records;
    private String collection_primary_records;
    private String collection_display_media_only;

    public String getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(String collection_id) {
        this.collection_id = collection_id;
    }

    public String getCollection_name() {
        return collection_name;
    }

    public void setCollection_name(String collection_name) {
        this.collection_name = collection_name;
    }

    public String getCollection_description() {
        return collection_description;
    }

    public void setCollection_description(String collection_description) {
        this.collection_description = collection_description;
    }

    public String getCollection_link_caption() {
        return collection_link_caption;
    }

    public void setCollection_link_caption(String collection_link_caption) {
        this.collection_link_caption = collection_link_caption;
    }

    public String getCollection_link_url() {
        return collection_link_url;
    }

    public void setCollection_link_url(String collection_link_url) {
        this.collection_link_url = collection_link_url;
    }

    public String getCollection_layout_type() {
        return collection_layout_type;
    }

    public void setCollection_layout_type(String collection_layout_type) {
        this.collection_layout_type = collection_layout_type;
    }

    public String getCollection_type() {
        return collection_type;
    }

    public void setCollection_type(String collection_type) {
        this.collection_type = collection_type;
    }

    public String getCollection_criteria() {
        return collection_criteria;
    }

    public void setCollection_criteria(String collection_criteria) {
        this.collection_criteria = collection_criteria;
    }

    public String getCollection_child_records() {
        return collection_child_records;
    }

    public void setCollection_child_records(String collection_child_records) {
        this.collection_child_records = collection_child_records;
    }

    public String getCollection_primary_records() {
        return collection_primary_records;
    }

    public void setCollection_primary_records(String collection_primary_records) {
        this.collection_primary_records = collection_primary_records;
    }

    public String getCollection_display_media_only() {
        return collection_display_media_only;
    }

    public void setCollection_display_media_only(String collection_display_media_only) {
        this.collection_display_media_only = collection_display_media_only;
    }
}
