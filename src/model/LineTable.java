
package model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class LineTable extends AbstractTableModel {
    private ArrayList<InvoiceLine> lines;
    private String[] columns = {"No.", "Item Name", "Item Price", "Count", "Item Total"};

    
    public LineTable(ArrayList<InvoiceLine> lines) {
        this.lines = lines;
    }
    public ArrayList<InvoiceLine> getLines() {
        return lines;
    }
    @Override
    public int getRowCount() {
        return lines.size();
    }
    
    @Override
    public int getColumnCount() {
       return lines.size();
    }

    @Override
    public String getColumnName(int x) {
        return columns[x];
    }
    

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            InvoiceLine line = lines.get(rowIndex);
        
        switch(columnIndex) {
            case 0: return line.getInvoice().getNum();
            case 1: return line.getItem();
            case 2: return line.getPrice();
            case 3: return line.getCount();
            case 4: return line.getLineTotal();
            default : return "";
        }
        
    }
    
}
