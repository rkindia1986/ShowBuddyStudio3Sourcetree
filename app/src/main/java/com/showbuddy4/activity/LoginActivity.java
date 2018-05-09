package com.showbuddy4.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.showbuddy4.MainActivity;
import com.showbuddy4.R;
import com.showbuddy4.Utils.CommonUses;
import com.showbuddy4.Utils.JSONParser;
import com.showbuddy4.helper.SessionManager;
import com.showbuddy4.model.LoginModel;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    String TAG = "LoginActivity";
    private GoogleApiClient mGoogleApiClient;

    // Instance of Facebook Class
    LoginButton loginButton;
    Button signinButton;
    CallbackManager callbackManager;
    CardView cardview_login_fb;
    TextView tv_terms;
    SessionManager sessionManager;
    PreferenceApp pref;
    ProgressDialog progressDialog;
    String fbprofile1, fbprofile2, fbprofile3, fbprofile4, fbprofile5, fbprofile6;
    String email_id, f_name, l_name, gender, fbid, LatLocation,ageRange;

    static final String APP_ID = "69154";
    static final String AUTH_KEY = "m67pYjJpq9QO3wG";
    static final String AUTH_SECRET = "DM62rUpKZF39uf9";
    static final String ACCOUNT_KEY = "UuvWmpLzoba5UV_A21vj";
    String birthday, strlocation, str_location, location;
    JSONParser jsonp1 = new JSONParser();
    AccessToken mAccessToken;
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();

        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("in", "LOGIN");
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(this);
        //generate key hash for facebook
        generateKeyHashFacebook();
// Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        pref = new PreferenceApp(this);
        sessionManager = new SessionManager(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_fb);
        signinButton = (Button) findViewById(R.id.btnGetPremium);

        cardview_login_fb = (CardView) findViewById(R.id.cardview_login_fb);
        tv_terms = (TextView) findViewById(R.id.tv_terms);

        tv_terms.setMovementMethod(LinkMovementMethod.getInstance());

        //fb permission
        loginButton.setReadPermissions(Arrays.asList("user_photos,email,public_profile,user_birthday,user_friends,user_location"));/*,user_birthday,user_location*/
//        loginButton.setReadPermissions(Arrays.asList("user_photos,email,public_profile,user_birthday,user_education_history,user_friends,user_location,user_work_history"));/*,user_birthday,user_location*/

        loginButton.requestFocus();


        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        QBChatService.setDebugEnabled(true); // enable chat logging

        QBChatService.setDefaultPacketReplyTimeout(10000);

        QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
        chatServiceConfigurationBuilder.setSocketTimeout(60); //Sets chat socket's read timeout in seconds
        chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
        chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
        QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);
        ConnectionListener();

       /* QBSession();

*/

        //login button callback
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Bundle parameters = new Bundle();

                mAccessToken=loginResult.getAccessToken();
                Log.e("IN Acess-TOKEN",mAccessToken+"***************************-------------------------------------------");
                parameters.putString("fields", "id,email,first_name,last_name,gender,age_range,education,work,friends,picture,birthday,location");/*,birthday,location*/



                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Log.e("Response", response.toString() + "<");
                                    Log.e("object", object.toString() + "<");
                                    fbid = response.getJSONObject().getString("id");
                                    if (response.getJSONObject().has("email")) {
                                        email_id = response.getJSONObject().getString("email");
                                    }
                                    f_name = response.getJSONObject().getString("first_name");
                                    l_name = response.getJSONObject().getString("last_name");
                                    gender = response.getJSONObject().getString("gender");
                                    Log.e("FB first name",f_name+"<");
                               if(response.getJSONObject().has("birthday")){
                                        birthday = response.getJSONObject().getString("birthday");
                                    }

//                                    location = response.getJSONObject().getString("location");
                                    ageRange=response.getJSONObject().getString("age_range");

//                                    Log.e("work",response.getJSONObject().getString("work")+"<<");
                                    //  Log.e("education",response.getJSONObject().getString("education")+"<<");

                                    try {
                                        if(object.has("location")){
                                            JSONObject jsonobject_location = object.getJSONObject("location");
                                            str_location = jsonobject_location.getString("name");
                                            Log.e("jsonobject_location", jsonobject_location.toString() + "<<");
                                            Log.e("str_location", str_location + "<<");
                                        }


                                    } catch (Exception e) {
                                        str_location = "";
                                        e.printStackTrace();
                                    }
                                    Log.e("birthday", birthday + "<<");

                                  /*  JSONObject location_object = new JSONObject(response.getJSONObject().getString("location"));

                                    String location_name = location_object.getString("name");
                                    String location_id = location_object.getString("id");*/

                                    final URL profile_pic = new URL("https://graph.facebook.com/" + fbid + "/picture?width=200&height=150");
                                    Log.i("profile_pic", profile_pic + "");

                                    fbprofile1 = profile_pic + "";

                                    String dob = "";
                                   /* SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                    SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    Date newDate = null;
                                    try {
                                        newDate = format.parse(birthday);
                                        dob = newformat.format(newDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    String day = (String) DateFormat.format("dd", newDate); // 20
                                    String monthNumber = (String) DateFormat.format("MM", newDate); // 06
                                    String year = (String) DateFormat.format("yyyy", newDate); // 2013*/

                                    pref.setFbId(fbid);

                                    new GraphRequest(

                                            AccessToken.getCurrentAccessToken(),  //your fb AccessToken
                                            "/" + AccessToken.getCurrentAccessToken().getUserId() + "/albums",//user id of login user
                                            null,
                                            HttpMethod.GET,
                                            new GraphRequest.Callback() {
                                                public void onCompleted(GraphResponse response) {
                                                    Log.e("TAG", "Facebook Albums: " + response.toString());
                                                    try {
                                                        if (response.getError() == null) {
                                                            JSONObject joMain = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                                            if (joMain.has("data")) {
                                                                JSONArray jaData = joMain.optJSONArray("data"); //find JSONArray from JSONObject
                                                                if (jaData.length() > 0) {
                                                                    for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                                                        final JSONObject joAlbum = jaData.getJSONObject(i);
                                                                        String profile_pictures = joAlbum.optString("name");
                                                                        if (profile_pictures.equalsIgnoreCase("Profile Pictures")) {
                                                                            GetFacebookImages(joAlbum.optString("id"), i, jaData.length());//find Album ID and get All Images from album
                                                                        }
                                                                    }
                                                                } else {
                                                                    RegisterUser(fbid, fbprofile1 + "", fbprofile2, fbprofile3,
                                                                            fbprofile4, fbprofile5, fbprofile6, f_name + " " + l_name,
                                                                            email_id, "", "", f_name, l_name, "", gender, "",
                                                                            "", "", "");
                                                                }
                                                            }
                                                        } else {
                                                            Log.d("Test", response.getError().toString());
                                                            RegisterUser(fbid, fbprofile1 + "", fbprofile2, fbprofile3,
                                                                    fbprofile4, fbprofile5, fbprofile6, f_name + " " + l_name,
                                                                    email_id, "", "", f_name, l_name, "", gender, "",
                                                                    "", "", "");
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        RegisterUser(fbid, fbprofile1 + "", fbprofile2, fbprofile3,
                                                                fbprofile4, fbprofile5, fbprofile6, f_name + " " + l_name,
                                                                email_id, "", "", f_name, l_name, "", gender, "",
                                                                "", "", "");
                                                    }
                                                }

                                            }
                                    ).executeAsync();


                                  /*  RegisterUser(id, profile_pic + "", "", "",
                                            "", "", "", f_name + " " + l_name,
                                            email_id, *//*location_name*//*"", dob, f_name, l_name, "", gender, "",
                                            *//*getAge(Integer.parseInt(year), Integer.parseInt(monthNumber), Integer.parseInt(day))*//*"",
                                            "", *//*location_name*//*"");*/
                                } catch (JSONException | MalformedURLException e) {
                                    e.printStackTrace();
                                    if(progressDialog!=null && progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }

                                }
                            }
                        });


                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("Cancel", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("Facebook exception", exception.toString());
            }
        });

        cardview_login_fb.setOnClickListener(this);

//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        finish();

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


        final QBUser user = new QBUser("showbuddy", "darsh123");


        QBAuth.createSession(user).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success, login to chat

                user.setId(session.getUserId());

                chatService.login(user, new QBEntityCallback() {

                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        Log.e(TAG, "onSuccess: " + user.getId());
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


    public void generateKeyHashFacebook() {
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Log.d("error", e.getLocalizedMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.cardview_login_fb:
                // do your code
                loginButton.performClick();
                break;

            default:
                break;
        }
    }

    public void RegisterUser(String fbid, String fbuserprofilephoto1,
                             String fbuserprofilephoto2, String fbuserprofilephoto3,
                             String fbuserprofilephoto4, String fbuserprofilephoto5,
                             String fbuserprofilephoto6, String profilename,
                             String useremail, String useraddress,
                             String dob, String firstname, String lastname,
                             String collegename, String gender, final String about, String age,
                             String profession, String location) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);
        Map<String, String> params = new HashMap<>();

        if(fbid!=null && fbid!=""){
            params.put("FbProfileId", fbid);
        }
        if(fbuserprofilephoto1!=null && fbuserprofilephoto1!=""){
            params.put("FbUserPhotoUrl1", fbuserprofilephoto1);

        }
        if(fbuserprofilephoto2!=null && fbuserprofilephoto2!=""){
            params.put("FbUserPhotoUrl2", fbuserprofilephoto2);
        }
        if(fbuserprofilephoto3!=null && fbuserprofilephoto3!=""){
            params.put("FbUserPhotoUrl3", fbuserprofilephoto3);
        }
        if(fbuserprofilephoto4!=null && fbuserprofilephoto4!=""){
            params.put("FbUserPhotoUrl4", fbuserprofilephoto4);
        }





if(fbuserprofilephoto5!=null && fbuserprofilephoto5!=""){
    params.put("FbUserPhotoUrl5", fbuserprofilephoto5);
}

        if(fbuserprofilephoto6!=null && fbuserprofilephoto6!=""){
            params.put("FbUserPhotoUrl6", fbuserprofilephoto6);

        }

        if(about!=null && about!=""){
            params.put("About", about);

        }
        if(profilename!=null && profilename!=""){
            params.put("ProfileName", profilename);

        }
        if(email_id!=null && email_id!=""){
            params.put("UserEmail", email_id);


        }
        if(useraddress!=null && useraddress!=""){
            params.put("UserAddress", useraddress);

        }

        if(birthday!=null && birthday!=""){
            params.put("UserDob", birthday);

        }
        if(firstname!=null && firstname!=""){
            params.put("FirstName", firstname);

        }
        if(lastname!=null && lastname!=""){
            params.put("LastName", lastname);

        }

        if(collegename!=null && collegename!=""){
            params.put("CollegeName", collegename);

        }








if(gender!=null && gender!=""){
    params.put("Gender", gender);
}




        if(birthday!=null && birthday!=""){
            params.put("Age", CommonUses.getAge(birthday));
        }else {
            if(ageRange!=null && ageRange!=""){
                params.put("Age", ageRange);

            }

        }

        if(profession!=null && profession!=""){
            params.put("Profession", profession);
        }

//        if(gender!=null && gender!=""){
//            params.put("Location", "");
//        }




//        new AsyncHttpTask().execute(fbid, fbuserprofilephoto1, fbuserprofilephoto2, fbuserprofilephoto3, fbuserprofilephoto4, fbuserprofilephoto5, fbuserprofilephoto6,
//                profilename, email_id, useraddress, birthday, firstname, lastname, collegename, gender, about, profession, str_location);

        Call<List<LoginModel>> call = apiService.registerUser(params);

//        Call<List<LoginModel>> call = apiService.registerUser(fbid, fbuserprofilephoto1, fbuserprofilephoto2,
//                fbuserprofilephoto3, fbuserprofilephoto4, fbuserprofilephoto5, fbuserprofilephoto6, profilename, useremail,
//                useraddress, "01/17/1991", firstname, lastname, collegename, gender, about, "26", profession, "Ahmedabad");

        call.enqueue(new Callback<List<LoginModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<LoginModel>> call, @NonNull Response<List<LoginModel>> response) {
                Log.e("RES",response.body().toString()+"<");

                List<LoginModel> singllist = response.body();
                String emailid = response.body().get(0).getUserEmail();
                String f_name = response.body().get(0).getFirstName();
                String l_name = response.body().get(0).getLastName();
                String gender = response.body().get(0).getGender();
                String dob = response.body().get(0).getUserDob();
                String location_name = response.body().get(0).getLocation();
                String profile_pic = response.body().get(0).getFbUserPhotoUrl1();
                String fbProfilePhoto2 = response.body().get(0).getFbUserPhotoUrl2();
                String fbProfilePhoto3 = response.body().get(0).getFbUserPhotoUrl3();
                String fbProfilePhoto4 = response.body().get(0).getFbUserPhotoUrl4();
                String relationStatus=response.body().get(0).getRelationshipStatus();
                String fbProfilePhoto5=null,fbProfilePhoto6=null,CollegeName=null,About=null,Profession=null,Age=null;
                if(response.body().get(0).getFbUserPhotoUrl5()!=null){
                    fbProfilePhoto5 = response.body().get(0).getFbUserPhotoUrl5();
                }

                if(response.body().get(0).getFbUserPhotoUrl6()!=null){
                     fbProfilePhoto6 = response.body().get(0).getFbUserPhotoUrl6();
                }
                if(response.body().get(0).getCollegeName()!=null){
                     CollegeName = response.body().get(0).getCollegeName();
                }

                  if(response.body().get(0).getAbout()!=null){
                       About = response.body().get(0).getAbout();
                }
               if( response.body().get(0).getProfession()!=null){
                    Profession = response.body().get(0).getProfession();

                }

                if( response.body().get(0).getAge()!=null){
                     Age = response.body().get(0).getAge();

                }

                String Qbuserid = response.body().get(0).getQbuserid();
                String Qbuserlogin = response.body().get(0).getQbuserlogin();
                String Qbpass = response.body().get(0).getQbpass();

                pref.setemailId(emailid);
                pref.setFirstName(f_name);
                pref.setLastName(l_name);
                pref.setGender(gender);
                pref.setBirthday(dob);
                pref.setLocation(location_name);
                pref.setProfilePhoto(profile_pic);
                pref.setFbProfilePhoto2(fbProfilePhoto2);
                pref.setFbProfilePhoto3(fbProfilePhoto3);
                pref.setFbProfilePhoto4(fbProfilePhoto4);
                pref.setFbProfilePhoto5(fbProfilePhoto5);
                pref.setFbProfilePhoto6(fbProfilePhoto6);
                pref.setCollegeName(CollegeName);
                pref.setAbout(About);
                pref.setAge(Age);
                pref.setProfession(Profession);
                pref.setQbuserid(Qbuserid);
                pref.setQbuserlogin(Qbuserlogin);
                pref.setQbpass(Qbpass);
                pref.setRelationshipStatus(relationStatus);

                sessionManager.setLogin(true);
                progressDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<List<LoginModel>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e("RES-T",t+"<");
            }
        });
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public void GetFacebookImages(final String albumId, int s1, int s2) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images,user_photos,friends_photos");

        GraphRequest requestt = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.v("TAG", "Facebook Photos response: " + response);
                        //tvTitle.setText("Facebook Images");
                        try {
                            if (response.getError() == null) {

                                JSONObject joMain = response.getJSONObject();
                                if (joMain.has("data")) {
                                    JSONObject joAlbum = new JSONObject();
                                    JSONArray jaData = joMain.optJSONArray("data");
                                    Log.d("data1", jaData.toString());
                                    //lstFBImages = new ArrayList<>();
                                    for (int i = 0; i < jaData.length(); i++)//Get no. of images {
                                    {
                                        joAlbum = jaData.getJSONObject(i);
                                        final JSONArray jaImages = joAlbum.getJSONArray("images");
                                        Log.d("data2", jaImages.toString());

                                        JSONObject jo = jaImages.getJSONObject(0);
                                        Log.d("data3", jo.toString());
                                        String width = jo.getString("width");
                                        String height = jo.getString("height");
                                        String source = jo.getString("source");
                                        source.replaceAll("\\\\", "");
//                                        FacebookImages_Object obj = new FacebookImages_Object(height, width, source);
//                                        arraylist.add(obj);
                                        if (i == 0) {
                                            fbprofile2 = source;
                                        }
                                        if (i == 1) {
                                            fbprofile3 = source;
                                        }

                                        if (i == 2) {
                                            fbprofile4 = source;
                                        }

                                        if (i == 3) {
                                            fbprofile5 = source;
                                        }

                                        if (i == 4) {
                                            fbprofile6 = source;
                                        }
                                        Log.d("fb images", jaImages.toString());
                                        // }
                                    }

                                }

                                RegisterUser(fbid, fbprofile1 + "", fbprofile2, fbprofile3,
                                        fbprofile4, fbprofile5, fbprofile6, f_name + " " + l_name,
                                        email_id, "", "", f_name, l_name, "", gender, "",
                                        "", "", "");
                              /*  if (arraylist.size() > 0) {
                                    Intent i = new Intent(getActivity(), ImageSelection.class);
                                    Bundle args = new Bundle();
                                    args.putSerializable("ARRAYLIST", (Serializable) arraylist);
                                    i.putExtra("BUNDLE", args);
                                    i.putExtra("friendid", user_id);
                                    i.putExtra("get_from", "facebook");
                                    getActivity().startActivity(i);
                                    getActivity().finish();
                                }
*/

                                //set your adapter here
                            } else {
                                Log.v("TAG", response.getError().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }
        );
        requestt.setParameters(parameters);
        requestt.executeAsync();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            // here we go you can see current lat long.
            Log.e(TAG, "onConnected: " + String.valueOf(mLastLocation.getLatitude()) + ":" + String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    class AsyncHttpTask extends AsyncTask<String, String, JSONObject> {

        //  boolean error;
        String message,error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected JSONObject doInBackground(String... params) {





            List<NameValuePair> params1 = new ArrayList<>();

            params1.add(new BasicNameValuePair("FbProfileId", params[0]));
            params1.add(new BasicNameValuePair("FbUserPhotoUrl1", params[1]));

            params1.add(new BasicNameValuePair("FbUserPhotoUrl2", params[2]));

            params1.add(new BasicNameValuePair("FbUserPhotoUrl3", params[3]));

            params1.add(new BasicNameValuePair("FbUserPhotoUrl4", params[4]));

            params1.add(new BasicNameValuePair("FbUserPhotoUrl5", params[5]));
            params1.add(new BasicNameValuePair("FbUserPhotoUrl6", params[6]));

            params1.add(new BasicNameValuePair("ProfileName", params[7]));

            params1.add(new BasicNameValuePair("UserEmail",params[8]));

            params1.add(new BasicNameValuePair("UserAddress", params[9]));

            params1.add(new BasicNameValuePair("UserDob", params[10]));


            params1.add(new BasicNameValuePair("FirstName", params[11]));
            params1.add(new BasicNameValuePair("LastName", params[12]));
            params1.add(new BasicNameValuePair("CollegeName", params[13]));
            params1.add(new BasicNameValuePair("Gender", params[14]));
            params1.add(new BasicNameValuePair("About", params[15]));
            params1.add(new BasicNameValuePair("Profession", params[16]));
            params1.add(new BasicNameValuePair("Location", ""));


            Log.e("param[1]",params1+"");

            JSONObject json = jsonp1.makeHttpRequest("http://demo.workdesirewebsolutions.com/showbuddy/register.php", "POST", params1);

              Log.e("json object",json+"<<");
            JSONObject array=null;
            try {
                array   =json.getJSONObject(json.toString());
                Log.e("in array",array+"<<");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int cnt = 0;



            return array;
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.e("s-->",s+"<<");
//Toast.makeText(AsycroEmail.this,"SENT",Toast.LENGTH_LONG).show();
            Log.e("done","mail sent ");
            Toast.makeText(LoginActivity.this, s.toString()+"Response"+"", Toast.LENGTH_SHORT).show();



            //  finish();

        }


    }



}
