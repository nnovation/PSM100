package com.example.psm100;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Voltage_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Voltage_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DBHandler dbHandler;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String color[] ={"#FF0000","#FBB917","#357EC7"};
    String phase[] ={"R","Y","B"};
    String PanelName[] ={"Panel_1","Panel_2","Panel_3","Panel_4","Panel_5","Panel_6","Panel_7"};
    String border[] = {"r_border","y_border","b_border"};
    String RYB_voltage[][]  = {
            {"0.00","0.00","0.00"},
            {"0.00","0.00","0.00"},
            {"0.00","0.00","0.00"},
            {"0.00","0.00","0.00"},
            {"0.00","0.00","0.00"},
            {"0.00","0.00","0.00"},
            {"0.00","0.00","0.00"}
    };
    String URL[]  = {"http://192.168.43.252/","http://192.168.43.99/","http://192.168.43.55/","http://192.168.43.154/","http://192.168.43.53/","http://192.168.43.159/","http://192.168.43.37/"};

    public Voltage_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Voltage_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Voltage_fragment newInstance(String param1, String param2) {
        Voltage_fragment fragment = new Voltage_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voltage_fragment, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Table(RYB_voltage, getView());
        for (int row_next=0; row_next <7; row_next++) {
                try {
//                Cell_Text[count] =httpRequest(URL[count]);
                    V_HttpGetRequest getRequest = new V_HttpGetRequest();
                    //Perform the doInBackground method, passing in our url
                    String result = getRequest.execute(URL[row_next]).get();
                    RYB_voltage[row_next] = result.split(",");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    for (int na = 0; na <= 3; na++){
                        RYB_voltage[row_next][na] ="NA";
                }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        cell_data(RYB_voltage,view);

    }
    public void cell_data(String Cell_Data[][],View rootView){
        for (int i = 0; i < 7; i++) {
            for (int d = 0; d < 3; d++) {
                TextView Data_R = new TextView(rootView.getContext());
                TableLayout stk = (TableLayout) rootView.findViewById(R.id.table);
                TableRow row= (TableRow) stk.getChildAt(((i+1)*3)-1);
                TextView cell = (TextView) row.getChildAt(d);
                cell.setText(Cell_Data[i][d]);

//                Data_R.findViewById(Integer.parseInt("cell_"+i+"_"+d));
//                Data_R.setText(Cell_Data[d]);
            }
        }

    }
    private static OkHttpClient createHttpclient() {
        final OkHttpClient.Builder builder =  new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.MILLISECONDS)
                .writeTimeout(100, TimeUnit.MILLISECONDS)
                .readTimeout(100, TimeUnit.MILLISECONDS);
//        setSocketFactory(builder); // To handle SSL certificate.
        return builder.build();
    }
    public static class V_HttpGetRequest extends AsyncTask<String, Void, String> {
        //        OkHttpClient client = new OkHttpClient();
        OkHttpClient client = createHttpclient();

        @Override
        protected String doInBackground(String... params) {
            String Response_body = "NA,NA,NA";

//            Response response;
            String url = params[0];
            Request V_request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = client.newCall(V_request).execute()) {
                if (response.isSuccessful()) {
                    Response_body = response.body().string();
                    response.close();
                } else {
                    Response_body = "NA,NA,NA";
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return Response_body;
        }
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void Table(String PanelVoltage[][], View rootView) {

        TableLayout stk = (TableLayout) rootView.findViewById(R.id.table);
        TableRow.LayoutParams row_with = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100);
        TableRow.LayoutParams panel_name_with = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100);
        panel_name_with.span = 4;
        TableRow.LayoutParams button_p = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100);
        button_p.span = 2;
//        TableLayout.LayoutParams tableLayout = new TableLayout.LayoutParams();
//        stk.setLayoutParams(tableLayout);

        for (int i = 0; i < 7; i++) {
            TableRow panel_row = new TableRow(rootView.getContext());

            TextView panel_name = new TextView(rootView.getContext());
            panel_name.setLayoutParams(panel_name_with);
            panel_name.setText(PanelName[i]);
            panel_name.setTextSize(20);
            panel_name.setBackgroundColor(Color.parseColor("#E0DBD7"));
            panel_name.setTextColor(Color.BLACK);
            panel_name.setGravity(Gravity.CENTER);
            panel_row.addView(panel_name);


            TableRow RYB_Row = new TableRow(rootView.getContext());

            for (int RYB = 0; RYB < 3; RYB++) {
                TextView R_text = new TextView(rootView.getContext());

                R_text.setLayoutParams(row_with);
                //         R1.setBackground(this.getDrawable(R.drawable.border));
      //          R_text.setBackgroundColor(Color.parseColor(color[RYB]));
                R_text.setText(phase[RYB]);
                R_text.setTextColor(Color.WHITE);
                R_text.setTextSize(20);
                R_text.setGravity(Gravity.CENTER);
                switch (RYB){
                    case 0:
                    R_text.setBackground(getContext().getDrawable(R.drawable.r_border));
                    break;
                    case 1:
                    R_text.setBackground(getContext().getDrawable(R.drawable.y_border));
                    break;
                    case 2:
                    R_text.setBackground(getContext().getDrawable(R.drawable.b_border));
                    break;
                }
                    RYB_Row.addView(R_text);
            }

            TableRow Data_Row = new TableRow(rootView.getContext());

            for (int d = 0; d < 3; d++) {
                TextView Data_R = new TextView(rootView.getContext());

                Data_R.setLayoutParams(row_with);
                Data_R.setBackgroundColor(Color.WHITE);
                Data_R.setText(PanelVoltage[i][d]);
                Data_R.setTextColor(Color.BLACK);
                Data_R.setTextSize(18);
                Data_R.setGravity(Gravity.CENTER);
                Data_R.setBackground(getContext().getDrawable(R.drawable.border));
                Data_Row.addView(Data_R);
            }
            Button save = new Button(rootView.getContext());
            save.setText("Save");
            //         save.setBackgroundColor(Color.parseColor("#0A837A"));
            save.setTextSize(18);
            save.setGravity(Gravity.CENTER);
            save.setId(i);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDateFormat Date = new SimpleDateFormat("dd-MM-yy HH:mm");
                    String currentDate = Date.format(new Date());
                    dbHandler = new DBHandler(rootView.getContext());
                    int save_id = save.getId();
                    String R=PanelVoltage[save_id][0];
                    String Y=PanelVoltage[save_id][1];
                    String B=PanelVoltage[save_id][2];
                    String P_Name = PanelName[save_id];
//                    String P_Name = "Panel " +(save_id+1);
                    dbHandler.addNewCourse(R,Y,B,currentDate,P_Name);
                }
            });
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