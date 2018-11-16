package com.example.foodagramapp.foodagram.Discover;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Post.Post;
import com.example.foodagramapp.foodagram.Profile.ProfileForFeed;
import com.example.foodagramapp.foodagram.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder> {

    private List<Post> postList;
    private List<ProfileForFeed> profiles;
    private Context context;

    public DiscoverAdapter(List<Post> postList, List<ProfileForFeed> profiles, Context context) {
        this.postList = postList;
        this.context = context;
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.discover_card_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    public Post getItem(int position) {
        return postList.get(position);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Post post = postList.get(position);
        holder.postThumbnail.requestLayout();
        Picasso.get().load(postList.get(position).getMenuImageURL()).into(holder.postThumbnail);
        Picasso.get().load(profiles.get(position).getProfile_img_url()).into(holder.profileImage);
        holder.postThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Send Post Obj
                // SleepForm obj;
                FragmentManager fm;
                FragmentTransaction ft;
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", postList.get(position));
//                            fm = getActivity().getSupportFragmentManager();
//                            ft = fm.beginTransaction();
//                            obj = new SleepForm();
//                            obj.setArguments(bundle);
//                            ft.replace(R.id.main_view, obj);
//                            ft.commit();
            }
        });
        holder.discover_user_name.setText(profiles.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView postThumbnail, profileImage;
        public TextView discover_user_name;

        public ViewHolder(View view) {
            super(view);

            postThumbnail = (ImageView) view.findViewById(R.id.discover_thumbnail);
            profileImage = (ImageView) view.findViewById(R.id.profileImage);
            discover_user_name = (TextView) view.findViewById(R.id.discover_user_name);
        }
    }

}
