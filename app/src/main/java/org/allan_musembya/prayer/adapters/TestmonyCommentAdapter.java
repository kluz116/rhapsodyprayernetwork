package org.allan_musembya.prayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;

import org.allan_musembya.prayer.models.TestimonyComments;
import org.allan_musembya.prayer.prayernetwork.R;
import org.allan_musembya.prayer.utils.GlideApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestmonyCommentAdapter extends  RecyclerView.Adapter<TestmonyCommentAdapter.MyViewHolder> {

    public Context mContext;
    public List<TestimonyComments> ListOfTestimonyComments;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_comment_time2, user_comment2,textView2;
        public ImageView overflow,imageView6;

        public MyViewHolder(View view) {
            super(view);
            user_comment_time2 = view.findViewById(R.id.user_comment_time2);
            user_comment2 = view.findViewById(R.id.user_comment2);
            textView2 = view.findViewById(R.id.textView2);
            imageView6 = view.findViewById(R.id.imageView6);

        }
    }

    public TestmonyCommentAdapter(Context mContext, List<TestimonyComments> ListOfTestimonyComments) {
        this.mContext = mContext;
        this.ListOfTestimonyComments = ListOfTestimonyComments;
    }

    @Override
    public TestmonyCommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list, parent, false);
        return new TestmonyCommentAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final TestmonyCommentAdapter.MyViewHolder holder, int position) {
        final TestimonyComments cmts = ListOfTestimonyComments.get(position);
        holder.user_comment2.setText(cmts.names);
        holder.textView2.setText(cmts.comment_text);
        GlideApp.with(mContext)
                .load(cmts.img)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .skipMemoryCache(true)
                .placeholder(R.drawable.profile)
                .into(holder.imageView6);

        //Glide.with(mContext).load(R.drawable.profile).dontAnimate().placeholder(R.drawable.profile).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail);
        try {
            long now = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date convertedDate = dateFormat.parse(cmts.timelog);

            CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS);
            holder.user_comment_time2.setText(relavetime1);

        }catch(ParseException e) {
            e.printStackTrace();
        }

        holder.user_comment_time2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {


            }
        });


    }

    @Override
    public int getItemCount() {
        return ListOfTestimonyComments.size();
    }

    public void clear() {
        ListOfTestimonyComments.clear();
        //notifyDataSetChanged();
    }
}
