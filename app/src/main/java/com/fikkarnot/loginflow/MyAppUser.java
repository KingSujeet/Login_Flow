package com.fikkarnot.loginflow;

public class MyAppUser {

    String name;
    String profession;
    String dob;
    String phone;

    public MyAppUser(){


    }


    public MyAppUser(String name, String profession, String dob, String phone) {
        this.name = name;
        this.profession = profession;
        this.dob = dob;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
