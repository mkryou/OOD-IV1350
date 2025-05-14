package se.kth.iv1350.pointofsale.view;
import se.kth.iv1350.pointofsale.integration.InvalidIDException;
import se.kth.iv1350.pointofsale.controller.Controller;
import se.kth.iv1350.pointofsale.controller.OperationFailedException;
import se.kth.iv1350.pointofsale.integration.ItemID;
import se.kth.iv1350.pointofsale.model.Amount;
import se.kth.iv1350.pointofsale.util.LogHandler;
import java.io.IOException;
/**
 * This program has no view, instead, this class is a placeholder for the entire
 * view.
 */
public class View {
    private final Controller contr;
    private final TotalRevenueFileOutput totalRevenueFileOutput;
    private ErrorMessageHandler errorMsgHandler = new ErrorMessageHandler();
    private LogHandler logger;
    /**
     * Creates a new instance.
     *
     * @param contr The controller that is used for all operations.
     */

    public View (Controller contr) throws IOException {
        this.contr = contr;
        this.logger = new LogHandler();
        this.totalRevenueFileOutput = new TotalRevenueFileOutput();
            contr.saleObservers(totalRevenueFileOutput);
            contr.saleObservers(new TotalRevenueView());
    }

    public void startSale(){
        contr.startSale();
        System.out.println("New Sale");
    }

    public void scannNewItem(ItemID itemID){
        try{
        System.out.println("Add 1 item with item id " + itemID + " :" +"\n"+ contr.addItem(itemID)+"\n");
        System.out.println("Total cost (incl VAT): " + contr.getCurRunningTotal()  + "\n" + "Total VAT :" + contr.getCurRunningVat()+"\n");
        }catch (InvalidIDException e) {
            errorMsgHandler.showErrorMsg("The itemID : " + itemID + " is invalid, scann different item ");
        } catch (OperationFailedException exc) {
            writeToLogAndUI("could not add item because of system failure", exc);   
            //errorMsgHandler.showErrorMsg("could not add item because of system failure");
        }
    }

    public void endSale(){
       Amount saleTotal = new Amount();
       saleTotal = contr.endSale();
       System.out.println("End Sale"+"\n"+ "Total incl. VAT: " + saleTotal);
       System.out.println("change to give to the customer: " + contr.enterPayment(new Amount(100.0)));
       totalRevenueFileOutput.printFinalTotal();
    }

    public void sampleExecution() {
        startSale();
        scannNewItem(new ItemID("abc123"));
        scannNewItem(new ItemID("def456"));
        scannNewItem(new ItemID("invalidID"));
        scannNewItem(new ItemID("abc123"));
        //scannNewItem(new ItemID("crachDB"));
        endSale();

        startSale();
        scannNewItem(new ItemID("abc123"));
        scannNewItem(new ItemID("def456"));
        scannNewItem(new ItemID("invalidID"));
        scannNewItem(new ItemID("abc123"));
        //scannNewItem(new ItemID("crachDB"));
        endSale();
        

    }

    /**
     * Simulates a user input that generates calls to all system operations.
     */
    /* 
    public void sampleExecution() {
       contr.startSale();

       try{
       ItemID firsItemID = new ItemID("abc123");
       System.out.println("Add 1 item with item id " + firsItemID + " :" +"\n"+ contr.addItem(firsItemID)+"\n");
       System.out.println("Total cost (incl VAT): " + contr.getCurRunningTotal()  + "\n" + "Total VAT :" + contr.getCurRunningVat()+"\n");
        
       //ItemID databaseFailureID = new ItemID("crachDB");
      // contr.addItem(databaseFailureID);

       ItemID secondItemID = new ItemID("def456");
       System.out.println("Add 1 item with item id " + secondItemID + " :" +"\n"+ contr.addItem(secondItemID)+"\n");
       System.out.println("Total cost (incl VAT): " + contr.getCurRunningTotal()  + "\n" + "Total VAT :" + contr.getCurRunningVat()+"\n");

        ItemID invalidItemID = new ItemID("invalid");
       System.out.println("Add 1 item with item id " + invalidItemID + " :" +"\n"+ contr.addItem(invalidItemID)+"\n");
       System.out.println("Total cost (incl VAT): " + contr.getCurRunningTotal()  + "\n" + "Total VAT :" + contr.getCurRunningVat()+"\n");
        
       ItemID sameItemID = new ItemID("abc123");
       System.out.println("Add 1 item with item id " + sameItemID + " :" +"\n"+ contr.addItem(sameItemID)+"\n");
       System.out.println("Total cost (incl VAT): " + contr.getCurRunningTotal()  + "\n" + "Total VAT :" + contr.getCurRunningVat()+"\n");

        } catch (InvalidIDException e) {
            errorMsgHandler.showErrorMsg("Correctly failed to add invalid item");
        } catch (OperationFailedException exc) {
            writeToLogAndUI("could not add item because of system failure", exc);   
            //errorMsgHandler.showErrorMsg("could not add item because of system failure");
        }

       Amount saleTotal = new Amount();
       saleTotal = contr.endSale();
       System.out.println("End Sale"+"\n"+ "Total incl. VAT: " + saleTotal);
       System.out.println("change to give to the customer: " + contr.enterPayment(new Amount(100.0)));
    } 

    private void writeToLogAndUI(String uiMsg, Exception exc) {
        errorMsgHandler.showErrorMsg(uiMsg);
        logger.logException(exc);
    }
 */
    private void writeToLogAndUI(String uiMsg, Exception exc) {
        errorMsgHandler.showErrorMsg(uiMsg);
        logger.logException(exc);
    }
}