package com.auto.jarvis.libraryicognite.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.models.BookByDay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Havh on 1/16/2017.
 */

public class BorrowByDayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<BookByDay> mBooks;

    public BorrowByDayAdapter(List<BookByDay> mBooks) {
        this.mBooks = mBooks;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_by_day_item, parent, false);
        return new BookByDayViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (null != holder) {
            BookByDayViewHolder viewHolder = (BookByDayViewHolder) holder;
            ((BookByDayViewHolder) holder).bind(mBooks.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class BookByDayViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.rvBookByDay)
        RecyclerView rvBookByDay;


        public BookByDayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(BookByDay bookByDay) {
            tvDate.setText(bookByDay.getBorrowedDate());

//            BorrowListAdapter adapter = new BorrowListAdapter(bookByDay.getBooks());
            rvBookByDay.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
//            rvBookByDay.setAdapter(adapter);
        }
    }


}
