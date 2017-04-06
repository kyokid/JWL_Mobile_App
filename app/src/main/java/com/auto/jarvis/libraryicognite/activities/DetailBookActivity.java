package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.ConvertUtils;
import com.auto.jarvis.libraryicognite.Utils.RxUltils;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.BookAuthorDto;
import com.auto.jarvis.libraryicognite.models.output.BookCategoryDto;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.auto.jarvis.libraryicognite.Utils.ConvertUtils.convertCurrency;

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

    @BindView(R.id.rlBackground)
    RelativeLayout rlBackground;

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

        RxUltils.checkConnectToServer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isOnline -> {
                    if (!isOnline) {
                        Intent intent = new Intent(DetailBookActivity.this, NoInternetActivity.class);
                        intent.putExtra("FROM", this.getClass().getCanonicalName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        final InformationBookBorrowed bookDetail = getIntent().getExtras().getParcelable("BOOK_DETAIL");
                        initView(bookDetail);
                    }
                });

        final InformationBookBorrowed bookDetail = getIntent().getExtras().getParcelable("BOOK_DETAIL");
        rfid = bookDetail.getBookCopyRfid();
        initView(bookDetail);


    }

    private void initView(InformationBookBorrowed bookDetail) {
        rfid = bookDetail.getBookCopyRfid();
        StringBuilder categories = new StringBuilder();
        StringBuilder author = new StringBuilder();
        tvBookTitle.setText(bookDetail.getBookTitle());
        tvDescription.setText(bookDetail.getDescription());
        tvNumberOfPages.setText(String.format("Số trang: %d", bookDetail.getNumberOfPages()));

        tvPublished.setText(String.format("Nhà xuất bản: %s" , bookDetail.getPublisher()));

        String price = convertCurrency(bookDetail.getPrice());
        String thumbnail = bookDetail.getThumbnail();
        tvPrice.setText("Giá 1 quyển: " + price);
        if (bookDetail.getAuthors() != null) {
            authorDtos = bookDetail.getAuthors();
            for (int i = 1; i <= authorDtos.size(); i++) {
                if (i != authorDtos.size()) {
                    author.append(authorDtos.get(i - 1).getAuthorName()).append(", ");
                } else {
                    author.append(authorDtos.get(i - 1).getAuthorName());
                }
            }
        }

        if (bookDetail.getCategories() != null && bookDetail.getCategories().size() > 0) {
            bookCategoryDtos = bookDetail.getCategories();
            for (int i = 1; i <= bookCategoryDtos.size(); i++) {
                if (i != bookCategoryDtos.size()) {
                    categories.append(bookCategoryDtos.get(i - 1).getCategoryName()).append("/");
                } else {
                    categories.append(bookCategoryDtos.get(i - 1).getCategoryName());
                }
            }
        }

        String strBorrowedDate = formateDate(bookDetail.getBorrowedDate());
        String strDeadlineDate = formateDate(bookDetail.getDeadlineDate());


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



        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(bookDetail.getBookTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        String dl = formateDate(bookDetail.getDeadlineDate());
        Date deadline = ConvertUtils.convertStringtoDate(bookDetail.getDeadlineDate());
        Long a = deadline.getTime() + (bookDetail.getDaysPerExtend() * 86400000);
        Date b = new Date(a);
        SimpleDateFormat dfx = new SimpleDateFormat("dd/MM/yyyy");
        String c = dfx.format(b);
        Log.d("aaa", "ngay gia han : " + c);

        int statusOfBook = Math.abs(bookDetail.getBookStatus());

        if (!bookDetail.isDeadline() || statusOfBook > bookDetail.getLateDaysLimit()) {
            btnRenew.setEnabled(false);
        }

        btnRenew.setOnClickListener(v -> {
            //show dialog
            showDialogReNew(dl, c);
        });


    }

    private void showDialogReNew(String deadlineDate, String newDeadline) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Gia hạn sách")
                .content("Sách này sẽ được gia hạn từ ngày " + deadlineDate + " đến ngày " + newDeadline)
                .positiveText("OK")
                .onPositive((dialog1, which) -> renewBook())
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
                    btnRenew.setEnabled(false);
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
