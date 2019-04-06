package org.allan_musembya.prayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.allan_musembya.prayer.models.Books;
import org.allan_musembya.prayer.prayernetwork.R;

import java.util.List;

public class Booksadapter extends RecyclerView.Adapter<Booksadapter.MyViewHolder> {

    private Context mContext;
    private List<Books> ListOfBooks;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  book_title,book_details,book_price;
        public ImageView book_cover_img;
        public Button purchase,price_tag;

        public MyViewHolder(View view) {
            super(view);
            book_cover_img = view.findViewById(R.id.book_cover_img);
            book_title = view.findViewById(R.id.book_title);
            //book_details = view.findViewById(R.id.book_details);
            price_tag = view.findViewById(R.id.price_tag);
            purchase = view.findViewById(R.id.purchase);

        }
    }

    public Booksadapter(Context mContext, List<Books> ListOfBooks) {
        this.mContext = mContext;
        this.ListOfBooks = ListOfBooks;
    }

    @Override
    public Booksadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_libray_card, parent, false);
        return new Booksadapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Booksadapter.MyViewHolder holder, int position) {
        final Books bks = ListOfBooks.get(position);
        holder.book_title.setText(bks.getBook_title());
        //holder.book_details.setText(bks.getBook_detail());
        holder.price_tag.setText(""+bks.getBook_price()+"$");
        holder.purchase.setText("Buy Book - "+bks.getBook_price()+"USD");
        Glide.with(mContext).load(bks.getBook_cover_img()).into(holder.book_cover_img);

        holder.book_title.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                
            }
        });


    }

    @Override
    public int getItemCount() {
        return ListOfBooks.size();
    }
}
