package com.auto.jarvis.libraryicognite.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.adapters.SearchBookListAdapter;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.Book;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.auto.jarvis.libraryicognite.R.id.ivQrCode;

/**
 * Created by thiendn on 21/03/2017.
 */

public class SearchFragment extends Fragment {

    @BindView(R.id.rvBooks)
    RecyclerView rvBooks;
    @BindView(R.id.tvNoMatchingBook)
    TextView tvNoMatchingBook;
    public static final String SEARCH = "search";
    private String searchKey;
    private ApiInterface apiService;
    private List<Book> mBooks;
    private SearchBookListAdapter mAdapter;
    private String mUserId;

    public static SearchFragment newInstance(String searchKey) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH, searchKey);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        String searchKey = getArguments().getString(SEARCH);
        mUserId = SaveSharedPreference.getUsername(getContext());
        return view;
    }

    public void search(String searchKey, String userId) {
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RestService<List<Book>>> result = apiService.search(searchKey, userId);
        result.enqueue(new Callback<RestService<List<Book>>>() {
            @Override
            public void onResponse(Call<RestService<List<Book>>> call, Response<RestService<List<Book>>> response) {
                if (response.body().getData() != null && response.body().getData().size() > 0) {
                    tvNoMatchingBook.setVisibility(View.GONE);
                    mBooks = response.body().getData();
                    mAdapter = new SearchBookListAdapter(getContext(), mBooks, mUserId);
                    rvBooks.setAdapter(mAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                            LinearLayoutManager.VERTICAL, false);
                    rvBooks.setLayoutManager(layoutManager);
                } else {
                    tvNoMatchingBook.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<RestService<List<Book>>> call, Throwable t) {
                Toast.makeText(getContext(), "Fail to call search API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void resetView() {
        if (mBooks != null && mBooks.size() > 0) {
            mBooks.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

}
