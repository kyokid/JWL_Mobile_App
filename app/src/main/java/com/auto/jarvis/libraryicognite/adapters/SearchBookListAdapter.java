package com.auto.jarvis.libraryicognite.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.activities.DetailBookActivity;
import com.auto.jarvis.libraryicognite.models.Book;

import java.util.List;

/**
 * Created by thiendn on 21/03/2017.
 */

public class SearchBookListAdapter extends RecyclerView.Adapter<SearchBookListAdapter.ViewHolder> {
    List<Book> mBooks;
    Context mContext;

    public SearchBookListAdapter(Context context, List<Book> mBooks) {
        this.mBooks = mBooks;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_search, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder != null) {
            ViewHolder viewHolder = holder;
            viewHolder.tvTitle.setText(mBooks.get(position).getTitle());
        }
    }


    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
