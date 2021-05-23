package dk.simonwither.staff.commands;

import dk.simonwither.staff.models.WrongCommandArgumentUsageException;
import org.bukkit.entity.Player;

import java.util.List;

public interface IStaffCommandArguments {
    String commandArgument();
    List<String> wrongCommandUsage();
    void perform(Player executor, String... args) throws WrongCommandArgumentUsageException;
}
