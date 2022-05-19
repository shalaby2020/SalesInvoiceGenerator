
package model;

public class InvoiceLine {
    private String itemName ;
    private double itemPrice ;
    private int count ;
    private InvoiceHeader invoice ; 

    public InvoiceLine() {
    }

    public InvoiceLine( String item, double price, int count) {
        this.itemName = item;
        this.itemPrice = price;
        this.count = count;
    }

    public InvoiceLine( String itemName, double itemPrice, int count, InvoiceHeader invoice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.invoice = invoice;
    }

    public double getLineTotal (){
        return itemPrice *count ;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getItem() {
        return itemName;
    }

    public void setItem(String item) {
        this.itemName = item;
    }

    public double getPrice() {
        return itemPrice;
    }

    public void setPrice(double price) {
        this.itemPrice = price;
    }

    @Override
    public String toString() {
        return "InvoiceLine{" + "num=" + invoice.getNum() + ", itemName=" + itemName + ", itemPrice=" + itemPrice + ", count=" + count + '}';
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public InvoiceHeader getInvoice() {
        return invoice;
    }
    public String getCSV() {
        return itemName + "," + itemPrice + "," + count;
    }
    
    
 
}
