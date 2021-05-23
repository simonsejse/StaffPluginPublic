package dk.simonwither.staff.service;

import dk.simonwither.staff.models.StaffData;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class StaffManager {
    private final Map<UUID, StaffData> staffs;

    public StaffManager() {
        staffs = new LinkedHashMap<>();
    }

    public Map<UUID, StaffData> getStaffs() {
        return staffs;
    }

    public void addUser(final UUID uuid, final StaffData userData){
        this.staffs.put(uuid, userData);
    }
}
