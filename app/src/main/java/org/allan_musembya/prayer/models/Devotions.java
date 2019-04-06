package org.allan_musembya.prayer.models;

public class Devotions {
    public String userid;
    public String names;
    public String comment_text;
    public String timelog;
    public String img;

    public Devotions(){}

    public Devotions(String userid,String names,String comment_text,String timelog,String img){
        this.userid =userid;
        this.names=names;
        this.comment_text=comment_text;
        this.timelog = timelog;
        this.img = img;
    }
}
