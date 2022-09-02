package com.example.psm100;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;

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
    String RYB[]  = {"0.00","0.00","0.00"};

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
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Table(RYB,getView());
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
                    RYB = PanelVoltage.split(",");
        //
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Table(RYB,getView());
                        }
                    });
                }
            }
        });

    }

    public void Table(String PanelVoltage[], View rootView) {

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
            panel_name.setText("Panel " + (i + 1));
            panel_name.setTextSize(20);
            panel_name.setBackgroundColor(Color.parseColor("#C0C0C0"));
            panel_name.setTextColor(Color.BLACK);
            panel_name.setGravity(Gravity.CENTER);
            panel_row.addView(panel_name);


            TableRow RYB_Row = new TableRow(rootView.getContext());
            //           RYB_Row.setBackground(this.getDrawable(R.drawable.border));

            for (int RYB = 0; RYB < 3; RYB++) {
                TextView R_text = new TextView(rootView.getContext());

                R_text.setLayoutParams(row_with);
                //         R1.setBackground(this.getDrawable(R.drawable.border));
                R_text.setBackgroundColor(Color.parseColor(color[RYB]));
                R_text.setText(phase[RYB]);
                R_text.setTextColor(Color.WHITE);
                R_text.setTextSize(20);
                R_text.setGravity(Gravity.CENTER);
                RYB_Row.addView(R_text);
            }

            TableRow Data_Row = new TableRow(rootView.getContext());

            for (int d = 0; d < 3; d++) {
                TextView Data_R = new TextView(rootView.getContext());
                Data_R.setLayoutParams(row_with);
                //        Data_R.setBackground(this.getDrawable(R.drawable.border));
                Data_R.setBackgroundColor(Color.WHITE);
                Data_R.setText(PanelVoltage[d]);
                Data_R.setTextColor(Color.BLACK);
                Data_R.setTextSize(18);
                Data_R.setGravity(Gravity.CENTER);
                Data_Row.addView(Data_R);
            }
            Button save = new Button(rootView.getContext());
            //         save.setBackground(this.getDrawable(R.drawable.border));
            save.setText("Save");
            //          save.setTextAppearance(com.google.android.material.R.style.Widget_Material3_Button_OutlinedButton);
            //         save.setBackgroundColor(Color.parseColor("#0A837A"));
            save.setTextSize(18);
            save.setGravity(Gravity.CENTER);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHandler = new DBHandler(rootView.getContext());
                    dbHandler.addNewCourse(RYB[0], RYB[1], RYB[2], RYB[2]);
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