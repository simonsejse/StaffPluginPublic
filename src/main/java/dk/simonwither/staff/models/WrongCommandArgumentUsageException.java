package dk.simonwither.staff.models;

public class WrongCommandArgumentUsageException extends Exception {
    public WrongCommandArgumentUsageException(){
        super();
    }
    public WrongCommandArgumentUsageException(String message){
        super(message);
    }
}
