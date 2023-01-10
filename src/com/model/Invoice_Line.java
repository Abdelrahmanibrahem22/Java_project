/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

/**
 *
 * @author SMART
 */
public class Invoice_Line {
     private String name;
    private double price;
    private int count;
    private Invoice_Header inv;

    public Invoice_Line(String name, double price, int count, Invoice_Header inv) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.inv = inv;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
     public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
   public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public double getTotal() {
        return price * count;
    }
    
    public Invoice_Header getInv() {
        return inv;
    }

    public void setInv(Invoice_Header inv) {
        this.inv = inv;
    }

   
 

    @Override
    public String toString() {
        return "InvoiceLine{" + "name=" + name + ", price=" + price + ", count=" + count + '}';
    }
    
    public String getAsCSV() {
        return inv.getNum() + "," + name + "," + price + "," + count;
    }
}
