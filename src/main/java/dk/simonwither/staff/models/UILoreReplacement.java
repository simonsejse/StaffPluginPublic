package dk.simonwither.staff.models;

import java.util.function.UnaryOperator;

public class UILoreReplacement implements UnaryOperator<String> {
    private final StaffData staffData;

    public UILoreReplacement(StaffData staffData){
        this.staffData = staffData;
    }

    @Override
    public String apply(String s) {
        System.out.println(s);
        System.out.println(staffData);
        System.out.println(staffData.getUsername());
        System.out.println(staffData.getDescription());

        return s.replace("{username}", staffData.getUsername())
                .replace("{age}", String.valueOf(staffData.getAge()))
                .replace("{rank}", staffData.getRank().getName())
                .replace("{desc}", staffData.getDescription());
    }
}
