package com.nanda.databindingexample.ui.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.nanda.databindingexample.R;
import com.nanda.databindingexample.base.BaseActivity;
import com.nanda.databindingexample.data.preferences.AppPreference;
import com.nanda.databindingexample.data.response.booklist.BooksModel;
import com.nanda.databindingexample.data.response.common.AppResponse;
import com.nanda.databindingexample.data.response.common.ResponseStatus;
import com.nanda.databindingexample.data.viewmodels.BookListViewModel;
import com.nanda.databindingexample.utils.UiUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    AppPreference appPreference;

    private BookListViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setHeaderTitle(getString(R.string.my_book));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BookListViewModel.class);

        observeLoadingStatus();
        fetchBookList();
    }

    private void fetchBookList() {
        viewModel.getBookList("hint").observe(this, new Observer<AppResponse>() {
            @Override
            public void onChanged(@Nullable AppResponse response) {
                if (response != null) {
                    if (response.status == ResponseStatus.SUCCESS) {
                        List<BooksModel> booksModelList = (List<BooksModel>) response.data;
                        UiUtils.showToast(HomeActivity.this, "" + booksModelList.size());
                    } else {
                        if (response != null && response.status == ResponseStatus.ERROR) {
                            UiUtils.showToast(HomeActivity.this, response.throwable.getMessage());
                        }
                    }
                }
            }
        });
    }

    private void observeLoadingStatus() {
        viewModel.getLoadingStatus().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object object) {
                Boolean isloading = (Boolean) object;
                if (isloading) {
                    showLoading();
                } else {
                    hideLoading();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_books) {

        } else if (id == R.id.nav_books_list) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
