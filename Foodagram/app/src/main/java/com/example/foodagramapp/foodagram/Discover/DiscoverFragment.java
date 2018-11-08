package com.example.foodagramapp.foodagram.Discover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodagramapp.foodagram.R;

public class DiscoverFragment extends Fragment {
    private RecyclerView recyclerView;
    private  Bitmap[] bitmaps;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bitmaps = setUpBitmaps();
        recyclerView = (RecyclerView) getView().findViewById(R.id.discoverRecycleViewMian);
        recyclerView.setAdapter(new DiscoverFragment.GridLayoutAdapter(bitmaps));
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    //
    private Bitmap[] setUpBitmaps() {
        Bitmap[] bitmaps = new Bitmap[5];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.img_1);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.img_2);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.img_3);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.img_4);
        bitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.img_5);
        return bitmaps;
    }

    public class GridLayoutAdapter extends RecyclerView.Adapter<DiscoverFragment.GridHolder>{
        private Bitmap[] bitmaps;
        public GridLayoutAdapter(Bitmap[] bitmaps) {
            this.bitmaps = bitmaps;
        }

        @Override
        public GridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.discover_card_view, parent, false);
            return new GridHolder(view);
        }

        @Override
        public void onBindViewHolder(GridHolder holder, int position) {
            holder.imageView.requestLayout();
            holder.imageView.setImageBitmap(bitmaps[position]);
            holder.textView.setText("Pongpanot Na Ubon");
        }

        @Override
        public int getItemCount() {
            return bitmaps.length;
        }
    }

    private class GridHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public GridHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.discover_thumbnail);
            textView = (TextView) itemView.findViewById(R.id.discover_user_name);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }
}
