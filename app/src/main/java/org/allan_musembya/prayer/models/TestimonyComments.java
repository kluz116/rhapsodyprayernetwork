package org.allan_musembya.prayer.models;

public class TestimonyComments {
    public String userid;
    public String names;
    public String comment_text;
    public String timelog;
    public String img;

    public TestimonyComments(){}

    public TestimonyComments(String userid,String names,String comment_text,String timelog,String img){
        this.userid =userid;
        this.names=names;
        this.comment_text=comment_text;
        this.timelog = timelog;
        this.img = img;
    }
}
