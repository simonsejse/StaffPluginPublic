package dk.simonwither.staff.dbhandling;

import dk.simonwither.staff.service.Configuration;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {

    public Configuration configuration;

    public ConnectionProvider(final Configuration configuration){
        this.configuration = configuration;
    }
    private Connection connection;

    public void openConnection() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://"+configuration.host+":"+configuration.port+"/"+configuration.databaseName, configuration.username, configuration.password);
        }catch(java.lang.ClassNotFoundException | SQLException exception){
            Bukkit.getLogger().warning(String.format("Exception was thrown: %s", exception.getMessage()));
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
