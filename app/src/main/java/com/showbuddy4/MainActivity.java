package com.showbuddy4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.showbuddy4.fragment.Fragment_Chat_View;
import com.showbuddy4.fragment.Fragment_Swipe_Card_List;
import com.showbuddy4.fragment.Fragment_User_Profile;
import com.showbuddy4.helper.FragmentAdvanceStatePagerAdapter;

import java.text.DateFormat;
import java.util.Date;

import com.google.android.gms.location.LocationListener;
import com.showbuddy4.model.SwipeAction;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ResultCallback<LocationSettingsResult> {

    TabLayout tabLayout;
    ViewPager viewPager;
    MyPagerAdapter pagerAdapter;
    private int[] mTabsIcons = {R.drawable.actionbar_profile_selector, R.drawable.actionbar_swiper_selector, R.drawable.actionbar_chat_selector};
    PreferenceApp pref;
    protected GoogleApiClient mGoogleApiClient;
    boolean GpsStatus;
    protected LocationRequest mLocationRequest;
    protected String mLastUpdateTime;

    protected LocationSettingsRequest mLocationSettingsRequest;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public static final int REQUEST_PERMISSION_LOCATION = 10;

    private static final int PERMISSION_REQUEST_CODE = 200;

    public static Location mCurrentLocation;

    protected Boolean mRequestingLocationUpdates;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    static final String APP_ID = "69154";
    static final String AUTH_KEY = "m67pYjJpq9QO3wG";
    static final String AUTH_SECRET = "DM62rUpKZF39uf9";
    static final String ACCOUNT_KEY = "UuvWmpLzoba5UV_A21vj";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        pref = new PreferenceApp(this);
//        pref.setFbId("104434930361336");
//        pref.setemailId("mnpdev.test@gmail.com");
//        pref.setFirstName("Mahesh");
//        pref.setLastName("Prajapati");
//        pref.setGender("male");
//        pref.setBirthday("");
//        pref.setLocation("23.050049,72.5089523");
//        pref.setProfilePhoto("https://scontent.xx.fbcdn.net/v/t1.0-1/c59.0.200.200/p200x200/10354686_10150004552801856_220367501106153455_n.jpg?oh=f621f9474a63f5b27b0514adda5db42b&oe=5B0F8625");
//        pref.setFbProfilePhoto2("");
//        pref.setFbProfilePhoto3("");
//        pref.setFbProfilePhoto4("");
//        pref.setFbProfilePhoto5("");
//        pref.setFbProfilePhoto6("");
//        pref.setCollegeName("K.S School of Business Management");
//        pref.setAbout("");
//        pref.setAge("");
//        pref.setProfession("");
//        pref.setQbuserid("44771703");
//        pref.setQbuserlogin("104434930361336");
//        pref.setQbpass("showbuddy@123");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mRequestingLocationUpdates = false;
        buildGoogleApiClient();

        if (checkForLocationEnabled()) {
            if (!checkPermission()) {
                requestPermission();
            } else {

                createLocationRequest();

                buildLocationSettingsRequest();

                checkLocationSettings();

            }
        }

        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(1);
            viewPager.setOffscreenPageLimit(3);
        }

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }
            tabLayout.getTabAt(0).getCustomView().setSelected(true);
        }

        viewPager.addOnPageChangeListener(MainActivity.this);

        QBSession();
    }
    public void QBSession() {
        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        QBChatService.setDebugEnabled(true); // enable chat logging

        QBChatService.setDefaultPacketReplyTimeout(10000);

        QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
        chatServiceConfigurationBuilder.setSocketTimeout(60); //Sets chat socket's read timeout in seconds
        chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
        chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
        QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);


        final QBChatService chatService = QBChatService.getInstance();


        final QBUser user = new QBUser(pref.getQbuserid(), pref.getQbpass());


        QBAuth.createSession(user).performAsync(new QBEntityCallback<com.quickblox.auth.session.QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success, login to chat

                user.setId(session.getUserId());

                chatService.login(user, new QBEntityCallback() {

                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        Log.e("TAG", "onSuccess: " + user.getId());

                    }

                    @Override
                    public void onError(QBResponseException errors) {
                        Log.e("TAG", "errors: " + errors);
                    }
                });
            }

            @Override
            public void onError(QBResponseException errors) {

            }
        });

    }
    protected synchronized void buildGoogleApiClient() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(250);
        /*
        BUILDING GOOGLE API CLIENT
         */
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }


    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
        } else {
            goAndDetectLocation();
        }

    }

    public void goAndDetectLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = true;

            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i("TAG", "Connected to GoogleApiClient");

        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateLocationUI();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i("TAG", "Connection suspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLocationUI();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("TAG", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    private void updateLocationUI() {
        if (mCurrentLocation != null) {

            UpdateLocation(String.valueOf(mCurrentLocation.getLatitude()) + "," + String.valueOf(mCurrentLocation.getLongitude()));
        }
    }


    public void UpdateLocation(String Location) {
        PreferenceApp pref = new PreferenceApp(MainActivity.this);

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<SwipeAction> call = apiService.updatelocation(pref.getFbId(), Location);

        call.enqueue(new Callback<SwipeAction>() {
            @Override
            public void onResponse(@NonNull Call<SwipeAction> call, @NonNull Response<SwipeAction> response) {
                try {
                    Log.e("LocationAwipe",response.body().toString()+"<");
                    if (response.code() == 200) {
                        String status = response.body().getStatus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SwipeAction> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {


        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.e("TAG", "All location settings are satisfied.");

                //Toast.makeText(getActivity(), "Location is already on.", Toast.LENGTH_SHORT).show();
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.e("TAG", "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {

                    Toast.makeText(MainActivity.this, "Location dialog will be open", Toast.LENGTH_SHORT).show();

                    status.startResolutionForResult(MainActivity.this
                            , REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i("TAG", "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    private class MyPagerAdapter extends FragmentAdvanceStatePagerAdapter {

        public final int PAGE_COUNT = 3;


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        public View getTabView(int position) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);

            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
            return view;
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getFragmentItem(int pos) {
            switch (pos) {
                case 0:
                    return Fragment_User_Profile.newInstance("param1", "param2");
                case 1:
                    return Fragment_Swipe_Card_List.newInstance("param1", "param2");
                case 2:
                    return Fragment_Chat_View.newInstance("param1", "param2");
            }
            return null;
        }

        @Override
        public void updateFragmentItem(int pos, Fragment fragment) {

        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }


    }

    private boolean checkForLocationEnabled() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnable = false;
        boolean isLocationEnable = false;

        try {
            isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isLocationEnable = isLocationEnabled(MainActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!isGpsEnable || !isLocationEnable) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("Enable GPS");
            dialog.setPositiveButton("Open settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int paramInt) {
                    dialogInterface.dismiss();
                }
            });
            dialog.show();
        }

        return isGpsEnable;
    }

    private boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)

                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }

                break;

            case REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goAndDetectLocation();
                }
                break;

        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
