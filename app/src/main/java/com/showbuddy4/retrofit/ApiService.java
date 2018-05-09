package com.showbuddy4.retrofit;

import com.showbuddy4.model.GetProfileModel;
import com.showbuddy4.model.GetSettingsModel;
import com.showbuddy4.model.GetTopTenListModel;
import com.showbuddy4.model.GetTopTenModel;
import com.showbuddy4.model.InboxModelNew;
import com.showbuddy4.model.LoginModel;
import com.showbuddy4.model.SwipeAction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Jainam on 14-12-2017.
 */

public interface ApiService {



    @POST("register.php")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @FormUrlEncoded

    Call<List<LoginModel>> registerUser(@FieldMap Map<String,String> params);


//    @FormUrlEncoded
//    @POST("register.php")
//    Call<List<LoginModel>> registerUser(@Field("FbProfileId") String FbProfileId,
//                                        @Field("FbUserPhotoUrl1") String FbUserPhotoUrl1,
//                                        @Field("FbUserPhotoUrl2") String FbUserPhotoUrl2,
//                                        @Field("FbUserPhotoUrl3") String FbUserPhotoUrl3,
//                                        @Field("FbUserPhotoUrl4") String FbUserPhotoUrl4,
//                                        @Field("FbUserPhotoUrl5") String FbUserPhotoUrl5,
//                                        @Field("FbUserPhotoUrl6") String FbUserPhotoUrl6,
//                                        @Field("ProfileName") String ProfileName,
//                                        @Field("UserEmail") String UserEmail,
//                                        @Field("UserAddress") String UserAddress,
//                                        @Field("UserDob") String UserDob,
//                                        @Field("FirstName") String FirstName,
//                                        @Field("LastName") String LastName,
//                                        @Field("CollegeName") String CollegeName,
//                                        @Field("Gender") String Gender,
//                                        @Field("About") String About,
//                                        @Field("Age") String Age,
//                                        @Field("Profession") String Profession,
//                                        @Field("Location") String Location);

    @FormUrlEncoded
    @POST("register.php")
    Call<List<GetProfileModel>> getUserProfile(@Field("FbProfileId") String FbProfileId,
                                               @Field("LoggedInUserId") String LoginUserId);

    //Edit Profile Api services
    @FormUrlEncoded
    @POST("update.php")
    Call<LoginModel> updateProfileWithoutPhoto(@Field("FbProfileId") String FbProfileId,
                                               @Field("ProfileName") String ProfileName,
                                               @Field("UserEmail") String UserEmail,
                                               @Field("UserAddress") String UserAddress,
                                               @Field("UserDob") String UserDob,
                                               @Field("FirstName") String FirstName,
                                               @Field("LastName") String LastName,
                                               @Field("CollegeName") String CollegeName,
                                               @Field("Gender") String Gender,
                                               @Field("About") String About,
                                               @Field("Age") String Age,
                                               @Field("Profession") String Profession,
                                               @Field("Location") String Location,
                                               @Field("relationship_status")String relationsheepStatus);

    @Multipart
    @POST("update.php")
    Call<LoginModel> uploadFileWithPhoto(
            @Part("FbProfileId") RequestBody FbProfileId,
            @Part MultipartBody.Part file,
            @Part("ProfileName") RequestBody ProfileName,
            @Part("UserEmail") RequestBody UserEmail,
            @Part("UserAddress") RequestBody UserAddress,
            @Part("UserDob") RequestBody UserDob,
            @Part("FirstName") RequestBody FirstName,
            @Part("LastName") RequestBody LastName,
            @Part("CollegeName") RequestBody CollegeName,
            @Part("Gender") RequestBody Gender,
            @Part("About") RequestBody About,
            @Part("Age") RequestBody Age,
            @Part("Profession") RequestBody Profession,
            @Part("Location") RequestBody Location);

    @FormUrlEncoded
    @POST("getallusers.php")
    Call<List<LoginModel>> getAllUsersResponse(@Field("FbProfileId") String FbProfileId);

    @FormUrlEncoded
    @POST("getmatchusers.php")
    Call<List<InboxModelNew>> getMatchUsersResponse(@Field("FbProfileId") String FbProfileId);


    @FormUrlEncoded
    @POST("insertchatusers.php")
    Call<JSONObject>isUserChat(@Field("fb_id")String FbId,@Field("match_id")String matchID);

    @FormUrlEncoded
    @POST("swipeaction.php")
    Call<SwipeAction> postSwipeAction(@Field("UserProfileId") String UserProfileId,
                                      @Field("LoggedInUserId") String LoggedInUserId,
                                      @Field("Action") String Action);


    //REMOVE PHOTO FROM Profile
    // Api services
    @FormUrlEncoded
    @POST("update.php")
    Call<LoginModel> removePhotoFromProfile(@Field("FbProfileId") String FbProfileId,
                                            @Field("ProfileName") String ProfileName,
                                            @Field("UserEmail") String UserEmail,
                                            @Field("UserAddress") String UserAddress,
                                            @Field("UserDob") String UserDob,
                                            @Field("FirstName") String FirstName,
                                            @Field("LastName") String LastName,
                                            @Field("CollegeName") String CollegeName,
                                            @Field("Gender") String Gender,
                                            @Field("About") String About,
                                            @Field("Age") String Age,
                                            @Field("Profession") String Profession,
                                            @Field("Location") String Location,
                                            @Field("removephotos") String removePhotosName);

    @FormUrlEncoded
    @POST("update_setting.php")
    Call<GetSettingsModel> updateSettings(@Field("FbProfileId") String FbProfileId,
                                          @Field("swipe_location") String swipe_location,
                                          @Field("show_me") String show_me,
                                          @Field("max_dist") String max_dist,
                                          @Field("age_range") String age_range,
                                          @Field("show_me_on_showbuddy") String show_me_on_showbuddy,
                                          @Field("new_matches_on_off") String new_matches_on_off,
                                          @Field("message_on_off") String message_on_off,
                                          @Field("message_like") String message_like,
                                          @Field("super_like") String super_like,
                                          @Field("show_dist") String show_dist,
                                          @Field("relationship_filter_man") String relationship_filter_man,
                                          @Field("relationship_filter_woman") String relationship_filter_woman
                                          );


    @FormUrlEncoded
    @POST("orders.php")
    Call<SwipeAction> addPurchaseOrder(@Field("orderId") String FbProfileId,
                                       @Field("packageName") String swipe_location,
                                       @Field("productId") String show_me,
                                       @Field("fbProfileId") String max_dist,
                                       @Field("useremail") String age_range,
                                       @Field("username") String show_me_on_showbuddy,
                                       @Field("productname") String new_matches_on_off,
                                       @Field("powerswipe") String message_on_off,
                                       @Field("subscription_month") String message_like);


    @FormUrlEncoded
    @POST("getusersettingdata.php")
    Call<List<GetSettingsModel>> getuserSettingAPI(@Field("FbProfileId") String FbProfileId);


    @FormUrlEncoded
    @POST("updatelocation.php")
    Call<SwipeAction> updatelocation(@Field("FbProfileId") String FbProfileId,
                                     @Field("Location") String Location);

    @FormUrlEncoded
    @POST("undo.php")
    Call<SwipeAction> undoSwipeAction(@Field("UserProfileId") String UserProfileId,
                                      @Field("LoggedInUserId") String LoggedInUserId);

    @FormUrlEncoded
    @POST("searchtopten.php")
    Call<List<GetTopTenModel>> GetTopList(@Field("search") String searchvalue,
                                          @Field("search_from") String searchfrom);


    @FormUrlEncoded
    @POST("searchtopten.php")
    Call<List<GetTopTenModel>> GetFirTopList(@Field("fbProfileId") String FbProfileId,
                                    @Field("search_from") String searchfrom);


    @FormUrlEncoded
    @POST("searchtopten.php")
    Call<JSONArray> GetFirstTopList(@Field("fbProfileId") String FbProfileId,
                                    @Field("search_from") String searchfrom);

//    @FormUrlEncoded
//    @POST("searchtopten.php")
//    Call<List<GetTopFirstTime>> GetFirstTopList(@Field("fbProfileId") String FbProfileId,
//                                                @Field("search_from") String searchfrom);



    @FormUrlEncoded
    @POST("getuserratings.php")
    Call<List<GetTopTenListModel>> GetTopTenList(@Field("fbProfileId") String FbProfileId,
                                                 @Field("get_from") String getfrom);

    @FormUrlEncoded
    @POST("checkpremium.php")
    Call<SwipeAction> checkpremium(@Field("LoggedInUserId") String loginUserId);


    @FormUrlEncoded
    @POST("insertrating.php")
    Call<SwipeAction> InsertUserRating(@Field("fbProfileId") String FbProfileId,
                                       @Field("top_type") String toptype,
                                       @Field("1") String one,
                                       @Field("2") String two,
                                       @Field("3") String three,
                                       @Field("4") String four,
                                       @Field("5") String five,
                                       @Field("6") String six,
                                       @Field("7") String seven,
                                       @Field("8") String eight,
                                       @Field("9") String nine,
                                       @Field("10") String ten);

    @FormUrlEncoded
    @POST("insertrating.php")
    Call<SwipeAction> InsertUserRatingGenres(@Field("fbProfileId") String FbProfileId,
                                             @Field("top_type") String toptype,
                                             @Field("1") String one,
                                             @Field("2") String two,
                                             @Field("3") String three,
                                             @Field("4") String four,
                                             @Field("5") String five,
                                             @Field("6") String six,
                                             @Field("7") String seven,
                                             @Field("8") String eight,
                                             @Field("9") String nine,
                                             @Field("10") String ten,
                                             @Field("11") String eleven,
                                             @Field("12") String twelve,
                                             @Field("13") String thirteen,
                                             @Field("14") String forteen,
                                             @Field("15") String fifteen);




    @FormUrlEncoded
    @POST("insertrating.php")
    Call<SwipeAction> UpdateTopgenrel(@Field("fbProfileId") String FbProfileId,
                                             @Field("top_type") String toptype,
                                      @FieldMap Map<String,String> params);


}
