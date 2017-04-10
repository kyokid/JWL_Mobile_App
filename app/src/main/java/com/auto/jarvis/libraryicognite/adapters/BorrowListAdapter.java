package com.auto.jarvis.libraryicognite.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.ConvertUtils;
import com.auto.jarvis.libraryicognite.fragments.BorrowListFragment;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by HaVH on 1/16/2017.
 */

public class BorrowListAdapter extends RecyclerView.Adapter<BorrowListAdapter.BookViewHolder> {

    public List<InformationBookBorrowed> mBooks;
    public List<InformationBookBorrowed> booksSelected = new ArrayList<>();
    Context context;

    ApiInterface apiInterface;
    String strCurrentDate;
    Date currentDate;
    Long distanceDate;



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
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Observable<RestService<String>> currentDateServer = apiInterface.getCurrentDate();
        currentDateServer.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<RestService<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RestService<String> stringRestService) {
                        Log.d("LIFE", "NEXT");
                        strCurrentDate = stringRestService.getData();
                        if (strCurrentDate != null) {
                            currentDate = ConvertUtils.convertStringtoDate(strCurrentDate);
                            Log.d("DATE-CONVERT", currentDate.toString());
                            Date deadLine = ConvertUtils.convertStringtoDate(book.getDeadlineDate());
                            boolean isAfter = deadLine.after(currentDate);
                            if (!isAfter) {
                                mBooks.get(position).setDeadline(true);
                                distanceDate = (currentDate.getTime() - deadLine.getTime()) / 86400000;
                                holder.tvDeadLine.setTextColor(holder.itemView.getResources().getColor(R.color.colorLateDeadline));
                                if (distanceDate == 0L) {
                                    holder.overdue.setText("Hôm nay là ngày trả sách");
                                } else {
                                    holder.overdue.setText("Quá hạn " + distanceDate + " ngày");
                                }
                                holder.overdue.setVisibility(View.VISIBLE);
                            } else  {
                                distanceDate = (deadLine.getTime() - currentDate.getTime()) / 86400000;
                                Log.d("DATE-CONVERT", "overdude: " + distanceDate);
                                if (distanceDate <= 3L) {
                                    mBooks.get(position).setDeadline(true);
                                    holder.tvDeadLine.setTextColor(holder.itemView.getResources().getColor(R.color.colorLateDeadline));
                                    holder.overdue.setText("Còn " + distanceDate + " ngày là phải trả sách");
                                    holder.overdue.setVisibility(View.VISIBLE);
                                } else {
                                    holder.overdue.setVisibility(View.GONE);
                                    holder.tvDeadLine.setTextColor(holder.itemView.getResources().getColor(R.color.cardview_dark_background));
                                }
                            }
                        }
                        String deadline = ConvertUtils.formateDate(book.getDeadlineDate());
                        holder.tvDeadLine.setText(deadline);

//                        if (position == mBooks.size() - 1) {
//                            holder.view.setVisibility(View.GONE);
//                        }
                    }
                });



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
//        @BindView(R.id.viewSeparate)
//        View view;
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
        this.notifyDataSetChanged();
    }

    public void addAll(List<InformationBookBorrowed> books) {
        mBooks.addAll(books);
        this.notifyDataSetChanged();
    }

}
