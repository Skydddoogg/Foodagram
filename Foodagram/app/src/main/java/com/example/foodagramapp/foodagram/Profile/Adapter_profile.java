package com.example.foodagramapp.foodagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Feed.MapsMarkerActivity;
import com.example.foodagramapp.foodagram.LikeAction;
import com.example.foodagramapp.foodagram.OnlineUser;
import com.example.foodagramapp.foodagram.Post.Post;
import com.example.foodagramapp.foodagram.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_profile extends ArrayAdapter<Post> {

    private List<Post> profileInfo = new ArrayList<>();
    private List<String> likeCountforRender = new ArrayList<>();
    private List<String> commentCountArrayList = new ArrayList<>();
    private LikeAction likeAction;
    private List<String> postId;
    private List<ProfileForFeed> profileArrayList = new ArrayList<>();
    Context context;

    Adapter_profile(Context context, int resource, List<Post> objects, List<String> likeCountforRender,
                    List<String> commentCountArrayList,
                    List<String> postId,
                    List<ProfileForFeed> profileArrayList
                    ){
        super(context, resource, objects);
        this.profileInfo = objects;
        this.context = context;
        this.likeCountforRender = likeCountforRender;
        this.commentCountArrayList = commentCountArrayList;
        this.postId = postId;
        this.profileArrayList = profileArrayList;


    }

    @Nullable
    @Override
    public Post getItem(int position) {
        return profileInfo.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View profileItem = LayoutInflater.from(context).inflate(R.layout.fragment_profile_item,parent,false);
        ImageView menuImage = profileItem.findViewById(R.id.menu_image);
        ImageView profileImage_profile_item = profileItem.findViewById(R.id.post_user_image);
        ImageView profileImage = profileItem.findViewById(R.id.post_user_image);
        TextView menuName = profileItem.findViewById(R.id.menu_name_post);
        TextView profileName  = profileItem.findViewById(R.id.post_profile_name);
        TextView postTime = profileItem.findViewById(R.id.post_timestamp);
        TextView menuPrice = profileItem.findViewById(R.id.menu_price);
        TextView location = profileItem.findViewById(R.id.checkin_location);
        TextView postDescription = profileItem.findViewById(R.id.post_description);


        final ImageView like_button_profile_item = profileItem.findViewById(R.id.like_button_profile_item);
        likeAction = new LikeAction(new OnlineUser().ONLINE_USER, postId.get(position), likeCountforRender.get(position), like_button_profile_item);
        likeAction.setColorButton();
        like_button_profile_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeAction = new LikeAction(new OnlineUser().ONLINE_USER, postId.get(position), likeCountforRender.get(position), like_button_profile_item);
                likeAction.likeAction();
            }
        });
        TextView like_count_profile_item = profileItem.findViewById(R.id.like_count_profile_item);
        like_count_profile_item.setText(likeCountforRender.get(position));


        TextView comment_profile_item = profileItem.findViewById(R.id.comment_profile_item);
        comment_profile_item.setText(commentCountArrayList.get(position));


        Picasso.get().load(profileInfo.get(position).getMenuImageURL()).into(menuImage);

        Picasso.get().load(profileArrayList.get(position).getProfile_img_url()).into(profileImage_profile_item);
        location.setText(profileInfo.get(position).getAddress());
        menuName.setText(profileInfo.get(position).getMenuName());
        menuPrice.setText((int)profileInfo.get(position).getMenuPrice()+"");
        postDescription.setText(profileInfo.get(position).getDescription());
        profileName.setText(profileArrayList.get(position).getUsername());
        postTime.setText(getCountOfDays((long) profileInfo.get(position).getTimestamp()));


        ConstraintLayout location_button_profile = profileItem.findViewById(R.id.location_button_profile);
        location_button_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send Post Obj To Map XML
                Intent intent = new Intent(getContext(), MapsMarkerActivity.class);
                intent.putExtra("post",profileInfo.get(position));
                getContext().startActivity(intent);
//                Log.i(TAG, "GO TO LOCATION XML ---------- Profile" + profileInfo.get(position).getMenuName());
            }
        });


        return profileItem;
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