package com.auto.jarvis.libraryicognite.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.activities.DetailBookActivity;
import com.auto.jarvis.libraryicognite.adapters.BorrowListAdapter;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.listeners.RecyclerItemClickListener;
import com.auto.jarvis.libraryicognite.models.input.User;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.auto.jarvis.libraryicognite.Utils.ConvertUtils.formateDate;


/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowListFragment extends Fragment {

    private static final String TAG = "onTest";
    @BindView(R.id.rvBooks)
    RecyclerView rvBooks;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;


    public Unbinder unbinder;

    List<InformationBookBorrowed> listBorrowed;
    List<InformationBookBorrowed> multiSelectedBook = new ArrayList<>();
    ApiInterface apiService;

    BorrowListAdapter adapter;

    boolean isMultiSelect = false;
    ActionMode actionMode;
    Menu context_menu;

    MaterialDialog dialog;
    String rfid, messageRenew;

    public BorrowListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_borrow_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {

        apiService = ApiClient.getClient().create(ApiInterface.class);
        //pull to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBorrowedBook(true);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getBorrowedBook(false);

        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getBorrowedBook(final boolean isRefreshing) {

        String username = SaveSharedPreference.getUsername(getActivity());
        User user = new User(username);
//        user.setUsername(username);
        Call<RestService<List<InformationBookBorrowed>>> getBorrowedBook = apiService.getBorrowedBook(user);
//        Log.d("borrow", getBorrowedBook.toString());

        getBorrowedBook.enqueue(new Callback<RestService<List<InformationBookBorrowed>>>() {
            @Override
            public void onResponse(Call<RestService<List<InformationBookBorrowed>>> call, Response<RestService<List<InformationBookBorrowed>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSucceed()) {
                        listBorrowed = response.body().getData();
//                        List<InformationBookBorrowed> book1 = new ArrayList<>();
//                        for (int i = 0; i < listBorrowed.size(); i++) {
//                            if (TextUtils.isEmpty(listBorrowed.get(i).getReturnDate())) {
//                                book1.add(Book.fromBorrowedList(listBorrowed.get(i)));
//                            }
//                        }
                        if (!isRefreshing) {
                            adapter = new BorrowListAdapter(listBorrowed, getActivity(), multiSelectedBook);
                        } else {
                            onLoadItemsComplete(listBorrowed);
                        }

                        rvBooks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        rvBooks.setItemAnimator(new DefaultItemAnimator());
                        rvBooks.setAdapter(adapter);

                        rvBooks.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvBooks, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Log.d("new_adapter", "one click");
                                if (isMultiSelect) {
                                    multiSelect(position);
                                } else {
                                    Intent detailIntent = new Intent(getContext(), DetailBookActivity.class);
                                    InformationBookBorrowed bookDetail = listBorrowed.get(position);
                                    detailIntent.putExtra("BOOK_DETAIL", bookDetail);
                                    getContext().startActivity(detailIntent);
                                }
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                Log.d("new_adapter", "long click");
                                if (!isMultiSelect) {
                                    multiSelectedBook = new ArrayList<InformationBookBorrowed>();
                                    isMultiSelect = true;

                                    if (actionMode == null) {
                                        actionMode = getActivity().startActionMode(mActionModeCallback);
                                    }
                                }

                                multiSelect(position);

                            }
                        }));

                    }
                }
            }

            @Override
            public void onFailure(Call<RestService<List<InformationBookBorrowed>>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "FAIL API", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void onLoadItemsComplete(List<InformationBookBorrowed> books) {
        adapter.clear();
        adapter.addAll(books);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void multiSelect(int position) {
        if (actionMode != null) {
            if (multiSelectedBook.contains(listBorrowed.get(position))) {
                multiSelectedBook.remove(listBorrowed.get(position));
            } else {
                multiSelectedBook.add(listBorrowed.get(position));
            }

            if (multiSelectedBook.size() > 0) {
                actionMode.setTitle("Selected " + multiSelectedBook.size() + " book(s)");
            } else {
                actionMode.finish();
            }

            refreshAdapter();
        }
    }

    private void refreshAdapter() {
        adapter.booksSelected = multiSelectedBook;
        adapter.mBooks = listBorrowed;
        adapter.notifyDataSetChanged();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_renew:
                    showDialogRenew();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            isMultiSelect = false;
            multiSelectedBook = new ArrayList<>();
            refreshAdapter();
        }
    };

    private void showDialogRenew() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title("Gia hạn sách")
                .content("Bạn muốn gia hạn những sách này?")
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

    private void renewBook() {
        for (InformationBookBorrowed book :
                multiSelectedBook) {
            rfid = book.getBookCopyRfid();

            Call<RestService<InformationBookBorrowed>> renew = apiService.renewBorrowedBook(rfid);
            renew.enqueue(new Callback<RestService<InformationBookBorrowed>>() {
                @Override
                public void onResponse(Call<RestService<InformationBookBorrowed>> call, Response<RestService<InformationBookBorrowed>> response) {
                    messageRenew = response.body().getTextMessage();
                    showDialogFinish(messageRenew);
                    if (!response.body().getCode().equals("400")) {
                        InformationBookBorrowed newBook = response.body().getData();
                        String strDeadlineDate = formateDate(newBook.getDeadlineDate());

                    }
                }

                @Override
                public void onFailure(Call<RestService<InformationBookBorrowed>> call, Throwable t) {

                }
            });
        }
    }

    private void showDialogFinish(String message) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title("Gia hạn sách")
                .content(message)
                .positiveText("OK");
        dialog = builder.build();
        dialog.show();
    }

    private void showDialogContinue(String message) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title("Gia hạn sách")
                .content(message)
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


}
