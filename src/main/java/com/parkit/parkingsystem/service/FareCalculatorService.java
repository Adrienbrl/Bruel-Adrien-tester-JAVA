package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    

    public void calculateFare(Ticket ticket){

        long inTimeMillis = ticket.getInTime().getTime();
        long outTimeMillis = ticket.getOutTime().getTime();
        long durationInMinutes = (outTimeMillis - inTimeMillis) / (60 * 1000);
        
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        } 
        
        if (durationInMinutes <= 29){
            ticket.setPrice(0);
            return;
        }

        long durationInMillis = outTimeMillis - inTimeMillis;

        double durationInHours = (double) durationInMillis / (60 * 60 * 1000);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(durationInHours * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(durationInHours * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}