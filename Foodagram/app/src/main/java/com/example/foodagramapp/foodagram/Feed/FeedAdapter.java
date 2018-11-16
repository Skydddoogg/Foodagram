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
    private List<Post> postStore;
    private List<ProfileForFeed> profiles;
    private List<String> likeCount;
    private List<String> postId, commentCount;
    private TextView menu_name, post_description, menu_price, timestamp, name, like_count, comment;
    private ImageView menu_image, feed_user_thumbnail, like_button;
    private Bitmap bitmap;
    private String src;
    private List<ProfileForFeed> proflies;
    private String ONLINE_USER;
    private LikeAction likeAction;

    public FeedAdapter(@NonNull Context context, int resource, List<Post> list,
                       List<String> likeCount, List<ProfileForFeed> proflies, List<String> postId,
                       String ONLINE_USER, List<String> commentCount) {
        super(context, resource, list);
        this.context = context;
        this.postStore = list;
        this.profiles = proflies;
        this.likeCount = likeCount;
        this.postId = postId;
        this.ONLINE_USER = ONLINE_USER;
        this.commentCount = commentCount;
    }

    @Nullable
    @Override
    public Post getItem(int position) {
        return postStore.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View listItems = LayoutInflater.from(context).inflate(
                R.layout.feed_crad_view,
                parent,
                false
        );


        menu_name = (TextView) listItems.findViewById(R.id.menu_name_post);
        menu_name.setText(postStore.get(position).getMenuName());
        menu_image = (ImageView) listItems.findViewById(R.id.menu_image);
        feed_user_thumbnail = (ImageView) listItems.findViewById(R.id.profileImage);
        src = postStore.get(position).getMenuImageURL();
        post_description = listItems.findViewById(R.id.post_description);
        post_description.setText(postStore.get(position).getDescription());

        menu_price = listItems.findViewById(R.id.menu_price);
        menu_price.setText("" + (int) postStore.get(position).getMenuPrice());

        timestamp = (TextView) listItems.findViewById(R.id.timestamp);

        timestamp.setText(getCountOfDays((long) postStore.get(position).getTimestamp()));

        like_count = (TextView) listItems.findViewById(R.id.like_count);
        like_count.setText(likeCount.get(position));


        comment = (TextView) listItems.findViewById(R.id.comment);
        comment.setText(commentCount.get(position));


        like_button = (ImageView) listItems.findViewById(R.id.like_button);


        likeAction = new LikeAction(ONLINE_USER, postId.get(position), likeCount.get(position), like_button);
        likeAction.setColorButton();

        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeAction = new LikeAction(ONLINE_USER, postId.get(position), likeCount.get(position), like_button);
                likeAction.checkLike();
            }
        });


        if (postStore.size() == profiles.size()) {
            name = (TextView) listItems.findViewById(R.id.name);
            name.setText(profiles.get(position).getName());
            Picasso.get().load(profiles.get(position).getProfile_img_url()).into(feed_user_thumbnail);
        }
        Picasso.get().load(postStore.get(position).getMenuImageURL()).into(menu_image);


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
