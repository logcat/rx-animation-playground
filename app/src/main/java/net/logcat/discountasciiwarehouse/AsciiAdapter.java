package net.logcat.discountasciiwarehouse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.logcat.discountasciiwarehouse.api.models.Ascii;

import java.util.ArrayList;
import java.util.LinkedList;

public class AsciiAdapter extends RecyclerView.Adapter<AsciiAdapter.ViewHolder> {

    private ArrayList<Ascii> asciis = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView asciiTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            asciiTextView = (TextView) itemView.findViewById(R.id.ascii);
        }
    }

    public void add(Ascii ascii) {
        asciis.add(ascii);
        notifyItemInserted(ascii.size - 1);
    }

    @Override
    public AsciiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ascii_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ascii ascii = asciis.get(position);
        holder.asciiTextView.setText(ascii.face);
        holder.asciiTextView.setTextSize(ascii.size);
    }

    @Override
    public int getItemCount() {
        return asciis.size();
    }

    public Ascii getItem(int position) {
        return asciis.get(position);
    }
}
