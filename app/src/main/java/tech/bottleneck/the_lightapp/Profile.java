package tech.bottleneck.the_lightapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {
    TextView textname,textemail,textsemster,textcountry,textnation;
    ImageView editdata;
    BottomNavigationView bottombar;
    RequestQueue rq;
    ProgressBar progressBar;
    Button signout,about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bottombar = (BottomNavigationView)findViewById(R.id.bottmnav);
        Bottombarass();
        textname = (TextView)findViewById(R.id.username);
        textemail = (TextView)findViewById(R.id.emailid);
        textsemster = (TextView)findViewById(R.id.semster);
        textcountry = (TextView)findViewById(R.id.countryy);
        textnation = (TextView)findViewById(R.id.nation);
        editdata = (ImageView) findViewById(R.id.editprofle);
        signout=(Button)findViewById(R.id.logout);
        progressBar = (ProgressBar)findViewById(R.id.pbar);
        about=(Button)findViewById(R.id.aboutpge);
        rq = Volley.newRequestQueue(this);
        loadData();
        Imageviewclick();

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),About.class);
                startActivity(intent);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


    }
    private  void  loadData(){
        SharedPreferences pref = getSharedPreferences("DEFAULT", MODE_PRIVATE);
        String user_id = pref.getString("user_id", "121");

        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://loopdata.in/Home_c/account_app/"+user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject a = new JSONObject(response);
                    JSONObject b = a. getJSONObject("result");

                    String name = b.getString("name");
                    String email = b.getString("email");
                    String sem = b.getString("sem");
                    String ntn = b.getString("nation");
                    String cntry = b.getString("country");
                    textname.setText(name);
                    textemail.setText(email);
                    textsemster.setText(sem);
                    textnation.setText(ntn);
                    textcountry.setText(cntry);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(stringRequest);
    }


    private void Imageviewclick(){
        editdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(),Update_profile.class);
                startActivity(intent);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }



    private void Bottombarass(){
        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){


                    case R.id.hme:
                        Intent intent = new Intent(getApplicationContext(),Homepage.class);
                        startActivity(intent);
                        return true;
                    case R.id.msge:
                        Intent intent1 = new Intent(getApplicationContext(),Messages.class);
                        startActivity(intent1);
                        return true;
                    case R.id.exm:
                        Intent intent4 = new Intent(getApplicationContext(),Examination.class);
                        startActivity(intent4);
                        return true;

                    case R.id.pfil:
                        Toast.makeText(getApplicationContext(),"your in profile",Toast.LENGTH_SHORT).show();
                        return true;



                }
                return false;

            }
        });


    }

    private void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();



                        //Starting login activity
                        Intent intent = new Intent(Profile.this, MainActivity.class);
                        startActivity(intent);

                        Toast.makeText(Profile.this, "you're signed out", Toast.LENGTH_LONG).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }


}
