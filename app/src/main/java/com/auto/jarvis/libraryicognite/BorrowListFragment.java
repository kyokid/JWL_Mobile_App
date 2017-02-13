package com.auto.jarvis.libraryicognite;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.auto.jarvis.libraryicognite.adapters.BorrowByDayAdapter;
import com.auto.jarvis.libraryicognite.models.Book;
import com.auto.jarvis.libraryicognite.models.BookByDay;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowListFragment extends Fragment {
    @BindView(R.id.rvBooks)
    RecyclerView rvBooks;
    public Unbinder unbinder;

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
//
        List<BookByDay> booksByDay = new ArrayList<>();
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return;
        } else {
            ArrayList<InformationBookBorrowed> borroweds = intent.getParcelableArrayListExtra("BORROW_LIST");
//
            List<Book> book1 = new ArrayList<>();
            for (InformationBookBorrowed bookBorrowed : borroweds) {
                book1.add(Book.fromBorrowedList(bookBorrowed));
            }
//        book1.add(new Book("Android", "Mark", 0));
//        book1.add(new Book("Android", "Mark", 0));
//        book1.add(new Book("Android", "Mark", 0));
//        book1.add(new Book("Android", "Mark", 0));
//        book1.add(new Book("Android", "Mark", 0));
//        book1.add(new Book("Android", "Mark", 0));
//        book1.add(new Book("Android", "Mark", 0));
            BookByDay bookbyDayItem1 = new BookByDay("02/14/2017", book1);
//        BookByDay bookbyDayItem2 = new BookByDay("02/01/2017", book1);
//
            booksByDay.add(bookbyDayItem1);
//        booksByDay.add(bookbyDayItem2);

            BorrowByDayAdapter adapter = new BorrowByDayAdapter(booksByDay);

            rvBooks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            rvBooks.setAdapter(adapter);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
