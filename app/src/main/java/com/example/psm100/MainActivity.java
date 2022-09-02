package com.example.psm100;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import com.example.psm100.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.pdf.PdfDocument;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private FirstFragment VoltageFragment;
    private SecondFragment Temperature;
    private DBHandler sqlLiteDBHandler;

    TableLayout table;
    Context context;
    TableLayout tableLayout;
    BottomNavigationView navigationView;
    String color[] = {"#FF0000", "#FBB917", "#357EC7"};
    String phase[] = {"R", "Y", "B"};
    String RYB[] = {"0.00", "0.00", "0.00"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Fragment V_fragment = new Voltage_fragment();
        Fragment T_fragment = new Temperature_fragment();
        Fragment R_fragment = new Report_fragment();

        navigationView = findViewById(R.id.navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Layout, R_fragment ).commit();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.PanelVoltage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Layout, V_fragment).commit();
                        return true;
                    case R.id.PanelTemperature:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Layout, T_fragment).commit();
                        return true;
                    case R.id.Setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Layout, R_fragment).commit();
                        return true;
                }
                return false;
            }
        });
//        try {
//            sqlLiteDBHandler = new DBHandler(this,”PDFDatabase”, null,1);
//            SQLiteDatabase sqLiteDatabase = sqlLiteDBHandler.getWritableDatabase();
//            sqLiteDatabase.execSQL(“CREATE TABLE PDFTable(SerialNumber TEXT, Text TEXT)”);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    public void CreatePDF(View view){
//        String query = “Select Text from PDFTable where SerialNumber=” + editTextSerialNumberFetch.getText().toString();
//        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
//        try {
//            cursor.moveToFirst();
//            textViewDisplay.setText(cursor.getString(0));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            textViewDisplay.setText(“”);
//            return;
//        }
//
//        PdfDocument pdfDocument = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600,1).create();
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//        page.getCanvas().drawText(cursor.getString(0),10, 25, new Paint());
//        pdfDocument.finishPage(page);
//        String filePath = Environment.getExternalStorageDirectory().getPath()+”/Download/”+editTextSerialNumberFetch.getText().toString()+”.pdf”;
//        File file = new File(filePath);
//        try {
//            pdfDocument.writeTo(new FileOutputStream(file));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        pdfDocument.close();
//    }
//}
//    }
 /*       OkHttpClient client = new OkHttpClient();
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
                    RYB = PanelVoltage.split(",");
                    Table(RYB);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
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


    }*/
    }
}