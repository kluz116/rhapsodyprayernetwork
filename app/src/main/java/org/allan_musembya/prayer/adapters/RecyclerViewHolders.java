package org.allan_musembya.prayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.allan_musembya.prayer.models.Comments;
import org.allan_musembya.prayer.prayernetwork.R;

import java.util.List;

class RecyclerViewHolders  extends RecyclerView.ViewHolder{
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    public TextView user_comment_time2, user_comment2,textView2;
    public ImageView overflow,thumbnail;

    public  List<Comments> ListOfComments;
    public RecyclerViewHolders(final View view, final List<Comments> ListOfComments) {
        super(view);
        this.ListOfComments = ListOfComments;
        user_comment_time2 = view.findViewById(R.id.user_comment_time2);
        user_comment2 = view.findViewById(R.id.user_comment2);
        textView2 = view.findViewById(R.id.textView2);

    }
}
