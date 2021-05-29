package dk.simonwither.staff.commands;

import java.net.URL;

public interface ResultCallback {
    void callback(boolean successful, String response, Exception exception, int responseCode);
}
