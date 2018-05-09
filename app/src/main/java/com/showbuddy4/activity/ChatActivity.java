package com.showbuddy4.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.showbuddy4.R;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.quickchatdialog.Common;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by APPESPERTA-4 on 06/04/2018.
 */

public class ChatActivity extends AppCompatActivity {
    public static final String TAG = MessageActivity.class.getSimpleName();
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
        setContentView(R.layout.chat);
        pref = new PreferenceApp(this);





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


    public void createDiaalog() {
       /* ArrayList<Integer> occupantIdsList = new ArrayList<Integer>();
        occupantIdsList.add(44771703);
        occupantIdsList.add(Integer.parseInt(qbuserid));
        Log.e(TAG, "createDiaalog: qbuserid = " + qbuserid );*/
        Log.e(TAG, "createDiaalog:qbuserid  "+qbuserid );
        QBChatDialog dialog = DialogUtils.buildPrivateDialog(Integer.parseInt(qbuserid));
        // dialog.setUserId(Integer.parseInt(pref.getQbuserid()));

/*        QBChatDialog dialog = new QBChatDialog();
        dialog.setName("Chat with Garry and John");
        dialog.setPhoto("1786");
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdsList);*/

//or just use DialogUtils
//for creating PRIVATE dialog
//QBChatDialog dialog = DialogUtils.buildPrivateDialog(recipientId);


//for creating GROUP dialog
        // QBChatDialog dialog = DialogUtils.buildDialog("xyz", QBDialogType.GROUP, occupantIdsList);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {
                Log.e(TAG, "onSuccess:  Dialog Sender Id: " + result.getUserId() +" Rec Id: "+ result.getRecipientId());
                // result.initForChat(QBChatService.getInstance());
                // result.initForChat(result.getDialogId(), QBDialogType.PRIVATE, QBChatService.getInstance());
                //  QBChatDialog qbChatDialog = (QBChatDialog) listchatdialog.getAdapter().getItem(position);
                Intent intent = new Intent(ChatActivity.this, ChatMessageActivity.class);
                intent.putExtra(Common.DIALOG_EXTRA, result);
                // intent.putExtra("recname", qbChatDialog);
                startActivity(intent);
            }

            @Override
            public void onError(QBResponseException responseException) {
                Log.e(TAG, "onError:  Dialog" + responseException.getLocalizedMessage());
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
