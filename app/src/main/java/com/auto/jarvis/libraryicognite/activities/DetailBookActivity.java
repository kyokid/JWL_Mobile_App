package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.Book;
import com.auto.jarvis.libraryicognite.models.output.BookAuthorDto;
import com.auto.jarvis.libraryicognite.models.output.BookCategoryDto;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @BindView(R.id.tvAuthor)
    TextView tvAuthor;

    @BindView(R.id.tvPublishedYear)
    TextView tvPublishedYear;

    @BindView(R.id.tvDuration)
    TextView tvDuration;

    @BindView(R.id.pgLoadingRenew)
    ProgressBar pgLoadingRenew;

    ApiInterface apiService;
    ArrayList<BookAuthorDto> authorDtos;
    ArrayList<BookCategoryDto> bookCategoryDtos;

    String rfid, messageRenew;
    MaterialDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        ButterKnife.bind(this);

        final Book bookDetail = getIntent().getExtras().getParcelable("BOOK_DETAIL");
        rfid = bookDetail.getRfidBook();
        initView(bookDetail);

        StringBuilder categories = new StringBuilder();
        StringBuilder author = new StringBuilder();
        tvBookTitle.setText(bookDetail.getTitle());
        tvDescription.setText(bookDetail.getDescription());
        tvNumberOfPages.setText(String.format("Số trang: %d", bookDetail.getNumberOfPages()));

        tvPublished.setText("Nhà xuất bản: " + bookDetail.getPublisher());
        String price = String.valueOf(bookDetail.getPrice());
        String thumbnail = bookDetail.getThumbnail();
        tvPrice.setText("Giá 1 quyển: " + price + "$");
        if (bookDetail.getBookCopyBookBookAuthors() != null) {
            authorDtos = bookDetail.getBookCopyBookBookAuthors();
            for (int i = 1; i <= authorDtos.size(); i++) {
                if (i != authorDtos.size()) {
                    author.append(authorDtos.get(i - 1).getAuthorName()).append(", ");
                } else {
                    author.append(authorDtos.get(i - 1).getAuthorName());
                }
            }
        }

        if (bookDetail.getBookCopyBookBookCategories() != null && bookDetail.getBookCopyBookBookCategories().size() > 0) {
            bookCategoryDtos = bookDetail.getBookCopyBookBookCategories();
            for (int i = 1; i <= bookCategoryDtos.size(); i++) {
                if (i != bookCategoryDtos.size()) {
                    categories.append(bookCategoryDtos.get(i - 1).getCategoryName()).append("/");
                } else {
                    categories.append(bookCategoryDtos.get(i - 1).getCategoryName());
                }
            }
        }

        String strBorrowedDate = formateDate(bookDetail.getBorrowedDate());
        String strDeadlineDate = formateDate(bookDetail.getDeadLine());
//        try {
//            SimpleDateFormat formatSource = new SimpleDateFormat("yyyy-MM-dd");
//
//            Date dateDL = formatSource.parse(strDeadlineDate);
//            Date dateBR = formatSource.parse(strBorrowedDate);
//            SimpleDateFormat formatDestination = new SimpleDateFormat("dd/MM/yyyy");
//
//            strBorrowedDate = formatDestination.format(dateBR);
//            strDeadlineDate = formatDestination.format(dateDL);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        tvCategories.setText(categories);
        tvAuthor.setText("Tác giả: " + author);
        tvPublishedYear.setText("Năm xuất bản: " + bookDetail.getPublishYear());
        tvDuration.setText(strBorrowedDate + " - " + strDeadlineDate);


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

        btnRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog
                showDialogReNew();
            }
        });


    }

    private void showDialogReNew() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Gia hạn sách")
                .content("Bạn muốn gia hạn sách này?")
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        renewBook();

                    }
                })
                .negativeText("Cancel");

        dialog = builder.build();
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void renewBook() {
        Call<RestService<InformationBookBorrowed>> renew = apiService.renewBorrowedBook(rfid);
        renew.enqueue(new Callback<RestService<InformationBookBorrowed>>() {
            @Override
            public void onResponse(Call<RestService<InformationBookBorrowed>> call, Response<RestService<InformationBookBorrowed>> response) {
                messageRenew = response.body().getTextMessage();
                showDialogFinish(messageRenew);
                if (!response.body().getCode().equals("400")) {
                    InformationBookBorrowed newBook = response.body().getData();
                    String strBorrowedDate = formateDate(newBook.getBorrowedDate());
                    String strDeadlineDate = formateDate(newBook.getDeadlineDate());
                    String duration = strBorrowedDate + " - " + strDeadlineDate;
                    tvDuration.setText(duration);
                }
            }

            @Override
            public void onFailure(Call<RestService<InformationBookBorrowed>> call, Throwable t) {

            }
        });
    }

    private String formateDate(String strSource) {
        String strDestination = "";
        try {
            SimpleDateFormat formatSource = new SimpleDateFormat("yyyy-MM-dd");

            Date date = formatSource.parse(strSource);
            SimpleDateFormat formatDestination = new SimpleDateFormat("dd/MM/yyyy");

            return strDestination = formatDestination.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDestination;
    }

    private void showDialogFinish(String message) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Gia hạn sách")
                .content(message)
                .positiveText("OK");
        dialog = builder.build();
        dialog.show();
    }


}
