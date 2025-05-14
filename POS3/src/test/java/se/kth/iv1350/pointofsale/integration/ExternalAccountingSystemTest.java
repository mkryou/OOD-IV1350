package se.kth.iv1350.pointofsale.integration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.pointofsale.model.Amount;
import se.kth.iv1350.pointofsale.model.Item;
import se.kth.iv1350.pointofsale.model.Sale;


public class ExternalAccountingSystemTest {

    @Test
    public void recordSaleTest(){
        Amount price = new Amount(100.0);
        Sale sale = new Sale();
        ItemID itemID = new ItemID("abs");
        ItemDTO itemDTO = new ItemDTO(itemID, "hejehj", price, new Amount(1.0), "kakor");
        Item item = new Item(itemDTO);
        sale.addItemToList(item);
  
        ExternalAccountingSystem instance = new ExternalAccountingSystem();
        instance.recordSale(sale);
        assertEquals(itemDTO.getPriceWithVAT().getValue(), instance.getTotalRevenue().getValue());
    }
}