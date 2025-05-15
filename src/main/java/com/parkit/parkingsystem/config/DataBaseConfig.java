package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/* Classe utilitaire pour gérer la configuration de la base de données.
 * Elle fournit des méthodes pour obtenir une connexion et fermer les ressources JDBC.
 */
public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");
    
    /**
     * Établit une connexion à la base de données MySQL.
     *
     * @return une instance de {@link Connection}
     * @throws ClassNotFoundException si le driver JDBC n'est pas trouvé
     * @throws SQLException si une erreur SQL se produit lors de la connexion
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/prod?serverTimezone=Europe/Paris","root","rootroot");
    }

    /**
     * Ferme proprement une connexion JDBC.
     *
     * @param con la connexion à fermer
     */
    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

    /**
     * Ferme un {@link PreparedStatement} pour libérer les ressources.
     *
     * @param ps le PreparedStatement à fermer
     */
    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }

    /**
     * Ferme un {@link ResultSet} après usage.
     *
     * @param rs le ResultSet à fermer
     */
    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}

