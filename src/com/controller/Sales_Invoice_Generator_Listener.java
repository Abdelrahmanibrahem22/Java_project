/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.model.Invoice_Header;
import com.model.Invoice_Header_Table_Model;
import com.model.Invoice_Line;
import com.model.Invoice_Line_Table_Model;
import com.view.Invoice_Header_Dialog;
import com.view.Invoice_Line_Dialog;
import com.view.Sales_Invoice_Generator_Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author SMART
 */
public class Sales_Invoice_Generator_Listener implements ActionListener, ListSelectionListener {
     private Sales_Invoice_Generator_Frame frame;
    private Invoice_Header_Dialog headerDialog;
    private Invoice_Line_Dialog lineDialog;
    public Sales_Invoice_Generator_Listener(Sales_Invoice_Generator_Frame frame) {
        this.frame = frame;
    }

    
    public void load(String headerPath, String linePath) {
        File headerFile = null;
        File lineFile = null;
        if (headerPath == null && linePath == null) {
            JOptionPane.showMessageDialog(frame, "Select header file first, then select line file.", "Invoice files", JOptionPane.WARNING_MESSAGE);
            JFileChooser fc = new JFileChooser();
            int result = fc.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                headerFile = fc.getSelectedFile();
                result = fc.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    lineFile = fc.getSelectedFile();
                }
            }
        } else {
            headerFile = new File(headerPath);
            lineFile = new File(linePath);
        }

        if (headerFile != null && lineFile != null) {
            try {
              
                List<String> headerLines = Files.lines(Paths.get(headerFile.getAbsolutePath())).collect(Collectors.toList());
              
                List<String> lineLines = Files.lines(Paths.get(lineFile.getAbsolutePath())).collect(Collectors.toList());
                frame.get_Invoices().clear();
                for (String headerLine : headerLines) {
                    String[] parts = headerLine.split(",");  
                    String numString = parts[0];
                    String dateString = parts[1];
                    String name = parts[2];
                    int num = Integer.parseInt(numString);
                    Date date = Sales_Invoice_Generator_Frame.sdf.parse(dateString);
                    Invoice_Header inv = new Invoice_Header(num, name, date);
                    frame.get_Invoices().add(inv);
                }
                
                for (String lineLine : lineLines) {
                   
                    String[] parts = lineLine.split(",");
                  
                    int num = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int count = Integer.parseInt(parts[3]);
                    Invoice_Header inv = frame.get_Invoice_By_Num(num);
                    Invoice_Line line = new Invoice_Line(name, price, count, inv);
                    inv.getLines().add(line);
                }
                
                frame.setHeaderTableModel(new Invoice_Header_Table_Model(frame.get_Invoices()));
               
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void save() {
        JFileChooser fc = new JFileChooser();
        File headerFile = null;
        File lineFile = null;
        int result = fc.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            headerFile = fc.getSelectedFile();
            result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                lineFile = fc.getSelectedFile();
            }
        }
        
        if (headerFile != null && lineFile != null) {
            String headerData = "";
            String lineData = "";
            for (Invoice_Header inv : frame.get_Invoices()) {
                headerData += inv.getAsCSV();
                headerData += "\n";
                for (Invoice_Line line : inv.getLines()) {
                    lineData += line.getAsCSV();
                    lineData += "\n";
                }
            }
            try {
                FileWriter headerFW = new FileWriter(headerFile);
                FileWriter lineFW = new FileWriter(lineFile);
                headerFW.write(headerData);
                lineFW.write(lineData);
                headerFW.flush();
                lineFW.flush();
                headerFW.close();
                lineFW.close();
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error while writing file(s)", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }

    private void create_Invoice() {
        headerDialog = new Invoice_Header_Dialog(frame);
        headerDialog.setLocation(300, 300);
        headerDialog.setVisible(true);
    }

    private void delete_Invoice() {

        int selectedRow = frame.getInvoicesTable().getSelectedRow();
        if (selectedRow > -1) {
            frame.get_Invoices().remove(selectedRow);
            frame.getHeaderTableModel().fireTableDataChanged();
        }
    }

    private void create_Item() {
        if (frame.getInvoicesTable().getSelectedRow() > -1) {
            lineDialog = new Invoice_Line_Dialog(frame);
            lineDialog.setLocation(300, 300);
            lineDialog.setVisible(true);
        }
    }

    private void delete_Item() {
        int selected_Invoice = frame.getInvoicesTable().getSelectedRow();
        int selectedItem = frame.getLinesTable().getSelectedRow();

        if (selected_Invoice > -1 && selectedItem > -1) {
            frame.get_Invoices().get(selected_Invoice).getLines().remove(selectedItem);
            frame.getLineTableModel().fireTableDataChanged();
            frame.getHeaderTableModel().fireTableDataChanged();
            frame.getInvoicesTable().setRowSelectionInterval(selected_Invoice, selected_Invoice);
        }
    }

    private void new_Invoice_OK() {
        String name = headerDialog.getCustNameField().getText();
        String dateStr = headerDialog.getInvDateField().getText();
        new_Invoice_Cancel();
        try {
            Date date = frame.sdf.parse(dateStr);
            Invoice_Header inv = new Invoice_Header(frame.get_Next_Inv_Num(), name, date);
            frame.get_Invoices().add(inv);
            frame.getHeaderTableModel().fireTableDataChanged();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid Date Format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void new_Invoice_Cancel() {
        headerDialog.setVisible(false);
        headerDialog.dispose();
        headerDialog = null;
    }

    private void new_Line_OK() {
        String name = lineDialog.get_Item_Name_Field().getText();
        String countStr = lineDialog.get_Item_Count_Field().getText();
        String priceStr = lineDialog.get_Item_Price_Field().getText();
        new_Line_Cancel();
        try {
            int count = Integer.parseInt(countStr);
            double price = Double.parseDouble(priceStr);
            int currentInv = frame.getInvoicesTable().getSelectedRow();
            Invoice_Header inv = frame.get_Invoices().get(currentInv);
            Invoice_Line line = new Invoice_Line(name, price, count, inv);
            inv.getLines().add(line);
            frame.getHeaderTableModel().fireTableDataChanged();
            frame.getInvoicesTable().setRowSelectionInterval(currentInv, currentInv);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid Number Format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void new_Line_Cancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedRow = frame.getInvoicesTable().getSelectedRow();
        if (selectedRow > -1) {
            Invoice_Header inv = frame.get_Invoices().get(selectedRow);
            frame.getInvNumLbl().setText("" + inv.getNum());
            frame.getInvDateLbl().setText(Sales_Invoice_Generator_Frame.sdf.format(inv.getDate()));
            frame.getInvCustNameLbl().setText(inv.getName());
            frame.getInvTotalLbl().setText("" + inv.getTotal());
            ArrayList<Invoice_Line> lines = inv.getLines();
            frame.setLineTableModel(new Invoice_Line_Table_Model(lines));
        } else {
            frame.getInvNumLbl().setText("");
            frame.getInvDateLbl().setText("");
            frame.getInvCustNameLbl().setText("");
            frame.getInvTotalLbl().setText("");
            frame.setLineTableModel(new Invoice_Line_Table_Model());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Load":
                load(null, null);
                break;
            case "Save":
                save();
                break;
            case "Create Invoice":
                create_Invoice();
                break;
            case "Delete Invoice":
                delete_Invoice();
                break;
            case "Create Item":
                create_Item();
                break;
            case "Delete Item":
                delete_Item();
                break;
            case "newInvoiceOK":
                new_Invoice_OK();
                break;
            case "newInvoiceCancel":
                new_Invoice_Cancel();
                break;
            case "newLineOK":
                new_Line_OK();
                break;
            case "newLineCancel":
                new_Line_Cancel();
                break;
        }
    }

}
