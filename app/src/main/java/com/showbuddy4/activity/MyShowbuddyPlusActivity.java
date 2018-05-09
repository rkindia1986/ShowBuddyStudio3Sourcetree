package com.showbuddy4.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.showbuddy4.R;
import com.showbuddy4.Utils.PreferenceHelper;
import com.showbuddy4.model.SwipeAction;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;
import com.showbuddy4.util.IabBroadcastReceiver;
import com.showbuddy4.util.IabHelper;
import com.showbuddy4.util.IabResult;
import com.showbuddy4.util.Inventory;
import com.showbuddy4.util.Purchase;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyShowbuddyPlusActivity extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener {

    @BindView(R.id.btnGetPowerSwipe)
    Button btnGetPowerSwipe;

    @BindView(R.id.btnGetPremium)
    Button btnGetPremium;


    Context context;
    static PreferenceHelper sharedpreference;

    private static final int PERMISSION_REQUEST_CODE = 200;


    //Google purchase

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


    // SKU for our subscription (infinite gas)
//    static final String SKU_INFINITE_GAS_MONTHLY = "infinite_gas_monthly";
//    static final String SKU_INFINITE_GAS_YEARLY = "infinite_gas_yearly";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;


    // The helper object
    IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showbuddy_plus);

        init();


        btnGetPowerSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MyShowbuddyPlusActivity.this);
                builderSingle.setTitle("Choose Power Swipe Plans");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MyShowbuddyPlusActivity.this, android.R.layout.simple_list_item_1);
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
                builderSingle.show();



            }
        });


        btnGetPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MyShowbuddyPlusActivity.this);
                builderSingle.setTitle("Choose Subscription options");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MyShowbuddyPlusActivity.this, android.R.layout.simple_list_item_1);
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
                builderSingle.show();


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


    private void init() {
        context = MyShowbuddyPlusActivity.this;
        ButterKnife.bind(this);

        sharedpreference = new PreferenceHelper(context, "SHOWBUDDY");
        sharedpreference.initPref();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Purchase");
        context = MyShowbuddyPlusActivity.this;

    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    private void restartThisActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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

            /*if (purchase.getSku().equals(SKU_GAS)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
                    setWaitScreen(false);
                    return;
                }
            }
            else */
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
            /*else if (purchase.getSku().equals(SKU_INFINITE_GAS_MONTHLY)
                    || purchase.getSku().equals(SKU_INFINITE_GAS_YEARLY)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");
                alert("Thank you for subscribing to infinite gas!");
                mSubscribedToInfiniteGas = true;
                mAutoRenewEnabled = purchase.isAutoRenewing();
                mInfiniteGasSku = purchase.getSku();
                mTank = TANK_MAX;
//                updateUi();

            }*/
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

   /* // updates UI to reflect model
    public void updateUi() {
        // update the car color to reflect premium status or lack thereof
        ((ImageView)findViewById(R.id.free_or_premium)).setImageResource(mIsPremium ? R.drawable.premium : R.drawable.free);

        // "Upgrade" button is only visible if the user is not premium
        findViewById(R.id.upgrade_button).setVisibility(mIsPremium ? View.GONE : View.VISIBLE);

        ImageView infiniteGasButton = (ImageView) findViewById(R.id.infinite_gas_button);
        if (mSubscribedToInfiniteGas) {
            // If subscription is active, show "Manage Infinite Gas"
            infiniteGasButton.setImageResource(R.drawable.manage_infinite_gas);
        } else {
            // The user does not have infinite gas, show "Get Infinite Gas"
            infiniteGasButton.setImageResource(R.drawable.get_infinite_gas);
        }

        // update gas gauge to reflect tank status
        if (mSubscribedToInfiniteGas) {
            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(R.drawable.gas_inf);
        }
        else {
            int index = mTank >= TANK_RES_IDS.length ? TANK_RES_IDS.length - 1 : mTank;
            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(TANK_RES_IDS[index]);
        }
    }
*/


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
            pref = new PreferenceApp(MyShowbuddyPlusActivity.this);
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

}
