package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/*
 * Classeutilitaire pour lire les entrées de l'utilisateur depuis la console.
 * Elle fournit également des méthodes pour lire des sélections numériques ou des chaînes de caractères.
 */
public class InputReaderUtil {

    private static Scanner scan = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    /**
     * Lit un entier depuis la console (utilisé pour les menus).
     * En cas d'erreur de saisie, retourne -1.
     *
     * @return l'entier saisi par l'utilisateur, ou -1 en cas d'erreur.
     */
    public int readSelection() {
        try {
            int input = Integer.parseInt(scan.nextLine());
            return input;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    /**
     * Lit un numéro d'immatriculation.
     * Vérifie que la saisie n'est pas vide et n'est pas nulle.
     *
     * @return la chaîne représentant le numéro d'immatriculation.
     * @throws Exception si la saisie est invalide ou si une erreur de lecture se produit.
     */
    public String readVehicleRegistrationNumber() throws Exception {
        try {
            String vehicleRegNumber= scan.nextLine();
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }
}
