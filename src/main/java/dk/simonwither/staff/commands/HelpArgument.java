package dk.simonwither.staff.commands;

import dk.simonwither.staff.StaffPlugin;
import dk.simonwither.staff.menus.HelpMenu;
import dk.simonwither.staff.models.WrongCommandArgumentUsageException;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpArgument implements IStaffCommandArguments{

    private final StaffPlugin staffPlugin;

    public HelpArgument(StaffPlugin staffPlugin) {
        this.staffPlugin = staffPlugin;
    }

    @Override
    public String commandArgument() {
        return "help";
    }

    @Override
    public List<String> wrongCommandUsage() {
        return staffPlugin.getConfiguration().wrongHelpUsage;
    }

    @Override
    public void perform(Player executor, String... args) throws WrongCommandArgumentUsageException {
        if (args.length != 1) throw new WrongCommandArgumentUsageException();
        executor.openInventory(new HelpMenu(this.staffPlugin).initializeInventory(1));
    }
}
