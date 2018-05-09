package com.showbuddy4.quickchatdialog;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;
import com.showbuddy4.R;
import com.showbuddy4.activity.ProfileViewActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SAHIL on 4/1/2017.
 */

public class ChatMessageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBChatMessage> qbChatMessages;

    public ChatMessageAdapter(Context context, ArrayList<QBChatMessage> qbChatMessages) {
        this.context = context;
        this.qbChatMessages = qbChatMessages;
    }

    @Override
    public int getCount() {
        return qbChatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            if (qbChatMessages.get(position).getSenderId().equals(QBChatService.getInstance().getUser().getId())) {
                view = inflater.inflate(R.layout.row_sender_msg, null);
                TextView bubbleTextView = (TextView) view.findViewById(R.id.txt_sender_msg);
                TextView txt_sender_time = (TextView) view.findViewById(R.id.txt_sender_time);
                txt_sender_time.setText(getDate(qbChatMessages.get(position).getDateSent(), "dd/MM/yyyy hh:mm:ss.SSS"));
                bubbleTextView.setText(qbChatMessages.get(position).getBody());


            } else {
                view = inflater.inflate(R.layout.row_receiver_msg, null);
                TextView bubbleTextView = (TextView) view.findViewById(R.id.txt_recei_msg);
                TextView txt_recei_time = (TextView) view.findViewById(R.id.txt_recei_time);
                bubbleTextView.setText(qbChatMessages.get(position).getBody());
                CircleImageView imageRecever = (CircleImageView) view.findViewById(R.id.img_profile);
                setReceverImage(imageRecever,qbChatMessages.get(position));
                txt_recei_time.setText(getDate(qbChatMessages.get(position).getDateSent(), "dd/MM/yyyy hh:mm:ss.SSS"));
                imageRecever.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("getProID",qbChatMessages.get(position).getRecipientId()+"<<");
                        //CommonUses.showToast(getActivity(), getItem(position).getProfile_id());
                        String profile = "profile";
                        String id = String.valueOf(qbChatMessages.get(position).getRecipientId());
                        context.startActivity(new Intent(context,
                                ProfileViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id", id).putExtra("profileview", profile));
                    }
                });
            }
        }
        return view;
    }

    private void setReceverImage(CircleImageView imageRecever, QBChatMessage qbChatMessage) {
        //if(qbChatMessage.get)
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Date d = new Date();
        d.setTime(milliSeconds);
        // Create a calendar object that will convert the date and time value in milliseconds to date.

        return formatter.format(d);
    }
}

