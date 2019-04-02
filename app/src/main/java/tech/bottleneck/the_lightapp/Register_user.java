package tech.bottleneck.the_lightapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register_user extends AppCompatActivity {

    private static final String URL_ROOT ="http://loopdata.in/Home_c/registration_app";


    EditText entername,enterpassword,enteremail,enternation,entermobile,enterwhatsapp,enterresno,enterofficeno;
    Spinner selectcountry,selectarea;
    Button registerbutn;
    ProgressBar progressBar;
    private RadioGroup genderRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);



        entername = (EditText)findViewById(R.id.name);
        enteremail = (EditText)findViewById(R.id.email);
        enterpassword = (EditText)findViewById(R.id.password);
        enternation = (EditText)findViewById(R.id.nation);
        entermobile = (EditText)findViewById(R.id.mobilenumber);
        enterwhatsapp = (EditText)findViewById(R.id.whatsappno);
        enterresno = (EditText)findViewById(R.id.residenceno);
        enterofficeno = (EditText)findViewById(R.id.officeno);
        selectcountry = (Spinner) findViewById(R.id.country);
        selectarea = (Spinner) findViewById(R.id.area);
        registerbutn= (Button) findViewById(R.id.registeruser);
        genderRadioGroup=(RadioGroup)findViewById(R.id.radiogp);
        progressBar = (ProgressBar)findViewById(R.id.pbar);
        hidekeybord();

        countryarray();
        contryarea();


        registerbutn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressBar.setVisibility(View.VISIBLE);
                registeruser();

            }
        });






    }


    public void countryarray(){
        List<String> contry =new ArrayList<>();

        contry.add(0,"Select Country");
        contry.add("Saudi Arabia");
        contry.add("UAE");
        contry.add("Kuwait");
        contry.add("Qatar");
        contry.add("Baharain");

        ArrayAdapter<String>dataAdapter;
        dataAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,contry);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectcountry.setAdapter(dataAdapter);

        selectcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Select Country")){

                }
                else {
                    String item = parent.getItemAtPosition(position).toString();

                    if (item.equals("Qatar")) {
                        selectarea.setVisibility(View.VISIBLE);
                    }

                    else {
                        selectarea.setVisibility(View.INVISIBLE);
                    }



                }


            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }
        });

    }


    public void contryarea(){
        List<String> resare =new ArrayList<>();

        resare.add(0,"Select Area In qatar");
        resare.add("Wakara");
        resare.add("Madina Khalifa");
        resare.add("Hilal");
        resare.add("Al khore");
        resare.add("Dukhan");
        resare.add("Doha");
        resare.add("Abu hamour");
        resare.add("Musaeed");
        resare.add("Shahaniya");
        resare.add("Shamal");
        resare.add("Bu samra");

        ArrayAdapter<String> redataAdapter;
        redataAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,resare);
        redataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectarea.setAdapter(redataAdapter);

        selectarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Area In qatar")){


                }

                else {
                    String item =parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void registeruser() {

        final String username = entername.getText().toString().trim();
        final String email = enteremail.getText().toString().trim();
        final String pwd = enterpassword.getText().toString().trim();
        final String entrnation = enternation.getText().toString().trim();
        final String entmob = entermobile.getText().toString().trim();
        final String entewhatsapp = enterwhatsapp.getText().toString().trim();
        final String enteresno = enterresno.getText().toString().trim();
        final String enteofficen = enterofficeno.getText().toString().trim();
        final String selectcntry = selectcountry.getSelectedItem().toString();
        final String selarea = selectarea.getSelectedItem().toString();
        final String gendr = ((RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString();


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


        StringRequest request = new StringRequest(Request.Method.POST, "http://loopdata.in/Home_c/registration_app", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                if (response.contains("result")) {
                    Toast.makeText(getApplicationContext(), "You Have Seccusfully Registerd! waiting for confirmation", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error Registering", Toast.LENGTH_SHORT).show();
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
                params.put("api_app", "thelight");
                params.put("name", username);
                params.put("email", email);
                params.put("password", pwd);
                params.put("nation", entrnation);
                params.put("mnq", entmob);
                params.put("wno", entewhatsapp);
                params.put("resno", enteresno);
                params.put("ofno", enteofficen);
                params.put("country", selectcntry);
                params.put("raq", selarea);
                params.put("gender", gendr);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }

    public void hidekeybord() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


    }




}
