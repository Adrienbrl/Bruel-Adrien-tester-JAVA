package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/* ParkingSpot représente une place de parking avec son numéro, son type (CAR ou BIKE) et sa disponibilité. */
public class ParkingSpot {
    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;

    /**
     * Constructeur complet.
     *
     * @param number       numéro de la place
     * @param parkingType  type de véhicule autorisé
     * @param isAvailable  disponibilité de la place (true = libre)
     */
    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    /**
     * @return le numéro de la place
     */
    public int getId() {
        return number;
    }

    /**
     * Définit le numéro de la place.
     *
     * @param number le nouveau numéro
     */
    public void setId(int number) {
        this.number = number;
    }

    /**
     * @return le type de la place (CAR ou BIKE)
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     * Modifie le type de la place.
     *
     * @param parkingType type de véhicule autorisé
     */
    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    /**
     * @return true si la place est disponible, false sinon
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Définit la disponibilité de la place.
     *
     * @param available true = libre, false = occupée
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /* Compare deux objets ParkingSpot sur la base de leur numéro. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    /* HashCode basé uniquement sur le numéro de la place. */
    @Override
    public int hashCode() {
        return number;
    }
}
