package com.example.foodagramapp.foodagram.Feed;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.LikeAction;
import com.example.foodagramapp.foodagram.Post.Post;
import com.example.foodagramapp.foodagram.Profile.ProfileForFeed;
import com.example.foodagramapp.foodagram.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter extends ArrayAdapter<Post> {
    private Context context;
    private List<Post> posts;
    private List<String> likeCount;
    private List<ProfileForFeed> profiles;
    private List<String> commentCountArrayList;
    private TextView menuName, menuPrice, location, decscription, likeCountTextView, commentCount, username, timestamp;
    private ImageView postThumnail,profileImage;

    public FeedAdapter(@NonNull Context context, int resource, List<Post> posts,  List<String> likeCount,
                       List<ProfileForFeed> profiles,
                       List<String> commentCountArrayList
                       ) {
        super(context, resource, posts);
        this.context = context;
        this.posts = posts;
        this.likeCount = likeCount;
        this.profiles = profiles;
        this.commentCountArrayList = commentCountArrayList;
    }

    @Nullable
    @Override
    public Post getItem(int position) {
        return posts.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View listItems = LayoutInflater.from(context).inflate(
                R.layout.feed_crad_view,
                parent,
                false
        );


        //Post
        menuName = listItems.findViewById(R.id.menu_name_post);
        menuName.setText(posts.get(position).getMenuName());

        menuPrice = listItems.findViewById(R.id.menu_price);
        menuPrice.setText((int) posts.get(position).getMenuPrice()+"");

        location = listItems.findViewById(R.id.location);
        location.setText(posts.get(position).getAddress());

        decscription = listItems.findViewById(R.id.post_description);
        decscription.setText(posts.get(position).getDescription());

        likeCountTextView = listItems.findViewById(R.id.like_count);
        likeCountTextView.setText(likeCount.get(position));

        timestamp = listItems.findViewById(R.id.timestamp);
        timestamp.setText(getCountOfDays((long) posts.get(position).getTimestamp()));

        postThumnail = listItems.findViewById(R.id.menu_image);
        Picasso.get().load(posts.get(position).getMenuImageURL()).into(postThumnail);



        //Comment
        commentCount = listItems.findViewById(R.id.comment);
        commentCount.setText(commentCountArrayList.get(position));


        //Profile
        username = listItems.findViewById(R.id.name);
        username.setText(profiles.get(position).getUsername());

        profileImage = listItems.findViewById(R.id.profileImage);
        Picasso.get().load(profiles.get(position).getProfile_img_url()).into(profileImage);


        return listItems;
    }


    public String getCountOfDays(long time) {
        final int SECOND_MILLIS = 1000;
        final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final int DAY_MILLIS = 24 * HOUR_MILLIS;
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


}
