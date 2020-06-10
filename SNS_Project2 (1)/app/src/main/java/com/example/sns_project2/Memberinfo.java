package com.example.sns_project2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
//db 생성자
public class Memberinfo implements Serializable {
    private String name;
    private String age;
    private String sex;
    private String status;
    private String image;
    private String id;

    //회원정보 데이터베이스 저장
    public Memberinfo(String name, String age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public Memberinfo(String status) {
        this.status = status;
    }

    public Memberinfo(String name, String age, String sex, String status, String image) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.status = status;
        this.image = image;
    }

    public Memberinfo(String name, String age, String sex, String status) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.status = status;
    }


    public Map<String, Object> getMemberinfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("name",name);
        docData.put("age",age);
        docData.put("sex",sex);
        docData.put("status",status);
        docData.put("image",image);
        return  docData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
