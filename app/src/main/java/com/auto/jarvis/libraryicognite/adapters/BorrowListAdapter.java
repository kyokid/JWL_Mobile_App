package com.auto.jarvis.libraryicognite.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.models.Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nguyen.D.Hoang on 1/16/2017.
 */

public class BorrowListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Book> mBooks;

    public BorrowListAdapter(List<Book> mBooks) {
        this.mBooks = mBooks;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);

        return new BookViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (null != holder) {
            BookViewHolder viewHolder = (BookViewHolder) holder;
            viewHolder.bind(mBooks.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvAuthor)
        TextView tvAuthor;


        BookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Book book) {
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
        }
    }
}
