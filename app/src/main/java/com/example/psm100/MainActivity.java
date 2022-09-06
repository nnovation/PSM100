package com.example.psm100;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import com.example.psm100.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.pdf.PdfDocument;
import android.widget.Toast;


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

    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.light_green));
        }
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Fragment V_fragment = new Voltage_fragment();
        Fragment T_fragment = new Temperature_fragment();
        Fragment R_fragment = new Report_fragment();

        navigationView = findViewById(R.id.navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Layout, R_fragment).commit();

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
*/
    }
}