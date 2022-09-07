package com.example.psm100;

import android.graphics.Color;
import android.os.Environment;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;

public class AddingTable {

    public void CreateTable() throws FileNotFoundException {        // Creating a PdfDocument object
        File file = new File(Environment.getExternalStorageDirectory(), "GlowGarden/report.pdf");
        PdfWriter writer = new PdfWriter(file);

        // Creating a PdfDocument object
        PdfDocument pdf = new PdfDocument(writer);

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
}
