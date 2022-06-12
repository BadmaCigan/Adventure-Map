package com.example.maps.entity;

public class User {
    public int id;
    public String nickName;
    public String aboutUser;
    public int age;
    public int sex;
    public long bdate;

    public static final int MALE = 2;
    public static final int FEMALE = 1;
    public static final int NONE = 0;


    public User(int id, String nickName, String aboutUser, int age, int sex, long bdate) {
        this.id = id;
        this.nickName = nickName;
        this.aboutUser = aboutUser;
        this.age = age;
        this.sex = sex;
        this.bdate = bdate;
    }


    public User(int id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
