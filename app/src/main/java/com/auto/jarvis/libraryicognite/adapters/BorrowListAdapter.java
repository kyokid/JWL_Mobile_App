package com.auto.jarvis.libraryicognite.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.ConvertUtils;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.Book;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.rest.ApiClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HaVH on 1/16/2017.
 */

public class BorrowListAdapter extends RecyclerView.Adapter<BorrowListAdapter.BookViewHolder> {

    public List<InformationBookBorrowed> mBooks;
    public List<InformationBookBorrowed> booksSelected = new ArrayList<>();
    Context context;



    public BorrowListAdapter(List<InformationBookBorrowed> mBooks, Context context, List<InformationBookBorrowed> booksSelected) {
        this.context = context;
        this.mBooks = mBooks;
        this.booksSelected = booksSelected;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);

        return new BookViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        InformationBookBorrowed book = mBooks.get(position);
        holder.tvTitle.setText(String.valueOf(position + 1) + ". " + book.getBookTitle());

        //Todo get date from server
        Date currentDate = new Date();
        Log.d("DATE", currentDate.toString());
        Date deadLine = ConvertUtils.convertStringtoDate(book.getDeadlineDate());
        boolean result = deadLine.after(currentDate);

        String deadline = ConvertUtils.formateDate(book.getDeadlineDate());

        holder.tvDeadLine.setText(deadline);
        if (!result) {
            holder.tvDeadLine.setTextColor(holder.itemView.getResources().getColor(R.color.colorLateDeadline));
            holder.overdue.setVisibility(View.VISIBLE);
        }
        if (position == mBooks.size() - 1) {
            holder.view.setVisibility(View.GONE);
        }

        if (booksSelected.contains(mBooks.get(position))) {
            holder.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
        } else {
            holder.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_normal_state));
        }
    }




    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDeadline)
        TextView tvDeadLine;
        @BindView(R.id.viewSeparate)
        View view;
        @BindView(R.id.tvOverdue)
        TextView overdue;

        @BindView(R.id.llItem)
        LinearLayout llItem;


        BookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public void clear() {
        mBooks.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<InformationBookBorrowed> books) {
        mBooks.addAll(books);
        notifyDataSetChanged();
    }



}
