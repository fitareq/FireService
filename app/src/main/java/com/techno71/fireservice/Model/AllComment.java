package com.techno71.fireservice.Model;

public class AllComment {
    private String id;
    private String store_id;
    private String user_id;
    private String comment;
    private String alert_tag;
    private String status;
    private String loc_id;
    private String created_at;
    private String updated_at;

    public AllComment(String id, String store_id, String user_id, String comment, String alert_tag, String status, String loc_id, String created_at, String updated_at) {
        this.id = id;
        this.store_id = store_id;
        this.user_id = user_id;
        this.comment = comment;
        this.alert_tag = alert_tag;
        this.status = status;
        this.loc_id = loc_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public String getStore_id() {
        return store_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getComment() {
        return comment;
    }

    public String getAlert_tag() {
        return alert_tag;
    }

    public String getStatus() {
        return status;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

}
