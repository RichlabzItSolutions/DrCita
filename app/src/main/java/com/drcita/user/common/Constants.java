package com.drcita.user.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.drcita.user.models.signup.SignupResponse;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

public class Constants {
    public static final String LANGUAGE = "language";
    public static String PREFERENCE_NAME = "caafisom";
    public static final String USER_ID = "userid";
    public static final String Currancy_Type= "currencytype";
    public static final String PROVIDER_ID = "providerId";
    public static final String NAME = "name";
    public static final String MOBILE = "mobile";
    public static final String REGION = "region";
    public static final String LANGUAGE1 = "language1";
    public static final String STATE_ID = "stateId";
    public static final String CITY_ID = "cityid";
    public static final String PROFILESTATUS = "profile_status";
    public static final Boolean LOGIN = false;
    public static String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static String MobilePattern = "[6]{1}[1235]{1}[0-9]{7}";
    public static String MobilePattern1 = "[7]{1}[7]{1}[0-9]{7}";
    public static String MobilePatternzaad = "[6]{1}[3]{1}[0-9]{7}";

    public static String MobilePatternzaadevc = "[6]{1}[1]{1}[0-9]{7}";
    public static String MobilePatternzaadevc1 = "[7]{1}[7]{1}[0-9]{7}";

    public static String MobilePatternedaahab = "[6]{1}[5]{1}[0-9]{7}";

    public static String MobilePatternzaadevcezad = "[6]{1}[2]{1}[0-9]{7}";

    public static final String enter_valid_mobile_no="Enter valid Phone Number";
    public static final String enter_valid_email_id="Enter valid email id";
    public static final String signup ="signup";
    public static final String list ="listFrom";
    public static final String recipt ="reciptFrom";
    public static final String isfromdental ="isfromdental";



    public static final String forgetpassword = "forgetpassword";

    public static boolean haveInternet(Context ctx) {
        try {
            NetworkInfo info = ((ConnectivityManager) Objects.requireNonNull(ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE)))
                    .getActiveNetworkInfo();

            if (info == null || !info.isConnected()) {
                return false;
            }
        } catch (Exception e) {
            Log.d("err", e.toString());
        }
        return true;
    }
    public static void InternetSettings(final Context ctx) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder
                .setMessage("No internet connection on your device. Would you like to enable it?")
                .setTitle("No Internet Connection")
                .setCancelable(false)
                .setPositiveButton(" Enable Internet ",
                        new DialogInterface.OnClickListener()
                        {

                            public void onClick(DialogInterface dialog, int id)
                            {
                                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ctx.startActivity(dialogIntent);

                            }
                        });

        alertDialogBuilder.setNegativeButton(" Cancel ", (dialog, id) -> dialog.cancel());

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    public static void ToastShort(Context context,String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
    public static void ToastLong(Context context,String string){
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public static void displayError(String msg, Context context){
        String string;
        string = msg;
        try {
            SignupResponse signupResponse = new Gson().fromJson(string, SignupResponse.class);
            List<String> errors = signupResponse.getErrors();
            if (errors != null && errors.size() > 0) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < errors.size(); i++) {
                    builder.append(errors.get(i));
                    if (i < errors.size() - 1) {
                        builder.append("\n");
                    }
                }
                Toast.makeText(context, builder.toString(), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
        }
    }
}
