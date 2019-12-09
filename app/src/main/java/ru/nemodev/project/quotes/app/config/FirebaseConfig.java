package ru.nemodev.project.quotes.app.config;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import ru.nemodev.project.quotes.R;


public final class FirebaseConfig {

    private static final FirebaseConfig instance = new FirebaseConfig();
    private final FirebaseRemoteConfig firebaseRemoteConfig;

    private FirebaseConfig() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaultsAsync(R.xml.firebase_config);
        firebaseRemoteConfig.fetchAndActivate();
    }

    public static FirebaseConfig getInstance() {
        return instance;
    }

    public static String getString(String key) {
        return instance.firebaseRemoteConfig.getString(key);
    }

    public static Long getLong(String key) {
        return instance.firebaseRemoteConfig.getLong(key);
    }

    public static Integer getInteger(String key) {
        return (int) instance.firebaseRemoteConfig.getLong(key);
    }
}
