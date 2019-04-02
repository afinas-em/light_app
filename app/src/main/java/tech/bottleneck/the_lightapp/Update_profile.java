package tech.bottleneck.the_lightapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Update_profile extends AppCompatActivity {
    EditText entername,enteremail,enternation,entermobile,enterwhatsapp,enterresno,enterofficeno;
    Spinner selectcountry,selectarea;
    RequestQueue rq;
    Button updatebtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        entername = (EditText)findViewById(R.id.name);
        enteremail = (EditText)findViewById(R.id.email);
        enternation = (EditText)findViewById(R.id.nation);
        entermobile = (EditText)findViewById(R.id.mobilenumber);
        enterwhatsapp = (EditText)findViewById(R.id.whatsappno);
        enterresno = (EditText)findViewById(R.id.residenceno);
        enterofficeno = (EditText)findViewById(R.id.officeno);
        selectcountry = (Spinner) findViewById(R.id.country);
        selectarea = (Spinner) findViewById(R.id.area);
        updatebtn= (Button) findViewById(R.id.registeruser);
        progressBar = (ProgressBar)findViewById(R.id.pbar);
        rq = Volley.newRequestQueue(this);
        loadprofile();
        updateprofile();
    }




    private void loadprofile(){

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
                    String ntn = b.getString("nation");
                    String mobilenoqtr = b.getString("mnq");
                    String resno = b.getString("resno");
                    String whatsapqtr = b.getString("wno");
                    String offciceno = b.getString("ofno");
                    entername.setText(name);
                    enteremail.setText(email);
                    enternation.setText(ntn);
                    entermobile.setText(mobilenoqtr);
                    enterwhatsapp.setText(whatsapqtr);
                    enterresno.setText(resno);
                    enterofficeno.setText(offciceno);



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


    private void updateprofile(){
        updatebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                Toast.makeText(Update_profile.this,"User Updated",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(Update_profile.this,Profile.class);
                startActivity(intent);

            }
        });
    }

    private void userupdate(){
        final String username = entername.getText().toString().trim();
        final String email = enteremail.getText().toString().trim();
        final String entrnation = enternation.getText().toString().trim();
        final String entmob = entermobile.getText().toString().trim();
        final String entewhatsapp = enterwhatsapp.getText().toString().trim();
        final String enteresno = enterresno.getText().toString().trim();
        final String enteofficen = enterofficeno.getText().toString().trim();


        // email valiation

        if (TextUtils.isEmpty(username)){
            entername.setError("please enter username");
            entername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            enteremail.setError("Please enter your email");
            enteremail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            enteremail.setError("Enter a valid mail");
            enteremail.requestFocus();
            return;

        }


        StringRequest request = new StringRequest(Request.Method.POST, "http://loopdata.in/Home_c/update_account_app", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Update_profile.this,(response),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                if (response.contains("result")) {
                    Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error update", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", username);
                params.put("email", email);
                params.put("nation", entrnation);
                params.put("mnq", entmob);
                params.put("wno", entewhatsapp);
                params.put("resno", enteresno);
                params.put("ofno", enteofficen);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }



    }

