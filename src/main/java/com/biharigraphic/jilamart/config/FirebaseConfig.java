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

<<<<<<< HEAD
            String path = "/etc/secrets/firebase-service-account.json";
            File file = new File(path);

            if (file.exists()) {
                serviceAccount = new FileInputStream(file);
                System.out.println("🔥 Firebase loaded from Render secrets");
            } else {
=======
            String renderPath = "/etc/secrets/firebase-service-account.json";
            File file = new File(renderPath);

            if (file.exists()) {
                // ✅ Production (Render)
                serviceAccount = new FileInputStream(file);
                System.out.println("🔥 Firebase loaded from Render secrets");
            } else {
                // ✅ Local (resources)
>>>>>>> 3817eeb (fix firebase production config)
                serviceAccount = getClass().getClassLoader()
                        .getResourceAsStream("firebase-service-account.json");

                if (serviceAccount == null) {
<<<<<<< HEAD
                    throw new RuntimeException("Firebase config not found");
                }

                System.out.println("🔥 Firebase loaded from local");
=======
                    throw new RuntimeException("❌ Firebase config file not found in resources");
                }

                System.out.println("🔥 Firebase loaded from local resources");
>>>>>>> 3817eeb (fix firebase production config)
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
<<<<<<< HEAD
            }

        } catch (Exception e) {
=======
                System.out.println("✅ Firebase initialized successfully");
            }

        } catch (Exception e) {
            System.err.println("❌ Firebase initialization failed: " + e.getMessage());
            e.printStackTrace();

            // ❗ Optional: app crash karwana hai ya nahi
>>>>>>> 3817eeb (fix firebase production config)
            throw new RuntimeException(e);
        }
    }
}
