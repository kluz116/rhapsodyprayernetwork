package org.allan_musembya.prayer.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import org.allan_musembya.prayer.models.Videos;
import org.allan_musembya.prayer.prayernetwork.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    private Context mContext;
    private List<Videos> ListOfVideos;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView2,textView4,online_heading,online_details;
        private BetterVideoPlayer bvp;

        public MyViewHolder(View view) {
            super(view);

            textView2 = view.findViewById(R.id.textView2);
            textView4 = view.findViewById(R.id.textView4);
            bvp = view.findViewById(R.id.online_programs);
            online_heading = view.findViewById(R.id.online_heading);
            online_details = view.findViewById(R.id.online_details);

        }
    }

    public VideoAdapter(Context mContext, List<Videos> ListOfVideos) {
        this.mContext = mContext;
        this.ListOfVideos = ListOfVideos;
    }

    @Override
    public VideoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_card, parent, false);
        return new VideoAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoAdapter.MyViewHolder holder, int position) {
        final Videos vids = ListOfVideos.get(position);
       holder.textView2.setText(vids.getVideo_heaing_one());
        holder.textView4.setText(vids.getVideo_heaing_one_description());
        holder.online_heading.setText(vids.getVideo_heaing_two());
        holder.online_details.setText(vids.getVideo_heaing_two_description());

        holder.bvp.setAutoPlay(false);
        holder.bvp.setSource(Uri.parse(vids.getVideo_heaing_url()));
        holder.bvp.setHideControlsOnPlay(true);

        holder.bvp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                holder.bvp.start();


            }
        });


    }

    @Override
    public int getItemCount() {
        return ListOfVideos.size();
    }
}
