package se.kth.iv1350.pointofsale.controller;

import java.util.ArrayList;
import java.util.List;

import se.kth.iv1350.pointofsale.integration.DatabaseConnectionFailureException;
import se.kth.iv1350.pointofsale.integration.ExternalAccountingSystem;
import se.kth.iv1350.pointofsale.integration.ExternalCreator;
import se.kth.iv1350.pointofsale.integration.ExternalInventorySystem;
import se.kth.iv1350.pointofsale.integration.InvalidIDException;
import se.kth.iv1350.pointofsale.integration.ItemDTO;
import se.kth.iv1350.pointofsale.integration.ItemID;
import se.kth.iv1350.pointofsale.integration.Printer;
import se.kth.iv1350.pointofsale.model.Amount;
import se.kth.iv1350.pointofsale.model.CashPayment;
import se.kth.iv1350.pointofsale.model.CashRegister;
import se.kth.iv1350.pointofsale.model.Item;
import se.kth.iv1350.pointofsale.model.POSFactory;
import se.kth.iv1350.pointofsale.model.Receipt;
import se.kth.iv1350.pointofsale.model.Sale;
import se.kth.iv1350.pointofsale.model.TotalRevenueObserver;

/**
 * This is the application's only controller class. All calls to the model pass
 * through here.
 */
public class Controller {
    private Sale currentSale;
    private final Printer printer;
    private final CashRegister cashRegister;
    private final ExternalAccountingSystem eas;
    private final ExternalInventorySystem eis;
    private final ExternalCreator creator;
    List<TotalRevenueObserver> saleObservers = new ArrayList<>();
    private final POSFactory factory;

    /**
     * Creates a new instance of the Controller.
     * 
     * @param printer Interface to printer.
     * @param creator Used to get all classes that handle database calls.
     */
    public Controller(Printer printer /*ExternalCreator creator*/) {
        this.printer = printer;
        this.factory = POSFactory.getInstance();
        this.creator = factory.createExternalCreator();
        this.eas = creator.getEas();
        this.eis = creator.getEis();
        this.cashRegister = factory.createCashRegister(new Amount(1234));

    }

    /**
     * Starts a new sale.
     */
    public void startSale() {
        currentSale = factory.createSale();
        this.currentSale.addSaleObservers(saleObservers);
    }

    /** 
     * Retrieves the item data from inventory and adds 
     * the scanned item to the current sale 
     * 
     * @param itemID the identifier of the scanned item.
     * @return the <code>ItemDTO</code> containing the the scanned items data.
     * @throws InvalidIDException if the item ID does not exist in the inventory.
     * @throws OperationFailedException if the inventory system cannot be accessed because of a system failure.
     */
    public ItemDTO addItem(ItemID itemID) throws InvalidIDException, OperationFailedException{
        ItemDTO itemDTO;
        try{
            itemDTO = eis.findItem(itemID);
            currentSale.addItemToList(new Item (itemDTO));
        }catch(DatabaseConnectionFailureException databaseExc){
            throw new OperationFailedException("could not register item", databaseExc);
        }
        return itemDTO;     
    }

    /** NY
     * Retrives the current running total incl VAT of the current sale.
     * 
     * @return an <code>Amount</code> representing the running total.
     */
    public Amount getCurRunningTotal(){
        return currentSale.getRunningTotal();
    }

    /** NY
     * Retrives the current running VAT of the current sale.
     * 
     * @return an <code>Amount</code> representing the running VAT.
     */
    public Amount getCurRunningVat(){
        return currentSale.getRunningVat();
    }
        

    /**ÄNDRAD
    *  Ends the current sale and retrives the total amount of the sale.
    * @return an <code>Amount</code> representing the Total Amount.
    */
    public Amount endSale(){
        return currentSale.getRunningTotal();
    }

    /**ÄNDRAD 
    * Processes the customer's payment. Sends sale info to inventory
    * and accounting. Updates the balance of the cash register where
    * the payment was performed. Calculates change. Prints the receipt.
    *
    * @param paidAmount The amount paid by the customer.
    * @return the <code>Amount</code> representing the change to give to the customer.
    */
    public Amount enterPayment(Amount paidAmount) {
        CashPayment payment = factory.createCashPayment(paidAmount);
        currentSale.processPayment(payment);
        cashRegister.addAmount(paidAmount);
        eas.recordSale(currentSale);
        eis.updateInventory(currentSale);
        Receipt receipt = new Receipt(currentSale, paidAmount);
        printer.printReceipt(receipt);
        return payment.getChange(currentSale.getRunningTotal());
    }
    
    /**
     * Register a new observer that will be notified when a sale is completed
     * @param anotherObserver the observer to be registered.
     *  
     */
    public void saleObservers(TotalRevenueObserver anotherObserver) {
        saleObservers.add(anotherObserver);
    }

}
