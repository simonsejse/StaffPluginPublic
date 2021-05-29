package dk.simonwither.staff.service;

import dk.simonwither.staff.StaffPlugin;
import dk.simonwither.staff.commands.ResultCallback;
import dk.simonwither.staff.models.StaffData;
import dk.simonwither.staff.models.WrongCommandArgumentUsageException;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class StaffManager {
    private final Map<UUID, StaffData> staffs;
    private final StaffPlugin staffPlugin;

    public StaffManager(StaffPlugin staffPlugin) {
        staffs = new LinkedHashMap<>();
        this.staffPlugin = staffPlugin;
    }

    public Map<UUID, StaffData> getStaffs() {
        return staffs;
    }

    public void addUser(final UUID uuid, final StaffData userData){
        this.staffs.put(uuid, userData);
    }

    public void removeUser(UUID uniqueId) {
        this.staffs.remove(uniqueId);
    }

    public void makeAsyncGetRequestForApi(String username, ResultCallback resultCallback) throws WrongCommandArgumentUsageException {
        String APICall = "https://api.mojang.com/users/profiles/minecraft/"+username;
        new BukkitRunnable(){
            @Override
            public void run() {
                final StringBuilder response = new StringBuilder();
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(APICall).openConnection();
                    httpURLConnection.connect();

                    try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))){
                        String line = bufferedReader.readLine();
                        while(line != null){
                            response.append(line);
                            line = bufferedReader.readLine();
                        }
                    }
                    resultCallback.callback(true, response.toString(), null, httpURLConnection.getResponseCode());
                } catch (IOException exception) {
                    resultCallback.callback(false, response.toString(), exception, -1);
                }
            }
        }.runTaskAsynchronously(this.staffPlugin);
    }
}
