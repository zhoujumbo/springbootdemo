package com.jum.db2.entity;

public class User2 {
    private Integer uuid;

    private String username;

    private Integer age;

    private Integer oldAge;

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getOldAge() {
        return oldAge;
    }

    public void setOldAge(Integer oldAge) {
        this.oldAge = oldAge;
    }
}