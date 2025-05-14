package se.kth.iv1350.pointofsale.view;

import se.kth.iv1350.pointofsale.model.Amount;
import se.kth.iv1350.pointofsale.model.TotalRevenueObserver;
/**
 * A wiew class that implements TotalRevenueObserver and displays the updated
 * total revenue tp the user after each completed sale.
 */
public class TotalRevenueView implements TotalRevenueObserver {
    private Amount totalRevenue = new Amount(0.0); 
    /**
     * New instance of TotalRevenueView.
     */
    public TotalRevenueView() {
    }
        
    /**
     * Updates the total revenue and prints new total to the console.
     * @param saleAmount the revenue from the most recently completed sale.
     */
    @Override
    public void printTotalRevenue(Amount saleAmount) {
        totalRevenue = (totalRevenue.add(saleAmount));
    }

}
