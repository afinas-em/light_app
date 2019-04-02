
package tech.bottleneck.the_lightapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RelativeLayout center, bottom, parent;

    EditText uname, pswd;
    ProgressBar progressBar;
    Button register, login;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        hidekeybord();

        layoutintial();
        onclick();

    }


    private void layoutintial() {
        center = findViewById(R.id.centerrlatve);
        bottom = findViewById(R.id.bottmrlatv);
        login = findViewById(R.id.loginbtn);
        register = findViewById(R.id.signup);
        parent = findViewById(R.id.rootView);
        uname = findViewById(R.id.loginuzr);
        pswd = findViewById(R.id.loginpass);
        progressBar = (ProgressBar)findViewById(R.id.pbar);


    }





    public void onclick() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register_user.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(),Homepage.class);
                startActivity(intent);
                //userLogin();


            }
        });
    }


    private void userLogin() {
        //first getting the values
        final String username = uname.getText().toString();
        final String password = pswd.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            progressBar.setVisibility(View.GONE);

            uname.setError("Please enter your username");
            uname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            progressBar.setVisibility(View.GONE);
            pswd.setError("Please enter your password");
            pswd.requestFocus();
            return;
        }

        final String emil = uname.getText().toString().trim();
        final String pswrd = pswd.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://loopdata.in/Home_c/user_login_app",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);




                        try {

                            JSONObject a = new JSONObject(response);
                            //Log.e("your in",a.toString());
                            //JSONObject b = a.getJSONObject("result");
                            String userid = a.getString("userid");
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONObject result = jsonObject.getJSONObject("result");
//                            String resllt = jsonObject.getString("result");
//                            String userid = jsonObject.getString("userid");

                            if (userid == null){

                              Toast.makeText(MainActivity.this,"Enter valid credentials",Toast.LENGTH_LONG).show();

                            }else {

                                SharedPreferences pref = (MainActivity.this).getSharedPreferences("DEFAULT",MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id",userid);
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(),Homepage.class);
                                startActivity(intent);
                            }



//                            String success = jsonObject.getString("success");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // Toast.makeText(MainActivity.this,(response).toString(),Toast.LENGTH_LONG).show();
                        //If we are getting success from server
                      //  if (response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {

//                            Intent intent = new Intent(MainActivity.this, Homepage.class);
//                            startActivity(intent);
                       //  }else {
                            //If the server response is not success
                            //Displaying an error message on toast
//                            Toast.makeText(MainActivity.this, "Invalid  or password", Toast.LENGTH_LONG).show();
                        //}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("api","thelight");
                params.put("email", emil);
                params.put("pass", pswrd);
                //returning parameter
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void hidekeybord() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


    }


//    hidekey

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}




