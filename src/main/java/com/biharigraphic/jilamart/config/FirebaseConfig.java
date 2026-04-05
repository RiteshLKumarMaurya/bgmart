package com.biharigraphic.jilamart.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {

            InputStream serviceAccount;

            String path = "/etc/secrets/firebase-service-account.json";
            File file = new File(path);

            if (file.exists()) {
                serviceAccount = new FileInputStream(file);
                System.out.println("🔥 Firebase loaded from Render secrets");
            } else {
                serviceAccount = getClass().getClassLoader()
                        .getResourceAsStream("firebase-service-account.json");

                if (serviceAccount == null) {
                    throw new RuntimeException("Firebase config not found");
                }

                System.out.println("🔥 Firebase loaded from local");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
