package com.cosmic.firebaseauthentication.models;

public class Enemy {

    private String name, image_url, reason, date_added, status, alias, image_filename, post_key, priority, danger_level, last_known_location;
    long timestamp;

    public Enemy() {
        //default empty constructor
    }

    public Enemy(String name, String image_url, String reason, String date_added, String status,
                 String alias, String image_filename, String post_key, String priority, String danger_level, String last_known_location, long timestamp) {
        this.name = name;
        this.image_url = image_url;
        this.reason = reason;
        this.date_added = date_added;
        this.status = status;
        this.alias = alias;
        this.image_filename = image_filename;
        this.post_key = post_key;
        this.priority = priority;
        this.danger_level = danger_level;
        this.last_known_location = last_known_location;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage_filename() {
        return image_filename;
    }

    public void setImage_filename(String image_filename) {
        this.image_filename = image_filename;
    }

    public String getPost_key() {
        return post_key;
    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDanger_level() {
        return danger_level;
    }

    public void setDanger_level(String danger_level) {
        this.danger_level = danger_level;
    }

    public String getLast_known_location() {
        return last_known_location;
    }

    public void setLast_known_location(String last_known_location) {
        this.last_known_location = last_known_location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "name='" + name + '\'' +
                ", image_url='" + image_url + '\'' +
                ", reason='" + reason + '\'' +
                ", date_added='" + date_added + '\'' +
                ", status='" + status + '\'' +
                ", alias='" + alias + '\'' +
                ", image_filename='" + image_filename + '\'' +
                ", post_key='" + post_key + '\'' +
                ", priority='" + priority + '\'' +
                ", danger_level='" + danger_level + '\'' +
                ", last_known_location='" + last_known_location + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
