package com.android.fik.filterrecyclerview.filter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.fik.filterrecyclerview.R;

import java.text.Normalizer;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<String> names;
    private String search = null;
    private static Context context;

    public CustomAdapter(ArrayList<String> names, Context c) {
        this.names = names;
        this.context = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(v);
    }

    public void setDataSearch(String data) {
        this.search = data;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(search != null) {
            holder.textViewName.setText(highlight(search, names.get(position)));
        }else if (search == null){
            //holder.textViewName.setText(arrlist.get(position).getCountry_name());
            holder.textViewName.setText(names.get(position));
        }

        //holder.textViewName.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;

        ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
        }
    }

    public void filterList(ArrayList<String> filterdNames) {
        this.names = filterdNames;
        notifyDataSetChanged();
    }

    public static CharSequence highlight(String search, String originalText) {

        String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();

        int start = normalizedText.indexOf(search);

        Spannable highlighted = new SpannableString(originalText);
        if (start < 0) {
            // not found, nothing to to
            return originalText;
        } else {

            while (start >= 0) {

                int spanStart 	= Math.min(start, originalText.length());
                int spanEnd 	= Math.min(start + search.length(), originalText.length());

                highlighted.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                highlighted.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                start = normalizedText.indexOf(search, spanEnd);
            }

            return highlighted;
        }
    }


}
