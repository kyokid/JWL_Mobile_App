package com.auto.jarvis.libraryicognite.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.adapters.BorrowListAdapter;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.Book;
import com.auto.jarvis.libraryicognite.models.BookByDay;
import com.auto.jarvis.libraryicognite.models.input.User;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecentBooksFragment extends Fragment {

    @BindView(R.id.rvBookRecent)
    RecyclerView rvBooks;

    public Unbinder unbinder;

    List<InformationBookBorrowed> listRecent;
    List<InformationBookBorrowed> multiSelectedBook = new ArrayList<>();


    public RecentBooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }


    private void initView() {
        Intent intentResult = getActivity().getIntent();
        if (intentResult != null) {
            listRecent = intentResult.getParcelableArrayListExtra("RECENT_LIST");
        }


        String username = SaveSharedPreference.getUsername(getActivity());
        List<Book> book1 = new ArrayList<>();
        if (listRecent != null &&listRecent.size() > 0) {
            for (InformationBookBorrowed bookBorrowed : listRecent) {
                book1.add(Book.fromBorrowedList(bookBorrowed));
            }
        }
        BorrowListAdapter adapter = new BorrowListAdapter(listRecent, getActivity(), multiSelectedBook);

        rvBooks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvBooks.setAdapter(adapter);


        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return;
        }

    }




}
