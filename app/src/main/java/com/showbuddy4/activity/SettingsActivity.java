package com.showbuddy4.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.showbuddy4.R;
import com.showbuddy4.Utils.CommonUses;
import com.showbuddy4.model.GetSettingsModel;
import com.showbuddy4.model.SwipeAction;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.showbuddy4.util.IabBroadcastReceiver;
import com.showbuddy4.util.IabHelper;
import com.showbuddy4.util.IabResult;
import com.showbuddy4.util.Inventory;
import com.showbuddy4.util.Purchase;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener, PlaceSelectionListener {

    ImageView img_back;
    CardView card_logout;
    PreferenceApp pref;

    TextView swippingin;
    TextView maxDIstanceTv;
    TextView ageRangeTV;
    TextView txt_update_settings;
    TextView finalDistanceIn;
    TextView txt_username;

    RelativeLayout relativelayouthelpandsupport;
    RelativeLayout relativelayoutmylocation;
    LinearLayout linearlayoutlicenses;
    LinearLayout linearlayoutprivacypolicy;
    LinearLayout linearlayouttermsofservice;

    Switch showme_men, showSingleMan, showSingleWoman;
    Switch showme_women;
    Switch showme_couple;

    Switch showmeonShowbuddy;

    Switch noti_newmatches;
    Switch noti_messages;
    Switch noti_Messagelikes;
    Switch noti_SuperLike;

    SeekBar maxDistanceSeekBar;
    CrystalRangeSeekbar ageSeekBar;

    Button distancein_km;
    Button distancein_mi;
    Button btncontinue;
    Button btnsubscriptioncontinue;

    String maxDistanceFinalValue = "160";
    String minAge = "";
    String maxAge = "";
double kmval=0;
    String selectedDistance = "km";
    String filterMan = "", filterWoman = "";
    LinearLayout powerswipes;
    LinearLayout showbuddyplus;
    LinearLayout linear_firstsubscription;
    LinearLayout linear_secondsubscription;
    LinearLayout linear_thirdsubscription;

    LinearLayout linear_firstsubscriptionplan;
    LinearLayout linear_secondsubscriptionplan;
    LinearLayout linear_thirdsubscriptionplan;

    View alertLayout;
    View alertLayout2;


    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int REQUEST_SELECT_PLACE = 1000;

    // Debug tag, for logging
    static final String TAG = "SHOWBUDDY_PLUS";

    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    static final String PREMIUM_MEMBERSHIP_1MONTH = "premium_membership_1month";
    static final String PREMIUM_MEMBERSHIP_6MONTH = "premium_membership_6month";
    static final String PREMIUM_MEMBERSHIP_12MONTH = "premium_membership_12month";

    static final String POWER_SWIPE_5 = "power_swipe_5";
    static final String POWER_SWIPE_10 = "power_swipe_10";
    static final String POWER_SWIPE_15 = "power_swipe_15";

    static String SELECTED_SKU = "SELECTED_SKU";

    String latitute = "", longitute = "";

    // SKU for our subscription (infinite gas)
//    static final String SKU_INFINITE_GAS_MONTHLY = "infinite_gas_monthly";
//    static final String SKU_INFINITE_GAS_YEARLY = "infinite_gas_yearly";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    int a = 0;
    int a1 = 0;
    // The helper object
    IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        pref = new PreferenceApp(this);
        img_back = (ImageView) findViewById(R.id.img_back);
        card_logout = (CardView) findViewById(R.id.card_logout);

        swippingin = (TextView) findViewById(R.id.swippingin);
        ageRangeTV = (TextView) findViewById(R.id.ageRangeTV);
        finalDistanceIn = (TextView) findViewById(R.id.finalDistanceIn);
        maxDIstanceTv = (TextView) findViewById(R.id.maxDIstanceTv);
        txt_username = (TextView) findViewById(R.id.txtusername);
        relativelayouthelpandsupport = (RelativeLayout) findViewById(R.id.relativelayouthelpandsupport);
        relativelayoutmylocation = (RelativeLayout) findViewById(R.id.relativelayoutmylocation);
        linearlayoutlicenses = (LinearLayout) findViewById(R.id.linearlayoutlicenses);
        linearlayoutprivacypolicy = (LinearLayout) findViewById(R.id.linearlayoutprivacypolicy);
        linearlayouttermsofservice = (LinearLayout) findViewById(R.id.linearlayouttermsofservice);
        txt_update_settings = (TextView) findViewById(R.id.txt_update_settings);
        showme_men = (Switch) findViewById(R.id.showme_men);
        showme_women = (Switch) findViewById(R.id.showme_women);
        showme_couple = (Switch) findViewById(R.id.showme_couple);

        showSingleMan = (Switch) findViewById(R.id.showme_Single_MenOnly);
        showSingleWoman = (Switch) findViewById(R.id.showme_SingleWomenOnly);

        showmeonShowbuddy = (Switch) findViewById(R.id.showmeonShowbuddy);

        LayoutInflater inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.dialog_inapppurchase, null);
        linear_firstsubscription = (LinearLayout) alertLayout.findViewById(R.id.linear_firstsubscription);
        linear_secondsubscription = (LinearLayout) alertLayout.findViewById(R.id.linear_secondsubscription);
        linear_thirdsubscription = (LinearLayout) alertLayout.findViewById(R.id.linear_thirdsubscription);
        btncontinue = (Button) alertLayout.findViewById(R.id.btncontinue);

        LayoutInflater inflater2 = getLayoutInflater();
        alertLayout2 = inflater2.inflate(R.layout.dialog_subscriptionplans, null);
        linear_firstsubscriptionplan = (LinearLayout) alertLayout2.findViewById(R.id.linear_firstsubscriptionpplan);
        linear_secondsubscriptionplan = (LinearLayout) alertLayout2.findViewById(R.id.linear_secondsubscriptionplan);
        linear_thirdsubscriptionplan = (LinearLayout) alertLayout2.findViewById(R.id.linear_thirdsubscriptionplan);
        btnsubscriptioncontinue = (Button) alertLayout2.findViewById(R.id.btnsubscriptioncontinue);

        maxDistanceSeekBar = (SeekBar) findViewById(R.id.maxDistanceSeekBar);
        ageSeekBar = (CrystalRangeSeekbar) findViewById(R.id.ageSeekBar);

        noti_newmatches = (Switch) findViewById(R.id.noti_newmatches);
        noti_messages = (Switch) findViewById(R.id.noti_messages);
        noti_Messagelikes = (Switch) findViewById(R.id.noti_Messagelikes);
        noti_SuperLike = (Switch) findViewById(R.id.noti_SuperLike);

        distancein_km = (Button) findViewById(R.id.distancein_km);
        distancein_mi = (Button) findViewById(R.id.distancein_mi);

        powerswipes = (LinearLayout) findViewById(R.id.powerswipes);
        showbuddyplus = (LinearLayout) findViewById(R.id.showbuddyplus);

        if (pref.getGender().equalsIgnoreCase("male")) {
            showme_men.setChecked(true);
        } else if (pref.getGender().equalsIgnoreCase("female")) {
            showme_women.setChecked(true);
        } else if (pref.getGender().equalsIgnoreCase("couple")) {
            showme_couple.setChecked(true);
        }

        setFilterMen(pref.getFilterSingleMan());
        setFilterWoman(pref.getFilterSingleWoman());

        String username = pref.getFirstName() + " " + pref.getLastName();
        txt_username.setText(username);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        card_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.clear();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finish();
            }
        });

        relativelayouthelpandsupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SettingsActivity.this, HelpSupportActivity.class);
                startActivity(in);
            }
        });

        linearlayoutlicenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SettingsActivity.this, LicenseActivity.class);
                startActivity(in);
            }
        });

        linearlayoutprivacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SettingsActivity.this, PrivacyPolicyActivity.class);
                startActivity(in);
            }
        });

        linearlayouttermsofservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SettingsActivity.this, TermsofServiceActivity.class);
                startActivity(in);
            }
        });

        maxDistanceSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        maxDistanceFinalValue = String.valueOf(progressValue);
                        maxDIstanceTv.setText(maxDistanceFinalValue + " " + finalDistanceIn.getText().toString().trim());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview
                        kmval=seekBar.getProgress();
                    }
                });


        // set listener
        ageSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                ageRangeTV.setText(String.valueOf(minValue) + " - " + String.valueOf(maxValue));
                minAge = String.valueOf(minValue);
                maxAge = String.valueOf(maxValue);

            }
        });

        // set final value listener
        ageSeekBar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {

                ageRangeTV.setText(String.valueOf(minValue) + " - " + String.valueOf(maxValue));
                minAge = String.valueOf(minValue);
                maxAge = String.valueOf(maxValue);

            }
        });

        txt_update_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSettingsAPI();
            }
        });

        distancein_mi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!finalDistanceIn.getText().toString().equalsIgnoreCase("MI."))
            {   selectedDistance = "mi";
                finalDistanceIn.setText("MI.");
                kmval=kmTomiles(kmval);
                maxDIstanceTv.setText((int)Math.round(kmval) + " " + "MI.");
                maxDistanceSeekBar.setProgress((int)Math.round(kmval));
                distancein_mi.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                distancein_km.setBackgroundColor(getResources().getColor(R.color.white));}

            }
        });

        distancein_km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!finalDistanceIn.getText().toString().equalsIgnoreCase("KM."))
                {     selectedDistance = "km";
                finalDistanceIn.setText("KM.");
                kmval=milesTokm(kmval);
                maxDIstanceTv.setText(Math.round(kmval)+ " " + "KM.");
                maxDistanceSeekBar.setProgress((int)Math.round(kmval));
                distancein_km.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                distancein_mi.setBackgroundColor(getResources().getColor(R.color.white));}

            }
        });

        swippingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mIsPremium) {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().build();
                    try {
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setBoundsBias(BOUNDS_MOUNTAIN_VIEW).build(SettingsActivity.this);
                        startActivityForResult(intent, REQUEST_SELECT_PLACE);

                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                    }
                } else {
                    openForPrmiumbyAshish();
                }

            }
        });

        getSettingsAPI();


        powerswipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*AlertDialog.Builder builderSingle = new AlertDialog.Builder(SettingsActivity.this);
                builderSingle.setTitle("Choose Power Swipe Plans");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_list_item_1);
                arrayAdapter.add("5 Power Swipes / 4.99 $");
                arrayAdapter.add("10 Power Swipes / 9.99 $");
                arrayAdapter.add("15 Power Swipes / 14.99 $");


                builderSingle.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();
                        String strName = arrayAdapter.getItem(pos);

                        if (pos == 0) {
                            if (!mIsPremium) {
                                //premium for 1 month
                                SELECTED_SKU = POWER_SWIPE_5;
                                onUpgradeAppButtonClicked(SELECTED_SKU);
                            }
                        }

                        if (pos == 1) {
                            if (!mIsPremium) {
                                //premium for 6 month
                                SELECTED_SKU = POWER_SWIPE_10;
                                onUpgradeAppButtonClicked(SELECTED_SKU);
                            }
                        }

                        if (pos == 2) {
                            if (!mIsPremium) {
                                //premium for 12 month
                                SELECTED_SKU = POWER_SWIPE_15;
                                onUpgradeAppButtonClicked(SELECTED_SKU);
                            }
                        }


                    }
                });
                builderSingle.show();*/

                AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
                // this is set the view from XML inside AlertDialog
                if (alertLayout.getParent() != null)
                    ((ViewGroup) alertLayout.getParent()).removeView(alertLayout);
                alert.setView(alertLayout);

                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);

                linear_firstsubscription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linear_firstsubscription.setBackgroundResource(R.drawable.border_layout);
                        linear_secondsubscription.setBackgroundResource(0);
                        linear_thirdsubscription.setBackgroundResource(0);
                        a = 1;
                    }
                });
                linear_secondsubscription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linear_secondsubscription.setBackgroundResource(R.drawable.border_layout);
                        linear_firstsubscription.setBackgroundResource(0);
                        linear_thirdsubscription.setBackgroundResource(0);
                        a = 2;
                    }
                });
                linear_thirdsubscription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linear_thirdsubscription.setBackgroundResource(R.drawable.border_layout);
                        linear_firstsubscription.setBackgroundResource(0);
                        linear_secondsubscription.setBackgroundResource(0);
                        a = 3;
                    }
                });
                btncontinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (a == 1) {
                            if (!mIsPremium) {
                                //premium for 1 month
                                SELECTED_SKU = POWER_SWIPE_5;
                                onUpgradeAppButtonClicked(SELECTED_SKU);
                            }
                        } else if (a == 2) {
                            if (!mIsPremium) {
                                //premium for 6 month
                                SELECTED_SKU = POWER_SWIPE_10;
                                onUpgradeAppButtonClicked(SELECTED_SKU);
                            }

                        } else if (a == 3) {
                            if (!mIsPremium) {
                                //premium for 12 month
                                SELECTED_SKU = POWER_SWIPE_15;
                                onUpgradeAppButtonClicked(SELECTED_SKU);
                            }

                        }
                    }
                });
                /*alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = etUsername.getText().toString();
                        String pass = etEmail.getText().toString();
                        Toast.makeText(getBaseContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();
                    }
                });*/
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();


            }
        });


        showbuddyplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*AlertDialog.Builder builderSingle = new AlertDialog.Builder(SettingsActivity.this);
                builderSingle.setTitle("Choose Subscription options");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_list_item_1);
                arrayAdapter.add("1 month / 14.99 $");
                arrayAdapter.add("6 month / 89.99 $");
                arrayAdapter.add("12 month / 179.99 $");


                builderSingle.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();

                        if (pos == 0) {
                            if (!mIsPremium) {
                                //premium for 1 month
                                SELECTED_SKU = PREMIUM_MEMBERSHIP_1MONTH;
                                onUpgradeAppButtonClicked(SELECTED_SKU);
                            }
                        }

                        if (pos == 1) {
                            if (!mIsPremium) {
                                //premium for 6 month
                                SELECTED_SKU = PREMIUM_MEMBERSHIP_6MONTH;
                                onUpgradeAppButtonClicked(SELECTED_SKU);
                            }
                        }

                        if (pos == 2) {
                            if (!mIsPremium) {
                                //premium for 12 month
                                SELECTED_SKU = PREMIUM_MEMBERSHIP_12MONTH;
                                onUpgradeAppButtonClicked(SELECTED_SKU);
                            }
                        }


                    }
                });
                builderSingle.show();*/
                openForPrmiumbyAshish();


            }
        });


        //IN-APP Purchase start

        // load game data
        loadData();

        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         *
         * Instead of just storing the entire literal string here embedded in the
         * program,  construct the key at runtime from pieces or
         * use bit manipulation (for example, XOR with some other string) to hide
         * the actual key.  The key itself is not secret information, but we don't
         * want to make it easy for an attacker to replace the public key with one
         * of their own and then fake messages from the server.
         */
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm42HifEm6sSTBsuCtRyDdTkTXyeV2XtkusNTo74NhMMEpxgeUAdFOXtPhJ4zc6uVg5zPUbVsDESSzAuI5CmF21Y1ntVc/r5YPLiuo5Zx/XNj/9GZopCeQcLbM2dv9X3kYWBrpm8pV+yPi+IpWsLaojeJY/h2LpkyPEJm9etsmr0OvInuKV492PmhGCWGGguLtsKxbzjpPcPgHXxJSNhgHXhyLI8DU/ubKgOEYQgHUx5Cea/+jrYuu9UHNJlRHT32grxKt3S8YySqo3UEP76zDsE/K8Aba2ENuvuGen2bqJbqppHdps/5jqB1UUX0+VQFjU0ZBORItmqrlYrAJMot9QIDAQAB";

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(getApplicationContext());
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });


    }

    private void openForPrmiumbyAshish() {
        AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);

        if (alertLayout2.getParent() != null)
            ((ViewGroup) alertLayout2.getParent()).removeView(alertLayout2);
        alert.setView(alertLayout2);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout2);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);

        linear_firstsubscriptionplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_firstsubscriptionplan.setBackgroundResource(R.drawable.border_layout);
                linear_secondsubscriptionplan.setBackgroundResource(0);
                linear_thirdsubscriptionplan.setBackgroundResource(0);
                a1 = 1;
            }
        });
        linear_secondsubscriptionplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_secondsubscriptionplan.setBackgroundResource(R.drawable.border_layout);
                linear_firstsubscriptionplan.setBackgroundResource(0);
                linear_thirdsubscriptionplan.setBackgroundResource(0);
                a1 = 2;
            }
        });
        linear_thirdsubscriptionplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_thirdsubscriptionplan.setBackgroundResource(R.drawable.border_layout);
                linear_firstsubscriptionplan.setBackgroundResource(0);
                linear_secondsubscriptionplan.setBackgroundResource(0);
                a1 = 3;
            }
        });
        btnsubscriptioncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a1 == 1) {
                    if (!mIsPremium) {
                        //premium for 1 month
                        SELECTED_SKU = PREMIUM_MEMBERSHIP_1MONTH;
                        onUpgradeAppButtonClicked(SELECTED_SKU);
                    }
                } else if (a1 == 2) {
                    if (!mIsPremium) {
                        //premium for 6 month
                        SELECTED_SKU = PREMIUM_MEMBERSHIP_6MONTH;
                        onUpgradeAppButtonClicked(SELECTED_SKU);
                    }

                } else if (a1 == 3) {
                    if (!mIsPremium) {
                        //premium for 12 month
                        SELECTED_SKU = PREMIUM_MEMBERSHIP_12MONTH;
                        onUpgradeAppButtonClicked(SELECTED_SKU);
                    }

                }
            }
        });
                /*alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = etUsername.getText().toString();
                        String pass = etEmail.getText().toString();
                        Toast.makeText(getBaseContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();
                    }
                });*/
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }


    public void getSettingsAPI() {
        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<List<GetSettingsModel>> call = apiService.getuserSettingAPI(pref.getFbId());

        call.enqueue(new Callback<List<GetSettingsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetSettingsModel>> call, @NonNull Response<List<GetSettingsModel>> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body() == null) {
                            updateSettingsAPI();
                        } else {
                            List<GetSettingsModel> list = response.body();
                            String agerange = list.get(0).getAgeRange();
                            String showme = list.get(0).getShowMe();
                            String maxdistance = list.get(0).getMaxDist();
                            String show_me_on_showbuddy = list.get(0).getShowMeOnShowbuddy();
                            String new_matches_on_off = list.get(0).getNewMatchesOnOff();
                            String message_on_off = list.get(0).getMessageOnOff();
                            String message_like = list.get(0).getMessageLike();
                            String show_dist = list.get(0).getShowDist();
                            String super_like = list.get(0).getSuperLike();
                            String filterMen = list.get(0).getRelationshipFilterMan();
                            String filterWoman = list.get(0).getRelationshipFilterWoman();
                            setFilterMen(filterMen);
                            setFilterWoman(filterWoman);
                            if (filterMen != null || TextUtils.isEmpty(filterMen)) {
                                pref.setFilterSingleMan(filterMen);
                            }
                            if (filterWoman != null || TextUtils.isEmpty(filterWoman)) {
                                pref.setFilterSingleWoman(filterWoman);
                            }
                            List<String> result = Arrays.asList(agerange.split("\\s*,\\s*"));

                            ageRangeTV.setText(result.get(0) + " - " + result.get(1));
                            minAge = String.valueOf(result.get(0));
                            maxAge = String.valueOf(result.get(1));
                            ageSeekBar.setMaxStartValue(Float.parseFloat(maxAge));
                            ageSeekBar.setMinStartValue(Float.parseFloat(minAge));

                            ageSeekBar.setMinValue(18);
                            ageSeekBar.setMaxValue(56);

                            if (showme.equalsIgnoreCase("male")) {
                                showme_men.setChecked(true);
                                showme_women.setChecked(false);
                            } else if (showme.equalsIgnoreCase("female")) {
                                showme_men.setChecked(false);
                                showme_women.setChecked(true);
                            } else if (showme.equalsIgnoreCase("male,female")) {
                                showme_men.setChecked(true);
                                showme_women.setChecked(true);
                            } else if (showme.equalsIgnoreCase("male,female,couple")) {
                                showme_men.setChecked(true);
                                showme_women.setChecked(true);
                                showme_couple.setChecked(true);
                            } else if (showme.isEmpty()) {
                                showme_men.setChecked(false);
                                showme_women.setChecked(false);
                                showme_couple.setChecked(false);
                            }

                            maxDistanceSeekBar.setProgress(Integer.parseInt(maxdistance));
                            kmval=Integer.parseInt(maxdistance);
                            if (show_me_on_showbuddy.equalsIgnoreCase("0")) {
                                showmeonShowbuddy.setChecked(false);
                            } else {
                                showmeonShowbuddy.setChecked(true);
                            }

                            if (new_matches_on_off.equalsIgnoreCase("0")) {
                                noti_newmatches.setChecked(false);
                            } else {
                                noti_newmatches.setChecked(true);
                            }

                            if (message_on_off.equalsIgnoreCase("0")) {
                                noti_messages.setChecked(false);
                            } else {
                                noti_messages.setChecked(true);
                            }

                            if (message_like.equalsIgnoreCase("0")) {
                                noti_Messagelikes.setChecked(false);
                            } else {
                                noti_Messagelikes.setChecked(true);
                            }

                            if (super_like.equalsIgnoreCase("0")) {
                                noti_SuperLike.setChecked(false);
                            } else {
                                noti_SuperLike.setChecked(true);
                            }

                            if (show_dist.equalsIgnoreCase("km")) {
                                distancein_km.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                distancein_mi.setBackgroundColor(getResources().getColor(R.color.white));
                                finalDistanceIn.setText("KM.");
                                maxDIstanceTv.setText(maxDistanceFinalValue + " " + finalDistanceIn.getText().toString().trim());

                            } else {
                                distancein_mi.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                distancein_km.setBackgroundColor(getResources().getColor(R.color.white));
                                finalDistanceIn.setText("MI.");
                                maxDIstanceTv.setText(maxDistanceFinalValue + " " + finalDistanceIn.getText().toString().trim());

                            }
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GetSettingsModel>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setFilterMen(String filterMen) {

        if (filterMen != null && !TextUtils.isEmpty(filterMen) && filterMen.equalsIgnoreCase("yes")) {
            showSingleMan.setChecked(true);
            //pref.setFilterSingleMan("yes");
        } else if (filterMen != null && !TextUtils.isEmpty(filterMen) && filterMen.equalsIgnoreCase("no")) {
            showSingleMan.setChecked(false);
        } else {
            showSingleMan.setChecked(false);
        }
    }


    private void setFilterWoman(String filterWoman) {
        if (filterWoman != null && !TextUtils.isEmpty(filterWoman) && filterWoman.equalsIgnoreCase("yes")) {
            showSingleWoman.setChecked(true);
            //pref.setFilterSingleMan("yes");
        } else if (filterWoman != null && !TextUtils.isEmpty(filterWoman) && filterWoman.equalsIgnoreCase("no")) {
            showSingleWoman.setChecked(false);
        } else {
            showSingleWoman.setChecked(false);
        }
    }

    public void updateSettingsAPI() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String swipingIn = "";
        String showmeStr = "";
        String ageRange = "";
        String shwOnShowbuddy = "";
        String new_matches_on_off = "";
        String message_on_off = "";
        String message_like = "";
        String super_like = "";

        if (swippingin.getText().toString().trim().equalsIgnoreCase("My Current Location")) {
            swipingIn = "me";
        } else {
            swipingIn = "me";
        }

        if (showme_men.isChecked()) {
            showmeStr = "male";
        }
        if (showme_women.isChecked()) {
            showmeStr = "female";
        }
        if (showme_couple.isChecked()) {
            showmeStr = "couple";
        }
        if (showme_men.isChecked() && showme_women.isChecked() && showme_couple.isChecked()) {
            showmeStr = "male,female,couple";
        }

        if (showmeonShowbuddy.isChecked()) {
            shwOnShowbuddy = "1";
        } else {
            shwOnShowbuddy = "0";
        }

        if (noti_newmatches.isChecked()) {
            new_matches_on_off = "1";
        } else {
            new_matches_on_off = "0";
        }

        if (noti_messages.isChecked()) {
            message_on_off = "1";
        } else {
            message_on_off = "0";
        }

        if (noti_Messagelikes.isChecked()) {
            message_like = "1";
        } else {
            message_like = "0";
        }

        if (noti_SuperLike.isChecked()) {
            super_like = "1";
        } else {
            super_like = "0";
        }


        ageRange = minAge + "," + maxAge;

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<GetSettingsModel> call = apiService.updateSettings(pref.getFbId(), swipingIn, showmeStr,
                maxDistanceFinalValue, ageRange, shwOnShowbuddy,
                new_matches_on_off, message_on_off, message_like,
                super_like, selectedDistance, showSingleMan.isChecked() ? "yes" : "no", showSingleWoman.isChecked() ? "yes" : "no");

        call.enqueue(new Callback<GetSettingsModel>() {
            @Override
            public void onResponse(@NonNull Call<GetSettingsModel> call, @NonNull Response<GetSettingsModel> response) {
                try {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        CommonUses.showToast(SettingsActivity.this, "Settings Updated Successfully.");
                        finish();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetSettingsModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    public void checkForPremiumMembership() {
        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<SwipeAction> call = apiService.checkpremium(pref.getFbId());

        call.enqueue(new Callback<SwipeAction>() {
            @Override
            public void onResponse(@NonNull Call<SwipeAction> call, @NonNull Response<SwipeAction> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            SwipeAction list = response.body();

                            if (list.getStatus().equalsIgnoreCase("0")) {
                                mIsPremium = false;
                            } else if (list.getStatus().equalsIgnoreCase("1")) {
                                mIsPremium = true;
                            }

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mIsPremium = false;

                }
            }

            @Override
            public void onFailure(@NonNull Call<SwipeAction> call, @NonNull Throwable t) {
                t.printStackTrace();
                mIsPremium = false;

            }
        });
    }


    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(PREMIUM_MEMBERSHIP_1MONTH);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));


            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    /*// User clicked the "Buy Gas" button
    public void onBuyGasButtonClicked(View arg0) {
        Log.d(TAG, "Buy gas button clicked.");

        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }

        if (mTank >= TANK_MAX) {
            complain("Your tank is full. Drive around a bit!");
            return;
        }

        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for gas.");

        *//* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. *//*
        String payload = "";

        try {
            mHelper.launchPurchaseFlow(this, SKU_GAS, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }*/

    // User clicked the "Upgrade to Premium" button.
    public void onUpgradeAppButtonClicked(String selectedSKU) {
        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
//        setWaitScreen(true);

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            mHelper.launchPurchaseFlow(this, SELECTED_SKU, RC_REQUEST,
                    mPurchaseFinishedListener, payload);

        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);


        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }

        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == SettingsActivity.RESULT_OK) {
                System.out.println("Place data" + data);
                Bundle bundle = data.getExtras();
                String myplcae = bundle.getString("");
                Place place = PlaceAutocomplete.getPlace(SettingsActivity.this, data);
                System.out.println("Place data" + place.getAddress());
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(SettingsActivity.this, data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);

                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");

                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SELECTED_SKU)) {
                // bought the premium upgrade!


                mIsPremium = true;
//                updateUi();
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
                    return;
                }


            }

        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");


                mIsPremium = true;
                updateSettingsAPI(purchase.getOriginalJson());
//                alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");

            } else {
                complain("Error while consuming: " + result);
            }
//            updateUi();

            Log.d(TAG, "End consumption flow.");
        }
    };


    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }


    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }


    public void updateSettingsAPI(String originalJson) {

        try {

            JSONObject purchasedDetailObj = new JSONObject(originalJson);

            PreferenceApp pref;
            pref = new PreferenceApp(SettingsActivity.this);
            pref.getFbId();

            String productName = "";
            String powerSwipe = "0";
            String subscriptionMonth = "0";


            if (purchasedDetailObj.optString("productId").equalsIgnoreCase(PREMIUM_MEMBERSHIP_1MONTH)) {
                productName = "PREMIUM MEMBERSHIP 1MONTH";
                subscriptionMonth = "1";
            } else if (purchasedDetailObj.optString("productId").equalsIgnoreCase(PREMIUM_MEMBERSHIP_6MONTH)) {
                productName = "PREMIUM MEMBERSHIP 6MONTH";
                subscriptionMonth = "6";
            } else if (purchasedDetailObj.optString("productId").equalsIgnoreCase(PREMIUM_MEMBERSHIP_12MONTH)) {
                productName = "PREMIUM MEMBERSHIP 12MONTH";
                subscriptionMonth = "12";
            } else if (purchasedDetailObj.optString("productId").equalsIgnoreCase(POWER_SWIPE_5)) {
                productName = "POWER SWIPE 5";
                powerSwipe = "5";
            } else if (purchasedDetailObj.optString("productId").equalsIgnoreCase(POWER_SWIPE_10)) {
                productName = "POWER SWIPE 10";
                powerSwipe = "10";
            } else if (purchasedDetailObj.optString("productId").equalsIgnoreCase(POWER_SWIPE_15)) {
                productName = "POWER SWIPE 15";
                powerSwipe = "15";
            }


            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            ApiService apiService =
                    ApiClient.getClient().create(ApiService.class);

            Call<SwipeAction> call = apiService.addPurchaseOrder(purchasedDetailObj.optString("orderId"),
                    purchasedDetailObj.optString("packageName"),
                    purchasedDetailObj.optString("productId"),
                    pref.getFbId(),
                    pref.getEmailId(),
                    pref.getFirstName() + " " + pref.getLastName(),
                    productName, powerSwipe, subscriptionMonth);

            call.enqueue(new Callback<SwipeAction>() {
                @Override
                public void onResponse(@NonNull Call<SwipeAction> call, @NonNull Response<SwipeAction> response) {
                    try {
                        if (response.code() == 200) {
                            progressDialog.dismiss();

                            if (response.body().getStatus().equalsIgnoreCase("0")) {
                                alert("Some Failure in Order Booking!");
                            }

                            if (response.body().getStatus().equalsIgnoreCase("1")) {
                                alert("You have successfully Subscribed for Premium Membership! Thank you for Premium Membership!");
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SwipeAction> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void loadData() {
//        /*SharedPreferences sp = getPreferences(MODE_PRIVATE);
//        mTank = sp.getInt("tank", 2);
//        mIndonesia = sp.getInt("INDONESIA", 0);
//
//        Log.d(TAG, "Loaded data: tank = " + String.valueOf(mTank));
//
//        if (mIndonesia == 1)
//            mIsPremium = true;*/

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPlaceSelected(Place place) {
        LatLng latLng = place.getLatLng();
        latitute = String.valueOf(latLng.latitude);
        longitute = String.valueOf(latLng.longitude);
        Locale locale = place.getLocale();
        Geocoder geoCoder = new Geocoder(SettingsActivity.this, Locale.ENGLISH);
        try {
            List<Address> addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            String add = "";
            if (addresses.size() > 0) {
                for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
                    add += addresses.get(0).getAddressLine(i) + "\n";
            }

            //  showToastMessage(add);
            System.out.println("latlang info is" + addresses.get(0).getCountryName());
            /*country_et.setText(addresses.get(0).getCountryName());*/
            CommonUses.showToast(getApplicationContext(), addresses.get(0).getCountryName());


        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("latlang info is" + e1);

        }
    }

    @Override
    public void onError(Status status) {

        CommonUses.showToast(SettingsActivity.this, "Place selection failed: " + status.getStatusMessage());

    }

    private double milesTokm(double distanceInMiles) {
        return distanceInMiles * 1.60934;
    }

    private double kmTomiles(double distanceInKm) {
        return distanceInKm * 0.621371;
    }
}
