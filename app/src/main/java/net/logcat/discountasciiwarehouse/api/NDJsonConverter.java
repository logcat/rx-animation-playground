package net.logcat.discountasciiwarehouse.api;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class NDJsonConverter implements Converter {

    private final Gson gson = new Gson();

    @Override
    public Object fromBody(final TypedInput body, final Type type) throws ConversionException {
        final PublishSubject observable = PublishSubject.create();
        Schedulers.io().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(body.in()));
                    Type[] typeArgumetns = ((ParameterizedType) type).getActualTypeArguments();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Object result = gson.fromJson(line, typeArgumetns[0]);
                        observable.onNext(result);
                    }
                    observable.onCompleted();
                } catch (Exception e) {
                    observable.onError(e);
                }
            }
        });
        return observable;
    }

    @Override
    public TypedOutput toBody(Object object) {
        throw new RuntimeException("not implemented");
    }
}
