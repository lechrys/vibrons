package fr.myapplication.dc.myapplication;

import java.util.List;

/**
 * Created by chris on 25/03/2016.
 */
public class User {

    private int id;
    private String regid;
    private String login;
    private String password;
    private List<User> contacts;
    private String avatar;


    public User() {
    }

    public User(String regid, String login, String password) {
        this.regid = regid;
        this.login = login;
        this.password = password;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegid() {
        return regid;
    }

    public void setRegid(String regid) {
        this.regid = regid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }

    public String getAvatar(){
        return avatar;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
}

