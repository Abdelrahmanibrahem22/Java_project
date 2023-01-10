/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

import com.view.Sales_Invoice_Generator_Frame;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author SMART
 */
public class Invoice_Header_Table_Model extends AbstractTableModel{
    private ArrayList<Invoice_Header> invoices;
    private String[] columns = {"Invoice Num", "Invoice Date", "Customer Name", "Invoice Total"};
    
    public Invoice_Header_Table_Model(ArrayList<Invoice_Header> invoices) {
        this.invoices = invoices;
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
        return invoices.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Invoice_Header inv = invoices.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return inv.getNum();
            case 1:
                return Sales_Invoice_Generator_Frame.sdf.format(inv.getDate());
            case 2:
                return inv.getName();
            case 3:
                return inv.getTotal();
            default:
                return "";
        }
    }
    
}
