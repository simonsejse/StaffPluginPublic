package dk.simonwither.staff.models;

public enum SQLConstants {
    INSERT_INTO_STAFF_DATA("insert into staff (uuid, username, age, description, rank) values (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE uuid=?");

    private final String SQL;

    SQLConstants(String SQL){
        this.SQL = SQL;
    }

    public String getSQL() {
        return SQL;
    }
}
