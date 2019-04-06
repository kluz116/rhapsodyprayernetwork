package org.allan_musembya.prayer.models;

public class Videos {

    private String video_heaing_one;
    private String video_heaing_one_description;
    private String video_heaing_url;
    private String video_heaing_two;
    private String video_heaing_two_description;

    public Videos(String video_heaing_one, String video_heaing_one_description, String video_heaing_url, String video_heaing_two, String video_heaing_two_description) {
        this.video_heaing_one = video_heaing_one;
        this.video_heaing_one_description = video_heaing_one_description;
        this.video_heaing_url = video_heaing_url;
        this.video_heaing_two = video_heaing_two;
        this.video_heaing_two_description = video_heaing_two_description;
    }

    public String getVideo_heaing_one() {
        return video_heaing_one;
    }

    public String getVideo_heaing_one_description() {
        return video_heaing_one_description;
    }

    public String getVideo_heaing_url() {
        return video_heaing_url;
    }

    public String getVideo_heaing_two() {
        return video_heaing_two;
    }

    public String getVideo_heaing_two_description() {
        return video_heaing_two_description;
    }
}
