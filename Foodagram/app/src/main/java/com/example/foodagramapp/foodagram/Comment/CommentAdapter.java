package com.example.foodagramapp.foodagram.Comment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Profile;
import com.example.foodagramapp.foodagram.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends ArrayAdapter <Comment> {

    private List<Comment> comment = new ArrayList<>();
    private List<Profile> profiles;
    private Context context;
    private String _userName, _content;

    public CommentAdapter (@NonNull Context context, int resource, @NonNull List<Comment> objects, @NonNull List<Profile> profiles){
        super(context, resource, objects);
        this.context = context;
        this.comment = objects;
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View commentItem = LayoutInflater.from(context).inflate(
                R.layout.fragment_comment_item,
                parent, false
        );

        TextView userId = (TextView) commentItem.findViewById(R.id.comment_item_user_name);
        TextView content = (TextView) commentItem.findViewById(R.id.comment_item_content);
        TextView timestamp = (TextView) commentItem.findViewById(R.id.comment_item_time_stamp);
        ImageView userThumbnail = (ImageView) commentItem.findViewById(R.id.comment_item_user_thumbnail);

        Comment row = comment.get(position);
        Profile profilePos = profiles.get(position);

        _userName = profilePos.getUsername();
        _content = row.getContent();
        Double _timeStamp = row.getTimestamp();

        userId.setText(_userName);
        content.setText(_content);
        timestamp.setText(getCountOfDays(_timeStamp.longValue()));
        Picasso.get().load(profiles.get(position).getProfile_img_url()).into(userThumbnail);

        return commentItem;
    }


    public Comment getItem(int position) {
        return comment.get(position);
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
