package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.models.Book;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailBookActivity extends AppCompatActivity {

    @BindView(R.id.tvBookTitle)
    TextView tvBookTitle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        ButterKnife.bind(this);

        Book bookDetail = getIntent().getParcelableExtra("BOOK_DETAIL");
        initView(bookDetail);


        tvBookTitle.setText(bookDetail.getTitle());

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
