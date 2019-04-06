package org.allan_musembya.prayer.models;

import java.io.Serializable;

/**
 * Created by kluz on 4/23/18.
 */

public class Testimony implements Serializable {
    private String userid;
    private String names;
    private String email;
    private String testimony;
    private String timelog;

    public Testimony(String userid,String names,String email,String testimony,String timelog){
        this.setUserid(userid);
        this.setNames(names);
        this.setEmail(email);
        this.setTestimony(testimony);
        this.setTimelog(timelog);
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTestimony() {
        return testimony;
    }

    public void setTestimony(String testimony) {
        this.testimony = testimony;
    }

    public String getTimelog() {
        return timelog;
    }

    public void setTimelog(String timelog) {
        this.timelog = timelog;
    }
}
