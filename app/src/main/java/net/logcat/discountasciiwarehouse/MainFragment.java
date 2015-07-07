package net.logcat.discountasciiwarehouse;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;

import net.logcat.discountasciiwarehouse.api.models.Ascii;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.logcat.discountasciiwarehouse.AsciiApp.asciiService;

public class MainFragment extends Fragment {

    private Observable<Ascii> asciiObservable;

    public MainFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asciiObservable = asciiService().search(null, null, null, null)
                .flatMap(asciiSubject -> asciiSubject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).cache();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.asciis);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new LandingAnimator());

        RecyclerViewHeader header = RecyclerViewHeader.fromXml(getActivity(), R.layout.search_box);
        header.attachTo(recyclerView);

        AsciiAdapter adapter = new AsciiAdapter();
        recyclerView.setAdapter(adapter);

        asciiObservable.subscribe(ascii -> {
            adapter.add(ascii);
            Log.d("TAG", "got ascii " + ascii.face);
        });
    }
}
