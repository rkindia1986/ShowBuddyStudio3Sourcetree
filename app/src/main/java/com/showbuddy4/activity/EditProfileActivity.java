package com.showbuddy4.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.nanchen.compresshelper.CompressHelper;
import com.showbuddy4.FinalDragDrop.ActiivityDragDropList;
import com.showbuddy4.R;
import com.showbuddy4.Utils.CommonUses;
import com.showbuddy4.model.FbAlbum;
import com.showbuddy4.model.FbImages;
import com.showbuddy4.model.GetFbImage;
import com.showbuddy4.model.GetProfileModel;
import com.showbuddy4.model.GetTopTenListModel;
import com.showbuddy4.model.LoginModel;
import com.showbuddy4.model.SwipeAction;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = EditProfileActivity.class.getSimpleName();
    PreferenceApp pref;
    ImageView img_first_photo, img_second_photo, img_third_photo, img_fourth_photo, img_fifth_photo, img_sixth_photo;
    ProgressBar progress_bar_first_photo, progress_bar_second_photo, progress_bar_third_photo, progress_bar_fourth_photo, progress_bar_fifth_photo, progress_bar_sixth_photo;
    ImageView img_back;
    EditText edt_about, edt_current_work, edt_education;
    String about, firstname, current_work, education, gender;
    TextView txt_about, txt_update_profile;
    RadioGroup radioSexGroup;
    RadioButton rb_male, rb_female;
    //String rg_gender;
    ProgressDialog progressDialog;
    ProgressDialog pb;

    private static final int PERMISSION_REQUEST_CODE = 200;
    protected static final int REQUEST_CAMERA = 1888;
    private static int RESULT_LOAD_IMAGE = 1;
     List<FbImages>fbImagesList=new ArrayList<>();;
    private Uri imageUriglobal;

    private ArrayList<String> images;

    private Uri selectedImageUri1;
    private Bitmap thumbnail1;
    private String selectedpicturePath1 = "";
    private File imgFile1;

    private Uri selectedImageUri2;
    private Bitmap thumbnail2;
    private String selectedpicturePath2 = "";
    private File imgFile2;

    private Uri selectedImageUri3;
    private Bitmap thumbnail3;
    private String selectedpicturePath3 = "";
    private File imgFile3;

    private Uri selectedImageUri4;
    private Bitmap thumbnail4;
    private String selectedpicturePath4 = "";
    private File imgFile4;

    private Uri selectedImageUri5;
    private Bitmap thumbnail5;
    private String selectedpicturePath5 = "";
    private File imgFile5;


    private Uri selectedImageUri6;
    private Bitmap thumbnail6;
    private String selectedpicturePath6 = "";
    private File imgFile6;

    private boolean photo1clicked = false;
    private boolean photo2clicked = false;
    private boolean photo3clicked = false;
    private boolean photo4clicked = false;
    private boolean photo5clicked = false;
    private boolean photo6clicked = false;

    private int totalNumberofImages = 0;

    private boolean tensoloartistsreportLnExpand = false;

    private boolean tenbandssreportLnExpand = false;

    private boolean tenliveactsreportLnExpand = false;

    private boolean tenemergingactsreportLnExpand = false;

    private boolean fifteengenresreportLnExpand = false;

    ImageView img_remove_first_photo, img_remove_second_photo, img_remove_third_photo, img_remove_fourth_photo, img_remove_fifth_photo, img_remove_sixth_photo;

    LinearLayout tensoloartistsLn, tensoloartistsreportLn, tenbandsLn, tenbandsreportLn, tenliveactsLn, tenliveactsreportLn;

    LinearLayout tenemergingactsLn, tenemergingactsreportLn, fifteengenresLn, fifteengenresreportLn;

    EditText edtartist1, edtartist2, edtartist3, edtartist4, edtartist5, edtartist6, edtartist7, edtartist8, edtartist9, edtartist10;

    EditText edtband1, edtband2, edtband3, edtband4, edtband5, edtband6, edtband7, edtband8, edtband9, edtband10;

    EditText edtliveacts1, edtliveacts2, edtliveacts3, edtliveacts4, edtliveacts5, edtliveacts6, edtliveacts7, edtliveacts8, edtliveacts9, edtliveacts10;

    EditText edtemergingacts1, edtemergingacts2, edtemergingacts3, edtemergingacts4, edtemergingacts5, edtemergingacts6, edtemergingacts7, edtemergingacts8, edtemergingacts9, edtemergingacts10;

    EditText edtgenres1, edtgenres2, edtgenres3, edtgenres4, edtgenres5, edtgenres6, edtgenres7, edtgenres8, edtgenres9, edtgenres10,
            edtgenres11, edtgenres12, edtgenres13, edtgenres14, edtgenres15;

String relationSheep="";
    Spinner spGender,spRelationship;
    String strArtists = "Top 10 Solo Artists";
    String strBands = "Top 10 Bands";
    String strLiveacts = "Top 10 Live Acts";
    String strEmergingActs = "Top 10 Emerging Acts";
    String strGeneres = "Top 15 Genres";

    String stredtartist1 = "";
    String stredtartist2 = "";
    String stredtartist3 = "";
    String stredtartist4 = "";
    String stredtartist5 = "";
    String stredtartist6 = "";
    String stredtartist7 = "";
    String stredtartist8 = "";
    String stredtartist9 = "";
    String stredtartist10 = "";

    String stredtband1 = "";
    String stredtband2 = "";
    String stredtband3 = "";
    String stredtband4 = "";
    String stredtband5 = "";
    String stredtband6 = "";
    String stredtband7 = "";
    String stredtband8 = "";
    String stredtband9 = "";
    String stredtband10 = "";

    String stredtliveacts1 = "";
    String stredtliveacts2 = "";
    String stredtliveacts3 = "";
    String stredtliveacts4 = "";
    String stredtliveacts5 = "";
    String stredtliveacts6 = "";
    String stredtliveacts7 = "";
    String stredtliveacts8 = "";
    String stredtliveacts9 = "";
    String stredtliveacts10 = "";

    String stredtemergingacts1 = "";
    String stredtemergingacts2 = "";
    String stredtemergingacts3 = "";
    String stredtemergingacts4 = "";
    String stredtemergingacts5 = "";
    String stredtemergingacts6 = "";
    String stredtemergingacts7 = "";
    String stredtemergingacts8 = "";
    String stredtemergingacts9 = "";
    String stredtemergingacts10 = "";

    String stredtgenres1 = "";
    String stredtgenres2 = "";
    String stredtgenres3 = "";
    String stredtgenres4 = "";
    String stredtgenres5 = "";
    String stredtgenres6 = "";
    String stredtgenres7 = "";
    String stredtgenres8 = "";
    String stredtgenres9 = "";
    String stredtgenres10 = "";
    String stredtgenres11 = "";
    String stredtgenres12 = "";
    String stredtgenres13 = "";
    String stredtgenres14 = "";
    String stredtgenres15 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_profile);

        pref = new PreferenceApp(this);
        about = pref.getAbout();
        firstname = pref.getFirstName();
        current_work = pref.getProfession();
        education = pref.getCollegeName();
        gender = pref.getGender();
        relationSheep=pref.getRelationshipStatus();

        //radio button
        radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);
        tensoloartistsLn = (LinearLayout) findViewById(R.id.tensoloartistsLn);
        tensoloartistsreportLn = (LinearLayout) findViewById(R.id.tensoloartistsreportLn);
        tenbandsLn = (LinearLayout) findViewById(R.id.tenbandsLn);
        tenbandsreportLn = (LinearLayout) findViewById(R.id.tenbandsreportLn);
        tenliveactsLn = (LinearLayout) findViewById(R.id.tenliveactsLn);
        tenliveactsreportLn = (LinearLayout) findViewById(R.id.tenliveactsreportLn);
        tenliveactsLn = (LinearLayout) findViewById(R.id.tenliveactsLn);
        tenliveactsreportLn = (LinearLayout) findViewById(R.id.tenliveactsreportLn);
        tenemergingactsLn = (LinearLayout) findViewById(R.id.tenemergingactsLn);
        tenemergingactsreportLn = (LinearLayout) findViewById(R.id.tenemergingactsreportLn);
        fifteengenresLn = (LinearLayout) findViewById(R.id.fifteengenresLn);
        fifteengenresreportLn = (LinearLayout) findViewById(R.id.fifteengenresreportLn);

        edtartist1 = (EditText) findViewById(R.id.edtartist1);
        edtartist2 = (EditText) findViewById(R.id.edtartist2);
        edtartist3 = (EditText) findViewById(R.id.edtartist3);
        edtartist4 = (EditText) findViewById(R.id.edtartist4);
        edtartist5 = (EditText) findViewById(R.id.edtartist5);
        edtartist6 = (EditText) findViewById(R.id.edtartist6);
        edtartist7 = (EditText) findViewById(R.id.edtartist7);
        edtartist8 = (EditText) findViewById(R.id.edtartist8);
        edtartist9 = (EditText) findViewById(R.id.edtartist9);
        edtartist10 = (EditText) findViewById(R.id.edtartist10);

        edtband1 = (EditText) findViewById(R.id.edtband1);
        edtband2 = (EditText) findViewById(R.id.edtband2);
        edtband3 = (EditText) findViewById(R.id.edtband3);
        edtband4 = (EditText) findViewById(R.id.edtband4);
        edtband5 = (EditText) findViewById(R.id.edtband5);
        edtband6 = (EditText) findViewById(R.id.edtband6);
        edtband7 = (EditText) findViewById(R.id.edtband7);
        edtband8 = (EditText) findViewById(R.id.edtband8);
        edtband9 = (EditText) findViewById(R.id.edtband9);
        edtband10 = (EditText) findViewById(R.id.edtband10);

        edtliveacts1 = (EditText) findViewById(R.id.edtliveacts1);
        edtliveacts2 = (EditText) findViewById(R.id.edtliveacts2);
        edtliveacts3 = (EditText) findViewById(R.id.edtliveacts3);
        edtliveacts4 = (EditText) findViewById(R.id.edtliveacts4);
        edtliveacts5 = (EditText) findViewById(R.id.edtliveacts5);
        edtliveacts6 = (EditText) findViewById(R.id.edtliveacts6);
        edtliveacts7 = (EditText) findViewById(R.id.edtliveacts7);
        edtliveacts8 = (EditText) findViewById(R.id.edtliveacts8);
        edtliveacts9 = (EditText) findViewById(R.id.edtliveacts9);
        edtliveacts10 = (EditText) findViewById(R.id.edtliveacts10);

        edtemergingacts1 = (EditText) findViewById(R.id.edtemergingacts1);
        edtemergingacts2 = (EditText) findViewById(R.id.edtemergingacts2);
        edtemergingacts3 = (EditText) findViewById(R.id.edtemergingacts3);
        edtemergingacts4 = (EditText) findViewById(R.id.edtemergingacts4);
        edtemergingacts5 = (EditText) findViewById(R.id.edtemergingacts5);
        edtemergingacts6 = (EditText) findViewById(R.id.edtemergingacts6);
        edtemergingacts7 = (EditText) findViewById(R.id.edtemergingacts7);
        edtemergingacts8 = (EditText) findViewById(R.id.edtemergingacts8);
        edtemergingacts9 = (EditText) findViewById(R.id.edtemergingacts9);
        edtemergingacts10 = (EditText) findViewById(R.id.edtemergingacts10);

        edtgenres1 = (EditText) findViewById(R.id.edtgenres1);
        edtgenres2 = (EditText) findViewById(R.id.edtgenres2);
        edtgenres3 = (EditText) findViewById(R.id.edtgenres3);
        edtgenres4 = (EditText) findViewById(R.id.edtgenres4);
        edtgenres5 = (EditText) findViewById(R.id.edtgenres5);
        edtgenres6 = (EditText) findViewById(R.id.edtgenres6);
        edtgenres7 = (EditText) findViewById(R.id.edtgenres7);
        edtgenres8 = (EditText) findViewById(R.id.edtgenres8);
        edtgenres9 = (EditText) findViewById(R.id.edtgenres9);
        edtgenres10 = (EditText) findViewById(R.id.edtgenres10);
        edtgenres11 = (EditText) findViewById(R.id.edtgenres11);
        edtgenres12 = (EditText) findViewById(R.id.edtgenres12);
        edtgenres13 = (EditText) findViewById(R.id.edtgenres13);
        edtgenres14 = (EditText) findViewById(R.id.edtgenres14);
        edtgenres15 = (EditText) findViewById(R.id.edtgenres15);
        spGender=(Spinner)findViewById(R.id.spGender);
        spRelationship=(Spinner)findViewById(R.id.spRelationship);

        setGender(gender);
        setRelationSheep(relationSheep);


        ClickListener();
        getfullTopTenList("solo_artists");
        getfullTopTenList("live_acts");
        getfullTopTenList("bands_groups");
        getfullTopTenList("emerging_acts");
        getfullTopTenList("genres");

        Intent i = getIntent();
        if (i != null) {
            i.putExtra("id", "");
            setResult(111, i);
        }

        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
      //  rg_gender = (String) radioSexButton.getText();
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
                //rg_gender = (String) radioSexButton.getText();
            }
        });


        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender=adapterView.getItemAtPosition(i).toString();
                Log.e("IN sp Adapter",gender+"<<");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spRelationship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                relationSheep=adapterView.getItemAtPosition(i).toString();
                Log.e("IN relationSheep ",relationSheep+"<<");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

           tensoloartistsLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expandOrCollapsenew(tensoloartistsreportLn, tensoloartistsreportLnExpand, 1);
                tensoloartistsreportLn.setVisibility(View.VISIBLE);
                edtartist10.requestFocus();
            }
        });
        tenbandsLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expandOrCollapsenew(tenbandsreportLn, tenbandssreportLnExpand, 2);
                tenbandsreportLn.setVisibility(View.VISIBLE);
                edtband10.requestFocus();
            }
        });
        tenliveactsLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expandOrCollapsenew(tenliveactsreportLn, tenliveactsreportLnExpand, 3);
                tenliveactsreportLn.setVisibility(View.VISIBLE);
                edtliveacts10.requestFocus();
            }
        });
        tenemergingactsLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expandOrCollapsenew(tenemergingactsreportLn, tenemergingactsreportLnExpand, 4);
                tenemergingactsreportLn.setVisibility(View.VISIBLE);
                edtemergingacts10.requestFocus();
            }
        });
        fifteengenresLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EditProfileActivity.this, ActiivityDragDropList.class));

//                expandOrCollapsenew(fifteengenresreportLn, fifteengenresreportLnExpand, 5);
//                fifteengenresreportLn.setVisibility(View.VISIBLE);
//                edtgenres15.requestFocus();
            }
        });

        //textview
        txt_about = (TextView) findViewById(R.id.txt_about);
        txt_update_profile = (TextView) findViewById(R.id.txt_update_profile);

        //imageview
        img_first_photo = (ImageView) findViewById(R.id.img_first_photo);
        img_second_photo = (ImageView) findViewById(R.id.img_second_photo);
        img_third_photo = (ImageView) findViewById(R.id.img_third_photo);
        img_fourth_photo = (ImageView) findViewById(R.id.img_fourth_photo);
        img_fifth_photo = (ImageView) findViewById(R.id.img_fifth_photo);
        img_sixth_photo = (ImageView) findViewById(R.id.img_sixth_photo);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_remove_first_photo = (ImageView) findViewById(R.id.img_remove_first_photo);
        img_remove_second_photo = (ImageView) findViewById(R.id.img_remove_second_photo);
        img_remove_third_photo = (ImageView) findViewById(R.id.img_remove_third_photo);
        img_remove_fourth_photo = (ImageView) findViewById(R.id.img_remove_fourth_photo);
        img_remove_fifth_photo = (ImageView) findViewById(R.id.img_remove_fifth_photo);
        img_remove_sixth_photo = (ImageView) findViewById(R.id.img_remove_sixth_photo);

        //progress bar
        progress_bar_first_photo = (ProgressBar) findViewById(R.id.progress_bar_first_photo);
        progress_bar_second_photo = (ProgressBar) findViewById(R.id.progress_bar_second_photo);
        progress_bar_third_photo = (ProgressBar) findViewById(R.id.progress_bar_third_photo);
        progress_bar_fourth_photo = (ProgressBar) findViewById(R.id.progress_bar_fourth_photo);
        progress_bar_fifth_photo = (ProgressBar) findViewById(R.id.progress_bar_fifth_photo);
        progress_bar_sixth_photo = (ProgressBar) findViewById(R.id.progress_bar_sixth_photo);

        //edit text
        edt_current_work = (EditText) findViewById(R.id.edt_current_work);
        edt_about = (EditText) findViewById(R.id.edt_about);
        edt_about.setFilters(new InputFilter[] { filter });

        edt_education = (EditText) findViewById(R.id.edt_education);
edt_about.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
if(charSequence!=null && isValidEmail(charSequence)){
    edt_about.setText("");
    edt_about.setError("can't enter Email");
}
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
});

        img_first_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//getfbAlbum();
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    photo1clicked = true;
                    photo2clicked = false;
                    photo3clicked = false;
                    photo4clicked = false;
                    photo5clicked = false;
                    photo6clicked = false;

                    selectImage();
                }
            }
        });

        img_second_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkPermission()) {
                    requestPermission();
                } else {
                    photo1clicked = false;
                    photo2clicked = true;
                    photo3clicked = false;
                    photo4clicked = false;
                    photo5clicked = false;
                    photo6clicked = false;

                    selectImage();
                }

            }
        });

        img_third_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkPermission()) {
                    requestPermission();
                } else {
                    photo1clicked = false;
                    photo2clicked = false;
                    photo3clicked = true;
                    photo4clicked = false;
                    photo5clicked = false;
                    photo6clicked = false;

                    selectImage();
                }
            }
        });

        img_fourth_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkPermission()) {
                    requestPermission();
                } else {
                    photo1clicked = false;
                    photo2clicked = false;
                    photo3clicked = false;
                    photo4clicked = true;
                    photo5clicked = false;
                    photo6clicked = false;

                    selectImage();
                }

            }
        });

        img_fifth_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkPermission()) {
                    requestPermission();
                } else {
                    photo1clicked = false;
                    photo2clicked = false;
                    photo3clicked = false;
                    photo4clicked = false;
                    photo5clicked = true;
                    photo6clicked = false;

                    selectImage();
                }

            }
        });

        img_sixth_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkPermission()) {
                    requestPermission();
                } else {
                    photo1clicked = false;
                    photo2clicked = false;
                    photo3clicked = false;
                    photo4clicked = false;
                    photo5clicked = false;
                    photo6clicked = true;

                    selectImage();
                }

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txt_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(EditProfileActivity.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getStringFromEditText();
                updateProfile();
                updateTopTenList("solo_artists", stredtartist1, stredtartist2, stredtartist3, stredtartist4, stredtartist5, stredtartist6, stredtartist7, stredtartist8, stredtartist9, stredtartist10);
                updateTopTenList("live_acts", stredtliveacts1, stredtliveacts2, stredtliveacts3, stredtliveacts4, stredtliveacts5, stredtliveacts6, stredtliveacts7, stredtliveacts8, stredtliveacts9, stredtliveacts10);
                updateTopTenList("bands_groups", stredtband1, stredtband2, stredtband3, stredtband4, stredtband5, stredtband6, stredtband7, stredtband8, stredtband9, stredtband10);
                updateTopTenList("emerging_acts", stredtemergingacts1, stredtemergingacts2, stredtemergingacts3, stredtemergingacts4, stredtemergingacts5, stredtemergingacts6, stredtemergingacts7, stredtemergingacts8, stredtemergingacts9, stredtemergingacts10);
                updateTopGenresList("genres", stredtgenres1, stredtgenres2, stredtgenres3, stredtgenres4, stredtgenres5, stredtgenres6, stredtgenres7, stredtgenres8, stredtgenres9, stredtgenres10, stredtgenres11, stredtgenres12, stredtgenres13, stredtgenres14, stredtgenres15);
            }
        });

        img_remove_first_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePhotoFromProfile(1);
            }
        });

        img_remove_second_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePhotoFromProfile(2);
            }
        });

        img_remove_third_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePhotoFromProfile(3);
            }
        });

        img_remove_fourth_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePhotoFromProfile(4);
            }
        });

        img_remove_fifth_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePhotoFromProfile(5);
            }
        });

        img_remove_sixth_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePhotoFromProfile(6);
            }
        });

        setData();
        getUserProfile();
    }

    private void setRelationSheep(String relationSheep) {
        if (relationSheep!=null && relationSheep.equalsIgnoreCase("Single")) {
            spRelationship.setSelection(0,false);
        } else if (relationSheep!=null && relationSheep.equalsIgnoreCase("Taken"))
        {
            spRelationship.setSelection(1,false);
        }
        else {
            spRelationship.setSelection(0,false);

        }
    }

    public void expandOrCollapsenew(final View v, boolean exp_or_colpse, int type) {

        if (!exp_or_colpse) {
            expand(v);
            if (type == 1) {
                tensoloartistsreportLnExpand = true;
            }
            if (type == 2) {
                tenbandssreportLnExpand = true;
            }
            if (type == 3) {
                tenliveactsreportLnExpand = true;
            }
            if (type == 4) {
                tenemergingactsreportLnExpand = true;
            }
            if (type == 5) {
                fifteengenresreportLnExpand = true;
            }

        } else {
            collapse(v);
            if (type == 1) {
                tensoloartistsreportLnExpand = false;
            }
            if (type == 2) {
                tenbandssreportLnExpand = false;
            }
            if (type == 3) {
                tenliveactsreportLnExpand = false;
            }
            if (type == 4) {
                tenemergingactsreportLnExpand = false;
            }
            if (type == 5) {
                fifteengenresreportLnExpand = false;
            }
        }
    }

    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    //collapse child views
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
//        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(500);
        v.startAnimation(a);
    }

    public void ClickListener() {
        edtartist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strArtists);
                in.putExtra("POSITION", "1");
                startActivityForResult(in, 111);

            }
        });
        edtartist2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strArtists);
                in.putExtra("POSITION", "2");
                startActivityForResult(in, 111);

            }
        });
        edtartist3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strArtists);
                in.putExtra("POSITION", "3");
                startActivityForResult(in, 111);
            }
        });
        edtartist4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strArtists);
                in.putExtra("POSITION", "4");
                startActivityForResult(in, 111);
            }
        });
        edtartist5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strArtists);
                in.putExtra("POSITION", "5");
                startActivityForResult(in, 111);
            }
        });
        edtartist6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strArtists);
                in.putExtra("POSITION", "6");
                startActivityForResult(in, 111);
            }
        });
        edtartist7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strArtists);
                in.putExtra("POSITION", "7");
                startActivityForResult(in, 111);
            }
        });
        edtartist8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strArtists);
                in.putExtra("POSITION", "8");
                startActivityForResult(in, 111);
            }
        });
        edtartist9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strArtists);
                in.putExtra("POSITION", "9");
                startActivityForResult(in, 111);
            }
        });
        edtartist10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strArtists);
                in.putExtra("POSITION", "10");
                startActivityForResult(in, 111);
            }
        });

        edtband1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strBands);
                in.putExtra("POSITION", "1");
                startActivityForResult(in, 111);
            }
        });
        edtband2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strBands);
                in.putExtra("POSITION", "2");
                startActivityForResult(in, 111);
            }
        });
        edtband3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strBands);
                in.putExtra("POSITION", "3");
                startActivityForResult(in, 111);
            }
        });
        edtband4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strBands);
                in.putExtra("POSITION", "4");
                startActivityForResult(in, 111);
            }
        });
        edtband5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strBands);
                in.putExtra("POSITION", "5");
                startActivityForResult(in, 111);
            }
        });
        edtband6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strBands);
                in.putExtra("POSITION", "6");
                startActivityForResult(in, 111);
            }
        });
        edtband7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strBands);
                in.putExtra("POSITION", "7");
                startActivityForResult(in, 111);
            }
        });
        edtband8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strBands);
                in.putExtra("POSITION", "8");
                startActivityForResult(in, 111);
            }
        });
        edtband9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strBands);
                in.putExtra("POSITION", "9");
                startActivityForResult(in, 111);
            }
        });
        edtband10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strBands);
                in.putExtra("POSITION", "10");
                startActivityForResult(in, 111);
            }
        });

        edtliveacts1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strLiveacts);
                in.putExtra("POSITION", "1");
                startActivityForResult(in, 111);
            }
        });
        edtliveacts2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strLiveacts);
                in.putExtra("POSITION", "2");
                startActivityForResult(in, 111);
            }
        });
        edtliveacts3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strLiveacts);
                in.putExtra("POSITION", "3");
                startActivityForResult(in, 111);
            }
        });
        edtliveacts4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strLiveacts);
                in.putExtra("POSITION", "4");
                startActivityForResult(in, 111);
            }
        });
        edtliveacts5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strLiveacts);
                in.putExtra("POSITION", "5");
                startActivityForResult(in, 111);
            }
        });
        edtliveacts6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strLiveacts);
                in.putExtra("POSITION", "6");
                startActivityForResult(in, 111);
            }
        });
        edtliveacts7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strLiveacts);
                in.putExtra("POSITION", "7");
                startActivityForResult(in, 111);
            }
        });
        edtliveacts8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strLiveacts);
                in.putExtra("POSITION", "8");
                startActivityForResult(in, 111);
            }
        });
        edtliveacts9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strLiveacts);
                in.putExtra("POSITION", "9");
                startActivityForResult(in, 111);
            }
        });
        edtliveacts10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strLiveacts);
                in.putExtra("POSITION", "10");
                startActivityForResult(in, 111);
            }
        });

        edtemergingacts1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strEmergingActs);
                in.putExtra("POSITION", "1");
                startActivityForResult(in, 111);
            }
        });
        edtemergingacts2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strEmergingActs);
                in.putExtra("POSITION", "2");
                startActivityForResult(in, 111);
            }
        });
        edtemergingacts3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strEmergingActs);
                in.putExtra("POSITION", "3");
                startActivityForResult(in, 111);
            }
        });
        edtemergingacts4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strEmergingActs);
                in.putExtra("POSITION", "4");
                startActivityForResult(in, 111);
            }
        });
        edtemergingacts5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strEmergingActs);
                in.putExtra("POSITION", "5");
                startActivityForResult(in, 111);
            }
        });
        edtemergingacts6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strEmergingActs);
                in.putExtra("POSITION", "6");
                startActivityForResult(in, 111);
            }
        });
        edtemergingacts7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strEmergingActs);
                in.putExtra("POSITION", "7");
                startActivityForResult(in, 111);
            }
        });
        edtemergingacts8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strEmergingActs);
                in.putExtra("POSITION", "8");
                startActivityForResult(in, 111);
            }
        });
        edtemergingacts9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strEmergingActs);
                in.putExtra("POSITION", "9");
                startActivityForResult(in, 111);
            }
        });
        edtemergingacts10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strEmergingActs);
                in.putExtra("POSITION", "10");
                startActivityForResult(in, 111);
            }
        });

        edtgenres1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "1");
                startActivityForResult(in, 111);
            }
        });
        edtgenres2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "2");
                startActivityForResult(in, 111);
            }
        });
        edtgenres3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "3");
                startActivityForResult(in, 111);
            }
        });
        edtgenres4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "4");
                startActivityForResult(in, 111);
            }
        });
        edtgenres5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "5");
                startActivityForResult(in, 111);
            }
        });
        edtgenres6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "6");
                startActivityForResult(in, 111);
            }
        });
        edtgenres7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "7");
                startActivityForResult(in, 111);
            }
        });
        edtgenres8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "8");
                startActivityForResult(in, 111);
            }
        });
        edtgenres9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "9");
                startActivityForResult(in, 111);
            }
        });
        edtgenres10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "10");
                startActivityForResult(in, 111);
            }
        });
        edtgenres11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "11");
                startActivityForResult(in, 111);
            }
        });
        edtgenres12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "12");
                startActivityForResult(in, 111);
            }
        });
        edtgenres13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "13");
                startActivityForResult(in, 111);
            }
        });
        edtgenres14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "14");
                startActivityForResult(in, 111);
            }
        });
        edtgenres15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EditProfileActivity.this, NewSearchListActivity.class);
                in.putExtra("Category_Title", strGeneres);
                in.putExtra("POSITION", "15");
                startActivityForResult(in, 111);
            }
        });
    }


    public void getUserProfile() {
        img_first_photo.setImageBitmap(null);
        img_second_photo.setImageBitmap(null);
        img_third_photo.setImageBitmap(null);
        img_fourth_photo.setImageBitmap(null);
        img_fifth_photo.setImageBitmap(null);
        img_sixth_photo.setImageBitmap(null);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<List<GetProfileModel>> call = apiService.getUserProfile(pref.getFbId(), pref.getFbId());


        call.enqueue(new Callback<List<GetProfileModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetProfileModel>> call, @NonNull Response<List<GetProfileModel>> response) {
                images = new ArrayList<>();
                try {
                    List<GetProfileModel> list = response.body();
                    if (response.code() == 200) {

                        Log.e(TAG, "onResponse: succesResponse" + response.body());

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
                        String relationsheepStatus=list.get(0).getRelationshipStatus();

                        setGender(gender);
                        setRelationSheep(relationsheepStatus);
                        pref.setGender(gender);
                      pref.setRelationshipStatus(relationsheepStatus);

                        if (!TextUtils.isEmpty(fbProfilePhoto1)) {
                            images.add(fbProfilePhoto1);
                        }

                        if (!TextUtils.isEmpty(fbProfilePhoto2)) {
                            images.add(fbProfilePhoto2);
                        }

                        if (!TextUtils.isEmpty(fbProfilePhoto3)) {
                            images.add(fbProfilePhoto3);
                        }

                        if (!TextUtils.isEmpty(fbProfilePhoto4)) {
                            images.add(fbProfilePhoto4);
                        }

                        if (!TextUtils.isEmpty(fbProfilePhoto5)) {
                            images.add(fbProfilePhoto5);
                        }

                        if (!TextUtils.isEmpty(fbProfilePhoto6)) {
                            images.add(fbProfilePhoto6);
                        }

                        if (images.size() > 0) {

                            Log.e(TAG, "onResponse: imagesSize" + String.valueOf(images.size()));

                            pref.setProfilePhoto(images.get(0));
                            if (images.size() == 1) {
                                progress_bar_first_photo.setVisibility(View.VISIBLE);
                                img_remove_first_photo.setVisibility(View.VISIBLE);
                                Glide.with(EditProfileActivity.this)
                                        .load(images.get(0))
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_first_photo);

                            } else if (images.size() == 2) {
                                progress_bar_first_photo.setVisibility(View.VISIBLE);
                                img_remove_first_photo.setVisibility(View.VISIBLE);
                                progress_bar_second_photo.setVisibility(View.VISIBLE);
                                img_remove_second_photo.setVisibility(View.VISIBLE);

                                //first image
                                Glide.with(EditProfileActivity.this)
                                        .load(images.get(0))
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_first_photo);


                                //second image
                                Glide.with(EditProfileActivity.this)
                                        .load(images.get(1))
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_second_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_second_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_second_photo);

                            } else if (images.size() == 3) {
                                progress_bar_first_photo.setVisibility(View.VISIBLE);
                                img_remove_first_photo.setVisibility(View.VISIBLE);
                                progress_bar_second_photo.setVisibility(View.VISIBLE);
                                img_remove_second_photo.setVisibility(View.VISIBLE);
                                progress_bar_third_photo.setVisibility(View.VISIBLE);
                                img_remove_third_photo.setVisibility(View.VISIBLE);

                                //first image
                                Glide.with(EditProfileActivity.this)
                                        .load(images.get(0))
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_first_photo);


                                //second image
                                Glide.with(EditProfileActivity.this)
                                        .load(images.get(1))
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_second_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_second_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_second_photo);

                                //third image
                                Glide.with(EditProfileActivity.this)
                                        .load(fbProfilePhoto3)
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_third_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_third_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_third_photo);

                            } else if (images.size() == 4) {
                                progress_bar_first_photo.setVisibility(View.VISIBLE);
                                img_remove_first_photo.setVisibility(View.VISIBLE);
                                progress_bar_second_photo.setVisibility(View.VISIBLE);
                                img_remove_second_photo.setVisibility(View.VISIBLE);
                                progress_bar_third_photo.setVisibility(View.VISIBLE);
                                img_remove_third_photo.setVisibility(View.VISIBLE);
                                progress_bar_fourth_photo.setVisibility(View.VISIBLE);
                                img_remove_fourth_photo.setVisibility(View.VISIBLE);

                                //first image
                                Glide.with(EditProfileActivity.this)
                                        .load(images.get(0))
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_first_photo);


                                //second image
                                Glide.with(EditProfileActivity.this)
                                        .load(images.get(1))
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_second_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_second_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_second_photo);

                                //third image
                                Glide.with(EditProfileActivity.this)
                                        .load(fbProfilePhoto3)
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_third_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_third_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_third_photo);

                                //fourth image
                                Glide.with(EditProfileActivity.this)
                                        .load(fbProfilePhoto4)
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_fourth_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_fourth_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_fourth_photo);
                            } else if (images.size() == 5) {
                                progress_bar_first_photo.setVisibility(View.VISIBLE);
                                img_remove_first_photo.setVisibility(View.VISIBLE);
                                progress_bar_second_photo.setVisibility(View.VISIBLE);
                                img_remove_second_photo.setVisibility(View.VISIBLE);
                                progress_bar_third_photo.setVisibility(View.VISIBLE);
                                img_remove_third_photo.setVisibility(View.VISIBLE);
                                progress_bar_fourth_photo.setVisibility(View.VISIBLE);
                                img_remove_fourth_photo.setVisibility(View.VISIBLE);
                                progress_bar_fifth_photo.setVisibility(View.VISIBLE);
                                img_remove_fifth_photo.setVisibility(View.VISIBLE);

                                //first image
                                Glide.with(EditProfileActivity.this)
                                        .load(images.get(0))
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_first_photo);


                                //second image
                                Glide.with(EditProfileActivity.this)
                                        .load(images.get(1))
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_second_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_second_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_second_photo);

                                //third image
                                Glide.with(EditProfileActivity.this)
                                        .load(fbProfilePhoto3)
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_third_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_third_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_third_photo);

                                //fourth image
                                Glide.with(EditProfileActivity.this)
                                        .load(fbProfilePhoto4)
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_fourth_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_fourth_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_fourth_photo);

                                //fifth image
                                Glide.with(EditProfileActivity.this)
                                        .load(fbProfilePhoto5)
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_fifth_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_fifth_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_fifth_photo);
                            } else if (images.size() == 6) {
                                progress_bar_first_photo.setVisibility(View.VISIBLE);
                                img_remove_first_photo.setVisibility(View.VISIBLE);
                                progress_bar_second_photo.setVisibility(View.VISIBLE);
                                img_remove_second_photo.setVisibility(View.VISIBLE);
                                progress_bar_third_photo.setVisibility(View.VISIBLE);
                                img_remove_third_photo.setVisibility(View.VISIBLE);
                                progress_bar_fourth_photo.setVisibility(View.VISIBLE);
                                img_remove_fourth_photo.setVisibility(View.VISIBLE);
                                progress_bar_fifth_photo.setVisibility(View.VISIBLE);
                                img_remove_fifth_photo.setVisibility(View.VISIBLE);
                                progress_bar_sixth_photo.setVisibility(View.VISIBLE);
                                img_remove_sixth_photo.setVisibility(View.VISIBLE);

                                //first image
                                Glide.with(EditProfileActivity.this)
                                        .load(images.get(0))
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_first_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_first_photo);


                                //second image
                                Glide.with(EditProfileActivity.this)
                                        .load(images.get(1))
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_second_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_second_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_second_photo);

                                //third image
                                Glide.with(EditProfileActivity.this)
                                        .load(fbProfilePhoto3)
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_third_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_third_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_third_photo);

                                //fourth image
                                Glide.with(EditProfileActivity.this)
                                        .load(fbProfilePhoto4)
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_fourth_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_fourth_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_fourth_photo);

                                //fifth image
                                Glide.with(EditProfileActivity.this)
                                        .load(fbProfilePhoto5)
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_fifth_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_fifth_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_fifth_photo);

                                //sixth image
                                Glide.with(EditProfileActivity.this)
                                        .load(fbProfilePhoto6)
                                        .asBitmap()
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                progress_bar_sixth_photo.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progress_bar_sixth_photo.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(img_sixth_photo);
                            }
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

    private void setGender(String gender) {
        if (gender!=null && gender.equalsIgnoreCase("male")) {
            radioSexGroup.check(rb_male.getId());
            spGender.setSelection(0,false);
        } else
            //if (gender!=null && gender.equalsIgnoreCase("female"))
            {
            radioSexGroup.check(rb_female.getId());
            spGender.setSelection(1,false);
        }
//        else if(gender!=null && gender.equalsIgnoreCase("couple")){
//            //radioSexGroup.check(rb_female.getId());
//            spGender.setSelection(2,false);
//        }

    }

    private void selectImage() {
//        final CharSequence[] items = {"Choose from Gallery",
//                "Cancel"};
        final CharSequence[] items = {"Choose from Facebook Album","Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(
                EditProfileActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(items[item].equals("Choose from Facebook Album")){

                    pb=new ProgressDialog(EditProfileActivity.this);
                    pb.setMessage("Loading...");
                    pb.setCancelable(true);
                    pb.show();

                    getfbAlbum();
                }
                else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public void updateProfile() {
        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);
        Call<LoginModel> call = apiService.updateProfileWithoutPhoto(pref.getFbId(), pref.getFirstName() + " " + pref.getLastName(), pref.getEmailId(),
                pref.getLocation(), pref.getBirthday(), pref.getFirstName(), pref.getLastName(), edt_education.getText().toString().trim(),
                gender, edt_about.getText().toString().trim(), pref.getAge(), edt_current_work.getText().toString().trim(), pref.getLocation(),relationSheep);

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginModel> call, @NonNull Response<LoginModel> response) {
                String emailid = response.body().getUserEmail();
                String f_name = response.body().getFirstName();
                String l_name = response.body().getLastName();
                String gender = response.body().getGender();
                String dob = response.body().getUserDob();
                String location_name = response.body().getLocation();
                String profile_pic = response.body().getFbUserPhotoUrl1();
                String fbProfilePhoto2 = response.body().getFbUserPhotoUrl2();
                String fbProfilePhoto3 = response.body().getFbUserPhotoUrl3();
                String fbProfilePhoto4 = response.body().getFbUserPhotoUrl4();
                String fbProfilePhoto5 = response.body().getFbUserPhotoUrl5();
                String fbProfilePhoto6 = response.body().getFbUserPhotoUrl6();
                String CollegeName = response.body().getCollegeName();
                String About = response.body().getAbout();
                String Age = response.body().getAge();
                String Profession = response.body().getProfession();

                pref.setGender(gender);
                pref.setCollegeName(CollegeName);
                pref.setAbout(About);
                pref.setProfession(Profession);

                progressDialog.dismiss();


                CommonUses.showToast(EditProfileActivity.this, "Profile updated successfully");
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<LoginModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void getfullTopTenList(final String getfrom) {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<GetTopTenListModel>> call = apiService.GetTopTenList(pref.getFbId(), getfrom);

        call.enqueue(new Callback<List<GetTopTenListModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetTopTenListModel>> call, @NonNull Response<List<GetTopTenListModel>> response) {
                List<GetTopTenListModel> list = response.body();

                try {
                    if (response.code() == 200) {

                        for (int i = 0; i < list.size(); i++) {
                            String edtNumber = response.body().get(i).getTextboxno();
                            String edtName = response.body().get(i).getName();
                            String edtTag = response.body().get(i).getArtistid();

                            if (getfrom.equalsIgnoreCase("solo_artists")) {
                                setArtistsTagText(edtTag, edtName, edtNumber);
                            }

                            if (getfrom.equalsIgnoreCase("live_acts")) {
                                setLiveActsTagText(edtTag, edtName, edtNumber);
                            }

                            if (getfrom.equalsIgnoreCase("bands_groups")) {
                                setBandsTagText(edtTag, edtName, edtNumber);
                            }

                            if (getfrom.equalsIgnoreCase("emerging_acts")) {
                                setEmergingTagText(edtTag, edtName, edtNumber);
                            }

                            if (getfrom.equalsIgnoreCase("genres")) {
                                setGenresTagText(edtTag, edtName, edtNumber);
                            }

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<GetTopTenListModel>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    public void updateTopTenList(String toptype, String one, String two, String three, String four, String five, String six, String seven,
                                 String eight, String nine, String ten) {
        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);
        Call<SwipeAction> call = apiService.InsertUserRating(pref.getFbId(), toptype, one, two, three, four, five, six, seven, eight, nine, ten);

        call.enqueue(new Callback<SwipeAction>() {
            @Override
            public void onResponse(@NonNull Call<SwipeAction> call, @NonNull Response<SwipeAction> response) {
                try {
                    if (response.code() == 200) {
                        CommonUses.showToast(EditProfileActivity.this, "Top Ten List updated successfully");
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SwipeAction> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void updateTopGenresList(String toptype, String one, String two, String three, String four, String five, String six, String seven,
                                    String eight, String nine, String ten, String eleven, String twelve, String thirteen, String forteen, String fifteen) {
        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);
        Call<SwipeAction> call = apiService.InsertUserRatingGenres(pref.getFbId(), toptype, one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen, forteen, fifteen);

        call.enqueue(new Callback<SwipeAction>() {
            @Override
            public void onResponse(@NonNull Call<SwipeAction> call, @NonNull Response<SwipeAction> response) {
                try {
                    if (response.code() == 200) {
                        CommonUses.showToast(EditProfileActivity.this, "Top Ten List updated successfully");
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SwipeAction> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void getStringFromEditText() {

        stredtartist1 = String.valueOf(getFinalTagStr(edtartist1));
        stredtartist2 = String.valueOf(getFinalTagStr(edtartist2));
        stredtartist3 = String.valueOf(getFinalTagStr(edtartist3));
        stredtartist4 = String.valueOf(getFinalTagStr(edtartist4));
        stredtartist5 = String.valueOf(getFinalTagStr(edtartist5));
        stredtartist6 = String.valueOf(getFinalTagStr(edtartist6));
        stredtartist7 = String.valueOf(getFinalTagStr(edtartist7));
        stredtartist8 = String.valueOf(getFinalTagStr(edtartist8));
        stredtartist9 = String.valueOf(getFinalTagStr(edtartist9));
        stredtartist10 = String.valueOf(getFinalTagStr(edtartist10));

        stredtband1 = String.valueOf(getFinalTagStr(edtband1));
        stredtband2 = String.valueOf(getFinalTagStr(edtband2));
        stredtband3 = String.valueOf(getFinalTagStr(edtband3));
        stredtband4 = String.valueOf(getFinalTagStr(edtband4));
        stredtband5 = String.valueOf(getFinalTagStr(edtband5));
        stredtband6 = String.valueOf(getFinalTagStr(edtband6));
        stredtband7 = String.valueOf(getFinalTagStr(edtband7));
        stredtband8 = String.valueOf(getFinalTagStr(edtband8));
        stredtband9 = String.valueOf(getFinalTagStr(edtband9));
        stredtband10 = String.valueOf(getFinalTagStr(edtband10));

        stredtliveacts1 = String.valueOf(getFinalTagStr(edtband1));
        stredtliveacts2 = String.valueOf(getFinalTagStr(edtband2));
        stredtliveacts3 = String.valueOf(getFinalTagStr(edtband3));
        stredtliveacts4 = String.valueOf(getFinalTagStr(edtband4));
        stredtliveacts5 = String.valueOf(getFinalTagStr(edtband5));
        stredtliveacts6 = String.valueOf(getFinalTagStr(edtband6));
        stredtliveacts7 = String.valueOf(getFinalTagStr(edtband7));
        stredtliveacts8 = String.valueOf(getFinalTagStr(edtband8));
        stredtliveacts9 = String.valueOf(getFinalTagStr(edtband9));
        stredtliveacts10 = String.valueOf(getFinalTagStr(edtband10));

        stredtemergingacts1 = String.valueOf(getFinalTagStr(edtemergingacts1));
        stredtemergingacts2 = String.valueOf(getFinalTagStr(edtemergingacts2));
        stredtemergingacts3 = String.valueOf(getFinalTagStr(edtemergingacts3));
        stredtemergingacts4 = String.valueOf(getFinalTagStr(edtemergingacts4));
        stredtemergingacts5 = String.valueOf(getFinalTagStr(edtemergingacts5));
        stredtemergingacts6 = String.valueOf(getFinalTagStr(edtemergingacts6));
        stredtemergingacts7 = String.valueOf(getFinalTagStr(edtemergingacts7));
        stredtemergingacts8 = String.valueOf(getFinalTagStr(edtemergingacts8));
        stredtemergingacts9 = String.valueOf(getFinalTagStr(edtemergingacts9));
        stredtemergingacts10 = String.valueOf(getFinalTagStr(edtemergingacts10));

        stredtgenres1 = String.valueOf(getFinalTagStr(edtgenres1));
        stredtgenres2 = String.valueOf(getFinalTagStr(edtgenres2));
        stredtgenres3 = String.valueOf(getFinalTagStr(edtgenres3));
        stredtgenres4 = String.valueOf(getFinalTagStr(edtgenres4));
        stredtgenres5 = String.valueOf(getFinalTagStr(edtgenres5));
        stredtgenres6 = String.valueOf(getFinalTagStr(edtgenres6));
        stredtgenres7 = String.valueOf(getFinalTagStr(edtgenres7));
        stredtgenres8 = String.valueOf(getFinalTagStr(edtgenres8));
        stredtgenres9 = String.valueOf(getFinalTagStr(edtgenres9));
        stredtgenres10 = String.valueOf(getFinalTagStr(edtgenres10));
        stredtgenres11 = String.valueOf(getFinalTagStr(edtgenres11));
        stredtgenres12 = String.valueOf(getFinalTagStr(edtgenres12));
        stredtgenres13 = String.valueOf(getFinalTagStr(edtgenres13));
        stredtgenres14 = String.valueOf(getFinalTagStr(edtgenres14));
        stredtgenres1 = String.valueOf(getFinalTagStr(edtgenres15));

    }

    String getFinalTagStr(EditText et) {

        String str = (et.getTag() == null) ? "" : String.valueOf(et.getTag());

        return str;
    }

    public void removePhotoFromProfile(final int numberofPhoto) {
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String removePhotoName = "";

        if (numberofPhoto == 1) {
            removePhotoName = "FbUserPhotoUrl1";
        }
        if (numberofPhoto == 2) {
            removePhotoName = "FbUserPhotoUrl2";
        }
        if (numberofPhoto == 3) {
            removePhotoName = "FbUserPhotoUrl3";
        }
        if (numberofPhoto == 4) {
            removePhotoName = "FbUserPhotoUrl4";
        }
        if (numberofPhoto == 5) {
            removePhotoName = "FbUserPhotoUrl5";
        }
        if (numberofPhoto == 6) {
            removePhotoName = "FbUserPhotoUrl6";
        }

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);
        Call<LoginModel> call = apiService.removePhotoFromProfile(pref.getFbId(), pref.getFirstName() + " " + pref.getLastName(), pref.getEmailId(),
                pref.getLocation(), pref.getBirthday(), pref.getFirstName(), pref.getLastName(), edt_education.getText().toString().trim(),
                gender, edt_about.getText().toString().trim(), pref.getAge(), edt_current_work.getText().toString().trim(), pref.getLocation(), removePhotoName);

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginModel> call, @NonNull Response<LoginModel> response) {
                String emailid = response.body().getUserEmail();
                String f_name = response.body().getFirstName();
                String l_name = response.body().getLastName();
                String gender = response.body().getGender();
                String dob = response.body().getUserDob();
                String location_name = response.body().getLocation();
                String profile_pic = response.body().getFbUserPhotoUrl1();
                String fbProfilePhoto2 = response.body().getFbUserPhotoUrl2();
                String fbProfilePhoto3 = response.body().getFbUserPhotoUrl3();
                String fbProfilePhoto4 = response.body().getFbUserPhotoUrl4();
                String fbProfilePhoto5 = response.body().getFbUserPhotoUrl5();
                String fbProfilePhoto6 = response.body().getFbUserPhotoUrl6();
                String CollegeName = response.body().getCollegeName();
                String About = response.body().getAbout();
                String Age = response.body().getAge();
                String Profession = response.body().getProfession();

                pref.setGender(gender);
                pref.setCollegeName(CollegeName);
                pref.setAbout(About);
                pref.setProfession(Profession);

                progressDialog.dismiss();
                if (numberofPhoto == 1) {
                    img_remove_first_photo.setVisibility(View.GONE);
                }
                if (numberofPhoto == 2) {
                    img_remove_second_photo.setVisibility(View.GONE);
                }
                if (numberofPhoto == 3) {
                    img_remove_third_photo.setVisibility(View.GONE);
                }
                if (numberofPhoto == 4) {
                    img_remove_fourth_photo.setVisibility(View.GONE);
                }
                if (numberofPhoto == 5) {
                    img_remove_fifth_photo.setVisibility(View.GONE);
                }
                if (numberofPhoto == 6) {
                    img_remove_sixth_photo.setVisibility(View.GONE);
                }

                Log.e(TAG, "onResponse: Success Remove" + response.body());
                getUserProfile();

                CommonUses.showToast(EditProfileActivity.this, "Photo Removed successfully");
            }

            @Override
            public void onFailure(@NonNull Call<LoginModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    public void setData() {
        if (!TextUtils.isEmpty(firstname)) {
            txt_about.setText("About " + firstname);
        }
        if (!TextUtils.isEmpty(about)) {
            edt_about.setText(about);
        }
        if (!TextUtils.isEmpty(current_work)) {
            edt_current_work.setText(current_work);
        }
        if (!TextUtils.isEmpty(education)) {
            edt_education.setText(education);
        }
    }

    private boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(EditProfileActivity.this, WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted) {
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},
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
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(EditProfileActivity.this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == RESULT_LOAD_IMAGE && null != data) {
                    if (photo1clicked) {
                        selectedImageUri1 = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri1);
                            img_first_photo.setImageBitmap(bitmap);
                            MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl1", selectedImageUri1);
                            updateProfileWithImage(body, 1);
                            removephotoclickedFlags();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (photo2clicked) {
                        selectedImageUri2 = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri2);
                            img_second_photo.setImageBitmap(bitmap);
                            MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl2", selectedImageUri2);
                            updateProfileWithImage(body, 2);
                            removephotoclickedFlags();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (photo3clicked) {
                        selectedImageUri3 = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri3);
                            img_third_photo.setImageBitmap(bitmap);
                            MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl3", selectedImageUri3);
                            updateProfileWithImage(body, 3);
                            removephotoclickedFlags();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (photo4clicked) {
                        selectedImageUri4 = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri4);
                            img_fourth_photo.setImageBitmap(bitmap);
                            MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl4", selectedImageUri4);
                            updateProfileWithImage(body, 4);
                            removephotoclickedFlags();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (photo5clicked) {
                        selectedImageUri5 = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri5);
                            img_fifth_photo.setImageBitmap(bitmap);
                            MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl5", selectedImageUri5);
                            updateProfileWithImage(body, 5);
                            removephotoclickedFlags();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (photo6clicked) {
                        selectedImageUri6 = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri6);
                            img_sixth_photo.setImageBitmap(bitmap);
                            MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl6", selectedImageUri6);
                            updateProfileWithImage(body, 6);
                            removephotoclickedFlags();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(EditProfileActivity.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        if (requestCode == 111 && resultCode == 111) {

            String selectedValueId = data.getExtras().getString("id");
            String selectedValuename = data.getExtras().getString("name");
            String POSITION = data.getExtras().getString("POSITION");
            String Category_Title = data.getExtras().getString("Category_Title");

            if (Category_Title.equalsIgnoreCase(strArtists)) {
                setArtistsTagText(selectedValueId, selectedValuename, POSITION);
            }

            if (Category_Title.equalsIgnoreCase(strBands)) {
                setBandsTagText(selectedValueId, selectedValuename, POSITION);
            }

            if (Category_Title.equalsIgnoreCase(strLiveacts)) {
                setLiveActsTagText(selectedValueId, selectedValuename, POSITION);
            }

            if (Category_Title.equalsIgnoreCase(strEmergingActs)) {
                setEmergingTagText(selectedValueId, selectedValuename, POSITION);
            }

            if (Category_Title.equalsIgnoreCase(strGeneres)) {
                setGenresTagText(selectedValueId, selectedValuename, POSITION);
            }

        }


    }

    private void setGenresTagText(String selectedValueId, String selectedValuename, String POSITION) {
        switch (POSITION) {
            case "1":
                edtgenres1.setText(selectedValuename);
                edtgenres1.setTag(selectedValueId);
                break;
            case "2":
                edtgenres2.setText(selectedValuename);
                edtgenres2.setTag(selectedValueId);
                break;
            case "3":
                edtgenres3.setText(selectedValuename);
                edtgenres3.setTag(selectedValueId);
                break;
            case "4":
                edtgenres4.setText(selectedValuename);
                edtgenres4.setTag(selectedValueId);
                break;
            case "5":
                edtgenres5.setText(selectedValuename);
                edtgenres5.setTag(selectedValueId);
                break;
            case "6":
                edtgenres6.setText(selectedValuename);
                edtgenres6.setTag(selectedValueId);
                break;
            case "7":
                edtgenres7.setText(selectedValuename);
                edtgenres7.setTag(selectedValueId);
                break;
            case "8":
                edtgenres8.setText(selectedValuename);
                edtgenres8.setTag(selectedValueId);
                break;
            case "9":
                edtgenres9.setText(selectedValuename);
                edtgenres9.setTag(selectedValueId);
                break;
            case "10":
                edtgenres10.setText(selectedValuename);
                edtgenres10.setTag(selectedValueId);
                break;
            case "11":
                edtgenres11.setText(selectedValuename);
                edtgenres11.setTag(selectedValueId);
                break;
            case "12":
                edtgenres12.setText(selectedValuename);
                edtgenres12.setTag(selectedValueId);
                break;
            case "13":
                edtgenres13.setText(selectedValuename);
                edtgenres13.setTag(selectedValueId);
                break;
            case "14":
                edtgenres14.setText(selectedValuename);
                edtgenres14.setTag(selectedValueId);
                break;
            case "15":
                edtgenres15.setText(selectedValuename);
                edtgenres15.setTag(selectedValueId);
                break;

        }
    }

    private void setEmergingTagText(String selectedValueId, String selectedValuename, String POSITION) {
        switch (POSITION) {
            case "1":
                edtemergingacts1.setText(selectedValuename);
                edtemergingacts1.setTag(selectedValueId);
                break;
            case "2":
                edtemergingacts2.setText(selectedValuename);
                edtemergingacts2.setTag(selectedValueId);
                break;
            case "3":
                edtemergingacts3.setText(selectedValuename);
                edtemergingacts3.setTag(selectedValueId);
                break;
            case "4":
                edtemergingacts4.setText(selectedValuename);
                edtemergingacts4.setTag(selectedValueId);
                break;
            case "5":
                edtemergingacts5.setText(selectedValuename);
                edtemergingacts5.setTag(selectedValueId);
                break;
            case "6":
                edtemergingacts6.setText(selectedValuename);
                edtemergingacts6.setTag(selectedValueId);
                break;
            case "7":
                edtemergingacts7.setText(selectedValuename);
                edtemergingacts7.setTag(selectedValueId);
                break;
            case "8":
                edtemergingacts8.setText(selectedValuename);
                edtemergingacts8.setTag(selectedValueId);
                break;
            case "9":
                edtemergingacts9.setText(selectedValuename);
                edtemergingacts9.setTag(selectedValueId);
                break;
            case "10":
                edtemergingacts10.setText(selectedValuename);
                edtemergingacts10.setTag(selectedValueId);
                break;

        }
    }

    private void setLiveActsTagText(String selectedValueId, String selectedValuename, String POSITION) {
        switch (POSITION) {
            case "1":
                edtliveacts1.setText(selectedValuename);
                edtliveacts1.setTag(selectedValueId);
                break;
            case "2":
                edtliveacts2.setText(selectedValuename);
                edtliveacts2.setTag(selectedValueId);
                break;
            case "3":
                edtliveacts3.setText(selectedValuename);
                edtliveacts3.setTag(selectedValueId);
                break;
            case "4":
                edtliveacts4.setText(selectedValuename);
                edtliveacts4.setTag(selectedValueId);
                break;
            case "5":
                edtliveacts5.setText(selectedValuename);
                edtliveacts5.setTag(selectedValueId);
                break;
            case "6":
                edtliveacts6.setText(selectedValuename);
                edtliveacts6.setTag(selectedValueId);
                break;
            case "7":
                edtliveacts7.setText(selectedValuename);
                edtliveacts7.setTag(selectedValueId);
                break;
            case "8":
                edtliveacts8.setText(selectedValuename);
                edtliveacts8.setTag(selectedValueId);
                break;
            case "9":
                edtliveacts9.setText(selectedValuename);
                edtliveacts9.setTag(selectedValueId);
                break;
            case "10":
                edtliveacts10.setText(selectedValuename);
                edtliveacts10.setTag(selectedValueId);
                break;

        }
    }

    private void setBandsTagText(String selectedValueId, String selectedValuename, String POSITION) {
        switch (POSITION) {
            case "1":
                edtband1.setText(selectedValuename);
                edtband1.setTag(selectedValueId);
                break;
            case "2":
                edtband2.setText(selectedValuename);
                edtband2.setTag(selectedValueId);
                break;
            case "3":
                edtband3.setText(selectedValuename);
                edtband3.setTag(selectedValueId);
                break;
            case "4":
                edtband4.setText(selectedValuename);
                edtband4.setTag(selectedValueId);
                break;
            case "5":
                edtband5.setText(selectedValuename);
                edtband5.setTag(selectedValueId);
                break;
            case "6":
                edtband6.setText(selectedValuename);
                edtband6.setTag(selectedValueId);
                break;
            case "7":
                edtband7.setText(selectedValuename);
                edtband7.setTag(selectedValueId);
                break;
            case "8":
                edtband8.setText(selectedValuename);
                edtband8.setTag(selectedValueId);
                break;
            case "9":
                edtband9.setText(selectedValuename);
                edtband9.setTag(selectedValueId);
                break;
            case "10":
                edtband10.setText(selectedValuename);
                edtband10.setTag(selectedValueId);
                break;

        }
    }

    private void setArtistsTagText(String selectedValueId, String selectedValuename, String POSITION) {
        switch (POSITION) {
            case "1":
                edtartist1.setText(selectedValuename);
                edtartist1.setTag(selectedValueId);
                break;
            case "2":
                edtartist2.setText(selectedValuename);
                edtartist2.setTag(selectedValueId);
                break;
            case "3":
                edtartist3.setText(selectedValuename);
                edtartist3.setTag(selectedValueId);
                break;
            case "4":
                edtartist4.setText(selectedValuename);
                edtartist4.setTag(selectedValueId);
                break;
            case "5":
                edtartist5.setText(selectedValuename);
                edtartist5.setTag(selectedValueId);
                break;
            case "6":
                edtartist6.setText(selectedValuename);
                edtartist6.setTag(selectedValueId);
                break;
            case "7":
                edtartist7.setText(selectedValuename);
                edtartist7.setTag(selectedValueId);
                break;
            case "8":
                edtartist8.setText(selectedValuename);
                edtartist8.setTag(selectedValueId);
                break;
            case "9":
                edtartist9.setText(selectedValuename);
                edtartist9.setTag(selectedValueId);
                break;
            case "10":
                edtartist10.setText(selectedValuename);
                edtartist10.setTag(selectedValueId);
                break;

        }
    }

    public void updateProfileWithImage(MultipartBody.Part body, final int photoNumber) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait, Image Upload Will take time for couple of minutes based upon internet speed & Image Size...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);
        Call<LoginModel> call = null;
        RequestBody FbProfileId =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, pref.getFbId());
        RequestBody ProfileName =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, pref.getFirstName() + " " + pref.getLastName());
        RequestBody UserEmail =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, pref.getEmailId());
        RequestBody UserAddress =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, pref.getLocation());
        RequestBody UserDob =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, pref.getBirthday());
        RequestBody FirstName =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, pref.getFirstName());
        RequestBody LastName =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, pref.getLastName());
        RequestBody CollegeName =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, edt_education.getText().toString().trim());
        RequestBody Gender =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, gender);
        RequestBody About =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, edt_about.getText().toString().trim());
        RequestBody Age =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, pref.getAge());
        RequestBody Profession =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, edt_current_work.getText().toString());
        RequestBody Location =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, pref.getLocation());

        call = apiService.uploadFileWithPhoto(FbProfileId, body, ProfileName, UserEmail, UserAddress,
                UserDob, FirstName, LastName, CollegeName, Gender, About, Age, Profession, Location);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginModel> call, @NonNull Response<LoginModel> response) {
                try {
                    if (response.code() == 200) {

                        if (photoNumber == 1) {
                            img_remove_first_photo.setVisibility(View.VISIBLE);
                        }
                        if (photoNumber == 2) {
                            img_remove_second_photo.setVisibility(View.VISIBLE);
                        }
                        if (photoNumber == 3) {
                            img_remove_third_photo.setVisibility(View.VISIBLE);
                        }
                        if (photoNumber == 4) {
                            img_remove_fourth_photo.setVisibility(View.VISIBLE);
                        }
                        if (photoNumber == 5) {
                            img_remove_fifth_photo.setVisibility(View.VISIBLE);
                        }
                        if (photoNumber == 6) {
                            img_remove_sixth_photo.setVisibility(View.VISIBLE);
                        }

                        progressDialog.dismiss();
                        CommonUses.showToast(EditProfileActivity.this, "Image Uploaded Successfully.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void removephotoclickedFlags() {
        photo1clicked = false;
        photo2clicked = false;
        photo3clicked = false;
        photo4clicked = false;
        photo5clicked = false;
        photo6clicked = false;
    }


    public static String saveToInternalStorage1(Bitmap bitmapImage, Context context, String resolution) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/imageDir");

        // Create imageDir
        Date d = new Date();
        File mypath = new File(directory, d.getTime() + ".jpg");

        try {
            FileOutputStream fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            if (resolution.equalsIgnoreCase("low")) {
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } else {
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }

            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mypath.getAbsolutePath();
    }

    private Uri getImageContentUri(Context context, String filePath, File imgFile) {
//        String filePath = imageFile.getAbsolutePath();
        String filePathloc = imgFile.getAbsolutePath();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePathloc}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imgFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = getFile(this, fileUri);

        File ImageFile = new CompressHelper.Builder(EditProfileActivity.this)
                .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(file);


        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        ImageFile
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, ImageFile.getName(), requestFile);
    }

    public File getFile(Context context, Uri uri) {
        if (uri != null) {
            String path = getPath(uri);
            if (path != null) {
                return new File(path);
            }
        }
        return null;
    }

    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String profile_image = "";
        if (cursor.moveToFirst()) {
            profile_image = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        cursor.close();
        return profile_image;
    }

    private void getfbAlbum(){
         final ArrayList<FbAlbum> alFBAlbum = new ArrayList<>();
         final ArrayList<String>albumlist=new ArrayList<>();
/*make API call*/
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),  //your fb AccessToken
                "/" + AccessToken.getCurrentAccessToken().getUserId() + "/albums",//user id of login user
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("TAG", "Facebook Albums: " + response.toString());
                        try {
                            if (response.getError() == null) {
                                if(pb!=null && pb.isShowing()){
                                    pb.dismiss();
                                }

                                JSONObject joMain = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data"); //find JSONArray from JSONObject

                                    for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                        JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                      Log.e("getAlbum",joAlbum.toString()+"<");
                                      alFBAlbum.add(new FbAlbum(joAlbum.getString("created_time"),joAlbum.getString("name"),joAlbum.getString("id")));
                                      albumlist.add(joAlbum.getString("name"));
                                     // GetFacebookImages(joAlbum.optString("id")); //find Album ID and get All Images from album
                                    }
                                    String[] listArr = new String[albumlist.size()];
                                    listArr = albumlist.toArray(listArr );

                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(

                                            EditProfileActivity.this);

                                    LayoutInflater inflater = getLayoutInflater();

                                    // create view for add item in dialog

                                    View convertView = (View) inflater.inflate(R.layout.alert_fb_picker, null);

                                    // on dialog cancel button listner

                                    alertDialog.setNegativeButton("Cancel",

                                            new DialogInterface.OnClickListener() {

                                                @Override

                                                public void onClick(DialogInterface dialog,

                                                                    int which) {
                                                    dialog.dismiss();
                                                    // TODO Auto-generated method stub



                                                }

                                            });



                                    // add custom view in dialog

                                    alertDialog.setView(convertView);

                                    GridView lv = (GridView) convertView.findViewById(R.id.mylistview);

                                    final AlertDialog alert = alertDialog.create();

                                    alert.setTitle(" Select Album"); // Title

                                    MyAdapter1 myadapter = new MyAdapter1(EditProfileActivity.this,

                                            R.layout.raw_single_list,alFBAlbum );

                                    lv.setAdapter(myadapter);

                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override

                                        public void onItemClick(AdapterView<?> arg0, View arg1,

                                                                int position, long arg3) {

                                            // TODO Auto-generated method stub
                                            GetFacebookImages(alFBAlbum.get(position).getId());

//                        dialog.dismiss();

                                            Log.e("click",alFBAlbum.get(position).getId()+"<<<<<<<<<<<<");

                                            alert.dismiss();

                                        }

                                    });

                                    // show dialog

                                    alert.show();



//                                            .setSingleChoiceItems(listArr, 0, null)
//
//                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
//                        // Do something useful withe the position of the selected radio button
//                        GetFacebookImages(alFBAlbum.get(selectedPosition).getId());
//                        dialog.dismiss();
//                    }
//                })
//                                            .show();
//



                                }
                            } else {
                                Toast.makeText(EditProfileActivity.this,response.getError().toString(),Toast.LENGTH_LONG).show();
                                Log.d("Test", response.getError().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    public void GetFacebookImages(final String albumId) {
        pb=new ProgressDialog(EditProfileActivity.this);
        pb.setMessage("Loading...");
        pb.setCancelable(true);
        pb.show();
//        String url = "https://graph.facebook.com/" + "me" + "/"+albumId+"/photos?access_token=" + AccessToken.getCurrentAccessToken() + "&fields=images";
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        /* make the API call */

                new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.e("TAG", "Facebook Photos response: " + response);
                        //tvTitle.setText("Facebook Images");
                        GetFbImage getFbImage=null;

                   /////////     getFbImage.setData();
                        try {
                            if (response.getError() == null) {



                                JSONObject joMain = response.getJSONObject();
                                Log.e("TAG", "joMAIn " + joMain);
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data");

                 //                   GetFbImage.Datum datum=joMain.optJSONArray("data");
                                    Log.e("TAG", "array: " + jaData);

                                    //lstFBImages = new ArrayList<>();
                                    //FbImages fbImages=new FbImages();
                                    Log.e("lenth",jaData.length()+"<<");
                                    for (int i = 0; i < jaData.length(); i++)//Get no. of images
                                    {
                                        try {
                                            JSONObject joAlbum = jaData.getJSONObject(i);
                                            JSONArray jaImages = joAlbum.getJSONArray("images");// get images Array in JSONArray format
                                            if (jaImages.length() > 0) {
                                                Log.e("in jaimage size",jaImages.length()+"<<");
                                                //for(int j=0;j<jaImages.length();j++){
                                                    fbImagesList.add(new FbImages(jaImages.getJSONObject(0).getString("height"),jaImages.getJSONObject(0).getString("source"),
                                                            jaImages.getJSONObject(0).getString("width")));
//                                                fbImages.setHeight(jaImages.getJSONObject(j).getString("height"));
//                                                fbImages.setSource(jaImages.getJSONObject(j).getString("source"));
//                                                fbImages.setWidth(jaImages.getJSONObject(j).getString("width"));
                                                Log.e("in image", jaImages.getJSONObject(0).getString("source"));

                                                //}

//                                                MediaStore.Images objImages=new MediaStore.Images();//Images is custom class with string url field
//                                                objImages.setImage_url(jaImages.getJSONObject(0).getString("source"));
//                                                lstFBImages.add(objImages);//lstFBImages is Images object array

                                            }

                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                            if (pb != null && pb.isShowing()) {
                                                pb.dismiss();
                                            }
                                        }
                                   }
                                    if (pb != null && pb.isShowing()) {
                                        pb.dismiss();
                                    }
                                    showFbPicker(fbImagesList);

                                }

                                //set your adapter here
                            }
                         else {
                            Log.e("TAG", response.getError().toString());
                                if (pb!=null && pb.isShowing()){
                                    pb.dismiss();
                                }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                            if (pb!=null && pb.isShowing()){
                                pb.dismiss();
                            }
                    }

                }
    }
        ).executeAsync();
}

    private void showFbPicker(final List<FbImages> fbImagesList) {
        Log.e("in adapteer",fbImagesList.get(0).getSource()+"<<");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(

                EditProfileActivity.this);

        LayoutInflater inflater = getLayoutInflater();

        // create view for add item in dialog

        View convertView = (View) inflater.inflate(R.layout.alert_fb_picker, null);

        // on dialog cancel button listner

        alertDialog.setNegativeButton("Cancel",

                new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog,

                                        int which) {
                        dialog.dismiss();
                        // TODO Auto-generated method stub



                    }

                });



        // add custom view in dialog

        alertDialog.setView(convertView);

        GridView lv = (GridView) convertView.findViewById(R.id.mylistview);
        lv.setNumColumns(2);

        final AlertDialog alert = alertDialog.create();

        alert.setTitle(" Select Image...."); // Title

        MyAdapter myadapter = new MyAdapter(EditProfileActivity.this,

                R.layout.raw_item_fb_picker, fbImagesList);

        lv.setAdapter(myadapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> arg0, View arg1,

                                    int position, long arg3) {

                // TODO Auto-generated method stub
Log.e("click",fbImagesList.get(position).getSource()+"<<<<<<<<<<<<");
                if (photo1clicked) {
                    selectedImageUri1 = Uri.parse(fbImagesList.get(position).getSource());
                    try {
                        Glide.with(EditProfileActivity.this)
                                .load(fbImagesList.get(position).getSource())
                                .asBitmap()
                                .listener(new RequestListener<String, Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                        Log.e("Image eXECPTIO",e+"><<<");
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(img_first_photo);

//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri1);
//                        img_first_photo.setImageBitmap(bitmap);
                        MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl1", selectedImageUri1);
                        updateProfileWithImage(body, 1);
                        removephotoclickedFlags();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (photo2clicked) {
                    selectedImageUri1 = Uri.parse(fbImagesList.get(position).getSource());
                    try {
                        Glide.with(EditProfileActivity.this)
                                .load(fbImagesList.get(position).getSource())
                                .asBitmap()
                                .listener(new RequestListener<String, Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                        Log.e("Image eXECPTIO",e+"><<<");
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(img_second_photo);
                 //       Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri2);
                   //     img_second_photo.setImageBitmap(bitmap);
                        MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl2", selectedImageUri2);
                        updateProfileWithImage(body, 2);
                        removephotoclickedFlags();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (photo3clicked) {
                    selectedImageUri1 = Uri.parse(fbImagesList.get(position).getSource());
                    try {
                        Glide.with(EditProfileActivity.this)
                                .load(fbImagesList.get(position).getSource())
                                .asBitmap()
                                .listener(new RequestListener<String, Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                        Log.e("Image eXECPTIO",e+"><<<");
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(img_third_photo);

                       // Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri3);
                        //img_third_photo.setImageBitmap(bitmap);
                        MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl3", selectedImageUri3);
                        updateProfileWithImage(body, 3);
                        removephotoclickedFlags();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (photo4clicked) {
                    selectedImageUri1 = Uri.parse(fbImagesList.get(position).getSource());
                    try {
                        Glide.with(EditProfileActivity.this)
                                .load(fbImagesList.get(position).getSource())
                                .asBitmap()
                                .listener(new RequestListener<String, Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                        Log.e("Image eXECPTIO",e+"><<<");
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(img_fourth_photo);

                        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri4);
                        //img_fourth_photo.setImageBitmap(bitmap);
                        MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl4", selectedImageUri4);
                        updateProfileWithImage(body, 4);
                        removephotoclickedFlags();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (photo5clicked) {
                    selectedImageUri1 = Uri.parse(fbImagesList.get(position).getSource());
                    try {
                        Glide.with(EditProfileActivity.this)
                                .load(fbImagesList.get(position).getSource())
                                .asBitmap()
                                .listener(new RequestListener<String, Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                        Log.e("Image eXECPTIO",e+"><<<");
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(img_fifth_photo);

                        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri5);
                        //img_fifth_photo.setImageBitmap(bitmap);
                        MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl5", selectedImageUri5);
                        updateProfileWithImage(body, 5);
                        removephotoclickedFlags();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (photo6clicked) {
                    selectedImageUri1 = Uri.parse(fbImagesList.get(position).getSource());
                    try {
                        Glide.with(EditProfileActivity.this)
                                .load(fbImagesList.get(position).getSource())
                                .asBitmap()
                                .listener(new RequestListener<String, Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                        Log.e("Image eXECPTIO",e+"><<<");
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(img_sixth_photo);

                        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri6);
                        //img_sixth_photo.setImageBitmap(bitmap);
                        MultipartBody.Part body = prepareFilePart("FbUserPhotoUrl6", selectedImageUri6);
                        updateProfileWithImage(body, 6);
                        removephotoclickedFlags();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                alert.dismiss();

            }

        });

        // show dialog

        alert.show();

    }



    class MyAdapter extends BaseAdapter{

        LayoutInflater inflater;

        Context myContext;

        List<FbImages> newList;

        public MyAdapter(Context context, int resource, List<FbImages> list) {

//            super(context, resource, list);

            // TODO Auto-generated constructor stub

            myContext = context;

            newList = list;

            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return (newList==null)?0:newList.size();
        }

        @Override
        public Object getItem(int i) {
            return newList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override

        public View getView(final int position, View view, ViewGroup parent) {

            final ViewHolder holder;

            if (view == null) {

                holder = new ViewHolder();

                view = inflater.inflate(R.layout.raw_item_fb_picker, null);

                holder.tvSname = (TextView) view.findViewById(R.id.tvtext_item);
                holder.imageView=(ImageView)view.findViewById(R.id.image_item);

                view.setTag(holder);

            } else {

                holder = (ViewHolder) view.getTag();

            }
holder.tvSname.setVisibility(View.GONE);
            //holder.tvSname.setText(newList.get(position).get);
            Glide.with(myContext)
                    .load(newList.get(position).getSource())
                    .asBitmap()
                     .listener(new RequestListener<String, Bitmap>() {
                         @Override
                         public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                             Log.e("Image eXECPTIO",e+"><<<");
                             return false;
                         }

                         @Override
                         public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                             return false;
                         }
                     })
                    .into(holder.imageView);



            return view;

        }

    }

    class MyAdapter1 extends BaseAdapter

    {

        LayoutInflater inflater;

        Context myContext;

        List<FbAlbum> newList;

        public MyAdapter1(Context context, int resource, ArrayList<FbAlbum> list) {

//            super(context, resource, list);

            // TODO Auto-generated constructor stub

            myContext = context;

            newList = list;

            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return (newList==null)?0:newList.size();
        }

        @Override
        public Object getItem(int i) {
            return newList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override

        public View getView(final int position, View view, ViewGroup parent) {

            final ViewHolder holder;

            if (view == null) {

                holder = new ViewHolder();

                view = inflater.inflate(R.layout.raw_single_list, null);

                holder.tvSname = (TextView) view.findViewById(R.id.itemName);


                view.setTag(holder);

            } else {

                holder = (ViewHolder) view.getTag();

            }

            holder.tvSname.setText(newList.get(position).getName());


            return view;

        }

    }


    private class ViewHolder {

        TextView tvSname;
        ImageView imageView;

    }
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && isValidEmail(source)) {
                Toast.makeText(EditProfileActivity.this,"You Can not Enter Email ",Toast.LENGTH_LONG).show();
                return "";
            }

            if (source != null && isValidMobile(source)) {
                Toast.makeText(EditProfileActivity.this,"You Can not Phone Number ",Toast.LENGTH_LONG).show();
                return "";
            }
            return null;
        }
    };

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean isValidMobile(CharSequence phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
