package com.auto.jarvis.libraryicognite.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.activities.DetailBookActivity;
import com.auto.jarvis.libraryicognite.models.Book;
import com.auto.jarvis.libraryicognite.models.output.BookAuthorDto;

import java.util.List;

import static android.R.attr.author;

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
            final ViewHolder viewHolder = holder;
            viewHolder.tvTitle.setText(mBooks.get(position).getTitle());
            List<BookAuthorDto> bookAuthorDtos = mBooks.get(position).getBookCopyBookBookAuthors();
            if (bookAuthorDtos != null && bookAuthorDtos.size() > 0){
                String authorStr = "";
                for (BookAuthorDto authorDto: bookAuthorDtos){
                    authorStr += authorDto.getAuthorName() + ", ";
                }
                authorStr = authorStr.substring(0, authorStr.length() - 2);
                viewHolder.tvAuthor.setText(authorStr);
            }else {
                viewHolder.tvAuthor.setText("Unknown");
            }
            if (!mBooks.get(position).isAvailable()){
                viewHolder.tvAvailable.setText("Unavailable");
                viewHolder.tvAvailable.setTextColor(ContextCompat.getColor(mContext, R.color.not_available));
                viewHolder.ivIsFavorite.setImageResource(R.drawable.ic_not_favorite);
                viewHolder.ivIsFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.ivIsFavorite.setImageResource(R.drawable.ic_favorite);
                    }
                });

            }else {
                viewHolder.ivIsFavorite.setVisibility(View.GONE);
                viewHolder.tvAvailable.setText("Available");
                viewHolder.tvAvailable.setTextColor(ContextCompat.getColor(mContext, R.color.avalable));
            }
        }
    }


    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvAvailable;
        TextView tvAuthor;
        ImageView ivIsFavorite;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvAvailable = (TextView) itemView.findViewById(R.id.tvAvalable);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            ivIsFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
        }
    }
}
