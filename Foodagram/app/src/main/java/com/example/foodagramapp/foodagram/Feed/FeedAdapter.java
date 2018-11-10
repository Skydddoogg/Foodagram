package com.example.foodagramapp.foodagram.Feed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.R;

import java.util.List;

public class FeedAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> postStore;
    private TextView titlePost;
    public FeedAdapter(@NonNull Context context, int resource, List<String> list) {
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

        titlePost = (TextView) listItems.findViewById(R.id.menu_name_post);
        titlePost.setText(postStore.get(position));
        return listItems;
    }
}
