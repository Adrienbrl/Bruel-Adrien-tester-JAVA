package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    
    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    /* Vérifie que lorsqu’un véhicule entre, un ticket est bien enregistré et qu’une place est prise */
    @Test
    public void testParkingACar(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
       
        Ticket ticketTestParkingACar = ticketDAO.getTicket("ABCDEF");
        int parkingSpotTest = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertNotNull(ticketTestParkingACar);
        assertEquals(2,parkingSpotTest);
    }

    /* Vérifie que lorsqu’un véhicule sort, l’heure de sortie et le prix sont bien enregistrés et la place libérée */
    @Test
    public void testParkingLotExit() {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setInTime(new Date(System.currentTimeMillis()-50000000));
        ticket.setOutTime(null);
        ticket.setPrice(0);
        ticket.setVehicleRegNumber("ABCDEF");
        ticketDAO.saveTicket(ticket);
        parkingSpotDAO.updateParking(ticket.getParkingSpot());
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processExitingVehicle();

        ticket = ticketDAO.getTicket("ABCDEF");
        assertEquals(new Date().getTime(), ticket.getOutTime().getTime(), 5000);
        assertNotEquals(0,ticket.getPrice());
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    /* Vérifie qu’un utilisateur récurrent bénéficie bien d’une remise de 5 % sur le tarif du ticket */
    @Test
    public void testParkingLotExitRecurringUser() throws Exception {
        Ticket oldTicket = new Ticket();
        oldTicket.setId(200);
        oldTicket.setVehicleRegNumber("ABCDEF");
        oldTicket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
        oldTicket.setInTime(new Date(System.currentTimeMillis() - (6 * 60 * 60 * 1000)));
        oldTicket.setOutTime(new Date(System.currentTimeMillis() - (5 * 60 * 60 * 1000)));   
        oldTicket.setPrice(1.5);
        ticketDAO.saveTicket(oldTicket);

        when(inputReaderUtil.readSelection()).thenReturn(1); 
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        Ticket newTicket = ticketDAO.getTicket("ABCDEF");
        newTicket.setInTime(new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000))); 
        newTicket.setOutTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticketDAO.saveTicket(newTicket);

        parkingService.processExitingVehicle();

        Ticket updatedTicket = ticketDAO.getTicket("ABCDEF");
        double expectedPrice = 1.5 * 0.95;
        updatedTicket.setPrice(expectedPrice);

        assertEquals(expectedPrice, updatedTicket.getPrice(), 0.01, "Le prix doit inclure une remise de 5%");
    }

}
