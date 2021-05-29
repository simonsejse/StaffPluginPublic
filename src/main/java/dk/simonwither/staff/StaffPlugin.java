package dk.simonwither.staff;

import dk.simonwither.staff.commands.StaffCommand;
import dk.simonwither.staff.dbhandling.ConnectionProvider;
import dk.simonwither.staff.models.ICallback;
import dk.simonwither.staff.models.SQLConstants;
import dk.simonwither.staff.models.Rank;
import dk.simonwither.staff.models.StaffData;
import dk.simonwither.staff.service.ClickEventHandling;
import dk.simonwither.staff.service.Configuration;
import dk.simonwither.staff.service.StaffManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class StaffPlugin extends JavaPlugin {

    private ConnectionProvider connectionProvider;
    private Configuration configuration;
    private StaffCommand staffExecutor;
    private StaffManager staffManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("staff").setExecutor(getStaffExecutor());
        this.getConnectionProvider().openConnection();
        this.createTables();

        getServer().getPluginManager().registerEvents(new ClickEventHandling(), this);
        CompletableFuture.runAsync(() -> this.readData(() -> this.getLogger().info("Successfully read data from DB")));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::saveAsync, 0l, 36000L);
    }

    /**
     * Invoke saveData() asyncrounously.
     */
    protected void saveAsync(){
        CompletableFuture.runAsync(() -> this.saveData(() -> this.getLogger().info("Data was successfully written to DB")));
    }

    @Override
    public void onDisable() {
        this.saveData(() -> getLogger().info("Saving data to database is finished!"));
    }

    /**
     * Create tables if table does not exist on server startup
     */
    private void createTables() {
        final InputStream resourceAsStream = getClass().getResourceAsStream("/tables.sql");
        try(final Statement statement = getConnectionProvider().getConnection().createStatement()){
            byte[] data = new byte[resourceAsStream.available()];
            resourceAsStream.read(data);
            statement.execute(new String(data));
        }catch(SQLException e){
            getLogger().warning(String.format("Exception thrown from sql: %s", e.getMessage()));
        }catch(IOException e){
            getLogger().warning(String.format("Problem with input stream: %s", e.getMessage()));
        }
    }

    /**
     *
     * @param whenComplete callback to the anonymous class when done reading data
     */
    protected void readData(ICallback whenComplete){
        try(final ResultSet resultSet = getConnectionProvider().getConnection().createStatement().executeQuery("SELECT * from staff")){
            while(resultSet.next()){
                final UUID playerUniqueID = UUID.fromString(resultSet.getString(1));
                final String username = resultSet.getString(2);
                final Integer age = resultSet.getInt(3);
                final String description = resultSet.getString(4);
                final Integer rankID = resultSet.getInt(5);

                final Rank rankByID = getRankByID(rankID);
                if (rankByID != null) {
                    getStaffManager().addUser(playerUniqueID, new StaffData(username, rankByID, age, description));
                }
            }
        }catch(SQLException e){
            getLogger().warning(String.format("SQL exception was thrown: %s", e.getMessage()));
        }
        whenComplete.onComplete();
    }

    /**
     *
     * @param callbackWhenDone interface that invokes when method is done running
     */
    protected void saveData(ICallback callbackWhenDone){
        if (getConfiguration().sendAutomaticallySavedMessage){
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(getConfiguration().automaticallySavedMessage));
        }
        for(Map.Entry<UUID, StaffData> staffDataEntry : getStaffManager().getStaffs().entrySet()){
            try{
                final Connection connection = getConnectionProvider().getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(SQLConstants.INSERT_INTO_STAFF_DATA.getSQL());
                preparedStatement.setString(1, staffDataEntry.getKey().toString());
                preparedStatement.setString(2, staffDataEntry.getValue().getUsername());
                preparedStatement.setInt(3, staffDataEntry.getValue().getAge());
                preparedStatement.setString(4, staffDataEntry.getValue().getDescription());
                preparedStatement.setInt(5, staffDataEntry.getValue().getRank().getPriority());
                preparedStatement.setString(6, staffDataEntry.getKey().toString());
                preparedStatement.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
                System.out.println(String.format("Exception was thrown while trying to save to DB: %s", e.getMessage()));
            }
        }
        callbackWhenDone.onComplete();
    }

    /**
     *
     * @param id the id of which rank we want to return
     * @return a Rank object
     */
    public Rank getRankByID(int id){
        return this.getConfiguration().ranks.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == id)
                .findFirst()
                .map(entry -> new Rank(entry.getKey(), entry.getValue()))
                .get();
    }
    /**
     *
     * @return CommandExecutor instance for /staff cmd
     */
    private CommandExecutor getStaffExecutor() {
        if (staffExecutor == null){
            this.staffExecutor = new StaffCommand(this);
        }
        return staffExecutor;
    }

    /**
     *
     * @return ConnectionProvider object instance
     */
    public ConnectionProvider getConnectionProvider() {
        if (connectionProvider == null){
            this.connectionProvider = new ConnectionProvider(getConfiguration());
            this.connectionProvider.openConnection();
        }
        return connectionProvider;
    }

    /**
     *
     * @return Configuration object instance
     */
    public Configuration getConfiguration() {
        if (configuration == null){
            try {
                this.configuration = loadConfiguration();
            } catch (IOException exception) {
                getPluginLoader().disablePlugin(this);
                getLogger().warning("Plugin disabled! Configuration not loaded properly!");
            }
        }
        return configuration;
    }

    /**
     *
     * @return StaffManager object instance
     */
    public StaffManager getStaffManager() {
        if (staffManager == null){
            this.staffManager = new StaffManager(this);
        }
        return staffManager;
    }

    /**
     *
     * @return Configuration object instance from the config.yml file
     * @throws IOException in case config.yml file is not written correctly
     */
    protected Configuration loadConfiguration() throws IOException {
        final Yaml yaml = new Yaml(new CustomClassLoaderConstructor(Configuration.class.getClassLoader()));
        return yaml.loadAs(new FileInputStream(getDataFolder().getPath()+"/config.yml"), Configuration.class);
    }
}
