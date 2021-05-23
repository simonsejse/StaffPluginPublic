package dk.simonwither.staff.commands;

import dk.simonwither.staff.StaffPlugin;
import dk.simonwither.staff.models.Rank;
import dk.simonwither.staff.models.StaffData;
import dk.simonwither.staff.models.WrongCommandArgumentUsageException;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.UUID;

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

    private Rank[] randomRank = new Rank[]{new Rank("Owner", 1), new Rank("Admin", 2), new Rank("Mod", 3), new Rank("Helper", 4)};

    @Override
    public void perform(Player executor, String... args) throws WrongCommandArgumentUsageException {
        if (args.length < 5) throw new WrongCommandArgumentUsageException();
        if (this.staffPlugin.getConfiguration().ranks.keySet().stream().filter(s -> s.equalsIgnoreCase(args[2])).findAny().isPresent()){
            for(int i = 0; i < 100; i ++){
                staffPlugin.getStaffManager().addUser(UUID.randomUUID(), new StaffData(String.valueOf(i), 20, "dsa", randomRank[new Random().nextInt(3)]));
            }
        } else throw new WrongCommandArgumentUsageException("§cRank does not exist.");
        //TUDO: make other checks, for example checking if ranks exist...
    }
}
