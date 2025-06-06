package se.kth.iv1350.pointofsale.startup;

import java.io.IOException;

import se.kth.iv1350.pointofsale.controller.Controller;
//import se.kth.iv1350.posi.integration.ExternalCreator;
import se.kth.iv1350.pointofsale.integration.Printer;
import se.kth.iv1350.pointofsale.view.View;

public class Main {
     /**
     * Starts the application.
     *
     * @param args The application does not take any command line parameters.
     */
    public static void main(String[] args) {
        try{
       // ExternalCreator creator = new ExternalCreator();
        Printer printer = new Printer();
        Controller contr = new Controller(printer/* , creator*/);
        new View(contr).sampleExecution();
        } catch (IOException ex) {
            System.out.println("Unable to start the application");
            ex.printStackTrace();
        }
    }
}