/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author SMART
 */
public class Invoice_Line_Table_Model extends AbstractTableModel{
    private ArrayList<Invoice_Line> lines;
     private String[] columns = {"Item", "Unit Price", "Count", "Total"};
   

    public Invoice_Line_Table_Model() {
        this(new ArrayList<Invoice_Line>());
    }

    
    public Invoice_Line_Table_Model(ArrayList<Invoice_Line> lines) {
        this.lines = lines;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public int getRowCount() {
        return lines.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Invoice_Line line = lines.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return line.getName();
            case 1:
                return line.getPrice();
            case 2:
                return line.getCount();
            case 3:
                return line.getTotal();
            default:
                return "";
        }
    }
}
