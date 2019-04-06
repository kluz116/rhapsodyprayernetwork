package org.allan_musembya.prayer.Firebase;

import java.util.HashMap;
import java.util.Map;

public class Post {


    private String userid;
    private String names;
    private String comment_text;
    private String timelog;
    private String img;

    public Post(){}

    public Post(String userid,String names,String comment_text,String timelog,String img){
        this.userid= userid;
        this.names = names;
        this.comment_text = comment_text;
        this.timelog = timelog;
        this.img = img;

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userid",userid);
        params.put("names",names);
        params.put("comment_text",comment_text);
        params.put("timelog",timelog);
        params.put("img",img);
        return params;
    }
}
