package com.auto.jarvis.libraryicognite;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.adapters.BorrowByDayAdapter;
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
public class BorrowListFragment extends Fragment {

    private static final String TAG = "onTest";
    @BindView(R.id.rvBooks)
    RecyclerView rvBooks;



    public Unbinder unbinder;

    List<InformationBookBorrowed> listBorrowed;
    ApiInterface apiService;

    public BorrowListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_borrow_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        apiService = ApiClient.getClient().create(ApiInterface.class);
        String username = SaveSharedPreference.getUsername(getActivity());
        User user = new User(username);
//        user.setUsername(username);
        Call<RestService<List<InformationBookBorrowed>>> getBorrowedBook = apiService.getBorrowedBook(user);
//        Log.d("borrow", getBorrowedBook.toString());

        getBorrowedBook.enqueue(new Callback<RestService<List<InformationBookBorrowed>>>() {
            @Override
            public void onResponse(Call<RestService<List<InformationBookBorrowed>>> call, Response<RestService<List<InformationBookBorrowed>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSucceed()) {
                        listBorrowed = response.body().getData();
                        List<Book> book1 = new ArrayList<>();
                        List<BookByDay> booksByDay = new ArrayList<>();
                        for (int i = 0; i < listBorrowed.size(); i++) {
                            if (TextUtils.isEmpty(listBorrowed.get(i).getReturnDate())) {
                                book1.add(Book.fromBorrowedList(listBorrowed.get(i)));
                            }
                        }
//
//                        BookByDay bookbyDayItem1 = new BookByDay("", book1);
//                        booksByDay.add(bookbyDayItem1);
                        BorrowListAdapter adapter = new BorrowListAdapter(book1);

                        rvBooks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        rvBooks.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<RestService<List<InformationBookBorrowed>>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "FAIL API", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
