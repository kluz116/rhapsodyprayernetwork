package org.allan_musembya.prayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.allan_musembya.prayer.models.Testimony;
import org.allan_musembya.prayer.prayernetwork.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by kluz on 4/23/18.
 */

public class TestimonyAdapter extends RecyclerView.Adapter<TestimonyAdapter.MyViewHolder> {

    private Context mContext;
    private List<Testimony> ListOfClients;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textDate, testimonyUser,testimony_details;
        public ImageView overflow,thumbnail;

        public MyViewHolder(View view) {
            super(view);
            textDate = view.findViewById(R.id.textDate);
            testimonyUser = view.findViewById(R.id.testimonyUser);
            testimony_details = view.findViewById(R.id.testimony_details);

        }
    }


    public TestimonyAdapter(Context mContext, List<Testimony> ListOfClients) {
        this.mContext = mContext;
        this.ListOfClients = ListOfClients;
    }

    @Override
    public TestimonyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.testimony_card, parent, false);
        return new TestimonyAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final TestimonyAdapter.MyViewHolder holder, int position) {
        final Testimony testimonies = ListOfClients.get(position);
        holder.testimonyUser.setText(testimonies.getNames());
        holder.testimony_details.setText(testimonies.getTestimony());

        //Glide.with(mContext).load(R.drawable.profile).dontAnimate().placeholder(R.drawable.profile).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail);
        try {
            long now = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date convertedDate = dateFormat.parse(testimonies.getTimelog());

            CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS);
            holder.textDate.setText(relavetime1);

        }catch(ParseException e) {
            e.printStackTrace();
        }

        holder.textDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {


            }
        });


    }

    @Override
    public int getItemCount() {
        return ListOfClients.size();
    }

    public void clear() {
        ListOfClients.clear();
        notifyDataSetChanged();
    }
}
