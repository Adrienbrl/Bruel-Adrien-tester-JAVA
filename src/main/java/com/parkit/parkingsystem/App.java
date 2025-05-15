package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* Classe principale de l'application, elle initialise le système de parking. */
public class App {
    private static final Logger logger = LogManager.getLogger("App");

    /** Méthode principale, c'est le point d'entrée de l'application.
     * Affiche un message de démarrage et lance l'interface utilisateur.
     * 
     * @param args les arguments de la ligne de commande (non utilisés ici)
     */
    public static void main(String args[]){
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
