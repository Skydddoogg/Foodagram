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

import com.example.foodagramapp.foodagram.Post;
import com.example.foodagramapp.foodagram.Profile;
import com.example.foodagramapp.foodagram.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class FeedAdapter extends ArrayAdapter<Post> {
    private Context context;
    private List<Post> postStore;
    private List<Profile> profiles;
    private TextView menu_name, post_description, menu_price, timestamp,name;
    private ImageView menu_image, feed_user_thumbnail;
    private Bitmap bitmap;
    private String src;

    public FeedAdapter(@NonNull Context context, int resource, List<Post> list) {
        super(context, resource, list);
        this.context = context;
        this.postStore = list;
//        this.profiles = proflies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItems = LayoutInflater.from(context).inflate(
                R.layout.feed_crad_view,
                parent,
                false
        );

        menu_name = (TextView) listItems.findViewById(R.id.menu_name_post);
        menu_name.setText(postStore.get(position).getMenu_name());
        menu_image = (ImageView) listItems.findViewById(R.id.menu_image);
        feed_user_thumbnail = (ImageView) listItems.findViewById(R.id.feed_user_thumbnail);
        src = postStore.get(position).getMenu_image_url();
        post_description = listItems.findViewById(R.id.post_description);
        post_description.setText(postStore.get(position).getDescription());

        menu_price = listItems.findViewById(R.id.menu_price);
        menu_price.setText("" + (int) postStore.get(position).getMenu_price());

        timestamp = (TextView) listItems.findViewById(R.id.timestamp);
        timestamp.setText(convertMillisecToDate(postStore.get(position).getTimestamp()));


//        name = (TextView) listItems.findViewById(R.id.name);
//        name.setText(profiles.get(position).getName());



//        Picasso.get().load(profiles.get(position).getProfile_img_url()).into(feed_user_thumbnail);
        Picasso.get().load(postStore.get(position).getMenu_image_url()).into(menu_image);
//        menu_image.setImageBitmap(bitmap);

        return listItems;
    }


    private String convertMillisecToDate(long postTime) {
        long millis = postTime;
        //creating Date from millisecond
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date currentDate = new Date(postTime - (7 * DAY_IN_MS));

        //printing value of Date

        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

        //formatted value of current Date
        return df.format(currentDate).toString();
    }


}
