package dk.simonwither.staff;

import dk.simonwither.staff.commands.StaffCommand;
import dk.simonwither.staff.dbhandling.ConnectionProvider;
import dk.simonwither.staff.models.ICallback;
import dk.simonwither.staff.models.Query;
import dk.simonwither.staff.models.Rank;
import dk.simonwither.staff.models.StaffData;
import dk.simonwither.staff.service.ClickEventHandling;
import dk.simonwither.staff.service.Configuration;
import dk.simonwither.staff.service.StaffManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        //TODO: Automatically save scheduler every fifth minute
    }

    @Override
    public void onDisable() {
        CompletableFuture.runAsync(() -> this.saveData(() -> this.getLogger().info("Data was successfully written to DB")));
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
            final UUID playerUniqueID = UUID.fromString(resultSet.getString(0));
            final String username = resultSet.getString(1);
            final Integer age = resultSet.getInt(2);
            final String description = resultSet.getString(3);
            final Integer rankID = resultSet.getInt(4);

            final Rank rankByID = getRankByID(rankID);
            if (rankByID != null) {
                getStaffManager().addUser(playerUniqueID, new StaffData(username, rankByID, age, description));
            }
        }catch(SQLException e){
            getLogger().warning(String.format("SQL exception was thrown: %s", e.getMessage()));
        }
        whenComplete.onComplete();
    }

    /**
     *
     * @param callbackWhenDone interface that invokes when method is done running async thread
     */
    protected void saveData(ICallback callbackWhenDone){
        getStaffManager().getStaffs().entrySet().stream().forEach(staffEntry -> {
            try(final PreparedStatement preparedStatement = getConnectionProvider().getConnection().prepareStatement(Query.INSERT_INTO_STAFF_DATA.getSQL())){
                preparedStatement.setString(1, staffEntry.getKey().toString());
                preparedStatement.setString(2, staffEntry.getValue().getUsername());
                preparedStatement.setInt(3, staffEntry.getValue().getAge());
                preparedStatement.setString(4, staffEntry.getValue().getDescription());
                preparedStatement.setInt(5, staffEntry.getValue().getRank().getPriority());
                preparedStatement.executeUpdate();
            }catch(java.sql.SQLException e){
                getLogger().warning(String.format("Exception was thrown while trying to save to DB: %s", e.getMessage()));
            }
        });
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
            this.staffManager = new StaffManager();
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
