package com.showbuddy4.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.showbuddy4.R;
import com.showbuddy4.activity.EditProfileActivity;
import com.showbuddy4.activity.MyShowbuddyPlusActivity;
import com.showbuddy4.activity.ProfileViewActivity;
import com.showbuddy4.activity.SettingsActivity;
import com.showbuddy4.model.GetProfileModel;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_User_Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String mParam1;
    String mParam2;

    PreferenceApp pref;
    ImageView iv_profile_photo;
    TextView tv_profile_name;
    TextView myshowbuddy_plus;

    String fb_profile_image, fb_profile_name;

    FloatingActionButton cardview_edit_profile;
    FloatingActionButton cardview_setting;
    ProgressDialog progressDialog;





    public Fragment_User_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_User_Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_User_Profile newInstance(String param1, String param2) {
        Fragment_User_Profile fragment = new Fragment_User_Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_user__profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pref = new PreferenceApp(getActivity());

        iv_profile_photo = (ImageView) view.findViewById(R.id.iv_profile_photo);
        tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
        myshowbuddy_plus = (TextView) view.findViewById(R.id.myshowbuddy_plus);
        cardview_edit_profile = (FloatingActionButton) view.findViewById(R.id.cardview_edit_profile);
        cardview_setting = (FloatingActionButton) view.findViewById(R.id.cardview_settings);
        fb_profile_image = pref.getProfilePhoto();
        fb_profile_name = pref.getFirstName() + " " + pref.getLastName();

        tv_profile_name.setText(fb_profile_name+ "," + pref.getAge());

        getUserProfile();

        cardview_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });


        cardview_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });

        iv_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String profile = "profile";
                String id = pref.getFbId();
                startActivity(new Intent(getActivity(),
                        ProfileViewActivity.class).putExtra("id", id).putExtra("profileview", profile));
                //startActivity(new Intent(getActivity(), ProfileViewActivity.class).putExtra("id", pref.getFbId()));
            }
        });

        myshowbuddy_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent in = new Intent(getActivity(), MyShowbuddyPlusActivity.class);
                startActivity(in);

            }
        });

    }

    public void getUserProfile() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<List<GetProfileModel>> call = apiService.getUserProfile(pref.getFbId(),pref.getFbId());

        call.enqueue(new Callback<List<GetProfileModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetProfileModel>> call, @NonNull Response<List<GetProfileModel>> response) {

                try {
                    List<GetProfileModel> list = response.body();
                    if (response.code() == 200) {
                        String emailid = list.get(0).getUserEmail();
                        String f_name = list.get(0).getFirstName();
                        String l_name = list.get(0).getLastName();
                        String gender = list.get(0).getGender();
                        String dob = list.get(0).getUserDob();
                        String location_name = list.get(0).getLocation();
                        String fbProfilePhoto1 = list.get(0).getFbUserPhotoUrl1();
                        String fbProfilePhoto2 = list.get(0).getFbUserPhotoUrl2();
                        String fbProfilePhoto3 = list.get(0).getFbUserPhotoUrl3();
                        String fbProfilePhoto4 = list.get(0).getFbUserPhotoUrl4();
                        String fbProfilePhoto5 = list.get(0).getFbUserPhotoUrl5();
                        String fbProfilePhoto6 = list.get(0).getFbUserPhotoUrl6();
                        String CollegeName = list.get(0).getCollegeName();
                        String About = list.get(0).getAbout();
                        String Age = list.get(0).getAge();
                        String Profession = list.get(0).getProfession();

/*                if (!TextUtils.isEmpty(fbProfilePhoto1)) {*/
                        Glide.with(getActivity())
                                .load(pref.getProfilePhoto())
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .centerCrop()
                                .into(iv_profile_photo);
                /*}*/


                        progressDialog.dismiss();
                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(@NonNull Call<List<GetProfileModel>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
