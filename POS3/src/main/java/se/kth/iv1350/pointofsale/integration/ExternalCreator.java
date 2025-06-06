package se.kth.iv1350.pointofsale.integration;

/**
 * This class is responsible for instantiating all registries.
 */
public class ExternalCreator {
    private final ExternalInventorySystem eis = new ExternalInventorySystem();
    private final ExternalAccountingSystem eas = new ExternalAccountingSystem();

    /**
     * Get the value of externalAccountingSystem
     *
     * @return the value of externalAccountingSystem
     */
    public ExternalAccountingSystem getEas() {
        return eas;
    }

     /**
     * Get the value of externalInventorySystem
     *
     * @return the value of externalInventorySystem
     */
    public ExternalInventorySystem getEis() {
        return eis;
    }
}