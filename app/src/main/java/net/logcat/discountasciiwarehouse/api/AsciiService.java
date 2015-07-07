package net.logcat.discountasciiwarehouse.api;

import net.logcat.discountasciiwarehouse.api.models.Ascii;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.subjects.PublishSubject;

public interface AsciiService {

    public static String ENDPOINT = "http://74.50.59.155:5000/api";

    @GET("/search")
    public Observable<PublishSubject<Ascii>> search(
            @Query("limit") Integer limit,
            @Query("skip") Integer skip,
            @Query("q") String query,
            @Query("onlyInStock") Boolean onlyInStock);


}
