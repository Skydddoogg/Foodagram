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
import com.example.foodagramapp.foodagram.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter extends ArrayAdapter<Post> {
    private Context context;
    private List<Post> postStore;
    private TextView menu_name,post_description,menu_price;
    private ImageView menu_image;
    private Bitmap bitmap;
    private String src;
    public FeedAdapter(@NonNull Context context, int resource, List<Post> list) {
        super(context, resource, list);
        this.context = context;
        this.postStore = list;
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
        src = postStore.get(position).getMenu_image_url();
        post_description = listItems.findViewById(R.id.post_description);
        post_description.setText(postStore.get(position).getDescription());

        menu_price = listItems.findViewById(R.id.menu_price);
        menu_price.setText(""+postStore.get(position).getMenu_price());

        Picasso.get().load(postStore.get(position).getMenu_image_url()).into(menu_image);
//        menu_image.setImageBitmap(bitmap);

        return listItems;
    }




}
