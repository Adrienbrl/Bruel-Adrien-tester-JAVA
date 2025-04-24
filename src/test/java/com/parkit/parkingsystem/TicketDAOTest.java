package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.dao.TicketDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketDAOTest {

    @InjectMocks
    private TicketDAO ticketDAO;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    // Test 1 : ticket bien enregistré → true
    @Test
    void testSaveTicket_ShouldReturnTrue_WhenExecuteSuccess() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
        ticket.setVehicleRegNumber("ABC123");
        ticket.setPrice(3.0);
        ticket.setInTime(new Date());

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        assertTrue(ticketDAO.saveTicket(ticket));
    }

    // Test 2 : save échoue → false
    @Test
    void testSaveTicket_ShouldReturnFalse_WhenExceptionThrown() throws Exception {
        Ticket ticket = new Ticket();
        when(dataBaseConfig.getConnection()).thenThrow(new RuntimeException("DB error"));

        assertFalse(ticketDAO.saveTicket(ticket));
    }

    // Test 3 : bonne récupération du nombre de visites
    @Test
    void testCountByVehicleRegNumber_ShouldReturnVisitCount() throws Exception {
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(4);

        int count = ticketDAO.countByVehicleRegNumber("XYZ789");

        assertEquals(4, count);
    }

    // Test 4 : aucune visite → 0
    @Test
    void testCountByVehicleRegNumber_ShouldReturnZero_WhenNoResult() throws Exception {
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        int count = ticketDAO.countByVehicleRegNumber("NORESULT");

        assertEquals(0, count);
    }

    // Test 5 : updateTicket → true si pas d’exception
    @Test
    void testUpdateTicket_ShouldReturnTrue_WhenUpdateSuccess() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setPrice(5.0);
        ticket.setOutTime(new Date());

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        assertTrue(ticketDAO.updateTicket(ticket));
    }

        // Test 6 : updateTicket — retourne false si exception levée (ex: mauvais ID)
    @Test
    void testUpdateTicket_ShouldReturnFalse_WhenExceptionThrown() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(2);
        ticket.setPrice(6.0);
        ticket.setOutTime(new Date());

        when(dataBaseConfig.getConnection()).thenThrow(new RuntimeException("Erreur"));

        boolean result = ticketDAO.updateTicket(ticket);

        assertFalse(result);
    }

    // Test 7 : getNbTicket — retourne le bon nombre de tickets avec OUT_TIME non null
    @Test
    void testGetNbTicket_ShouldReturnCount_WhenDataFound() throws Exception {
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(2);

        int result = ticketDAO.getNbTicket("ABC123");

        assertEquals(2, result);
    }

     // Test 8 : getNbTicket — retourne 0 quand aucun résultat dans la requête
    @Test
    void testGetNbTicket_ShouldReturnZero_WhenNoResult() throws Exception {
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
    
        int result = ticketDAO.getNbTicket("XYZ999");
    
        assertEquals(0, result);
    }
    
    // Test 9 : getNbTicket — exception lors de la requête → retourne 0
    @Test
    void testGetNbTicket_ShouldReturnZero_WhenExceptionThrown() throws Exception {
        when(dataBaseConfig.getConnection()).thenThrow(new RuntimeException("Erreur SQL"));
    
        int result = ticketDAO.getNbTicket("ERROR");
    
        assertEquals(0, result);
    }
    
    // Test 10 : getTicket retourne null si aucun ticket trouvé
    @Test
    void testGetTicket_ShouldReturnNull_WhenNoResult() throws Exception {
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Ticket ticket = ticketDAO.getTicket("NOT_FOUND");

        assertNull(ticket);
    }

}

