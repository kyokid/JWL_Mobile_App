package com.auto.jarvis.libraryicognite.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.ConvertUtils;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HaVH on 3/31/17.
 */

public class BorrowedBooksAdapter extends RecyclerView.Adapter<BorrowedBooksAdapter.BookViewHolder>{


    public List<InformationBookBorrowed> mBooks;
    Context context;


    public BorrowedBooksAdapter(List<InformationBookBorrowed> mBooks, Context context) {
        this.context = context;
        this.mBooks = mBooks;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new BorrowedBooksAdapter.BookViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        InformationBookBorrowed book = mBooks.get(position);
        holder.tvTitle.setText(String.valueOf(position + 1) + ". " + book.getBookTitle());
        String returnDate = ConvertUtils.formateDate(book.getReturnDate());
        holder.tvReturnDate.setText(returnDate);
        int bookStatus = Math.abs(book.getBookStatus());
        if (bookStatus == 0) {
            holder.tvStatus.setText("Trạng thái: Tốt");
            holder.tvStatus.setTextColor(holder.itemView.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.tvStatus.setText("Trạng thái: quá hạn "+ bookStatus + " ngày");
            holder.tvStatus.setTextColor(holder.itemView.getResources().getColor(R.color.colorLateDeadline));
        }
        if (position == mBooks.size() - 1) {
            holder.view.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvReturnDate)
        TextView tvReturnDate;
        @BindView(R.id.viewSeparate)
        View view;
        @BindView(R.id.tvStatus)
        TextView tvStatus;



        BookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public void clear() {
        mBooks.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(List<InformationBookBorrowed> books) {
        mBooks.addAll(books);
        this.notifyDataSetChanged();
    }

}
