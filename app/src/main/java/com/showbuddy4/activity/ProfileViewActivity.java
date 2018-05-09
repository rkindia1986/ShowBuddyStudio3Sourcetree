package com.showbuddy4.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.showbuddy4.R;
import com.showbuddy4.model.GetProfileModel;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewActivity extends AppCompatActivity {

    ImageView img_profile,imgBack;
    EditText edt_about, edt_current_work, edt_education;
    RadioGroup radioSexGroup;
    RadioButton rb_male, rb_female;
    ProgressDialog progressDialog;
    String id;
    TextView txt_name, txtawayvalue;
    TextView workvalue, collegeValue,tvGender;
    TextView txtProgressValue;
    ProgressBar loadingImage;
    PreferenceApp pref;
    SeekBar seekProfileComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile_view);
        pref = new PreferenceApp(ProfileViewActivity.this);
        /*if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }*/

        Intent i = getIntent();
        try {
            if (i.hasExtra("profileview")) {
                id = getIntent().getStringExtra("id");
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //radio button
        radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);
        seekProfileComplete = (SeekBar) findViewById(R.id.seekProfileComplete);

        loadingImage = (ProgressBar) findViewById(R.id.loadingImage);

        //textview
        txt_name = (TextView) findViewById(R.id.txt_name);
        txtawayvalue = (TextView) findViewById(R.id.txtawayvalue);
        workvalue = (TextView) findViewById(R.id.workvalue);
        collegeValue = (TextView) findViewById(R.id.collegeValue);
        txtProgressValue = (TextView) findViewById(R.id.txtProgressValue);
        //imageview
        img_profile = (ImageView) findViewById(R.id.img_profile);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        //edit text
        edt_current_work = (EditText) findViewById(R.id.edt_current_work);
        edt_about = (EditText) findViewById(R.id.edt_about);
        edt_education = (EditText) findViewById(R.id.edt_education);
        tvGender=(TextView)findViewById(R.id.tvGender);
        getUserProfile();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getUserProfile() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<List<GetProfileModel>> call = apiService.getUserProfile(id, pref.getFbId());

        call.enqueue(new Callback<List<GetProfileModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<GetProfileModel>> call, @NonNull Response<List<GetProfileModel>> response) {
                try {
                    List<GetProfileModel> list = response.body();
                    if (response.code() == 200) {

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
                        String Away = String.valueOf(list.get(0).getAway());
                        String ProfileComPercentage = String.valueOf(list.get(0).getRatingpercentage());
                        if (!TextUtils.isEmpty(f_name)) {
                            txt_name.setText(f_name + " " + l_name + "," + " " + Age);
                        }
                        if (!TextUtils.isEmpty(About)) {
                            edt_about.setText(About);
                        }
                        if (!TextUtils.isEmpty(Profession)) {
                            edt_current_work.setText(Profession);
                        }
                        if (!TextUtils.isEmpty(CollegeName)) {
                            edt_education.setText(CollegeName);
                        }
                        if (!TextUtils.isEmpty(Away)) {
                            txtawayvalue.setText(Away + " " + list.get(0).getDistin() + " Away");
                        }
                        if (!TextUtils.isEmpty(Away)) {
                            collegeValue.setText(CollegeName);
                        }
                        if (!TextUtils.isEmpty(Away)) {
                            workvalue.setText(Profession);
                        }

                        if (!TextUtils.isEmpty(ProfileComPercentage)) {
                            Integer integer = Integer.parseInt(ProfileComPercentage);
                            seekProfileComplete.setProgress(integer);

                            txtProgressValue.setText(ProfileComPercentage + " % ");
                        }
                        if (gender.equalsIgnoreCase("male")) {
                            radioSexGroup.check(rb_male.getId());
                            tvGender.setText("Male");
                        }  if (gender.equalsIgnoreCase("female")) {
                            radioSexGroup.check(rb_female.getId());
                            tvGender.setText("Female");
                        }

                        if(gender.equalsIgnoreCase("couple")){
                            tvGender.setText("Couple");
                        }


                        if (!TextUtils.isEmpty(fbProfilePhoto1)) {

                            loadingImage.setVisibility(View.VISIBLE);
                            Glide.with(ProfileViewActivity.this)
                                    .load(fbProfilePhoto1)
                                    .asBitmap()
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .fitCenter()
                                    .listener(new RequestListener<String, Bitmap>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {

                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            loadingImage.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(img_profile);
                        }
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

}
