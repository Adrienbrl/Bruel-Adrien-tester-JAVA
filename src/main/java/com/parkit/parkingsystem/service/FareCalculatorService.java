package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    
    public void calculateFare(Ticket ticket, boolean discount){
        
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inTime = ticket.getInTime().getTime();
        long outTime = ticket.getOutTime().getTime();
       
        long duration = outTime - inTime;
        double minutes = duration / (1000.0 * 60);
        double hours = minutes / 60.0;
        
        if (minutes <= 29){
            ticket.setPrice(0);
            return;
        }

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

        if (discount) {
            ticket.setPrice(ticket.getPrice() * 0.95);
        }
        
    }

    public void calculateFare(Ticket ticket) {
        calculateFare(ticket, false);
    }
}