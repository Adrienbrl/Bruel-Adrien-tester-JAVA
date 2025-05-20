package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/* FareCalculatorService est responsable du calcul du tarif de stationnement
 * en fonction de la durée, du type de véhicule et d'une éventuelle réduction.
 */
public class FareCalculatorService {
    
    /**
     * Calcule le tarif d'un ticket en appliquant éventuellement une remise.
     *
     * @param ticket   le ticket contenant les informations d'entrée/sortie
     * @param discount si true, applique une réduction de 5%
     * @throws IllegalArgumentException si les dates sont invalides ou le type inconnu
     */
    public void calculateFare(Ticket ticket, boolean discount){
        
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        /* Récupération de l'heure d'entrée et de sortie en millisecondes */
        long inTime = ticket.getInTime().getTime();
        long outTime = ticket.getOutTime().getTime();
       
        /* Calcul de la durée en millisecondes, puis conversion en minutes et en heures */
        long duration = outTime - inTime;
        double minutes = duration / (1000.0 * 60);
        double hours = minutes / 60.0;
        
        /* Tarif gratuit si durée inférieur à 30 minutes */
        if (minutes < 30){
            ticket.setPrice(0);
            return;
        }

        /* Calcul du tarif selon le type de véhicule */
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(hours * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(hours * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }

        /* Application de la remise de 5% si nécessaire */
        if (discount) {
            ticket.setPrice(ticket.getPrice() * 0.95);
        }
        
    }

    /**
     * Surcharge sans remise : appelle {@link #calculateFare(Ticket, boolean)} avec false.
     *
     * @param ticket le ticket à traiter
     */
    public void calculateFare(Ticket ticket) {
        calculateFare(ticket, false);
    }
}

