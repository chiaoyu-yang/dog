package com.example.updatenickname;

public class Post {

    String FullName;
    String Email;
    String DateOfBirth;
    String Age;
    String Gender;

    public Post(){
    }

    @Override
    public String toString() {
        return "Post{" +
                "FullName='" + FullName + '\'' +
                ", Email='" + Email + '\'' +
                ", DateOfBirth='" + DateOfBirth + '\'' +
                ", Age='" + Age + '\'' +
                ", Gender='" + Gender + '\'' +
                '}';
    }

    public String getFullName() { return FullName; }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
