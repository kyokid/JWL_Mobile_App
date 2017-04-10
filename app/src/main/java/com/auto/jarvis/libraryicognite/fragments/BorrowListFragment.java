package com.auto.jarvis.libraryicognite.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.ConvertUtils;
import com.auto.jarvis.libraryicognite.activities.BorrowCartActivity;
import com.auto.jarvis.libraryicognite.activities.DetailBookActivity;
import com.auto.jarvis.libraryicognite.adapters.BorrowListAdapter;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.listeners.RecyclerItemClickListener;
import com.auto.jarvis.libraryicognite.models.input.User;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    String rfid, messageRenew, messageError;


    public BorrowListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getBorrowedBook(true);
        }
        Log.d("VISIBLE", "status " + hidden);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_borrow_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getBorrowedBook(true);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        listBorrowed = new ArrayList<>();
        apiService = ApiClient.getClient().create(ApiInterface.class);
        //pull to refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getBorrowedBook(true);
            ((BorrowCartActivity) getActivity()).setIsNewFlag(false);
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        adapter = new BorrowListAdapter(listBorrowed, getActivity(), multiSelectedBook);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvBooks.setLayoutManager(layoutManager);
        rvBooks.setItemAnimator(new DefaultItemAnimator());
        android.support.v7.widget.DividerItemDecoration divider =
                new android.support.v7.widget.DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        rvBooks.addItemDecoration(divider);
        rvBooks.setAdapter(adapter);
        rvBooks.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvBooks, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("new_adapter", "one click");
                if (isMultiSelect && (!listBorrowed.get(position).isDeadline() ||
                        listBorrowed.get(position).getBookStatus() >
                                listBorrowed.get(position).getLateDaysLimit())) {
                    return;
                }
                if (isMultiSelect && listBorrowed.get(position).isDeadline()) {
                    multiSelect(position);
                } else {
                    Intent detailIntent = new Intent(getContext(), DetailBookActivity.class);
                    InformationBookBorrowed bookDetail = listBorrowed.get(position);
                    detailIntent.putExtra("BOOK_DETAIL", bookDetail);
                    detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(detailIntent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!listBorrowed.get(position).isDeadline() ||
                        listBorrowed.get(position).getBookStatus() > listBorrowed.get(position).getLateDaysLimit()) {
                    return;
                }
                Log.d("new_adapter", "long click");
                if (!isMultiSelect) {
                    multiSelectedBook = new ArrayList<>();
                    isMultiSelect = true;

                    if (actionMode == null) {
                        actionMode = getActivity().startActionMode(mActionModeCallback);
                    }
                }
                multiSelect(position);
            }
        }));


//        getBorrowedBook(false);

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

    public void getBorrowedBook(final boolean isRefreshing) {

        String username = SaveSharedPreference.getUsername(getActivity());
        User user = new User(username);
        Call<RestService<List<InformationBookBorrowed>>> getBorrowedBook = apiService.getBorrowedBook(user);

        getBorrowedBook.enqueue(new Callback<RestService<List<InformationBookBorrowed>>>() {
            @Override
            public void onResponse(Call<RestService<List<InformationBookBorrowed>>> call, Response<RestService<List<InformationBookBorrowed>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSucceed()) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (isRefreshing) {
                            multiSelectedBook.clear();
//                            adapter.clear();
                            listBorrowed.clear();
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                        }
                        List<InformationBookBorrowed> listBorrowed = response.body().getData();
                        adapter.addAll(listBorrowed);
                    }
                }
            }

            @Override
            public void onFailure(Call<RestService<List<InformationBookBorrowed>>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "FAIL API", Toast.LENGTH_SHORT).show();
            }
        });


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
                    int quantity = multiSelectedBook.size();
                    if (quantity != 1) {
                        messageRenew = "Bạn muốn gia hạn " + quantity + " cuốn sách này?";
                    } else {
                        String deadlineDate = formateDate(multiSelectedBook.get(0).getDeadlineDate());
                        Date deadline = ConvertUtils.convertStringtoDate(multiSelectedBook.get(0).getDeadlineDate());
                        Long a = deadline.getTime() + (multiSelectedBook.get(0).getDaysPerExtend() * 86400000);
                        Date b = new Date(a);
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String newDeadline = df.format(b);
                        messageRenew = "Sách này sẽ được gia hạn từ ngày " + deadlineDate + " đến ngày " + newDeadline;
                    }
                    showDialogRenew(messageRenew);
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

    private void showDialogRenew(String message) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title("Gia hạn sách")
                .positiveText("OK")
                .content(message)
                .onPositive((dialog1, which) -> renewManyBooks())
                .negativeText("Cancel");

        dialog = builder.build();
        dialog.show();
    }

    private void renewBook(String rfid) {
        Call<RestService<InformationBookBorrowed>> renew = apiService.renewBorrowedBook(rfid);
        renew.enqueue(new Callback<RestService<InformationBookBorrowed>>() {
            @Override
            public void onResponse(Call<RestService<InformationBookBorrowed>> call, Response<RestService<InformationBookBorrowed>> response) {
                if (!response.body().getCode().equals("200")) {
                    Log.d("RENEW", "FAIL" + response.body().getTextMessage());
                    messageError = response.body().getTextMessage();
                    showDialogFinish(messageError);
                }
            }

            @Override
            public void onFailure(Call<RestService<InformationBookBorrowed>> call, Throwable t) {

            }
        });
    }


    private void showDialogFinish(String message) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title("Gia hạn sách")
                .content(message)
                .canceledOnTouchOutside(true)
                .onPositive((dialog1, which) -> getBorrowedBook(true))
                .positiveText("OK");
        dialog = builder.build();
        dialog.show();
    }


    private void renewManyBooks() {
        for (InformationBookBorrowed book : multiSelectedBook) {
            rfid = book.getBookCopyRfid();
            renewBook(rfid);
        }
        actionMode.finish();
        multiSelectedBook.clear();
        Log.d("RENEW", "DIALOG" + messageError);
//        if (messageError != null) {
//            showDialogFinish(messageError);
//        }

    }

    public void refreshList() {
        getBorrowedBook(true);
    }

}
