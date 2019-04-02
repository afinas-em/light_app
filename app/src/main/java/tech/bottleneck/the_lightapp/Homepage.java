package tech.bottleneck.the_lightapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import tech.bottleneck.the_lightapp.model.Data;
import tech.bottleneck.the_lightapp.model.Result;

public class Homepage extends AppCompatActivity {
    private static final String lesson_URL = "http://loopdata.in/Sem1_c/get_lesson_category_app/42/QURAN";
    RelativeLayout qrancard, seerahcard, fiqhcard, aqueedacard;
    BottomNavigationView bottombar;
    ProgressBar progressHome;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        bottombar = findViewById(R.id.bottmnav);
        qrancard = findViewById(R.id.quranbtn);
        seerahcard = findViewById(R.id.seerahbtn);
        fiqhcard = findViewById(R.id.fiquhbtn);
        aqueedacard = findViewById(R.id.aqueedabtn);
        progressHome = findViewById(R.id.progressHome);
        imageView = findViewById(R.id.logohome);

        intelizedata();
        bottombarass();
        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.colorlogo)).getBitmap();
        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 300, 300, mpaint);// Round Image Corner 100 100 100 100
        imageView.setImageBitmap(imageRounded);

    }

    //roundimage




    //home screen button click

    public void intelizedata() {


        qrancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("DEFAULT", MODE_PRIVATE);
                String user_id = pref.getString("user_id", "121");
                //API extension changed to subject name
                getJsonObject("http://loopdata.in/Sem1_c/get_lesson_category_app/"+user_id+"/QURAN");
            }
        });
        seerahcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("DEFAULT", MODE_PRIVATE);
                String user_id = pref.getString("user_id", "121");
                getJsonObject("http://loopdata.in/Sem1_c/get_lesson_category_app/"+user_id+"/Seerah");
            }
        });
        fiqhcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {SharedPreferences pref = getSharedPreferences("DEFAULT", MODE_PRIVATE);
                String user_id = pref.getString("user_id", "121");

                getJsonObject("http://loopdata.in/Sem1_c/get_lesson_category_app/"+user_id+"/Fiqh");
            }
        });
        aqueedacard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {SharedPreferences pref = getSharedPreferences("DEFAULT", MODE_PRIVATE);
                String user_id = pref.getString("user_id", "121");

                getJsonObject("http://loopdata.in/Sem1_c/get_lesson_category_app/"+user_id+"/Aqeeda");
            }
        });
    }

    public void getJsonObject(String urlExtension) {
        RequestQueue requestQueue;
        progressHome.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObbject = new JsonObjectRequest(Request.Method.GET, urlExtension, null,
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        progressHome.setVisibility(View.GONE);
                        processResponse(response);  // call to method
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressHome.setVisibility(View.GONE);
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });

        requestQueue.add(jsonObbject);
    }

    private void processResponse(JSONObject response) {
        Gson gson = new Gson();
        Data model = gson.fromJson(response.toString(), Data.class);
        ArrayList<Result> result = model.getResult();

        if(result== null || result.isEmpty()){
            Toast.makeText(this, "No lessons found", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, Lessons_page.class);
        intent.putParcelableArrayListExtra("RESULT", result);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottombar.setSelectedItemId(R.id.hme);
    }

    private void bottombarass() {
        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {


                    case R.id.hme:
//                        Toast.makeText(getApplicationContext(), "You Already in Home", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.msge:
                       Intent intent1 = new Intent(getApplicationContext(), Messages.class);
                      startActivity(intent1);
                        return true;
                    case R.id.exm:
                        Intent intent4 = new Intent(getApplicationContext(), Examination.class);
                        startActivity(intent4);
                        return true;

                    case R.id.pfil:
                        Intent intent3 = new Intent(getApplicationContext(), Profile.class);
                        startActivity(intent3);
                        return true;


                }
                return false;

            }
        });
    }


}
