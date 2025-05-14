package se.kth.iv1350.pointofsale.startup;

import se.kth.iv1350.pointofsale.controller.Controller;
import se.kth.iv1350.pointofsale.integration.ExternalCreator;
import se.kth.iv1350.pointofsale.integration.Printer;
import se.kth.iv1350.pointofsale.view.View;

public class Main {
     /**
     * Starts the application.
     *
     * @param args The application does not take any command line parameters.
     */
    public static void main(String[] args) {
        ExternalCreator creator = new ExternalCreator();
        Printer printer = new Printer();
        Controller contr = new Controller(printer, creator);
        new View(contr).sampleExecution();
    }
}