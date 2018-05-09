package com.showbuddy4.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.showbuddy4.R;
import com.showbuddy4.adapter.Message_Adapter;
import com.showbuddy4.model.Model_Message;
import com.showbuddy4.preference.PreferenceApp;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class MessageActivity extends AppCompatActivity {
    public static final String TAG = MessageActivity.class.getSimpleName();
    ArrayList<Model_Message> arrayList = new ArrayList<>();
    ArrayList<Model_Message> multiselect_list = new ArrayList<>();

    Model_Message modelMessage;

    RecyclerView recyclerView;
    Message_Adapter message_adapter;
    private ImageView Imgemoji, img_back;
    private EmojiconEditText edtSearch;
    RelativeLayout parent;
    LinearLayoutManager layoutManager;


    PreferenceApp pref;

    public static String qbuserid;
    public static String qbuserlogin;
    public static String qbpass;
    static final String APP_ID = "69154";
    static final String AUTH_KEY = "m67pYjJpq9QO3wG";
    static final String AUTH_SECRET = "DM62rUpKZF39uf9";
    static final String ACCOUNT_KEY = "UuvWmpLzoba5UV_A21vj";


    public static final String PROPERTY_OCCUPANTS_IDS = "occupants_ids";
    public static final String PROPERTY_DIALOG_TYPE = "dialog_type";
    public static final String PROPERTY_DIALOG_NAME = "dialog_name";
    public static final String PROPERTY_NOTIFICATION_TYPE = "notification_type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_message);
        pref = new PreferenceApp(this);


        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        QBChatService.setDebugEnabled(true); // enable chat logging

        QBChatService.setDefaultPacketReplyTimeout(10000);

        QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
        chatServiceConfigurationBuilder.setSocketTimeout(60); //Sets chat socket's read timeout in seconds
        chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
        chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
        QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);


        QBSession();
        ConnectionListener();


        ArrayList<Integer> occupantIdsList = new ArrayList<Integer>();
        occupantIdsList.add(Integer.parseInt(qbuserid));


        QBChatDialog dialog = new QBChatDialog();
        dialog.setName("Chat with Garry and John");
        dialog.setPhoto("1786");
        dialog.setType(QBDialogType.PRIVATE);
        dialog.setOccupantsIds(occupantIdsList);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Imgemoji = (ImageView) findViewById(R.id.Imgemoji);
        img_back = (ImageView) findViewById(R.id.img_back);
        edtSearch = (EmojiconEditText) findViewById(R.id.edt_search);
        parent = (RelativeLayout) findViewById(R.id.touch);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Imgemoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmojIconActions emojIcon = new EmojIconActions(MessageActivity.this, parent, edtSearch, Imgemoji);
                emojIcon.ShowEmojIcon();
            }
        });
        for (int i = 0; i < 5; i++) {
            modelMessage = new Model_Message();
            modelMessage.setName("John Woodman");
            modelMessage.setMessage("Hey How Are you ? call me whenever you");
            modelMessage.setTime("12:30");
            modelMessage.setType(1);
            arrayList.add(modelMessage);
        }
        for (int i = 0; i < 5; i++) {
            modelMessage = new Model_Message();
            modelMessage.setName("John Woodman");
            modelMessage.setMessage("Hey How Are you ? call me whenever you");
            modelMessage.setTime("12:30");
            modelMessage.setType(0);
            arrayList.add(modelMessage);
        }
        layoutManager = new LinearLayoutManager(MessageActivity.this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        message_adapter = new Message_Adapter(arrayList, multiselect_list, MessageActivity.this);
        recyclerView.setAdapter(message_adapter);
        recyclerView.smoothScrollToPosition(0);
    }


    public void ConnectionListener() {

        ConnectionListener connectionListener = new ConnectionListener() {
            @Override
            public void connected(XMPPConnection connection) {

            }

            @Override
            public void authenticated(XMPPConnection xmppConnection, boolean b) {

            }


            @Override
            public void connectionClosed() {

            }

            @Override
            public void connectionClosedOnError(Exception e) {
                // connection closed on error. It will be established soon
            }

            @Override
            public void reconnectingIn(int seconds) {

            }

            @Override
            public void reconnectionSuccessful() {

            }

            @Override
            public void reconnectionFailed(Exception e) {

            }
        };

        QBChatService.getInstance().addConnectionListener(connectionListener);

    }


    public void QBSession() {

        final QBChatService chatService = QBChatService.getInstance();


        final QBUser user = new QBUser(pref.getQbuserid(), pref.getQbpass());


        QBAuth.createSession(user).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success, login to chat

                user.setId(session.getUserId());

                chatService.login(user, new QBEntityCallback() {

                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        Log.e(TAG, "onSuccess: " + user.getId());
                        createDiaalog();
                    }

                    @Override
                    public void onError(QBResponseException errors) {

                    }
                });
            }

            @Override
            public void onError(QBResponseException errors) {

            }
        });

    }

    public void createDiaalog() {
        ArrayList<Integer> occupantIdsList = new ArrayList<Integer>();
        occupantIdsList.add(Integer.parseInt(qbuserid));

        QBChatDialog dialog = new QBChatDialog();
        dialog.setName(qbuserlogin);
        dialog.setPhoto("1786");
        dialog.setType(QBDialogType.PRIVATE);
        dialog.setOccupantsIds(occupantIdsList);
        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {

                result.initForChat(QBChatService.getInstance());

            }

            @Override
            public void onError(QBResponseException responseException) {

            }
        });


    }

    public void Logoutsession() {

        final QBChatService chatService = QBChatService.getInstance();
        boolean isLoggedIn = chatService.isLoggedIn();
        if (!isLoggedIn) {
            return;
        }

        chatService.logout(new QBEntityCallback() {

            @Override
            public void onSuccess(Object o, Bundle bundle) {
                chatService.destroy();
            }

            @Override
            public void onError(QBResponseException errors) {

            }
        });
    }
}
