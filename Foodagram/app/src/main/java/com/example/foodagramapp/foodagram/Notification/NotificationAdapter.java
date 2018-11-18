package com.example.foodagramapp.foodagram.Notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.foodagramapp.foodagram.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    List<Notification> notifications = new ArrayList<Notification>();
    Context context;

    public NotificationAdapter(@NonNull Context context, int resource, List<Notification> objects) {
        super(context, resource, objects);
        this.notifications = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View notificationItem = LayoutInflater.from(context).inflate(R.layout.fragment_notification_item, parent, false);

        ImageView imgProfile = notificationItem.findViewById(R.id.notification_img_profile);
        TextView type = notificationItem.findViewById(R.id.notification_type);
        TextView content = notificationItem.findViewById(R.id.notification_content);
        TextView time = notificationItem.findViewById(R.id.notification_time);
        TextView username = notificationItem.findViewById(R.id.notification_username);

        Notification row = notifications.get(position);

        Picasso.get().load(row.getPathImg()).into(imgProfile);
        username.setText(row.getFrom() + "");
        setTypeOfPost(row.getType(), time, type);
        time.setText(getCountOfDays(row.getTimestamp()) + "");
        if(setContentLimit(row.getContent()).equals("")){
            content.setText("");
        }else{
            content.setText('"' + setContentLimit(row.getContent()) + '"');
        }

        return notificationItem;
    }

    String getCountOfDays(long time) {
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

    void setTypeOfPost(String typeInput, TextView time, TextView type){
        switch (typeInput){
            case "post":
                time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.notification_ic_post, 0, 0, 0);
                type.setText("ได้โพสต์เมนูใหม่");
                break;
            case "comment":
                time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.notification_ic_comment, 0, 0, 0);
                type.setText("ได้แสดงความคิดเห็นต่อโพสต์ของคุณ");
                break;
            case "follow":
                time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.notification_ic_follow, 0, 0, 0);
                type.setText("ได้เริ่มติดตามคุณ");
                break;
            case "like":
                time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.notification_ic_like, 0, 0, 0);
                type.setText("ได้ถูกใจโพสต์ของคุณ");

                default:
                    break;
        }
    }

    String setContentLimit(String content){
        if (content.length() > 40) {
            content = content.substring(0, 40) + "...";
        }else if(content.equals("-")){
            content = "";
        }
        return content;
    }
}
