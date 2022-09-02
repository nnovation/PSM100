package com.example.psm100;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Temperature_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Temperature_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String color[] ={"#FF0000","#FBB917","#357EC7"};
    String Panel_Name[][] ={
            {"Panel 1","Panel 2","Panel 3","Panel 4"},
            {"Panel 5","Panel 6","Panel 7","Panel 8"},
            {"Panel 9","Panel 10","Panel 11","Panel 12"},
            {"Panel 13","Panel 14","Panel 15","Panel 16"},
            {"Panel 17","Panel 18","Panel 19","Panel 20"},
            {"Panel 21","Panel 22","Panel 23","Panel 24"},
            {"Panel 25","Panel 26","Panel 27","Panel 28"},
            {"Panel 29","Panel 30","Panel 31","Panel 32"},
            {"Panel 33","Panel 34","Panel 35","Panel 36"},
            {"Panel 37","Panel 38","Panel 39","Panel 40"},
    };
    String Temperature[][]  = {
            {"0.00","0.00","0.00","0.00"},
            {"0.00","0.00","0.00","0.00"},
            {"0.00","0.00","0.00","0.00"},
            {"0.00","0.00","0.00","0.00"},
            {"0.00","0.00","0.00","0.00"},
            {"0.00","0.00","0.00","0.00"},
            {"0.00","0.00","0.00","0.00"},
            {"0.00","0.00","0.00","0.00"},
            {"0.00","0.00","0.00","0.00"},
            {"0.00","0.00","0.00","0.00"},
    };


    public Temperature_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Temperature_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Temperature_fragment newInstance(String param1, String param2) {
        Temperature_fragment fragment = new Temperature_fragment();
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
        return inflater.inflate(R.layout.fragment_temperature_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       // Table(Temperature,getView());
        httpRequest();
    }

public void httpRequest(){

    OkHttpClient client = new OkHttpClient();
    String url = "http://192.168.43.25/";

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
                Temperature[0][0] =PanelVoltage;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run(){
                        Table(Temperature,getView());
                    }

                });
            }
        }
    });
        }
    public void Table(String PanelTemperature[][], View rootView) {

        TableLayout stk = (TableLayout) rootView.findViewById(R.id.table);
        TableRow.LayoutParams row_with = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100);

//        TableLayout.LayoutParams tableLayout = new TableLayout.LayoutParams();
//        stk.setLayoutParams(tableLayout);

        for (int i = 0; i < 10; i++) {


            TableRow RYB_Row = new TableRow(rootView.getContext());
            //           RYB_Row.setBackground(this.getDrawable(R.drawable.border));

            for (int N_Address = 0; N_Address < 4; N_Address++) {
                TextView R_text = new TextView(rootView.getContext());

                R_text.setLayoutParams(row_with);
                //         R1.setBackground(this.getDrawable(R.drawable.border));
                R_text.setText(Panel_Name[i][N_Address]);
                R_text.setTextSize(18);
                R_text.setTextColor(Color.BLACK);
                R_text.setGravity(Gravity.CENTER);
                RYB_Row.addView(R_text);
            }

            TableRow Data_Row = new TableRow(rootView.getContext());

            for (int d = 0; d < 4; d++) {
                TextView Data_R = new TextView(rootView.getContext());
                Data_R.setLayoutParams(row_with);
                //        Data_R.setBackground(this.getDrawable(R.drawable.border));
                Data_R.setBackgroundColor(Color.WHITE);
                Data_R.setBackgroundColor(Color.parseColor("#28B463"));
                Data_R.setText(PanelTemperature[i][d]+"°C");
                Data_R.setTextColor(Color.WHITE);
                Data_R.setTextSize(18);
                Data_R.setGravity(Gravity.CENTER);
                Data_Row.addView(Data_R);
            }


//            Data_Row.setPadding(0,0,0,20);

            stk.addView(RYB_Row);
            stk.addView(Data_Row);
//            stk.setBackground(this.getDrawable(R.drawable.border));
            stk.setPadding(15, 0, 15, 20);


        }


    }
}