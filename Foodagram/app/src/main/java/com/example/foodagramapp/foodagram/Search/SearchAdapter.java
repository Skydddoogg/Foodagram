package com.example.foodagramapp.foodagram.Search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.Profile;
import com.example.foodagramapp.foodagram.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends ArrayAdapter<Profile> {
    private Context context;
    private List<Profile> profiles;
    private List<Profile> following;
    private TextView name, userName, followed, vitae;
    private ImageView image_profile;
    private Filter filter;

    public SearchAdapter(@NonNull Context context, int resource, List<Profile> list, List<Profile> following) {
        super(context,resource,list);
        this.context = context;
        this.profiles = list;
        this.following = following;
    }

    @Nullable
    @Override
    public Profile getItem(int position) {
        return profiles.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItems = LayoutInflater.from(context).inflate(
                R.layout.search_adapter,
                parent,
                false
        );
        name = (TextView) listItems.findViewById(R.id.name);
        if(profiles.get(position).getName().equals("")) {
            name.setText(profiles.get(position).getName());
        }else{
            name.setText(profiles.get(position).getName()+" ");
        }

        userName = (TextView) listItems.findViewById(R.id.username);
        userName.setText(profiles.get(position).getUsername());


        vitae = (TextView) listItems.findViewById(R.id.vitae);
        vitae.setText(profiles.get(position).getVitae());

        followed = (TextView) listItems.findViewById(R.id.folloed);

        for(Profile usrId : following) {
            if (profiles.get(position).getEmail().equals(usrId.getEmail()) && usrId.getEmail() != null) {
                followed.setVisibility(View.VISIBLE);
            }
        }
        image_profile = (ImageView) listItems.findViewById(R.id.image_profile);
        Picasso.get().load(profiles.get(position).getProfile_img_url()).into(image_profile);


        return listItems;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<Profile>(profiles);
        return filter;
    }

    private class AppFilter<T> extends Filter {
        private ArrayList<Profile> sourceObjects;
        public AppFilter(List<Profile> objects) {
            sourceObjects = new ArrayList<Profile>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<Profile> filter = new ArrayList<Profile>();
                int count = 0;
                for (Profile object : sourceObjects) {
                    // the filtering itself:
                    count++;
                    if (object.getUsername().toString().toLowerCase().contains(filterSeq)) {
                        filter.add(object);
                    }
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            ArrayList<T> filtered = (ArrayList<T>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((Profile) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }

}
