package com.example.foodagramapp.foodagram.PostView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Comment.Comment;
import com.example.foodagramapp.foodagram.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class PostViewAdapter extends ArrayAdapter <Comment> {

    List<Comment> comment = new ArrayList<>();
    Context context;

    public PostViewAdapter (@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
        this.context = context;
        this.comment = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View postViewCommentItem = LayoutInflater.from(context).inflate(R.layout.fragment_comment_item, parent, false);

            TextView displayName = (TextView) postViewCommentItem.findViewById(R.id.comment_item_user_name);
            TextView timeStamp = (TextView) postViewCommentItem.findViewById(R.id.comment_item_time_stamp);
            CircularImageView userThumnail = (CircularImageView) postViewCommentItem.findViewById(R.id.post_user_thumbnail);
            TextView commentContent = (TextView) postViewCommentItem.findViewById(R.id.comment_item_content);

            userThumnail.setImageResource(R.drawable.food_pic_2);

    //      ImageView img = getView().findViewById(R.id.imageView);
    //      img.setImageResource(R.drawable.food_pic);
    //
    //      CircularImageView circularImageView = getView().findViewById(R.id.post_user_thumbnail);
    //      circularImageView.setImageResource(R.drawable.food_pic);

        return postViewCommentItem;
    }
}
