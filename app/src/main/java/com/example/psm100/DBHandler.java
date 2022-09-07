package com.example.psm100;

import static android.icu.lang.UCharacter.IndicPositionalCategory.NA;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "PSM100";

    // below int is our database version
    private static final int DB_VERSION = 4;

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
    private static final int PERMISSION_REQUEST_CODE = 200;

    public void generatePDF(View view) throws FileNotFoundException {
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
                File file = new File(Environment.getExternalStorageDirectory(), "GlowGarden/report.pdf");
        PdfWriter writer = new PdfWriter(file);

        // Creating a PdfDocument object
        com.itextpdf.kernel.pdf.PdfDocument pdf = new PdfDocument(writer);

        // Creating a Document object
        Document doc = new Document(pdf);

        // Creating a table
        float [] pointColumnWidths = {70F, 70F, 70F, 70F,70F};
        Table table = new Table(pointColumnWidths);

        // Adding cells to the table
        table.addCell(new Cell().add(new Paragraph("Date")).setBold());
        table.addCell(new Cell().add(new Paragraph("Time")).setBold());
        table.addCell(new Cell().add(new Paragraph("R_Phase")).setBold().setBackgroundColor(new DeviceRgb(255, 0, 0)) );
        table.addCell(new Cell().add(new Paragraph("B_Phase")).setBold().setBackgroundColor(new DeviceRgb(0, 255, 0)));
        table.addCell(new Cell().add(new Paragraph("Y_phase")).setBold().setBackgroundColor(new DeviceRgb(0, 0, 255)));

        // Adding Table to document
        doc.add(table);

        // Closing the document
        doc.close();
        System.out.println("Table created successfully..");
    }
//        PdfDocument pdfDocument = new PdfDocument();
//        Paint paint = new Paint();
//        Paint title = new Paint();
//        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(792 , 1120, 1).create();
//
//        // below line is used for setting
//        // start page for our PDF file.
//        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
//
//        // creating a variable for canvas
//        // from our page of PDF.
//        Canvas canvas = myPage.getCanvas();
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(view.getContext(), R.color.purple_200));
//        canvas.drawText(cursor.getString(1), 209, 100, title);
//        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//        title.setColor(ContextCompat.getColor(view.getContext(), R.color.purple_200));
//        title.setTextSize(15);
//        title.setTextAlign(Paint.Align.CENTER);
//        pdfDocument.finishPage(myPage);
//        File file = new File(Environment.getExternalStorageDirectory(), "GlowGarden/report.pdf");
//
//        try {
//            pdfDocument.writeTo(new FileOutputStream(file));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        pdfDocument.close();


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int T_db=0; T_db <7; T_db++) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[T_db]);
        }
        onCreate(db);
    }

}
