package com.google.sps.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Utility class for handling FirebaseApp initialisation.
 */
public class FirebaseHelper {
    /**
     * Initialises an instance of FirebaseApp.
     */
    public static void initialiseFirebaseApp() {
        if (FirebaseApp.getApps().isEmpty()) {
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
    }
}
