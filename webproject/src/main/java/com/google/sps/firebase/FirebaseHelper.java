package com.google.sps.firebase;

import com.google.appengine.api.users.User;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.sps.authentication.AuthenticationHandler;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Utility class for handling FirebaseApp initialisation.
 */
public class FirebaseHelper {
    private static String userName;

    /**
     * Constructs an instance of the FirebaseHelper class.
     */
    public FirebaseHelper() {
        if (FirebaseApp.getApps().isEmpty()) {
            initialiseFirebaseApp();
        }
        setUserName();
    }

    /**
     * Initialises an instance of FirebaseApp.
     */
    public void initialiseFirebaseApp() {
        try {
            // Fetch the service account key JSON file contents
            FileInputStream serviceAccount = new FileInputStream("./key.json");

            // Initialize the app with a service account, granting admin privileges
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://summer20-sps-47.firebaseio.com")
                .build();

            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sets the userName as the current User's nickname.
     */
    public void setUserName() {
        AuthenticationHandler handler = new AuthenticationHandler();
        User curr = handler.getCurrentUser();
        userName = curr.getNickname();
    }

    /**
     * @return the current User's nickname.
     */
    public String getUserName() {
        return userName;
    }
}
