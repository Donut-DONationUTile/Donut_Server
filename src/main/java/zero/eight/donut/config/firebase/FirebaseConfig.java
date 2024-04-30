package zero.eight.donut.config.firebase;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.List;

@Configuration
public class FirebaseConfig {

    private static final String FIREBASE_KEY_PATH = "Donut-Server-yml/serviceAccountKey.json";
    private static final String GOOGLE_AUTH_URL = "https://www.googleapis.com/auth/cloud-platform";
    private final CredentialsProvider googleCredentials;

    public FirebaseConfig(CredentialsProvider googleCredentials) {
        this.googleCredentials = googleCredentials;
    }

    @PostConstruct
    public void init() {
        try {

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FIREBASE_KEY_PATH).getInputStream())
                    .createScoped(List.of(GOOGLE_AUTH_URL));

            googleCredentials.refreshIfExpired();

            FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                    .setCredentials(credentials)
                    .build();
            Firestore db = firestoreOptions.getService();

//            FileInputStream serviceAccount =
//                    new FileInputStream("Donut-Server-yml/serviceAccountKey.json");
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        return FirebaseMessaging.getInstance();
    }
}
