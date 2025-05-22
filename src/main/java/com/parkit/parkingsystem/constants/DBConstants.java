package com.parkit.parkingsystem.constants;

/* Classe contenant les constantes SQL utilisées dans le système de gestion de parking. */
public class DBConstants {

    /* Requête SQL pour obtenir la prochaine place de parking disponible selon le type (CAR, BIKE...). */
    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";

    /* Requête SQL pour mettre à jour la disponibilité d'une place de parking. */
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

    /* Requête SQL pour enregistrer un nouveau ticket de parking. */
    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";

    /* Requête SQL pour mettre à jour un ticket existant (ajout du prix et de la date de sortie). */
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";

    /* Requête SQL pour récupérer le dernier ticket d'un véhicule à partir de son numéro d'immatriculation. */
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME limit 1";
}
