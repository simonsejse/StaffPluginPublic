package dk.simonwither.staff.commands;

import dk.simonwither.staff.StaffPlugin;
import dk.simonwither.staff.models.Rank;
import dk.simonwither.staff.models.StaffData;
import dk.simonwither.staff.models.WrongCommandArgumentUsageException;
import org.bukkit.entity.Player;

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

            IntStream.range(4, args.length).forEach(n -> motto.append(args[n]));

            staffPlugin.getStaffManager().addUser(UUID.randomUUID(), new StaffData(args[1], rank, age, motto.toString()));
            executor.sendMessage("§2"+args[1]+" §awas added as a "+rank.getName());
        } else throw new WrongCommandArgumentUsageException("§cRank does not exist.");
    }

    public int getAge(String sAge){
        try{
            int age = Integer.parseInt(sAge);
            return age;
        }catch(NumberFormatException e){
            return 99;
        }
    }
}


