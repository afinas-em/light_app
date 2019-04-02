package tech.bottleneck.the_lightapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Quiz extends AppCompatActivity {


    LinearLayout lytQuest;
    LinearLayout lytStartScreen;

    TextView txtIndex;
    TextView txtQ;
    RadioGroup rdoGrp;
    RadioButton btn1;
    RadioButton btn2;
    RadioButton btn3;
    RadioButton btn4;

    ScrollView scrollView;

    Button btnPre;
    Button btnNext;

    TextView txtHead;
    Button btnStart;
    private ArrayList<QuizModel> datalist;
    private int index = 0;
    private ArrayList<String> ans;
    private int exam_id;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        context = this;

        lytQuest = findViewById(R.id.lytQuest);
        lytStartScreen = findViewById(R.id.lytStartScreen);
        txtIndex = findViewById(R.id.txtIndex);
        txtQ = findViewById(R.id.txtQ);
        rdoGrp = findViewById(R.id.rdoGrp);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btnPre = findViewById(R.id.btnPre);
        btnNext = findViewById(R.id.btnNext);
        txtHead = findViewById(R.id.txtHead);
        btnStart = findViewById(R.id.btnStart);
        scrollView = findViewById(R.id.scrollView);

        ans = new ArrayList<>();
        loadData();
    }

    private void loadData() {

        if (getIntent().hasExtra("data"))
            datalist = getIntent().getParcelableArrayListExtra("data");

        if (getIntent().hasExtra("exam_id"))
            exam_id = getIntent().getIntExtra("exam_id", -1);

        lytStartScreen.setVisibility(View.VISIBLE);
        lytQuest.setVisibility(View.GONE);

        for (QuizModel ignored : datalist)
            ans.add(null);

        String head = datalist.get(0).getName().trim() + "\n category : " + datalist.get(0).getCategory().trim() + "\n Module : " + datalist.get(0).getModule().trim();
        txtHead.setText(head);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestion(0);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = rdoGrp.getCheckedRadioButtonId();
                if (id == -1)
                    return;

                ans.set(index, rdoGrp.findViewById(id).getTag().toString());
                if (index < datalist.size() - 1)
                    showQuestion(index + 1);
                else
                    showEndScreen();
            }
        });

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = rdoGrp.getCheckedRadioButtonId();
                if (id != -1)
                    ans.set(index, rdoGrp.findViewById(id).getTag().toString());


                if (index != 0)
                    showQuestion(index - 1);
            }
        });


    }

    private void showEndScreen() {

        int total = datalist.size();
        int mark = 0;

        for (int i = 0; i < datalist.size(); i++) {
            QuizModel quizModel = datalist.get(i);
            String s = ans.get(i);

            if (quizModel.getAnswer().equals(s))
                mark++;
        }

        int per = (mark / total) * 100;
        String s = "Passed";
        if (per < 60)
            s = "Failed";

        uploadMark(mark);


        String head = "Mark : " + mark +
                "\n Total : " + total +
                "\n You have " + s + " the exam";
        txtHead.setText(head);
        btnStart.setText("Finish");

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lytStartScreen.setVisibility(View.VISIBLE);
        lytQuest.setVisibility(View.GONE);


    }

    private void uploadMark(int mark) {

        SharedPreferences pref = getSharedPreferences("DEFAULT", MODE_PRIVATE);
        String user_id = pref.getString("user_id", "121");

//        if (user_id == null) {
//            Toast.makeText(this, "User id is missing", Toast.LENGTH_SHORT).show();
//            return;
//        }

        StringRequest request = new StringRequest(Request.Method.GET, "http://loopdata.in/Home_c/exam_val_app/" + user_id + "/" + exam_id + "/" + mark, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                try {
                    JSONObject res = new JSONObject(response);
                    String result = res.getString("result");
                    if (result != null)
                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(context).add(request);

    }

    private void showQuestion(int i) {

        index = i;

        lytStartScreen.setVisibility(View.GONE);
        lytQuest.setVisibility(View.VISIBLE);

        rdoGrp.clearCheck();

        txtIndex.setText(i + 1 + "");
        QuizModel model = datalist.get(i);
        txtQ.setText(model.getQuestion());
        btn1.setText(model.getOptionA());
        btn2.setText(model.getOptionB());
        btn3.setText(model.getOptionC());
        btn4.setText(model.getOptionD());

        if (!ans.isEmpty() && ans.get(index) != null) {
            RadioButton btn = rdoGrp.findViewWithTag(ans.get(index));
            btn.setChecked(true);
        }
        scrollView.fullScroll(ScrollView.FOCUS_UP);

        if (index < datalist.size() - 1)
            btnNext.setText("Finish");
        else
            btnNext.setText("Next");

    }

}
