package dk.simonwither.staff.service;

import java.util.List;
import java.util.Map;

public class Configuration {
    public String host;
    public String username;
    public String password;
    public int port;
    public String databaseName;
    public Map<String, Integer> ranks;
    public List<String> wrongAddNewStaffCommandUsage;
    public List<String> UILoreConfiguration;
    public String UINameConfiguration;
    public List<String> wrongHelpUsage;
    public List<String> wrongRemoveStaffUsage;
    public String userNotStaffErrorMessage;
    public String userRemovedFromStaff;
    public boolean sendAutomaticallySavedMessage;
    public String automaticallySavedMessage;
    public String couldNotFetchData;

}
