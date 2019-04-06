package org.allan_musembya.prayer.models;

public class Books {
    private String book_id;
    private int book_cover_img;
    private String book_title;
    private String book_detail;
    private String book_price;

    public Books(String book_id, int book_cover_img,String book_title, String book_detail,String book_price){
           this.book_id = book_id;
           this.book_cover_img =book_cover_img;
           this.book_title = book_title;
           this.book_detail = book_detail;
           this.book_price = book_price;
    }


    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public int getBook_cover_img() {
        return book_cover_img;
    }

    public void setBook_cover_img(int book_cover_img) {
        this.book_cover_img = book_cover_img;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_detail() {
        return book_detail;
    }

    public void setBook_detail(String book_detail) {
        this.book_detail = book_detail;
    }


    public String getBook_price() {
        return book_price;
    }

    public void setBook_price(String book_price) {
        this.book_price = book_price;
    }
}
