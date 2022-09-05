package com.example.psm100;

import static android.icu.lang.UCharacter.IndicPositionalCategory.NA;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.View;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "PSM100";

    // below int is our database version
    private static final int DB_VERSION = 3;

    // below variable is for our table name.
    private static final String TABLE_NAME[] = {"Panel_1","Panel_2","Panel_3","Panel_4","Panel_5","Panel_6","Panel_7"};

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String R_Phase = "R_Phase";

    // below variable id for our course duration column.
    private static final String Y_Phase = "Y_Phase";

    // below variable for our course description column.
    private static final String B_Phase = "B_Phase";

    // below variable is for our course tracks column.
    private static final String Date = "Date";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int T = 0; T < 7; T++) {
             String  CREATE_Panel = "CREATE TABLE " + TABLE_NAME[T] + " ("
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + R_Phase + " TEXT,"
                    + B_Phase + " TEXT,"
                    + Y_Phase + " TEXT,"
                    + Date + " TEXT)";
            db.execSQL(CREATE_Panel);
        }
    }


    // this method is use to add new course to our sqlite database.
    public void addNewCourse(String R_Voltage, String Y_Voltage, String B_Voltage, String Date_Time, String TABLE_NAME) {


        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(R_Phase, R_Voltage);
        values.put(B_Phase, B_Voltage);
        values.put(Y_Phase, Y_Voltage);
        values.put(Date, Date_Time);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }
    public String CreatePDF(View view){
        String query = "select * from Panel_1";
        SQLiteDatabase D_pdf = this.getWritableDatabase();
        Cursor cursor = D_pdf.rawQuery(query,null);
        try {
            cursor.moveToFirst();
      //      textViewDisplay.setText(cursor.getString(0));
        }
        catch (Exception e){
            e.printStackTrace();
       //     textViewDisplay.setText("");
        }

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//        page.getCanvas().drawText(cursor.getString(1),10, 25, new Paint());
        page.getCanvas().drawText(cursor.getString(1),10, 25, new Paint());
        pdfDocument.finishPage(page);
        String filePath = Environment.getExternalStorageDirectory().getPath()+"/Fonts/"+"report.pdf";
        File file = new File(filePath);
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
        return cursor.getString(0);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int T_db=0; T_db <7; T_db++) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[T_db]);
        }
        onCreate(db);
    }

}
