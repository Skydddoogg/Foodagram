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
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.R;
import com.example.foodagramapp.foodagram.User;


import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends ArrayAdapter<User> {
    private Context context;
    private ArrayList<User> userList;
    private TextView firstName;
    private Filter filter;

    public SearchAdapter(@NonNull Context context, int resource, ArrayList<User> list) {
        super(context, resource, list);
        this.context = context;
        this.userList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItems = LayoutInflater.from(context).inflate(
                R.layout.search_adapter,
                parent,
                false
        );
        firstName = (TextView) listItems.findViewById(R.id.user_first_name_searchAdapter);
        firstName.setText(userList.get(position).getName());
        return listItems;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<User>(userList);
        return filter;
    }

    private class AppFilter<T> extends Filter {

        private ArrayList<T> sourceObjects;

        public AppFilter(List<T> objects) {
            sourceObjects = new ArrayList<T>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<T> filter = new ArrayList<T>();

                for (T object : sourceObjects) {
                    // the filtering itself:
                    if (object.toString().toLowerCase().contains(filterSeq))
                        filter.add(object);
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
            if(results.values != null) {
                for (int i = 0, l = filtered.size(); i < l; i++)
                    add((User) filtered.get(i));
                notifyDataSetInvalidated();
            }
        }
    }


}
