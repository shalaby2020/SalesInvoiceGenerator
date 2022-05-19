
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.InvoiceHeader;
import model.InvoiceLine;
import model.InvoicesTable;
import model.LineTable;
import view.InvoiceDialog;
import view.LineDialog;
import view.NewJFrameInvoice;

public class Controller implements ActionListener , ListSelectionListener {
    
    private NewJFrameInvoice frame ;
    private InvoiceDialog invoiceDialog;
    private LineDialog lineDialog;
    public Controller(NewJFrameInvoice frame) {
        this.frame = frame;
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("Action " + actionCommand);
        switch (actionCommand){
            case "Load File" :
                LoadFile();
                break ;
            case "Save File" :
                SaveFile();
                break ;
            case "Create New Invoice" :
                CreateNewInvoice();
                break ;
            case "Delete Invoice" :
                DeleteInvoice();
                break ;
            case "Create New Item" :
                CreateNewItem();
                break ;
            case "Delete Item" :
                DeleteItem();
                break ;   
            case "NewInvoiceCancel":
                NewInvoiceCancel();
                break;
            case "NewInvoiceOK":
                NewInvoiceOK();
                break;
            case "NewLineOK":
                NewLineOK();
                break;
            case "NewLineCancel":
                NewLineCancel();
                break;    
        }
    }
     @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = frame.getInvoiceTable().getSelectedRow();
        if (selectedIndex != -1) {
        System.out.println("You have selected row: " + selectedIndex);
        InvoiceHeader currentInvoice = frame.getInvoices().get(selectedIndex);
        frame.getInvoiceNumLabel().setText(""+currentInvoice.getNum());
        frame.getInvoiceDateLabel().setText(currentInvoice.getDate());
        frame.getCustomerNameLabel().setText(currentInvoice.getCustomer());
        frame.getInvoiceTotalLabel().setText(""+currentInvoice.getInvoiceTotal());
        LineTable lineTable = new LineTable (currentInvoice.getLines());
        frame.getLineTable().setModel(lineTable);
        lineTable.fireTableDataChanged();
        }
      
    }
    private void LoadFile() {
        JFileChooser fc = new JFileChooser () ;
       try {
        int result = fc.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION){
            File HeaderFile = fc.getSelectedFile();
            Path headerpath = Paths.get(HeaderFile.getAbsolutePath());
            List<String>headerLines=Files.readAllLines(headerpath);
            ArrayList<InvoiceHeader>invoicesArray = new ArrayList<>();
            for (String headerLine : headerLines){
              String[] headerParts = headerLine.split(",");
              int invoiceNum =Integer.parseInt(headerParts[0]);
              String invoiceDate = headerParts[1];
              String customerName = headerParts[2];
              
              InvoiceHeader invoice= new InvoiceHeader(invoiceNum, invoiceDate, customerName);
              invoicesArray.add(invoice);
            }
            result = fc.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION){
                File lineFile = fc.getSelectedFile();
                Path linePath = Paths.get(lineFile.getAbsolutePath());
                List<String>lineLines = Files.readAllLines(linePath);
                for (String lineLine  : lineLines) {
                    String lineParts [] = lineLine.split(",");
                    int invoiceNum = Integer.parseInt(lineParts [0]);
                    String itemName = lineParts[1];
                    double itemPrice =Double.parseDouble(lineParts[2]);
                    int count = Integer.parseInt(lineParts[3]);
                    InvoiceHeader inv = null ;
                    for (InvoiceHeader invoice :invoicesArray ){
                        if (invoice.getNum()== invoiceNum){
                            inv = invoice ;
                            break ;
                        }
                    }
                    InvoiceLine line = new InvoiceLine( itemName, itemPrice, count,inv);
                    inv.getLines().add(line);
                    
                }
            }
            frame.setInvoices(invoicesArray);
                InvoicesTable invoicesTable = new InvoicesTable(invoicesArray);
                frame.setInvoiceTable (invoicesTable);
                frame.getInvoiceTable().setModel(invoicesTable);
                frame.getInvoicesTable().fireTableDataChanged();
           
        }
       }catch (IOException ex){
        ex.printStackTrace();
        }
            
       }
        

    private void SaveFile() {
        ArrayList<InvoiceHeader> invoices = frame.getInvoices();
        String headers = "";
        String lines = "";
        for (InvoiceHeader invoice : invoices) {
            String invCSV = invoice.getCSV();
            headers += invCSV;
            headers += "\n";

            for (InvoiceLine line : invoice.getLines()) {
                String lineCSV = line.getCSV();
                lines += lineCSV;
                lines += "\n";
            }
        }
        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter headerwriter = new FileWriter(headerFile);
                headerwriter.write(headers);
                headerwriter.flush();
                headerwriter.close();
                result = fc.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    FileWriter linewriter = new FileWriter(lineFile);
                    linewriter.write(lines);
                    linewriter.flush();
                    linewriter.close();
                }
            }
        } catch (Exception ex) {

        }
       }

    private void CreateNewInvoice() {
        invoiceDialog = new InvoiceDialog(frame);
        invoiceDialog.setVisible(true);
        
       }

    private void DeleteInvoice() {
        int selectedRow = frame.getInvoiceTable().getSelectedRow();
        if (selectedRow != -1) {
            frame.getInvoices().remove(selectedRow);
            frame.getInvoicesTable().fireTableDataChanged();
        }
       }

    private void CreateNewItem() {
        lineDialog = new LineDialog(frame);
        lineDialog.setVisible(true);
       }

    private void DeleteItem() {
        int selectedRow = frame.getLineTable().getSelectedRow();

        if (selectedRow != -1) {
            LineTable lineTable = (LineTable) frame.getLineTable().getModel();
            lineTable.getLines().remove(selectedRow);
            lineTable.fireTableDataChanged();
            frame.getInvoicesTable().fireTableDataChanged();
        }
       }

    private void NewInvoiceCancel() {
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
        }

    private void NewInvoiceOK() {
        String date = invoiceDialog.getInvDateField().getText();
        String customer = invoiceDialog.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();

        InvoiceHeader invoice = new InvoiceHeader(num, date, customer);
        frame.getInvoices().add(invoice);
        frame.getInvoicesTable().fireTableDataChanged();
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
       }

    private void NewLineOK() {
        String item = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = frame.getInvoiceTable().getSelectedRow();
        if (selectedInvoice != -1) {
            InvoiceHeader invoice = frame.getInvoices().get(selectedInvoice);
            InvoiceLine line = new InvoiceLine(item, price, count, invoice);
            invoice.getLines().add(line);
            LineTable lineTable = (LineTable) frame.getLineTable().getModel();
            //linesTableModel.getLines().add(line);
            lineTable.fireTableDataChanged();
            frame.getInvoicesTable().fireTableDataChanged();
        }
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
       }

    private void NewLineCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
        }

    
}
