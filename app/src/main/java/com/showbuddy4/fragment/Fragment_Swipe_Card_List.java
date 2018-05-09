package com.showbuddy4.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.showbuddy4.R;
import com.showbuddy4.Utils.CommonUses;
import com.showbuddy4.Utils.PreferenceHelper;
import com.showbuddy4.activity.ProfileViewActivity;
import com.showbuddy4.cardstack.SwipeStack;
import com.showbuddy4.helper.CustomViewPager;
import com.showbuddy4.model.Data;
import com.showbuddy4.model.LoginModel;
import com.showbuddy4.model.SwipeAction;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;
import com.showbuddy4.util.AppUtils;
import com.showbuddy4.util.IabBroadcastReceiver;
import com.showbuddy4.util.IabHelper;
import com.showbuddy4.util.IabResult;
import com.showbuddy4.util.Inventory;
import com.showbuddy4.util.ObjectSerializer;
import com.showbuddy4.util.Purchase;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Swipe_Card_List extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String mParam1;
    String mParam2;

    private ArrayList<Data> array;
    private ArrayList<Data> arrayUndo;
    PreferenceApp pref;
    RippleBackground rippleBackground;
    ImageView imageView;
    LinearLayout ll_card_layout;

    int count = 0;

    private SwipeStack cardStack;
    private CardsAdapter cardsAdapter;

    private int currentPosition;
    private int currentPosition1;

    private PreferenceHelper sharedpreference;

    FloatingActionButton fab_cancel, fab_super_like, fab_like, fab_undo;

    String strswipe = "";

    boolean superlikeboolean = true;

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

    static final int RC_REQUEST = 10001;

    IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    public Fragment_Swipe_Card_List() {
        // Required empty public constructor
    }

    int a;
    int a1;

    View alertLayout;
    View alertLayout2;

    LinearLayout linear_firstsubscription;
    LinearLayout linear_secondsubscription;
    LinearLayout linear_thirdsubscription;

    LinearLayout linear_firstsubscriptionplan;
    LinearLayout linear_secondsubscriptionplan;
    LinearLayout linear_thirdsubscriptionplan;

    Button btncontinue;
    Button btnsubscriptioncontinue;

    // TODO: Rename and change types and number of parameters
    public static Fragment_Swipe_Card_List newInstance(String param1, String param2) {
        Fragment_Swipe_Card_List fragment = new Fragment_Swipe_Card_List();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment__swipe__card__list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pref = new PreferenceApp(getActivity());
        ll_card_layout = (LinearLayout) view.findViewById(R.id.ll_card_layout);
        rippleBackground = (RippleBackground) view.findViewById(R.id.content);
        imageView = (ImageView) view.findViewById(R.id.centerImage);
        cardStack = (SwipeStack) view.findViewById(R.id.container);
        fab_like = (FloatingActionButton) view.findViewById(R.id.fab_like);
        fab_cancel = (FloatingActionButton) view.findViewById(R.id.fab_cancel);
        fab_undo = (FloatingActionButton) view.findViewById(R.id.fab_undo);
        fab_undo.setEnabled(false);
        fab_super_like = (FloatingActionButton) view.findViewById(R.id.fab_super_like);
        rippleBackground.startRippleAnimation();
        Log.e("Swipe","Profile pic--"+pref.getProfilePhoto());
        Glide.with(getActivity())
                .load(pref.getProfilePhoto())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                       Log.e("Swipe","prof ex"+e+"<");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                }).into(imageView);

        array = new ArrayList<>();
        sharedpreference = new PreferenceHelper(getActivity(), "ShowBuddy");
        sharedpreference.initPref();
        currentPosition = 0;

        LayoutInflater inflater = getActivity().getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.dialog_inapppurchase, null);
        linear_firstsubscription = (LinearLayout) alertLayout.findViewById(R.id.linear_firstsubscription);
        linear_secondsubscription = (LinearLayout) alertLayout.findViewById(R.id.linear_secondsubscription);
        linear_thirdsubscription = (LinearLayout) alertLayout.findViewById(R.id.linear_thirdsubscription);
        btncontinue = (Button) alertLayout.findViewById(R.id.btncontinue);

        LayoutInflater inflater2 = getActivity().getLayoutInflater();
        alertLayout2 = inflater2.inflate(R.layout.dialog_subscriptionplans, null);
        linear_firstsubscriptionplan = (LinearLayout) alertLayout2.findViewById(R.id.linear_firstsubscriptionpplan);
        linear_secondsubscriptionplan = (LinearLayout) alertLayout2.findViewById(R.id.linear_secondsubscriptionplan);
        linear_thirdsubscriptionplan = (LinearLayout) alertLayout2.findViewById(R.id.linear_thirdsubscriptionplan);
        btnsubscriptioncontinue = (Button) alertLayout2.findViewById(R.id.btnsubscriptioncontinue);

        //Handling swipe event of Cards stack
        cardStack.setListener(new SwipeStack.SwipeStackListener() {
            @Override
            public void onViewSwipedToLeft(int position) {
                fab_undo.setEnabled(true);
                String profileid = array.get(currentPosition).getProfile_id();
                arrayUndo = new ArrayList<>();
                arrayUndo.add(new Data(profileid, array.get(0).getImagePath(), array.get(0).getDescription(), array.get(0).getImages_list(),array.get(0).getProffesion(),
                        array.get(0).getAge(),array.get(0).getCollage()));
                LikeDislike(profileid, "dislike");
                currentPosition = position + 1;
                strswipe = "left";
                count = 0;
                a = 1;
            }

            @Override
            public void onViewSwipedToRight(int position) {
                fab_undo.setEnabled(true);
                String profileid = array.get(currentPosition).getProfile_id();
                arrayUndo = new ArrayList<>();
                arrayUndo.add(new Data(profileid, array.get(0).getImagePath(), array.get(0).getDescription(), array.get(0).getImages_list(),array.get(0).getProffesion(),
                        array.get(0).getAge(),array.get(0).getCollage()));
                LikeDislike(profileid, "like");
                currentPosition = position + 1;
                strswipe = "right";
                count = 0;
                a1 = 1;
            }

            @Override
            public void onViewSwipedToTop(int position) {
                fab_undo.setEnabled(true);
                String profileid = array.get(currentPosition).getProfile_id();
                arrayUndo = new ArrayList<>();
                arrayUndo.add(new Data(profileid, array.get(0).getImagePath(), array.get(0).getDescription(), array.get(0).getImages_list(),array.get(0).getProffesion(),
                        array.get(0).getAge(),array.get(0).getCollage()));
                if(superlikeboolean==true) {
                    LikeDislike(profileid, "superlike");
                }
                currentPosition = position + 1;
                count = 0;
            }

            @Override
            public void onStackEmpty() {
                geAllUsers();
            }
        });

        cardStack.setSwipeProgressListener(new SwipeStack.SwipeProgressListener() {
            @Override
            public void onSwipeStart(int position) {
            }

            @Override
            public void onSwipeProgress(int position, float progress, int swipeposition) {
                View view = cardStack.getTopView();
                view.findViewById(R.id.background).setAlpha(0);
                if (swipeposition == 2) {
                    view.findViewById(R.id.item_swipe_up_indicator).setAlpha(progress < 0 ? -progress : 0);
                }
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(progress < 0 ? -progress : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(progress > 0 ? progress : 0);
            }

            @Override
            public void onSwipeEnd(int position) {
               /* View view = cardStack.getTopView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(0);*/
            }
        });

        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardStack.swipeTopViewToLeft();
                String profileid = array.get(currentPosition).getProfile_id();
                LikeDislike(profileid, "dislike");
                count = 0;
            }
        });

        fab_super_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profileid = array.get(currentPosition).getProfile_id();
                LikeDislike(profileid, "superlike");
                count = 0;
                if (superlikeboolean == true) {
                    cardStack.swipeTopViewToTop();
                }
            }
        });

        fab_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardStack.swipeTopViewToRight();
                String profileid = array.get(currentPosition).getProfile_id();
                LikeDislike(profileid, "like");
                count = 0;
            }
        });

        fab_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                undoSwipeAction(arrayUndo.get(0).getProfile_id());
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_card_layout.setVisibility(View.VISIBLE);

                if (sharedpreference.LoadStringPref(AppUtils.UNDO, "").isEmpty()) {
                    sharedpreference.clearAllPrefs();
                    ll_card_layout.setVisibility(View.VISIBLE);
                    geAllUsers();

                } else {
                    ll_card_layout.setVisibility(View.VISIBLE);
                    SharedPreferences prefs = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
                    try {
                        array = (ArrayList) ObjectSerializer.deserialize(prefs.getString("Data", ObjectSerializer.serialize(new ArrayList())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    cardsAdapter = new CardsAdapter(getActivity(), array);
                    cardStack.setAdapter(cardsAdapter);
                    cardsAdapter.notifyDataSetChanged();
                    sharedpreference.clearAllPrefs();
                }
            }
        }, 3000);


    }

    private void undoSwipeAction(String UserProfileId) {
        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<SwipeAction> call = apiService.undoSwipeAction(UserProfileId, pref.getFbId());

        call.enqueue(new Callback<SwipeAction>() {
            @Override
            public void onResponse(@NonNull Call<SwipeAction> call, @NonNull Response<SwipeAction> response) {
                try {
                    if (response.code() == 200) {
                        String status = response.body().getStatus();
                        if (status.equalsIgnoreCase("1")) {
                            if (strswipe.equals("left")) {
                                CommonUses.showToast(getContext(), String.valueOf(cardStack.getCurrentPosition()));
                                currentPosition1 = currentPosition;

                                if (a == 1) {
                                    ArrayList<Data> tempArray = new ArrayList<>();
                                    int position = currentPosition - 1;
                                    for (int i = position; i < array.size(); i++) {
                                        tempArray.add(array.get(i));

                                    }

                                    array = tempArray;
                                    cardsAdapter = new CardsAdapter(getActivity(), array);
                                    cardStack.setAdapter(cardsAdapter);
                                    cardsAdapter.notifyDataSetChanged();
                                    sharedpreference.SaveStringPref(AppUtils.UNDO, "Undo");
                                    sharedpreference.ApplyPref();
                                    SharedPreferences prefs = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    try {
                                        editor.putString("Data", ObjectSerializer.serialize(array));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    editor.commit();
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.detach(Fragment_Swipe_Card_List.this).attach(Fragment_Swipe_Card_List.this).commit();
                                    rippleBackground.stopRippleAnimation();
                                    rippleBackground.setVisibility(View.GONE);
                                }
                                a = 0;
                                currentPosition1 = 0;
                            } else {

                                CommonUses.showToast(getContext(), String.valueOf(currentPosition));
                                cardStack.setScrollX(currentPosition - 1);
                                if (a1 == 1) {
                                    ArrayList<Data> tempArray = new ArrayList<>();
                                    int position = currentPosition - 1;
                                    for (int i = position; i < array.size(); i++) {
                                        tempArray.add(array.get(i));

                                    }

                                    array = tempArray;
                                    cardsAdapter = new CardsAdapter(getActivity(), array);
                                    cardStack.setAdapter(cardsAdapter);
                                    cardsAdapter.notifyDataSetChanged();
                                    sharedpreference.SaveStringPref(AppUtils.UNDO, "Undo");
                                    sharedpreference.ApplyPref();
                                    SharedPreferences prefs = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    try {
                                        editor.putString("Data", ObjectSerializer.serialize(array));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    editor.commit();
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.detach(Fragment_Swipe_Card_List.this).attach(Fragment_Swipe_Card_List.this).commit();
                                    rippleBackground.stopRippleAnimation();
                                    rippleBackground.setVisibility(View.GONE);
                                }
                                a1 = 0;
                            }

                        } else if (status.equalsIgnoreCase("0")) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            if(alertLayout2.getParent()!=null)
                                ((ViewGroup)alertLayout2.getParent()).removeView(alertLayout2);
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
                                    dialog.cancel();
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = alert.create();
                            dialog.show();
                           /* if(alertLayout2.getParent()!=null)
                                ((ViewGroup)alertLayout2.getParent()).removeView(alertLayout2);
                            alert.setView(alertLayout2);*/


                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SwipeAction> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void LikeDislike(String UserProfileId, final String Action) {
        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<SwipeAction> call = apiService.postSwipeAction(UserProfileId, pref.getFbId(), Action);

        call.enqueue(new Callback<SwipeAction>() {
            @Override
            public void onResponse(@NonNull Call<SwipeAction> call, @NonNull Response<SwipeAction> response) {
                try {
                    if (response.code() == 200) {
                        String status = response.body().getStatus();

                        if (Action.equals("superlike")) {
                            if (status.equalsIgnoreCase("1")) {
                                superlikeboolean = true;
                            } else if (status.equalsIgnoreCase("0")) {
                                superlikeboolean = false;
                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                if(alertLayout.getParent()!=null)
                                    ((ViewGroup)alertLayout.getParent()).removeView(alertLayout);
                                alert.setView(alertLayout);
                                // this is set the view from XML inside AlertDialog
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
                                        dialog.cancel();
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = alert.create();
                                dialog.show();


                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SwipeAction> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void geAllUsers() {
        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<List<LoginModel>> call = apiService.getAllUsersResponse(pref.getFbId());

        call.enqueue(new Callback<List<LoginModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<LoginModel>> call, @NonNull Response<List<LoginModel>> response) {
                try {
                    if (response.code() == 200) {
                        List<LoginModel> list = response.body();
                        ArrayList<String> images;
                        for (int i = 0; i < list.size(); i++) {
                            images = new ArrayList<>();
                            if (!TextUtils.isEmpty(list.get(i).getFbUserPhotoUrl1())) {
                                images.add(list.get(i).getFbUserPhotoUrl1());
                            }
                            if (!TextUtils.isEmpty(list.get(i).getFbUserPhotoUrl2())) {
                                images.add(list.get(i).getFbUserPhotoUrl2());
                            }
                            if (!TextUtils.isEmpty(list.get(i).getFbUserPhotoUrl3())) {
                                images.add(list.get(i).getFbUserPhotoUrl3());
                            }
                            if (!TextUtils.isEmpty(list.get(i).getFbUserPhotoUrl4())) {
                                images.add(list.get(i).getFbUserPhotoUrl4());
                            }
                            if (!TextUtils.isEmpty(list.get(i).getFbUserPhotoUrl5())) {
                                images.add(list.get(i).getFbUserPhotoUrl5());
                            }
                            if (!TextUtils.isEmpty(list.get(i).getFbUserPhotoUrl6())) {
                                images.add(list.get(i).getFbUserPhotoUrl6());
                            }
                            //
                            array.add(new Data(list.get(i).getFbProfileId(), list.get(i).getFbUserPhotoUrl1(), list.get(i).getProfileName(), images,list.get(i).getProfession(),
                                    list.get(i).getAge(),list.get(i).getCollegeName()));
                        }

                        cardsAdapter = new CardsAdapter(getActivity(), array);
                        cardStack.setAdapter(cardsAdapter);

                        cardsAdapter.notifyDataSetChanged();
                        rippleBackground.stopRippleAnimation();
                        rippleBackground.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    rippleBackground.stopRippleAnimation();
                    rippleBackground.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LoginModel>> call, @NonNull Throwable t) {
                rippleBackground.stopRippleAnimation();
                rippleBackground.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public class CardsAdapter extends BaseAdapter {

        private Activity activity;
        private final static int AVATAR_WIDTH = 150;
        private final static int AVATAR_HEIGHT = 300;
        private List<Data> data;
        private static final int PROGRESS_COUNT = 6;

        public CardsAdapter(Activity activity, List<Data> data) {
            this.data = data;
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Data getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            // If holder not exist then locate all view from UI file.
            if (convertView == null) {
                // inflate UI from XML file
                convertView = inflater.inflate(R.layout.item_card_swipe_list, parent, false);
                // get all UI view
                holder = new ViewHolder(convertView);
                // set tag for holder
                convertView.setTag(holder);
            } else {
                // if holder created, get tag from view
                holder = (ViewHolder) convertView.getTag();
            }

            //setting data to views




            Glide.with(getActivity()).load(getItem(position).getImagePath()).into(holder.cardImage);

            holder.img_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("getProID",getItem(position).getProfile_id());
                    //CommonUses.showToast(getActivity(), getItem(position).getProfile_id());
                    String profile = "profile";
                    String id = getItem(position).getProfile_id();
                    startActivity(new Intent(getActivity(),
                            ProfileViewActivity.class).putExtra("id", id).putExtra("profileview", profile));
                }
            });

if(getItem(position).getAge()!=null){
    holder.bookText.setText(getItem(position).getDescription()+","+getItem(position).getAge());
}else {
    holder.bookText.setText(getItem(position).getDescription());
}

if(getItem(position).getProffesion()!=null){
    holder.tvProffesion.setVisibility(View.VISIBLE);
    holder.tvProffesion.setText(getItem(position).getProffesion());
}else {
    holder.tvProffesion.setVisibility(View.GONE);

}

            if(getItem(position).getCollage()!=null){
                holder.tvCollage.setVisibility(View.VISIBLE);
                holder.tvCollage.setText(getItem(position).getCollage());
            }else {
                holder.tvCollage.setVisibility(View.GONE);

            }
            return convertView;
        }

        private class ViewHolder {
            private ImageView cardImage;
            private TextView bookText,tvProffesion,tvCollage;
            private StoriesProgressView storiesProgressView;
            private CustomViewPager viewpagercustom;
            private View skip;
            private ImageView img_info;

            public ViewHolder(View view) {
                cardImage = (ImageView) view.findViewById(R.id.cardImage);
                img_info = (ImageView) view.findViewById(R.id.img_info);
                bookText = (TextView) view.findViewById(R.id.bookText);
                viewpagercustom = (CustomViewPager) view.findViewById(R.id.viewpagercustom);
                storiesProgressView = (StoriesProgressView) view.findViewById(R.id.stories);
                skip = (View) view.findViewById(R.id.skip);
                tvProffesion = (TextView) view.findViewById(R.id.tvProffession);
                tvCollage = (TextView) view.findViewById(R.id.tvcollage);


            }
        }
    }

    public class SliderPagerAdapter extends PagerAdapter {
        Context context;
        LinearLayout ll;
        List<String> data;
        String TAG = "SliderPagerAdapter";
        boolean flag;
        private LayoutInflater layoutInflater;

        public SliderPagerAdapter(Context context, ArrayList<String> images, boolean flag) {
            super();
            this.context = context;
            this.data = images;
            this.flag = flag;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slidingimages_layout, container, false);
            ImageView im_slider = (ImageView) view.findViewById(R.id.grid_item);
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.viewPager_row);

            view.setTag(position);
            Log.d("img1", data.get(position));

            Glide.with(context).load(data.get(position)).into(im_slider);

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return null == data ? 0 : data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//        View view = (View) object;
//        container.removeView(view);
            try {
                // Remove the view from the container
                container.removeView((View) object);

                // Try to clear resources used for displaying this view
                Glide.clear(((View) object).findViewById(R.id.grid_item));
                // Remove any resources used by this view
                unbindDrawables((View) object);
                // Invalidate the object
                object = null;
            } catch (Exception e) {
                Log.w(TAG, "destroyItem: failed to destroy item and clear it's used resources", e);
            }
        }

        protected void unbindDrawables(View view) {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                ((ViewGroup) view).removeAllViews();
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void InAppPurchase() {
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
        if (getActivity().getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);

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
                mBroadcastReceiver = new IabBroadcastReceiver(getContext());
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                getActivity().registerReceiver(mBroadcastReceiver, broadcastFilter);

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

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
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


    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    // User clicked the "Upgrade to Premium" button.
    public void onUpgradeAppButtonClicked(String selectedSKU) {
        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
//        setWaitScreen(true);

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            mHelper.launchPurchaseFlow(getActivity(), SELECTED_SKU, RC_REQUEST,
                    mPurchaseFinishedListener, payload);

        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            getActivity().unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    public void updateSettingsAPI(String originalJson) {

        try {

            JSONObject purchasedDetailObj = new JSONObject(originalJson);

            PreferenceApp pref;
            pref = new PreferenceApp(getActivity());
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


            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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


}


