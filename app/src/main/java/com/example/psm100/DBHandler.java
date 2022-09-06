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
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Document;

import java.io.File;
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

    public void generatePDF(View view) {
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


        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(792 , 1120, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
//        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(view.getContext(), R.color.purple_200));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText(cursor.getString(1), 209, 100, title);
     //   canvas.drawText("Geeks for Geeks", 209, 80, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(view.getContext(), R.color.purple_200));
        title.setTextSize(15);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
    //    ((Canvas) canvas).drawText("This is sample document which we have created.", 396, 560, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStorageDirectory(), "GlowGarden/report.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
//            Toast.makeText(MainActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }
    public static void main(String[] args) throws IOException {
        String dest = "C:/itextExamples/addingTable.pdf";
        PdfWriter writer = new PdfWriter(dest);

        // Creating a PdfDocument object
        PdfDocument pdf = new PdfDocument(writer);

        // Creating a Document object
        Document doc = new Document(pdf);

        // Creating a table
        float [] pointColumnWidths = {150F, 150F, 150F};
        Table table = new Table(pointColumnWidths);

        // Adding cells to the table
        table.addCell(new Cell().add("Name"));
        table.addCell(new Cell().add("Raju"));
        table.addCell(new Cell().add("Id"));
        table.addCell(new Cell().add("1001"));
        table.addCell(new Cell().add("Designation"));
        table.addCell(new Cell().add("Programmer"));

        // Adding Table to document
        doc.add(table);

        // Closing the document
        doc.close();

    }
//    public Cursor CreatePDF(View view){
//        String query = "select * from Panel_1";
//        SQLiteDatabase D_pdf = this.getWritableDatabase();
//        Cursor cursor = D_pdf.rawQuery(query,null);
//        try {
//            cursor.moveToFirst();
//      //      textViewDisplay.setText(cursor.getString(0));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//       //     textViewDisplay.setText("");
//        }
//
//        PdfDocument pdfDocument = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600,1).create();
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
////        page.getCanvas().drawText(cursor.getString(1),10, 25, new Paint());
//        page.getCanvas().drawText(cursor.getString(1),10, 25, new Paint());
//        pdfDocument.finishPage(page);
//        String filePath = Environment.getExternalStorageDirectory().getPath()+"/GlowGarden/"+"report.pdf";
//        File file = new File(filePath);
//        try {
//            pdfDocument.writeTo(new FileOutputStream(file));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        pdfDocument.close();
//        return cursor;
//    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int T_db=0; T_db <7; T_db++) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[T_db]);
        }
        onCreate(db);
    }

}
