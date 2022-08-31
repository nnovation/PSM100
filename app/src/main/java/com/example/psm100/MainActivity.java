package com.example.psm100;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.psm100.databinding.ActivityMainBinding;

import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    TableLayout table;
    Button button;
    Context context;
    TableLayout tableLayout;
    TextView textView;
    String color[] ={"#FF0000","#FBB917","#357EC7"};
    String phase[] ={"R","Y","B"};
    String RYB[]  = {"0.00","0.00","0.00"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textView = findViewById(R.id.html);
        OkHttpClient client = new OkHttpClient();

        String url = "http://192.168.43.215/voltage";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String PanelVoltage = response.body().string();
                    textView.setText("data success getting");
                    RYB = PanelVoltage.split(",");
                    Table(RYB);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(PanelVoltage);
                        }
                    });
                }else {
                    textView.setText("data not getting");
                }
            }
        });

    }
    public void Table(String PanelVoltage[]) {

        TableLayout stk = (TableLayout) findViewById(R.id.table);
        TableRow.LayoutParams row_with = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100);
        TableRow.LayoutParams panel_name_with = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100);
        panel_name_with.span = 4;
        TableRow.LayoutParams button_p = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100);
        button_p.span = 2;
//        TableLayout.LayoutParams tableLayout = new TableLayout.LayoutParams();
//        stk.setLayoutParams(tableLayout);

        for (int i = 0; i < 7; i++) {
            TableRow panel_row = new TableRow(this);

            TextView panel_name = new TextView(this);
            panel_name.setLayoutParams(panel_name_with);
            panel_name.setText("Panel " + (i + 1));
            panel_name.setTextSize(20);
            panel_name.setBackgroundColor(Color.parseColor("#C0C0C0"));
            panel_name.setTextColor(Color.BLACK);
            panel_name.setGravity(Gravity.CENTER);
            panel_row.addView(panel_name);


            TableRow RYB_Row = new TableRow(this);
            //           RYB_Row.setBackground(this.getDrawable(R.drawable.border));

            for (int RYB = 0; RYB < 3; RYB++) {
                TextView R_text = new TextView(this);

                R_text.setLayoutParams(row_with);
                //         R1.setBackground(this.getDrawable(R.drawable.border));
                R_text.setBackgroundColor(Color.parseColor(color[RYB]));
                R_text.setText(phase[RYB]);
                R_text.setTextColor(Color.WHITE);
                R_text.setTextSize(20);
                R_text.setGravity(Gravity.CENTER);
                RYB_Row.addView(R_text);
            }

            TableRow Data_Row = new TableRow(this);

            for (int d = 0; d < 3; d++) {
                TextView Data_R = new TextView(this);
                Data_R.setLayoutParams(row_with);
                //        Data_R.setBackground(this.getDrawable(R.drawable.border));
                Data_R.setBackgroundColor(Color.WHITE);
                Data_R.setText(PanelVoltage[d]);
                Data_R.setTextColor(Color.BLACK);
                Data_R.setTextSize(18);
                Data_R.setGravity(Gravity.CENTER);
                Data_Row.addView(Data_R);
            }
            Button save = new Button(this);
            //         save.setBackground(this.getDrawable(R.drawable.border));
            save.setText("Save");
            //          save.setTextAppearance(com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
            //         save.setBackgroundColor(Color.parseColor("#0A837A"));
            save.setTextSize(18);
            save.setGravity(Gravity.CENTER);
            Data_Row.addView(save);


//            Data_Row.setPadding(0,0,0,20);
            stk.addView(panel_row);
            stk.addView(RYB_Row);
            stk.addView(Data_Row);
//            stk.setBackground(this.getDrawable(R.drawable.border));
            stk.setPadding(15, 0, 15, 20);


        }


    }
}




