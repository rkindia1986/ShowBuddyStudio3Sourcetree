package com.showbuddy4.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.showbuddy4.R;
import com.showbuddy4.activity.ChatActivity;
import com.showbuddy4.activity.ChatMessageActivity;
import com.showbuddy4.adapter.HorizontalChatList;
import com.showbuddy4.adapter.NewInboxAdaptor;
import com.showbuddy4.model.InboxModelNew;
import com.showbuddy4.model.Model_Inbox;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.quickchatdialog.Common;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Chat_View extends Fragment implements OnImageClickListener,HorizontalChatList.OnItemGetData  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView,recycHorizontal;
    ArrayList<InboxModelNew> arrayList = new ArrayList<InboxModelNew>();
    ArrayList<InboxModelNew> arrayList1 = new ArrayList<InboxModelNew>();
    Model_Inbox model_inbox;
    NewInboxAdaptor inboxAdapter;
    HorizontalChatList horizontalChatList;
    ProgressDialog progressDialog;
    PreferenceApp pref;
    TextView tv_profile_name;
String username="";
    public Fragment_Chat_View() {
        // Required empty public constructor
    }

    @Override
    public void onImageClick(InboxModelNew model_inbox) {

        ChatActivity.qbuserid = model_inbox.getQbuserid();
        ChatActivity.qbpass = model_inbox.getQbpass();
        ChatActivity.qbuserlogin = model_inbox.getQbuserlogin();
        username =model_inbox.getFirstName() ;
        createDiaalog(model_inbox.getQbuserid());
      //  startActivity(new Intent(getActivity(), ChatActivity.class));
    }

    public void createDiaalog(String qbuserid) {
       /* ArrayList<Integer> occupantIdsList = new ArrayList<Integer>();
        occupantIdsList.add(44771703);
        occupantIdsList.add(Integer.parseInt(qbuserid));
        Log.e(TAG, "createDiaalog: qbuserid = " + qbuserid );*/
        Log.e("Tag", "createDiaalog:qbuserid  "+qbuserid );
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
                Log.e("TAG", "onSuccess:  Dialog Sender Id: " + result.getUserId() +" Rec Id: "+ result.getRecipientId());
                // result.initForChat(QBChatService.getInstance());
                // result.initForChat(result.getDialogId(), QBDialogType.PRIVATE, QBChatService.getInstance());
                //  QBChatDialog qbChatDialog = (QBChatDialog) listchatdialog.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), ChatMessageActivity.class);
                intent.putExtra(Common.DIALOG_EXTRA, result);
                intent.putExtra("recname", username);
                startActivity(intent);
            }

            @Override
            public void onError(QBResponseException responseException) {
                Log.e("TAG", "onError:  Dialog" + responseException.getLocalizedMessage());
            }
        });


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Chat_View.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Chat_View newInstance(String param1, String param2) {
        Fragment_Chat_View fragment = new Fragment_Chat_View();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment__chat__view, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new PreferenceApp(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewinbox);
        recycHorizontal=(RecyclerView)view.findViewById(R.id.recyclerHorizontal);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager horizontalM
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recycHorizontal.setLayoutManager(horizontalM);
        inboxAdapter = new NewInboxAdaptor(arrayList, getActivity(),this);
        horizontalChatList=new HorizontalChatList(arrayList1,getActivity(),this,this);
        recyclerView.setAdapter(inboxAdapter);
        recycHorizontal.setAdapter(horizontalChatList);
        getMatchUsers();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getMatchUsers() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<List<InboxModelNew>> call = apiService.getMatchUsersResponse(pref.getFbId());

        call.enqueue(new Callback<List<InboxModelNew>>() {
            @Override
            public void onResponse(@NonNull Call<List<InboxModelNew>> call, @NonNull Response<List<InboxModelNew>> response) {

                try {
                    List<InboxModelNew> list = response.body();
                    if (response.code() == 200) {


                        Log.e("GETMATVH",response.body().toString()+"<");
//                        String qbuserid = list.get(0).getQbuserid();
//                        String qbuserlogin = list.get(0).getQbuserlogin();
//                        String qbpass = list.get(0).getQbpass();

                        if (list.size() > 0) {
                            for (int i=0;i<list.size();i++){
                                if(list.get(i).getChatwith().equalsIgnoreCase("yes")){
                                    InboxModelNew aNew=new InboxModelNew();
                                    aNew.setChatwith(list.get(i).getChatwith());
                                    aNew.setId(list.get(i).getId());
                                    aNew.setFbProfileId(list.get(i).getFbProfileId());
                                    aNew.setFbUserPhotoUrl1(list.get(i).getFbUserPhotoUrl1());
                                    aNew.setFbUserPhotoUrl2(list.get(i).getFbUserPhotoUrl2());
                                    aNew.setFbUserPhotoUrl3(list.get(i).getFbUserPhotoUrl3());
                                    aNew.setFbUserPhotoUrl4(list.get(i).getFbUserPhotoUrl4());
                                    aNew.setFbUserPhotoUrl5(list.get(i).getFbUserPhotoUrl5());
                                    aNew.setFbUserPhotoUrl6(list.get(i).getFbUserPhotoUrl6());
                                    aNew.setProfileName(list.get(i).getProfileName());
                                    aNew.setUserEmail(list.get(i).getUserEmail());
                                    aNew.setUserAddress(list.get(i).getUserAddress());
                                    aNew.setUserDob(list.get(i).getUserDob());
                                    aNew.setFirstName(list.get(i).getFirstName());
                                    aNew.setLastName(list.get(i).getLastName());
                                    aNew.setCollegeName(list.get(i).getCollegeName());
                                    aNew.setGender(list.get(i).getGender());
                                    aNew.setAbout(list.get(i).getAbout());
                                    aNew.setCreatedDate(list.get(i).getCreatedDate());
                                    aNew.setAge(list.get(i).getAge());
                                    aNew.setProfession(list.get(i).getProfession());
                                    aNew.setLocation(list.get(i).getLocation());
                                    aNew.setQbuserid(list.get(i).getQbuserid());
                                    aNew.setQbuserlogin(list.get(i).getQbuserlogin());
                                    aNew.setQbpass(list.get(i).getQbpass());
                                    aNew.setAction(list.get(i).getAction());
                                    aNew.setActionDate(list.get(i).getActionDate());
                                    aNew.setUserActionBy(list.get(i).getUserActionBy());
                                    aNew.setUserActionTo(list.get(i).getUserActionTo());
                                   arrayList.add(aNew);
                                }else {
                                    InboxModelNew aNew=new InboxModelNew();
                                    aNew.setChatwith(list.get(i).getChatwith());
                                    aNew.setId(list.get(i).getId());
                                    aNew.setFbProfileId(list.get(i).getFbProfileId());
                                    aNew.setFbUserPhotoUrl1(list.get(i).getFbUserPhotoUrl1());
                                    aNew.setFbUserPhotoUrl2(list.get(i).getFbUserPhotoUrl2());
                                    aNew.setFbUserPhotoUrl3(list.get(i).getFbUserPhotoUrl3());
                                    aNew.setFbUserPhotoUrl4(list.get(i).getFbUserPhotoUrl4());
                                    aNew.setFbUserPhotoUrl5(list.get(i).getFbUserPhotoUrl5());
                                    aNew.setFbUserPhotoUrl6(list.get(i).getFbUserPhotoUrl6());
                                    aNew.setProfileName(list.get(i).getProfileName());
                                    aNew.setUserEmail(list.get(i).getUserEmail());
                                    aNew.setUserAddress(list.get(i).getUserAddress());
                                    aNew.setUserDob(list.get(i).getUserDob());
                                    aNew.setFirstName(list.get(i).getFirstName());
                                    aNew.setLastName(list.get(i).getLastName());
                                    aNew.setCollegeName(list.get(i).getCollegeName());
                                    aNew.setGender(list.get(i).getGender());
                                    aNew.setAbout(list.get(i).getAbout());
                                    aNew.setCreatedDate(list.get(i).getCreatedDate());
                                    aNew.setAge(list.get(i).getAge());
                                    aNew.setProfession(list.get(i).getProfession());
                                    aNew.setLocation(list.get(i).getLocation());
                                    aNew.setQbuserid(list.get(i).getQbuserid());
                                    aNew.setQbuserlogin(list.get(i).getQbuserlogin());
                                    aNew.setQbpass(list.get(i).getQbpass());
                                    aNew.setAction(list.get(i).getAction());
                                    aNew.setActionDate(list.get(i).getActionDate());
                                    aNew.setUserActionBy(list.get(i).getUserActionBy());
                                    aNew.setUserActionTo(list.get(i).getUserActionTo());
                                    arrayList1.add(aNew);
                                }
                            }
                            //inboxAdapter = new NewInboxAdaptor((ArrayList<InboxModelNew>) list, getActivity(), Fragment_Chat_View.this);
                            inboxAdapter = new NewInboxAdaptor(arrayList, getActivity(), Fragment_Chat_View.this);
                            recyclerView.setAdapter(inboxAdapter);

                            inboxAdapter.notifyDataSetChanged();

                            horizontalChatList=new HorizontalChatList(arrayList1,getActivity(),Fragment_Chat_View.this,Fragment_Chat_View.this);
                            recycHorizontal.setAdapter(horizontalChatList);
                            horizontalChatList.notifyDataSetChanged();

                        }


                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<InboxModelNew>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void onItemGetData(InboxModelNew model) {
        if(model!=null){

            arrayList.add(arrayList.size(),model);
            inboxAdapter = new NewInboxAdaptor(arrayList, getActivity(), Fragment_Chat_View.this);
            recyclerView.setAdapter(inboxAdapter);
            inboxAdapter.notifyDataSetChanged();
            horizontalChatList.notifyDataSetChanged();


            ApiService apiService =
                    ApiClient.getClient().create(ApiService.class);

            Call<JSONObject> call = apiService.isUserChat(pref.getFbId(),model.getFbProfileId());
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    Log.e("in","isusetChat --"+response.body().toString()+"");
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    Log.e("in","isusetChat Failed --"+t.toString()+"");
                }
            });


//            horizontalChatList=new HorizontalChatList(arrayList1,getActivity(),Fragment_Chat_View.this,Fragment_Chat_View.this);
//            recycHorizontal.setAdapter(horizontalChatList);
//            horizontalChatList.notifyDataSetChanged();
//            inboxAdapter.notifyDataSetChanged();
        }

    }
}
