package dk.simonwither.staff.commands;

import com.google.gson.JsonObject;
import dk.simonwither.staff.StaffPlugin;
import dk.simonwither.staff.models.Rank;
import dk.simonwither.staff.models.StaffData;
import dk.simonwither.staff.models.WrongCommandArgumentUsageException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

public class AddNewStaffArgument implements IStaffCommandArguments{

    private final StaffPlugin staffPlugin;

    public AddNewStaffArgument(StaffPlugin staffPlugin){
        this.staffPlugin = staffPlugin;
    }

    @Override
    public String commandArgument() {
        return "add";
    }

    @Override
    public List<String> wrongCommandUsage() {
        return this.staffPlugin.getConfiguration().wrongAddNewStaffCommandUsage;
    }

    @Override
    public void perform(Player executor, String... args) throws WrongCommandArgumentUsageException {
        if (args.length < 5) throw new WrongCommandArgumentUsageException();
        final String rankArg = args[2];
        final Optional<Map.Entry<String, Integer>> first = this.staffPlugin.getConfiguration().ranks.entrySet().stream().filter(s -> s.getKey().equalsIgnoreCase(rankArg)).findFirst();
        final StringBuilder motto = new StringBuilder();
        if (first.isPresent()){
            //rank add <user> <rank> <age> <description>
            final Integer age = getAge(args[3]);
            final Rank rank = new Rank(first.get().getKey(), first.get().getValue());

            IntStream.range(4, args.length).forEach(n -> motto.append(args[n]).append(" "));
            executor.sendMessage("§cFetching data from api.mojang.com!");
            this.staffPlugin.getStaffManager().makeAsyncGetRequestForApi(args[1], ((successful, response, exception, responseCode) -> {
                if (exception == null && response.startsWith("{")){
                    //Success
                    executor.sendMessage("§aData fetched!");
                    JSONObject jsonObject = new JSONObject(response);
                    final UUID uuid = UUID.fromString(jsonObject.getString("id")
                            .replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
                    staffPlugin.getStaffManager().addUser(uuid, new StaffData(args[1], rank, age, motto.toString()));
                    executor.sendMessage("§2"+args[1]+" §awas added as a "+rank.getName());
                } else executor.sendMessage(staffPlugin.getConfiguration().couldNotFetchData);
            }));
        } else throw new WrongCommandArgumentUsageException("§cRank does not exist.");
    }

    public int getAge(String sAge) throws WrongCommandArgumentUsageException{
        try{
            int age = Integer.parseInt(sAge);
            return age;
        }catch(NumberFormatException e){
            throw new WrongCommandArgumentUsageException("§4"+sAge + " §ccannot be parsed as a number!");
        }
    }
}


