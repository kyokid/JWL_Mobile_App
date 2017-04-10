package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.RxUltils;
import com.auto.jarvis.libraryicognite.adapters.BorrowListAdapter;
import com.auto.jarvis.libraryicognite.adapters.BorrowedBooksAdapter;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.rvBooks)
    RecyclerView rvBooks;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ApiInterface apiInterface;
    BorrowedBooksAdapter adapter;
    List<InformationBookBorrowed> listBorrowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        RxUltils.checkConnectToServer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isOnline -> {
                    if (!isOnline) {
                        Intent intent = new Intent(HistoryActivity.this, NoInternetActivity.class);
                        intent.putExtra("FROM", this.getClass().getCanonicalName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        initView();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initView() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.borrowed_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getBorrowedBooks(true);
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        listBorrowed = new ArrayList<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        adapter = new BorrowedBooksAdapter(listBorrowed, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());
        rvBooks.addItemDecoration(divider);
        rvBooks.setItemAnimator(new DefaultItemAnimator());
        rvBooks.setAdapter(adapter);
        getBorrowedBooks(false);
    }

    private void getBorrowedBooks(boolean isRefreshing) {
        String userId = SaveSharedPreference.getUsername(this);
        doGetBorrowedBooks(userId)
                .subscribe(listRestService -> {
                    swipeRefreshLayout.setRefreshing(false);
                    if (isRefreshing) {
                        adapter.clear();
                    }
                    List<InformationBookBorrowed> listBorrowed1 = listRestService.getData();
                    adapter.addAll(listBorrowed1);
                });
    }


    private Observable<RestService<List<InformationBookBorrowed>>> doGetBorrowedBooks(String userId) {
        return apiInterface.getBorrowedBooksHistory(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
