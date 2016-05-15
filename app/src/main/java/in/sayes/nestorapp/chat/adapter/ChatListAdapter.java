package in.sayes.nestorapp.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import in.sayes.nestorapp.R;
import in.sayes.nestorapp.chat.helper.MessageEntity;
import in.sayes.nestorapp.chat.helper.UserType;

/**
 * Created by sourav on 27/04/16.
 * Project : NesterApp , Package Name : in.sayes.nestorapp.chat.adapter
 * Copyright : Sourav Bhattacharya eMail: sav.accharya@gmail.com
 */


public class ChatListAdapter extends BaseAdapter {

    private ArrayList<MessageEntity> chatMessages;
    private Context context;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");

    public ChatListAdapter(ArrayList<MessageEntity> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;

    }


    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        MessageEntity message = chatMessages.get(position);
        ViewHolder1 holder1;
        ViewHolder2 holder2;

        if (message.getUserType() == UserType.SELF) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_row_user_text, null, false);
                holder1 = new ViewHolder1();


                holder1.messageTextView = (TextView) v.findViewById(R.id.message_text);
                holder1.timeTextView = (TextView) v.findViewById(R.id.time_text);

                v.setTag(holder1);
            } else {
                v = convertView;
                holder1 = (ViewHolder1) v.getTag();
            }

            holder1.messageTextView.setText(message.getMsgText());
            holder1.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));



        } else if (message.getUserType() == UserType.NESTOR) {

            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_row_nestor, null, false);

                holder2 = new ViewHolder2();


                holder2.messageTextView = (TextView) v.findViewById(R.id.message_text);
                holder2.timeTextView = (TextView) v.findViewById(R.id.time_text);
                holder2.messageStatus = (ImageView) v.findViewById(R.id.user_reply_status);
                v.setTag(holder2);

            } else {
                v = convertView;
                holder2 = (ViewHolder2) v.getTag();

            }
             holder2.messageTextView.setText(message.getMsgText());
             holder2.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));




        }


        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        MessageEntity message = chatMessages.get(position);
        return message.getUserType().ordinal();
    }

    private class ViewHolder1 {
        public TextView messageTextView;
        public TextView timeTextView;


    }

    private class ViewHolder2 {
        public ImageView messageStatus;
        public TextView messageTextView;
        public TextView timeTextView;

    }
}
