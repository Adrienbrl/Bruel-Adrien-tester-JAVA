package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingSpotDAOTest {

    @InjectMocks
    private ParkingSpotDAO parkingSpotDAO;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    // Test 1 : retourne bien une place dispo si la requête a un résultat
    @Test
    void testGetNextAvailableSlot_ShouldReturnSlotNumber() throws Exception {
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(5);

        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(5, result);
    }

    // Test 2 : retourne -1 si aucun résultat dans la requête
    @Test
    void testGetNextAvailableSlot_ShouldReturnMinusOne_WhenNoResult() throws Exception {
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(-1, result);
    }

    // Test 3 : retourne false si update échoue avec une exception
    @Test
    void testUpdateParking_ShouldReturnFalse_WhenException() throws Exception {
        ParkingSpot spot = new ParkingSpot(1, ParkingType.CAR, false);
        when(dataBaseConfig.getConnection()).thenThrow(new RuntimeException("DB error"));

        boolean result = parkingSpotDAO.updateParking(spot);

        assertFalse(result);
    }

    // Test 4 : retourne true si 1 ligne est mise à jour
    @Test
    void testUpdateParking_ShouldReturnTrue_WhenUpdateSuccessful() throws Exception {
        ParkingSpot spot = new ParkingSpot(2, ParkingType.BIKE, true);
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = parkingSpotDAO.updateParking(spot);

        assertTrue(result);
    }

    // Test 5 : retourne false si aucune ligne mise à jour
    @Test
    void testUpdateParking_ShouldReturnFalse_WhenNoRowUpdated() throws Exception {
        ParkingSpot spot = new ParkingSpot(3, ParkingType.BIKE, false);
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = parkingSpotDAO.updateParking(spot);

        assertFalse(result);
    }

    // Test 6 : getNextAvailableSlot — test avec exception → doit retourner -1
    @Test
    void testGetNextAvailableSlot_ShouldReturnMinusOne_WhenExceptionOccurs() throws Exception {
        when(dataBaseConfig.getConnection()).thenThrow(new RuntimeException("Erreur DB"));

        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

        assertEquals(-1, result);
    }

    // Test 7 : updateParking — test avec SQLException levée après la connexion
    @Test
    void testUpdateParking_ShouldReturnFalse_WhenSQLExceptionAfterConnection() throws Exception {
        ParkingSpot spot = new ParkingSpot(10, ParkingType.CAR, false);
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new RuntimeException("Erreur requête"));

        boolean result = parkingSpotDAO.updateParking(spot);

        assertFalse(result);
    }

    // Test 8 : updateParking — vérifie bien l'appel à executeUpdate
    @Test
    void testUpdateParking_ShouldCallExecuteUpdate() throws Exception {
        ParkingSpot spot = new ParkingSpot(4, ParkingType.CAR, false);
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
    
        parkingSpotDAO.updateParking(spot);
    
        verify(preparedStatement, times(1)).executeUpdate(); // on vérifie que la requête est bien exécutée
    }
    
    // Test 9 : getNextAvailableSlot — type de véhicule nul → exception (robustesse)
    @Test
    void testGetNextAvailableSlot_ShouldHandleNullParkingType() {
        int result = parkingSpotDAO.getNextAvailableSlot(null);
    
        assertEquals(-1, result); // comportement attendu : retourne -1 si type invalide
    }

    // Test 10 : Vérifie que closeConnection est appelé même si exception pendant le traitement
    @Test
    void testGetNextAvailableSlot_ShouldAlwaysCloseConnection() throws Exception {
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new RuntimeException("Erreur"));

        parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

        verify(dataBaseConfig).closeConnection(connection); // on vérifie bien que le finally s'exécute
    }

    
}
