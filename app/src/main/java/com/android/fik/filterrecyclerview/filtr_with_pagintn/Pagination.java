package com.android.fik.filterrecyclerview.filtr_with_pagintn;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.android.fik.filterrecyclerview.R;

import java.util.ArrayList;

import static com.android.fik.filterrecyclerview.filtr_with_pagintn.PaginationListener.PAGE_START;

public class Pagination extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    EditText editTextSearchh;
    //@BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    //@BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private PostRecyclerAdapter adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    public ArrayList<PostItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagination_main);
        //ButterKnife.bind(this);

        editTextSearchh = (EditText)findViewById(R.id.editTextSearchh);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        swipeRefresh.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new PostRecyclerAdapter(items,Pagination.this);
        mRecyclerView.setAdapter(adapter);
        doApiCall();

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        mRecyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                doApiCall();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    /**
     * do api call here to fetch data from server
     * In example i'm adding data manually
     */
    private void doApiCall() {
        items = new ArrayList<>();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    itemCount++;
                    PostItem postItem = new PostItem();
                    postItem.setTitle(itemCount+"."+getString(R.string.text_title));
                    postItem.setDescription(getString(R.string.text_description));
                    items.add(postItem);
                }

                if (currentPage != PAGE_START) adapter.removeLoading();
                adapter.addItems(items);
                swipeRefresh.setRefreshing(false);

                // check weather is last page or not
                if (currentPage < totalPage) {
                    adapter.addLoading();

                    //for searching
                    //if (currentPage>0){
                      //  setAdapterData();
                    //}
                    //Log.d("items","items"+items.size());

                } else {
                    isLastPage = true;
                }
                isLoading = false;
            }
        }, 1500);
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        doApiCall();
    }

    public void setAdapterData(){

        editTextSearchh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("stringq","string1:===>"+charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                if (editable.toString().length()==0){
                    adapter = new PostRecyclerAdapter(items,Pagination.this);
                    mRecyclerView.setAdapter(adapter);
                }else {
                    filter(editable.toString());
                }

                //for text color change
                /*int textlength  = editable.length();
                if (textlength == 0) {
                    adapter.setDataSearch(null);
                } else {
                    String searchData = editable.toString().toLowerCase();
                    adapter.setDataSearch(searchData);
                }*/

            }
        });
        adapter = new PostRecyclerAdapter(items,Pagination.this);
        mRecyclerView.setAdapter(adapter);
    }


    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<PostItem> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (PostItem s : items) {
            //if the existing elements contains the search input
            if (s.getTitle().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }

}
