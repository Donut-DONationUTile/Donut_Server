package zero.eight.donut.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {
    String filepath = System.getenv("FIREBASE_CONFIG_FILE");
    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(filepath != null ? filepath : "src/main/resources/donut-efc32-firebase-adminsdk-et3f5-19da7f3f00.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Bean
    public FirebaseMessaging firebaseMessaging() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(filepath != null ? filepath : "src/main/resources/donut-efc32-firebase-adminsdk-et3f5-19da7f3f00.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) { // FirebaseApp의 중복 초기화 방지.
                FirebaseApp.initializeApp(options);
            }
            return FirebaseMessaging.getInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Firebase Messaging 서비스를 초기화하는 동안 문제가 발생했습니다.", e);
        }
    }
}