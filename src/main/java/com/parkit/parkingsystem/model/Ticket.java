package com.parkit.parkingsystem.model;

import java.util.Date;

/* Ticket représente un ticket de stationnement, un ticket est généré à chaque entrée dans le parking.
 * Il contient des informations sur la place, le véhicule, les horaires et le tarif.
 */
public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    /**
     * @return l'identifiant du ticket
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant du ticket.
     * @param id identifiant unique
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return la place de parking associée à ce ticket
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Définit la place de parking utilisée.
     * @param parkingSpot objet ParkingSpot
     */
    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    /**
     * @return le numéro d'immatriculation du véhicule
     */ 
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Définit le numéro d'immatriculation du véhicule.
     * @param vehicleRegNumber chaîne d'immatriculation
     */
    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     * @return le tarif calculé pour ce ticket
     */
    public double getPrice() {
        return price;
    }

    /**
     * Définit le prix du stationnement.
     * @param price montant en euros
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return la date/heure d'entrée dans le parking
     */
    public Date getInTime() {
        return inTime;
    }

    /**
     * Définit la date/heure d'entrée.
     * @param inTime date d'entrée
     */
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    /**
     * @return la date/heure de sortie du parking
     */
    public Date getOutTime() {
        return outTime;
    }

    /**
     * Définit la date/heure de sortie.
     * @param outTime date de sortie
     */
    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }
}
