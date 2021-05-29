package dk.simonwither.staff.commands;

import dk.simonwither.staff.StaffPlugin;
import dk.simonwither.staff.menus.StaffListMenu;
import dk.simonwither.staff.models.WrongCommandArgumentUsageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public class StaffCommand implements CommandExecutor {

    private StaffPlugin staffPlugin;
    private final IStaffCommandArguments[] iStaffCommandArguments;

    public StaffCommand(StaffPlugin staffPlugin) {
        this.staffPlugin = staffPlugin;
        this.iStaffCommandArguments = new IStaffCommandArguments[]{
                new AddNewStaffArgument(this.staffPlugin),
                new HelpArgument(this.staffPlugin),
                new RemoveStaffArgument(this.staffPlugin)
        };
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player executor = (Player) sender;
        if (args.length == 0){
            executor.openInventory(new StaffListMenu(this.staffPlugin).initializeInventory(1));
        }else{
            final Optional<IStaffCommandArguments> iStaffCommandArgumentsOptional = Arrays.stream(this.iStaffCommandArguments)
                    .filter(cmdArguments -> args[0].equalsIgnoreCase(cmdArguments.commandArgument()))
                    .findFirst();

            if (iStaffCommandArgumentsOptional.isPresent()){
                final IStaffCommandArguments iStaffCommandArguments = iStaffCommandArgumentsOptional.get();
                try{
                    iStaffCommandArguments.perform(executor, args);
                }catch(WrongCommandArgumentUsageException wrongCommandArgumentUsageException){
                    if (wrongCommandArgumentUsageException.getMessage() == null){
                        iStaffCommandArguments.wrongCommandUsage().stream().forEach(executor::sendMessage);
                    }else executor.sendMessage(wrongCommandArgumentUsageException.getMessage());
                }
            } else executor.sendMessage("Argument does not exist.");
        }
        return true;
    }
}
