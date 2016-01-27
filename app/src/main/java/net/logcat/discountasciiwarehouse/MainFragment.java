package net.logcat.discountasciiwarehouse;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;

import net.logcat.discountasciiwarehouse.api.models.Ascii;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.logcat.discountasciiwarehouse.AsciiApp.asciiService;

public class MainFragment extends Fragment {

    public static final int LANDSCAPE_SPAN_COUNT = 100;
    public static final int PORTRAIT_SPAN_COUNT = 50;
    public static final int WIDE_SPAN = 50;
    public static final int NARROW_SPAN = 25;

    private static final String TAG = MainFragment.class.getSimpleName();

    private Observable<Ascii> asciiObservable;
    private TextView measurementTextView;

    public MainFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        measurementTextView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.ascii_text, null, false);

        asciiObservable = asciiService().search(null, null, null, null)
                .flatMap(asciiSubject -> asciiSubject)
                .map(ascii -> {
                    if (ascii.size <= 0) {
                        ascii.size = getResources().getDimensionPixelSize(R.dimen.ascii_default_size);
                    }
                    return ascii;
                })
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
        final AsciiAdapter adapter = new AsciiAdapter();

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.asciis);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount());
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Ascii ascii = adapter.getItem(position);
                if (getTextWidth(ascii) <= 85) {
                    return NARROW_SPAN;
                } else {
                    return WIDE_SPAN;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new LandingAnimator());

        RecyclerViewHeader header = RecyclerViewHeader.fromXml(getActivity(), R.layout.search_box);
        header.attachTo(recyclerView);

        recyclerView.setAdapter(adapter);

        asciiObservable.subscribe(ascii -> {
            adapter.add(ascii);
            Log.d("TAG", "got ascii " + ascii.face + " and font size: " + ascii.size + " so width: " + getTextWidth(ascii));
        });
    }

    private float getTextWidth(Ascii ascii) {
        Paint paint = measurementTextView.getPaint();
        paint.setTextSize(ascii.size);
        return paint.measureText(ascii.face);
    }

    private int spanCount() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return LANDSCAPE_SPAN_COUNT;
        } else {
            return PORTRAIT_SPAN_COUNT;
        }
    }
}
