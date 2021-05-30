package com.cosmic.firebaseauthentication.models;

public class EnemyPhoto {

    private String url, filename, post_key, image_key;

    public EnemyPhoto() {

    }

    public EnemyPhoto(String url, String filename, String post_key, String image_key) {
        this.url = url;
        this.filename = filename;
        this.post_key = post_key;
        this.image_key = image_key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPost_key() {
        return post_key;
    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }

    public String getImage_key() {
        return image_key;
    }

    public void setImage_key(String image_key) {
        this.image_key = image_key;
    }

    @Override
    public String toString() {
        return "EnemyPhoto{" +
                "url='" + url + '\'' +
                ", filename='" + filename + '\'' +
                ", post_key='" + post_key + '\'' +
                ", image_key='" + image_key + '\'' +
                '}';
    }
}
