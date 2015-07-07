package net.logcat.discountasciiwarehouse;

import android.app.Application;

import net.logcat.discountasciiwarehouse.api.AsciiService;
import net.logcat.discountasciiwarehouse.api.NDJsonConverter;

import retrofit.RestAdapter;

public class AsciiApp extends Application {

    private static AsciiService asciiService;

    @Override
    public void onCreate() {
        super.onCreate();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AsciiService.ENDPOINT)
                .setConverter(new NDJsonConverter())
                .build();

        asciiService = restAdapter.create(AsciiService.class);
    }

    public static AsciiService asciiService() {
        return asciiService;
    }
}
