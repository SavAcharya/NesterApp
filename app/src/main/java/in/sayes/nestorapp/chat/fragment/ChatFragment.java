package in.sayes.nestorapp.chat.fragment;


import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import in.sayes.android.customui.dialogplus.DialogPlus;
import in.sayes.android.customui.dialogplus.GridHolder;
import in.sayes.android.customui.dialogplus.Holder;
import in.sayes.android.customui.dialogplus.ListHolder;
import in.sayes.android.customui.dialogplus.OnCancelListener;
import in.sayes.android.customui.dialogplus.OnClickListener;
import in.sayes.android.customui.dialogplus.OnDismissListener;
import in.sayes.android.customui.dialogplus.OnItemClickListener;
import in.sayes.nestorapp.NestorApplication;
import in.sayes.nestorapp.R;
import in.sayes.nestorapp.base.fragment.BaseFragment;
import in.sayes.nestorapp.chat.adapter.ChatListAdapter;
import in.sayes.nestorapp.chat.helper.AndroidUtilities;
import in.sayes.nestorapp.chat.helper.MessageEntity;
import in.sayes.nestorapp.chat.helper.NotificationCenter;
import in.sayes.nestorapp.chat.helper.Status;
import in.sayes.nestorapp.chat.helper.UserType;
import in.sayes.nestorapp.chat.helper.model.GSON_RootLevel;
import in.sayes.nestorapp.chat.helper.model.InputFromType;
import in.sayes.nestorapp.chat.widgets.DialogSimpleAdapter;
import in.sayes.nestorapp.chat.widgets.SizeNotifierRelativeLayout;
import in.sayes.nestorapp.storage.AppPreferences;
import in.sayes.nestorapp.storage.IPreferenceConstants;


public class ChatFragment extends BaseFragment implements SizeNotifierRelativeLayout.SizeNotifierRelativeLayoutDelegate, NotificationCenter.NotificationCenterDelegate {


    Button footerCloseButton;
    Button footerConfirmButton;

    private ListView chatListView;
    private EditText chatEditText1;
    private ArrayList<MessageEntity> chatMessages;
    private ImageView enterChatView1, emojiButton;
    private ChatListAdapter listAdapter;
    Gson gson = new Gson();
    GSON_RootLevel mNestorReply;
    private SizeNotifierRelativeLayout sizeNotifierRelativeLayout;

    private int keyboardHeight;
    private boolean keyboardVisible;
    private WindowManager.LayoutParams windowLayoutParams;
    private AppPreferences appPreferences;
    private List<GSON_RootLevel.QuestionsEntity> nestorQustions;
    private List<String> nestorStatements;
    private long MESSAGE_DELAY = 2000;
DialogPlus dialog;
    private GSON_RootLevel.QuestionsEntity currentQustion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        AndroidUtilities.statusBarHeight = getStatusBarHeight();

        chatMessages = new ArrayList<>();

        chatListView = (ListView) root.findViewById(R.id.chat_list_view);

        chatEditText1 = (EditText) root.findViewById(R.id.chat_edit_text1);
        enterChatView1 = (ImageView) root.findViewById(R.id.send_chat_text);

        listAdapter = new ChatListAdapter(chatMessages, getActivity());

        chatListView.setAdapter(listAdapter);

        chatEditText1.setOnKeyListener(keyListener);

        enterChatView1.setOnClickListener(clickListener);

        chatEditText1.addTextChangedListener(watcher1);

        sizeNotifierRelativeLayout = (SizeNotifierRelativeLayout) root.findViewById(R.id.chat_layout);
        sizeNotifierRelativeLayout.delegate = this;
        appPreferences = new AppPreferences(getActivity());
        // get signup data and create Nestor reply object
        mNestorReply = gson.fromJson(appPreferences.getString(IPreferenceConstants.PREF_SIGNUP_REPLY, ""), GSON_RootLevel.class);

        // Start chat with initial conversation for first time.


        nestorQustions = mNestorReply.getQuestions();

        for (int qustionsCoount = 0; qustionsCoount < nestorQustions.size(); qustionsCoount++) {
            currentQustion=nestorQustions.get(qustionsCoount);
            printStatement(currentQustion);


            //setInputFrom(nestorQustions.get(qustionsCoount),currentQustion);
        }


        return root;
    }

    private void printStatement(GSON_RootLevel.QuestionsEntity currentQustion) {

        nestorStatements = currentQustion.getStatements();

        for (int statementCount = 0; statementCount < nestorStatements.size(); statementCount++) {

            showNestorReply(nestorStatements.get(statementCount));


        }
    }


    private void setInputFrom(GSON_RootLevel.QuestionsEntity questionsEntity) {


        String inputFromType = questionsEntity.getInput_form().getType();

        List<GSON_RootLevel.OptionType> inputs = questionsEntity.getOptions();

        boolean isGrid;
        Holder holder;

        switch (inputFromType) {
            case InputFromType.CASCADE_CARDS:
                holder = new ListHolder();
                isGrid = false;

                showDialog(holder, isGrid, Gravity.BOTTOM, true, true, true, inputs);
                break;

            case InputFromType.CUSTOM_KEYBOARD:
                holder = new ListHolder();
                isGrid = false;

                showDialog(holder, isGrid, Gravity.BOTTOM, true, true, true, inputs);
                break;

            case InputFromType.FLOTING:

                break;

            case InputFromType.NUMERIC_KEYBOARD:
                chatEditText1.setVisibility(View.VISIBLE);
                chatEditText1.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;

            case InputFromType.SELECT_VAL:

                break;

            case InputFromType.GRID:
                holder = new GridHolder(2);
                isGrid = true;


                break;

            default:

                break;
        }


    }

    private void showNestorReply(final String mMessage) {
        Log.i("Nestor Message", mMessage);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final MessageEntity message = new MessageEntity();
                message.setMsgStatus(Status.SENT);
                message.setMsgText(mMessage);
                message.setUserType(UserType.NESTOR);
                message.setMessageTime(new Date().getTime());
                chatMessages.add(message);

                listAdapter.notifyDataSetChanged();

                ;
            }
        }, MESSAGE_DELAY);


    }


    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press

                EditText editText = (EditText) v;

                if (v == chatEditText1) {
                    // sendMessage(editText.getText().toString(), UserType.NESTOR);
                }

                chatEditText1.setText("");

                return true;
            }
            return false;

        }
    };

    private ImageView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == enterChatView1) {
                // sendMessage(chatEditText1.getText().toString(), UserType.NESTOR);
            }

            chatEditText1.setText("");

        }
    };

    private final TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (chatEditText1.getText().toString().equals("")) {

            } else {
                enterChatView1.setImageResource(R.drawable.ic_chat_send);

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                enterChatView1.setImageResource(R.drawable.ic_chat_send);
            } else {
                enterChatView1.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };


    @Override
    protected void getDataFromBundle() {

    }


    private void sendMessage(final String messageText, final UserType userType) {
        if (messageText.trim().length() == 0)
            return;

        final MessageEntity message = new MessageEntity();
        message.setMsgStatus(Status.SENT);
        message.setMsgText(messageText);
        message.setUserType(userType);
        message.setMessageTime(new Date().getTime());
        chatMessages.add(message);

        if (listAdapter != null)
            listAdapter.notifyDataSetChanged();

        // Mark message as delivered after one second

        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

        exec.schedule(new Runnable() {
            @Override
            public void run() {
                message.setMsgStatus(Status.DELIVERED);

                final MessageEntity message = new MessageEntity();
                message.setMsgStatus(Status.SENT);
                message.setMsgText(messageText);
                message.setUserType(UserType.NESTOR);
                message.setMessageTime(new Date().getTime());
                chatMessages.add(message);

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        listAdapter.notifyDataSetChanged();
                    }
                });


            }
        }, 2, TimeUnit.SECONDS);

    }


    @Override
    public void onSizeChanged(int height) {

        Rect localRect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);

        WindowManager wm = (WindowManager) NestorApplication.getInstance().getSystemService(Activity.WINDOW_SERVICE);
        if (wm == null || wm.getDefaultDisplay() == null) {
            return;
        }


        if (height > AndroidUtilities.dp(50) && keyboardVisible) {
            keyboardHeight = height;
            NestorApplication.getInstance().getSharedPreferences("emoji", 0).edit().putInt("kbd_height", keyboardHeight).commit();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    /**
     * Get the system status bar height
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public void didReceivedNotification(int id, Object... args) {

    }


    private void showDialog(Holder holder, boolean isGrid, int gravity, boolean showHeader, boolean showFooter, boolean expanded, List<GSON_RootLevel.OptionType> options) {


        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                       switch (view.getId()) {
                //          case R.id.header_container:
                //            Toast.makeText(MainActivity.this, "Header clicked", Toast.LENGTH_LONG).show();
                //            break;
                //          case R.id.like_it_button:
                //            Toast.makeText(MainActivity.this, "We're glad that you like it", Toast.LENGTH_LONG).show();
                //            break;
                //          case R.id.love_it_button:
                //            Toast.makeText(MainActivity.this, "We're glad that you love it", Toast.LENGTH_LONG).show();
                //            break;
                          case R.id.footer_confirm_button:
                           Toast.makeText(getContext(), "Confirm button clicked", Toast.LENGTH_LONG).show();
                           break;
                       case R.id.footer_close_button:
                            dialog.dismiss();
                           break;
                        }
                       dialog.dismiss();
            }
        };

        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.text_view);
                String clickedAppName = textView.getText().toString();
                //       dialog.dismiss();
                       Toast.makeText(getContext(), clickedAppName + " clicked", Toast.LENGTH_LONG).show();
                handleInput(position);
            }
        };

        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogPlus dialog) {
                //        Toast.makeText(MainActivity.this, "dismiss listener invoked!", Toast.LENGTH_SHORT).show();
            }
        };

        OnCancelListener cancelListener = new OnCancelListener() {
            @Override
            public void onCancel(DialogPlus dialog) {
                //        Toast.makeText(MainActivity.this, "cancel listener invoked!", Toast.LENGTH_SHORT).show();
            }
        };

        DialogSimpleAdapter adapter = new DialogSimpleAdapter(getContext(), isGrid, options);
        if (showHeader && showFooter) {
            showCompleteDialog(holder, gravity, adapter, clickListener, itemClickListener, dismissListener, cancelListener,
                    expanded);
            return;
        }

        if (showHeader && !showFooter) {
            showNoFooterDialog(holder, gravity, adapter, clickListener, itemClickListener, dismissListener, cancelListener,
                    expanded);
            return;
        }

        if (!showHeader && showFooter) {
            showNoHeaderDialog(holder, gravity, adapter, clickListener, itemClickListener, dismissListener, cancelListener,
                    expanded);
            return;
        }

        showOnlyContentDialog(holder, gravity, adapter, itemClickListener, dismissListener, cancelListener, expanded);
    }

    private void handleInput() {


    }


    private void showCompleteDialog(Holder holder, int gravity, BaseAdapter adapter,
                                    OnClickListener clickListener, OnItemClickListener itemClickListener,
                                    OnDismissListener dismissListener, OnCancelListener cancelListener,
                                    boolean expanded) {
        final DialogPlus dialog = DialogPlus.newDialog(getContext())
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_header)
                .setFooter(R.layout.dialog_footer)
                .setCancelable(true)
                .setGravity(gravity)
                .setAdapter(adapter)
                .setOnClickListener(clickListener)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Log.d("DialogPlus", "onItemClick() called with: " + "item = [" +
                                item + "], position = [" + position + "]");
                    }
                })
                .setOnDismissListener(dismissListener)
                .setExpanded(expanded)
//        .setContentWidth(800)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnCancelListener(cancelListener)
                .setOverlayBackgroundResource(android.R.color.transparent)
//        .setContentBackgroundResource(R.drawable.corner_background)
                        //                .setOutMostMargin(0, 100, 0, 0)
                .create();
        dialog.show();



    }

    private void showNoFooterDialog(Holder holder, int gravity, BaseAdapter adapter,
                                    OnClickListener clickListener, OnItemClickListener itemClickListener,
                                    OnDismissListener dismissListener, OnCancelListener cancelListener,
                                    boolean expanded) {
         dialog = DialogPlus.newDialog(getContext())
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_header)
                .setCancelable(true)
                .setGravity(gravity)
                .setAdapter(adapter)
                .setOnClickListener(clickListener)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Log.d("DialogPlus", "onItemClick() called with: " + "item = [" +
                                item + "], position = [" + position + "]");
                    }
                })
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setExpanded(expanded)
                .create();
        dialog.show();
    }

    private void showNoHeaderDialog(Holder holder, int gravity, BaseAdapter adapter,
                                    OnClickListener clickListener, OnItemClickListener itemClickListener,
                                    OnDismissListener dismissListener, OnCancelListener cancelListener,
                                    boolean expanded) {
         dialog = DialogPlus.newDialog(getContext())
                .setContentHolder(holder)
                .setFooter(R.layout.dialog_footer)
                .setCancelable(true)
                .setGravity(gravity)
                .setAdapter(adapter)
                .setOnClickListener(clickListener)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Log.d("DialogPlus", "onItemClick() called with: " + "item = [" +
                                item + "], position = [" + position + "]");
                    }
                })
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setExpanded(expanded)
                .create();
        dialog.show();



    }

    private void showOnlyContentDialog(Holder holder, int gravity, BaseAdapter adapter,
                                       OnItemClickListener itemClickListener, OnDismissListener dismissListener,
                                       OnCancelListener cancelListener, boolean expanded) {
        final DialogPlus dialog = DialogPlus.newDialog(getContext())
                .setContentHolder(holder)
                .setGravity(gravity)
                .setAdapter(adapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Log.d("DialogPlus", "onItemClick() called with: " + "item = [" +
                                item + "], position = [" + position + "]");
                    }
                })
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setExpanded(expanded)
                .setCancelable(true)
                .create();
        dialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}



