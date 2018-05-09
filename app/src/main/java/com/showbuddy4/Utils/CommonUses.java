package com.showbuddy4.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

 import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class CommonUses {

    Context context = null;


    public static String getCurrentTimestamp(String dateTimeFormat) {
        try {

            //for current date in dd-MM-yyyy format
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            //for current date in dd-MM-yyyy HH:mm:ss format
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }



    public static String getAge(String stDate){
        int year,  month,  day;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        String ageS = "";
        try {
            Date date = sdf.parse(stDate);// all done
            Calendar cal = sdf.getCalendar();
             year=cal.get(Calendar.YEAR);
             month=cal.get(Calendar.MONTH)+1;
             day=cal.get(Calendar.DATE);
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            dob.set(year, month, day);

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                age--;
            }

            Integer ageInt = new Integer(age);
            ageS = ageInt.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ageS;
    }

    public String currentDateWithPlus() {
        String CurrentDatewithPlus = "";

        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();

        DateFormat date = new SimpleDateFormat("dd+MMMM+yyyy");
        CurrentDatewithPlus = date.format(currentLocalTime);

        Log.d("CurrentDatewithPlus", CurrentDatewithPlus);

        return CurrentDatewithPlus;
    }

    public static void showSnackbar(View view, String showtext) {
        Snackbar.make(view, showtext, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static void printErrorMessage(Response response, EditText edittext) {

        try {
            ResponseBody user = response.errorBody();
            String errorMessage = user.string();
            if (errorMessage != null) {
                if (!errorMessage.equals("")) {
                    JSONObject erroObj = new JSONObject(errorMessage);
                    showSnackbar(edittext, erroObj.optString("message"));
                }
            }
        } catch (IOException e) {
            // handle failure to read error
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void showToastErrorMesage(Context context, Response response) {

        try {
            ResponseBody user = response.errorBody();
            String errorMessage = user.string();
            if (errorMessage != null) {
                if (!errorMessage.equals("")) {
                    JSONObject erroObj = new JSONObject(errorMessage);
                    showToast(context, erroObj.optString("message"));
                }
            }
        } catch (IOException e) {
            // handle failure to read error
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static String calculateAge(Date birthDate) {
        int years = 0;
        int months = 0;
        int days = 0;
        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());
        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);
        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH);
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;
        //Get difference between months
        months = currMonth - birthMonth;
        //if month difference is in negative then reduce years by one and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }
        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        String age = Integer.toString(years) + " Years " + Integer.toString(months) + " Months ";
        return age;
    }


    public static String calculateAgeDifference(Date startDate, Date endDate) {
        int years = 0;
        int months = 0;
        int days = 0;
        //create calendar object for birth day
        Calendar startDay = Calendar.getInstance();
        startDay.setTimeInMillis(startDate.getTime());
        //create calendar object for current day
        Calendar endDay = Calendar.getInstance();
        endDay.setTimeInMillis(endDate.getTime());
        //Get difference between years
        years = endDay.get(Calendar.YEAR) - startDay.get(Calendar.YEAR);
        int currMonth = endDay.get(Calendar.MONTH) + 1;
        int birthMonth = startDay.get(Calendar.MONTH) + 1;
        //Get difference between months
        months = currMonth - birthMonth;
        //if month difference is in negative then reduce years by one and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (endDay.get(Calendar.DATE) < startDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && endDay.get(Calendar.DATE) < startDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }
        //Calculate the days
        if (endDay.get(Calendar.DATE) > startDay.get(Calendar.DATE))
            days = endDay.get(Calendar.DATE) - startDay.get(Calendar.DATE);
        else if (endDay.get(Calendar.DATE) < startDay.get(Calendar.DATE)) {
            int today = endDay.get(Calendar.DAY_OF_MONTH);
            endDay.add(Calendar.MONTH, -1);
            days = endDay.getActualMaximum(Calendar.DAY_OF_MONTH) - startDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        String age = Integer.toString(years) + " Years " + Integer.toString(months) + " Months ";
        return age;
    }


    //inputdateFormat = yyyy-MM-dd , outputDateFormat = dd-MM-yyyy
    public static String convertDatetoShow(String inputDate_yyyyMMdd) {
        String outPutData = null;

        try {
            SimpleDateFormat outputParser = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd");

            Date dobdate = null;
            dobdate = inputParser.parse(inputDate_yyyyMMdd);
            outPutData = outputParser.format(dobdate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outPutData;

    }

    //inputdateFormat = dd-MM-yyyy, outputDateFormat = yyyy-MM-dd
    public static String convertDatetoSaveData(String inputDate_ddMMyyyy) {
        String outPutData = null;

        try {
            SimpleDateFormat inputParser = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat outputParser = new SimpleDateFormat("yyyy-MM-dd");

            Date dobdate = null;
            dobdate = inputParser.parse(inputDate_ddMMyyyy);
            outPutData = outputParser.format(dobdate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outPutData;
    }

    //get HeatIntimation Date after 21 days of last Heat date
    public static String getIntimationDate(String heatCycleDateString, int AfterDaysCount) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(heatCycleDateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, AfterDaysCount);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        return sdf.format(c.getTime());
    }


    //method for Check whether string is null or Blank
    //returns true if string have something
    public static boolean checkforNullorBlankString(String checkString) {

        if (checkString != null) {
            if (!checkString.equals("")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void showPositiveDialogButton(Context context, String title, String message) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public static void clearBadge(Context context) {
        setBadgeSamsung(context, 0);
    }

    public static void setBadgeSamsung(Context context, int count) {
        //Badge details get it from below URL :
        //http://stackoverflow.com/questions/19086189/adding-notification-badge-on-app-icon-in-android?rq=1


        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static void showMessageSnackbarForError(Context context, View view, String message) {

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();

        sbView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
        snackbar.show();
    }

    public static void addGPSTimerGet(HttpURLConnection con) {

        try {
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestMethod("GET");
            String USER_AGENT = "Mozilla/5.0";
            con.setRequestProperty("User-Agent", USER_AGENT);
        } catch (Exception e) {

        }
    }



    public static String convertTimeFormat(String inputTime, String inputFormat, String outputFormat) {
        String outputTimeString = "";
        try {
            DateFormat f1 = new SimpleDateFormat(inputFormat);
            Date d = f1.parse(inputTime);

            DateFormat f2 = new SimpleDateFormat(outputFormat);
            outputTimeString = f2.format(d); //

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputTimeString;
    }


    public static String convertDateFormat(String inputDate, String inputDateFormat, String outputDateFormat) {
        String outPutData = null;

        try {
            SimpleDateFormat inputParser = new SimpleDateFormat(inputDateFormat);
            SimpleDateFormat outputParser = new SimpleDateFormat(outputDateFormat);

            Date dobdate = null;
            dobdate = inputParser.parse(inputDate);
            outPutData = outputParser.format(dobdate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outPutData;
    }


    public static boolean datecomparision(String startDate, String endDate, String dateFormat) {
        try {

            SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            Date enddate = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (enddate.after(startingDate))
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public static void showToast(Context context, String showtext) {
        Toast.makeText(context, showtext, Toast.LENGTH_SHORT).show();
    }


    public static Toast mToastToShow;

    public static void showToastwithDuration(int timeinMillis, String text, Context context) {
        // Set the toast and duration
        int toastDurationInMilliSeconds = timeinMillis;
        mToastToShow = Toast.makeText(context, text, Toast.LENGTH_LONG);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                mToastToShow.show();
            }

            public void onFinish() {
                mToastToShow.cancel();
            }
        };

        // Show the toast and starts the countdown
        mToastToShow.show();
        toastCountDown.start();
    }

    public static void showSnackwithDuration(int timeinMillis, String text, View view, Context context) {
        // Set the toast and duration
        int toastDurationInMilliSeconds = timeinMillis;
        mToastToShow = Toast.makeText(context, text, Toast.LENGTH_LONG);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                mToastToShow.show();
            }

            public void onFinish() {
                mToastToShow.cancel();
            }
        };

        // Show the toast and starts the countdown
        mToastToShow.show();
        toastCountDown.start();

        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                snackbar.dismiss();
            }
        }, timeinMillis);

    }





    public static String getCurrentDateStr(String serverTime) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        if (!serverTime.isEmpty())
            return serverTime;
        else
            return df.format(c.getTime());

    }


    public static void submitBtnDisabled(Button btnSubmit) {
        btnSubmit.setVisibility(View.INVISIBLE);
//        btnSubmit.setClickable(false);
//        btnSubmit.setEnabled(false);
    }


    public static void submitBtnEnabled(Button btnSubmit) {
        btnSubmit.setVisibility(View.VISIBLE);
//        btnSubmit.setClickable(true);
//        btnSubmit.setEnabled(true);
    }


}
