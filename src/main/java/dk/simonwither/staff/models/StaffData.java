package dk.simonwither.staff.models;

public class StaffData {
    private String username;
    private Integer age;
    private String description;
    private Rank rank;

    public StaffData(String username, Rank rank, Integer age, String description) {
        this.username = username;
        this.age = age;
        this.description = description;
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }
}
