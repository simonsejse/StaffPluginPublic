package dk.simonwither.staff.commands;

import com.google.gson.JsonObject;
import dk.simonwither.staff.StaffPlugin;
import dk.simonwither.staff.models.WrongCommandArgumentUsageException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class RemoveStaffArgument implements IStaffCommandArguments {
    private final StaffPlugin staffPlugin;

    public RemoveStaffArgument(StaffPlugin staffPlugin) {
        this.staffPlugin = staffPlugin;
    }

    @Override
    public String commandArgument() {
        return "remove";
    }

    @Override
    public List<String> wrongCommandUsage() {
        return staffPlugin.getConfiguration().wrongRemoveStaffUsage;
    }

    @Override
    public void perform(Player executor, String... args) throws WrongCommandArgumentUsageException {
        if (args.length != 2) throw new WrongCommandArgumentUsageException();
        this.staffPlugin.getStaffManager().makeAsyncGetRequestForApi(args[1], ((successful, response, exception, responseCode) -> {
            if (exception == null && response.startsWith("{")){
                JSONObject jsonObject = new JSONObject(response);
                final UUID uuid = UUID.fromString(jsonObject.getString("id")
                        .replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));

                if (this.staffPlugin.getStaffManager().getStaffs().containsKey(uuid)){
                    executor.sendMessage(staffPlugin.getConfiguration().userRemovedFromStaff.replace("{user}", args[1]));
                    this.staffPlugin.getStaffManager().removeUser(uuid);
                }else executor.sendMessage(staffPlugin.getConfiguration().userNotStaffErrorMessage);
            }else executor.sendMessage(staffPlugin.getConfiguration().couldNotFetchData);
        }));
    }
}
