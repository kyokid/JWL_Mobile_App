package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.models.Book;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailBookActivity extends AppCompatActivity {

    @BindView(R.id.tvTitleBook)
    TextView tvBookTitle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imgThumbnail)
    CardView imgThumbnail;

    @BindView(R.id.tvCategories)
    TextView tvCategories;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.tvNumberOfPages)
    TextView tvNumberOfPages;

    @BindView(R.id.tvPublished)
    TextView tvPublished;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        ButterKnife.bind(this);

        Book bookDetail = getIntent().getParcelableExtra("BOOK_DETAIL");
        initView(bookDetail);


        tvBookTitle.setText(bookDetail.getTitle());
        tvDescription.setText(bookDetail.getDescription());
        tvNumberOfPages.setText(bookDetail.getNumberOfPages() + " Pages");

        String publishYear = String.valueOf(bookDetail.getPublishYear());
        tvPublished.setText(publishYear);
    }

    private void initView(Book book) {
        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(book.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
