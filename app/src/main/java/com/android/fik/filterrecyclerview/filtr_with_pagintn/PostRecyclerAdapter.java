package com.android.fik.filterrecyclerview.filtr_with_pagintn;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.fik.filterrecyclerview.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class PostRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private ArrayList<PostItem> mPostItems;
    private static Context context;
    private String search = null;

    public PostRecyclerAdapter(ArrayList<PostItem> objects, Pagination activity) {
        this.mPostItems = objects;
        this.context = activity;
    }

    @NonNull @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.pagination_list_item, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.pagination_item_loading, parent, false));
            default:
                return null;
        }
    }

    //for textcolor change
    public void setDataSearch(String data) {
        this.search = data;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mPostItems.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mPostItems == null ? 0 : mPostItems.size();
    }

    public void addItems(List<PostItem> postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new PostItem());
        notifyItemInserted(mPostItems.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        PostItem item = getItem(position);
        if (item != null) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
    }

    PostItem getItem(int position) {
        return mPostItems.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        //@BindView(R.id.textViewTitle)
        TextView textViewTitle;
        //@BindView(R.id.textViewDescription)
        TextView textViewDescription;

        ViewHolder(View itemView) {
            super(itemView);
            //ButterKnife.bind(this, itemView);
            this.textViewTitle = (TextView)itemView.findViewById(R.id.textViewTitle);
            this.textViewDescription = (TextView)itemView.findViewById(R.id.textViewDescription);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            PostItem item = mPostItems.get(position);

            if(search != null) {
                textViewTitle.setText(highlight(search, item.getTitle()));
                textViewDescription.setText(highlight(search, item.getDescription()));
            }else if (search == null){
                textViewTitle.setText(item.getTitle());
                textViewDescription.setText(item.getDescription());
            }
            textViewTitle.setText(item.getTitle());
            textViewDescription.setText(item.getDescription());
        }
    }

    public class ProgressHolder extends BaseViewHolder {
        ProgressBar progress_bar;
        ProgressHolder(View itemView) {
            super(itemView);
            //ButterKnife.bind(this, itemView);
            this.progress_bar = (ProgressBar)itemView.findViewById(R.id.progress_bar);
        }

        @Override
        protected void clear() {
        }
    }

    //for filter
    public void filterList(ArrayList<PostItem> filterdNames) {
        this.mPostItems = filterdNames;
        notifyDataSetChanged();
    }

    //for change text color
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

