/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

import com.view.Sales_Invoice_Generator_Frame;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author SMART
 */
public class Invoice_Header {
    
      private int num;
    private String name;
    private Date date;
    private ArrayList<Invoice_Line> lines;

    public Invoice_Header(int num, String name, Date date) {
        this.num = num;
        this.name = name;
        this.date = date;
    }

    public double getTotal() {
        double total = 0;
        
        for (Invoice_Line line : getLines()) {
            total += line.getTotal();
        }
        
        return total;
    }
    
    public ArrayList<Invoice_Line> getLines() {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines;
    }
    
     public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InvoiceHeader{" + "num=" + num + ", name=" + name + ", date=" + date + '}';
    }
    
    public String getAsCSV() {
        return num + "," + Sales_Invoice_Generator_Frame.sdf.format(date) + "," + name;
    }
    
    
}
