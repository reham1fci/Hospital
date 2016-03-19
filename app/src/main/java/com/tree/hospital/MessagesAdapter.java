package com.tree.hospital;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by thy on 29/02/2016.
 */
public class MessagesAdapter extends ArrayAdapter<message> {
    MessagesAdapter(ArrayList<message> messages,Context context) {
        super(context ,R.layout.message_item, R.id.message, messages);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        message message = getItem(position);

        TextView nameView = (TextView) convertView.findViewById(R.id.message);
        nameView.setText(message.getmText());

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) nameView.getLayoutParams();
        SharedPreferences pref =getContext().getSharedPreferences(webServices.SHARED, 0);
        String sender_name = pref.getString("user_name","");



        int sdk = Build.VERSION.SDK_INT;
        if (!message.getmSender().equals(sender_name)) {
            if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                nameView.setBackgroundResource(R.drawable.bubble_right_green);
            } else {
                nameView.setBackgroundResource(R.drawable.bubble_right_green);
            }
            layoutParams.gravity = Gravity.RIGHT;
        } else {
            if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                nameView.setBackgroundResource(R.drawable.bubble_left_gray);
            } else {
                nameView.setBackgroundResource(R.drawable.bubble_left_gray);
            }
            layoutParams.gravity = Gravity.LEFT;
        }

        nameView.setLayoutParams(layoutParams);


        return convertView;
    }


}
