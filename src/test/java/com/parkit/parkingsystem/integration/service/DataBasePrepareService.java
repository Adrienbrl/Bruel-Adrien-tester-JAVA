package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;

public class DataBasePrepareService {

    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    /* Nettoie la BDD : réinitialise toutes les places de parking comme étant disponibles et
     * supprime tous les tickets présents dans la base
     */
    public void clearDataBaseEntries(){
        Connection connection = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            //Rend toutes les places de parking disponibles
            connection.prepareStatement("update parking set available = true").execute();

            //Supprime tous les enregistrements de tickets
            connection.prepareStatement("truncate table ticket").execute();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }


}
