package dk.simonwither.staff.models;

public enum Query {
    INSERT_INTO_STAFF_DATA("INSERT INTO staff(uuid, username, age, description, rank) VALUES (?,?,?,?,?)");

    private final String SQL;

    Query(String SQL){
        this.SQL = SQL;
    }

    public String getSQL() {
        return SQL;
    }
}
