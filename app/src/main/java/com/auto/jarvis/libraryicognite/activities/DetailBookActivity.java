package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.Book;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailBookActivity extends AppCompatActivity {

    @BindView(R.id.tvTitleBook)
    TextView tvBookTitle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.cardThumbnail)
    CardView cardThumbnail;

    @BindView(R.id.tvCategories)
    TextView tvCategories;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.tvNumberOfPages)
    TextView tvNumberOfPages;

    @BindView(R.id.tvPublished)
    TextView tvPublished;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.imgThumbnail)
    ImageView imgThumbnail;

    @BindView(R.id.btnRenew)
    Button btnRenew;

    ApiInterface apiService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        ButterKnife.bind(this);

        final Book bookDetail = getIntent().getParcelableExtra("BOOK_DETAIL");
        initView(bookDetail);


        tvBookTitle.setText(bookDetail.getTitle());
        tvDescription.setText(bookDetail.getDescription());
        tvNumberOfPages.setText(String.format("%d Pages", bookDetail.getNumberOfPages()));

        String publishYear = String.valueOf(bookDetail.getPublishYear());
        tvPublished.setText("Published: " + publishYear);
        String price = String.valueOf(bookDetail.getPrice());
        String thumbnail = bookDetail.getThumbnail();
        tvPrice.setText("Price: " + price);

        btnRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailBookActivity.this, "renew book: " + bookDetail.getRfidBook(), Toast.LENGTH_SHORT).show();
            }
        });

        if (thumbnail != null) {
            Picasso.with(DetailBookActivity.this)
                    .load(thumbnail)
                    .fit()
                    .into(imgThumbnail);
        }

    }

    private void initView(Book book) {
        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(book.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        apiService = ApiClient.getClient().create(ApiInterface.class);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
