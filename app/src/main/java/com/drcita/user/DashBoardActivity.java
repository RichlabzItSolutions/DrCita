package com.drcita.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.drcita.user.Activity.VideoConsultationActivity;
import com.drcita.user.adapter.CaregoryAdapter;
import com.drcita.user.adapter.MainSliderAdapter;
import com.drcita.user.adapter.NotificationAdapter;
import com.drcita.user.adapter.dashbaord.hospital.HospitalAdapter;
import com.drcita.user.adapter.dashbaord.specilization.SpecializationAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.common.CustomDailog;
import com.drcita.user.common.ViewPagerAdapter;
import com.drcita.user.databinding.ActivityDashBoardBinding;
import com.drcita.user.models.GlobalRequest;
import com.drcita.user.models.ads.AdResponse;
import com.drcita.user.models.appointment.AppointmentRequest;
import com.drcita.user.models.dashboard.specilization.Specialization;
import com.drcita.user.models.home.City;
import com.drcita.user.models.home.HomeDataRequest;
import com.drcita.user.models.home.HomeResponse;
import com.drcita.user.models.home.Providers;
import com.drcita.user.models.notifications.NotificationResponse;
import com.drcita.user.models.profile.GetProfileRequest;
import com.drcita.user.models.profile.GetProfileResponse;
import com.drcita.user.models.specalities.DataItem;
import com.drcita.user.models.specalities.SpecalitiesResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RelativeCornerSize;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardActivity extends LanguageBaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private ActivityDashBoardBinding activityDashBoardBinding;
    private ArrayList<String> photosList = new ArrayList<>();
    MainSliderAdapter mainSliderAdapter;
    String name,profilestatus;
    int region;
    Boolean exit = false;
    ImageView image;
    private TextView nameView, navMobile;
    private String mobile, userId;
    private CustomDailog progress;
    private ArrayList<DataItem> dataItems;
    private ArrayList<com.drcita.user.models.notifications.DataItem> notificationList;
    private CaregoryAdapter caregoryAdapter;
    private NotificationAdapter notificationAdapter;
    boolean isfromdental = false;
    private AdResponse.Ad ad ;

    List<AdResponse.Ad> adResponse;
    Boolean doubleBackToExitPressedOnce = false;

    SpecializationAdapter specializationAdapter;
    List<Specialization> specializationList;

    private List<Specialization> specializations;
    private List<AdResponse.Ad> ads;
    private List<City> cities;
    private List<Providers> providers;

    List<String> citieslist = Arrays.asList("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashBoardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board);
        boolean notification = getIntent().getBooleanExtra("notification", false);

        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        name = sp.getString(Constants.NAME, name);
        mobile = sp.getString(Constants.MOBILE, mobile);
        userId = sp.getString(Constants.USER_ID, userId);

        setSupportActionBar(activityDashBoardBinding.layoutDashboard.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, activityDashBoardBinding.drawerlayout
                , activityDashBoardBinding.layoutDashboard.toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        activityDashBoardBinding.drawerlayout.addDrawerListener(toggle);
        View headerView = activityDashBoardBinding.navView.getHeaderView(0);
        nameView = headerView.findViewById(R.id.navName);
        navMobile = headerView.findViewById(R.id.navMobile);
        image = headerView.findViewById(R.id.imageView);
        navMobile.setText(mobile);

        activityDashBoardBinding.layoutDashboard.dashboardmainlayout.catRVCategory.setLayoutManager(new LinearLayoutManager(DashBoardActivity.this, LinearLayoutManager.HORIZONTAL,false));
        dataItems = new ArrayList<>();
        caregoryAdapter = new CaregoryAdapter(DashBoardActivity.this, dataItems);
        activityDashBoardBinding.layoutDashboard.dashboardmainlayout.catRVCategory.setAdapter(caregoryAdapter);

        activityDashBoardBinding.navView.setNavigationItemSelectedListener(this);

        // Create a MaterialShapeDrawable (required for FAB cradle)


        MaterialShapeDrawable bottomBarBackground = (MaterialShapeDrawable) activityDashBoardBinding.layoutDashboard.bottomAppBar.getBackground();
        bottomBarBackground.setShapeAppearanceModel(
                bottomBarBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setAllCorners(new RoundedCornerTreatment())
                        .setTopLeftCornerSize(new RelativeCornerSize(0.65f))
                        .setTopRightCornerSize(new RelativeCornerSize(0.65f))
                        .build());
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] { Color.parseColor("#9FC44A"), Color.parseColor("#55A447") }
        );
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(new float[]{
                120f, 120f,  // Top-left radius
                120f, 120f,  // Top-right radius
                0f, 0f,    // Bottom-right
                0f, 0f     // Bottom-left
        });

        BottomAppBar bottomAppBar = activityDashBoardBinding.layoutDashboard.bottomAppBar;
        bottomAppBar.setBackground(gradientDrawable);
        bottomAppBar.setBackgroundTintList(null);

        getDashBoardData();

        activityDashBoardBinding.layoutDrawer.logoutNav.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.logoutTV.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.profileNav.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.profileTV.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.aboutusNav.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.aboutusTV.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.contactusNav.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.contactusTV.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.appoitmentsNav.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.appoitmentsTV.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.languagesNav.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.languagesTV.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.regionNav.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.regionTV.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.medicalNav.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.medicalTV.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.changepasswordNav.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.changepasswordTV.setOnClickListener(this);
       /* activityDashBoardBinding.layoutDrawer.callNav.setOnClickListener(this);
        activityDashBoardBinding.layoutDrawer.callTV.setOnClickListener(this);*/
        activityDashBoardBinding.layoutDashboard.dashboardmainlayout.profileLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HospitalsListActivity.class);
            intent.putExtra("specailization", 0);
            startActivity(intent);
        });
        activityDashBoardBinding.layoutDashboard.dashboardmainlayout.bookadentalappotimentlayout.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HospitalsListActivity.class);
            intent.putExtra("specailization", 14);
            intent.putExtra(Constants.isfromdental,true);
            startActivity(intent);
        });
        activityDashBoardBinding.layoutDashboard.dashboardmainlayout.bookmrictscan.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), VideoConsultationActivity.class);

            intent.putExtra("type","2");
            startActivity(intent);

        });
        activityDashBoardBinding.layoutDashboard.dashboardmainlayout.bookanambulance.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), BookanambulanceActivity.class);
            startActivity(intent);

        });

        activityDashBoardBinding.layoutDashboard.specalist.setOnClickListener(view -> {
            activityDashBoardBinding.layoutDashboard.nodataimage.setVisibility(View.GONE);
            activityDashBoardBinding.layoutDashboard.catRV.setVisibility(View.VISIBLE);
            activityDashBoardBinding.layoutDashboard.dashboardmainlayout.getRoot().setVisibility(View.GONE);
            activityDashBoardBinding.layoutDashboard.notificationRV.setVisibility(View.GONE);
            activityDashBoardBinding.layoutDashboard.specalist.getDrawable().setTint(getResources().getColor(R.color.md_grey_1000));
            activityDashBoardBinding.layoutDashboard.notification.getDrawable().setTint(getResources().getColor(R.color.purple_500));
            activityDashBoardBinding.layoutDashboard.fab.getDrawable().setTint(getResources().getColor(R.color.white));
            activityDashBoardBinding.layoutDashboard.fab.getBackground().mutate().setTint(getResources().getColor(R.color.purple_500));
           getSpecalists();
        });
        activityDashBoardBinding.layoutDashboard.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityDashBoardBinding.layoutDashboard.catRV.setVisibility(View.GONE);
                activityDashBoardBinding.layoutDashboard.dashboardmainlayout.getRoot().setVisibility(View.GONE);
                activityDashBoardBinding.layoutDashboard.notificationRV.setVisibility(View.VISIBLE);
                activityDashBoardBinding.layoutDashboard.specalist.getDrawable().setTint(getResources().getColor(R.color.purple_500));
                activityDashBoardBinding.layoutDashboard.notification.getDrawable().setTint(getResources().getColor(R.color.md_grey_1000));
                activityDashBoardBinding.layoutDashboard.fab.getBackground().mutate().setTint(getResources().getColor(R.color.purple_500));
                getNotifications();
            }
        });

        activityDashBoardBinding.layoutDashboard.fab.setOnClickListener(view -> {
            activityDashBoardBinding.layoutDashboard.nodataimage.setVisibility(View.GONE);
            activityDashBoardBinding.layoutDashboard.catRV.setVisibility(View.GONE);
            activityDashBoardBinding.layoutDashboard.notificationRV.setVisibility(View.GONE);
            activityDashBoardBinding.layoutDashboard.dashboardmainlayout.getRoot().setVisibility(View.VISIBLE);
            activityDashBoardBinding.layoutDashboard.specalist.getDrawable().setTint(getResources().getColor(R.color.purple_500));
            activityDashBoardBinding.layoutDashboard.notification.getDrawable().setTint(getResources().getColor(R.color.purple_500));
            activityDashBoardBinding.layoutDashboard.fab.getBackground().mutate().setTint(getResources().getColor(R.color.md_grey_1000));
        });

        activityDashBoardBinding.layoutDashboard.dashboardmainlayout.viewPagerMain.setClipToPadding(false);
        activityDashBoardBinding.layoutDashboard.dashboardmainlayout.viewPagerMain.setPadding(60, 0, 60, 0);
        activityDashBoardBinding.layoutDashboard.dashboardmainlayout.viewPagerMain.setPageMargin(10);

        activityDashBoardBinding.layoutDashboard.catRV.setLayoutManager(new GridLayoutManager(this, 3));
        dataItems = new ArrayList<>();
        caregoryAdapter = new CaregoryAdapter(this, dataItems);
        activityDashBoardBinding.layoutDashboard.catRV.setAdapter(caregoryAdapter);

        notificationList = new ArrayList<>();
        activityDashBoardBinding.layoutDashboard.notificationRV.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(this, notificationList);
        activityDashBoardBinding.layoutDashboard.notificationRV.setAdapter(notificationAdapter);

        if (notification){
            activityDashBoardBinding.layoutDashboard.catRV.setVisibility(View.GONE);
            activityDashBoardBinding.layoutDashboard.dashboardmainlayout.getRoot().setVisibility(View.GONE);
            activityDashBoardBinding.layoutDashboard.notificationRV.setVisibility(View.VISIBLE);
            activityDashBoardBinding.layoutDashboard.specalist.getDrawable().setTint(getResources().getColor(R.color.purple_500));
            activityDashBoardBinding.layoutDashboard.notification.getDrawable().setTint(getResources().getColor(R.color.md_grey_1000));
           // activityDashBoardBinding.layoutDashboard.fab.getBackground().mutate().setTint(getResources().getColor(R.color.purple_500));
        }

        autoSlide();
    }
    private void autoSlide() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        final Runnable runnable = () -> {
            int currentPage=activityDashBoardBinding.layoutDashboard.dashboardmainlayout.viewPagerMain.getCurrentItem();
            //return to first page, if current page is last page
            if (currentPage == activityDashBoardBinding.layoutDashboard.dashboardmainlayout.viewPagerMain.getChildCount()-1) {
                currentPage = -1;
            }
            activityDashBoardBinding.layoutDashboard.dashboardmainlayout.viewPagerMain.setCurrentItem(++currentPage, true);
        };
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },500,5000);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1500);
    }



    @Override
    protected void onResume() {
        getUserProfile();
        getAdsApi();
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissLoadingDialog();
    }

    private void getAdsApi() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            ApiClient.getRestAPI().getAds().enqueue(new Callback<AdResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<AdResponse> call, @NonNull Response<AdResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();




                    } else {
                        try {
                            Constants.displayError(response.errorBody().string(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dismissLoadingDialog();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AdResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(DashBoardActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(DashBoardActivity.this);
        }
    }

    private void getUserProfile() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            GetProfileRequest request = new GetProfileRequest();
            request.setUserId(Integer.parseInt(userId));
            ApiClient.getRestAPI().getUserProfile(request).enqueue(new Callback<GetProfileResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<GetProfileResponse> call, @NonNull Response<GetProfileResponse> response) {
                    if (response.isSuccessful()) {
                       // dismissLoadingDialog();
                        getProfileResponse(Objects.requireNonNull(response.body()));
                    } else {
                        try {
                            Constants.displayError(response.errorBody().string(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                       // dismissLoadingDialog();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetProfileResponse> call, @NonNull Throwable t) {
                   // dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(DashBoardActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(DashBoardActivity.this);
        }
    }

    @SuppressLint("SetTextI18n")
    private void getProfileResponse(GetProfileResponse getProfileResponse) {
        nameView.setText("Hi, " + getProfileResponse.getData().getName());


        if (getProfileResponse.getData().getPicture()!=null && !getProfileResponse.getData().getPicture().isEmpty()) {
            Picasso.with(this).load(getProfileResponse.getData().getPicture()).into(image);
            image.setImageTintList(null);
            // image.setImageTintList(null);
        }else {
            image.setImageDrawable(getDrawable(R.drawable.avtar));
        }
       /* if (!getProfileResponse.getData().getPicture().isEmpty()) {
            Picasso.with(this).load(getProfileResponse.getData().getPicture())
                    .into(image);
        } else {

        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return false;
    }


    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int id = item.getItemId();
        activityDashBoardBinding.drawerlayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                notification();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void notification() {
        activityDashBoardBinding.layoutDashboard.catRV.setVisibility(View.GONE);
        activityDashBoardBinding.layoutDashboard.dashboardmainlayout.getRoot().setVisibility(View.GONE);
        activityDashBoardBinding.layoutDashboard.notificationRV.setVisibility(View.VISIBLE);
        activityDashBoardBinding.layoutDashboard.specalist.getDrawable().setTint(getResources().getColor(R.color.purple_500));
        activityDashBoardBinding.layoutDashboard.notification.getDrawable().setTint(getResources().getColor(R.color.md_grey_1000));
         activityDashBoardBinding.layoutDashboard.fab.getBackground().mutate().setTint(getResources().getColor(R.color.purple_500));
        getNotifications();
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (exit) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Press Again To Exit", Toast.LENGTH_LONG).show();
            exit = true;
        }
    }*/

    @Override
    public void onClick(View view) {
        activityDashBoardBinding.drawerlayout.closeDrawer(GravityCompat.START);
        switch (view.getId()) {
            case R.id.profileTV:
            case R.id.profileNav:
                Intent intent = new Intent(getApplicationContext(), UpdateProfile.class);
                startActivity(intent);
                break;

            case R.id.appoitmentsTV:
            case R.id.appoitmentsNav:
                Intent intent2 = new Intent(getApplicationContext(), AppointmentsActivity.class);
                startActivity(intent2);
                break;

            case R.id.contactusTV:
            case R.id.contactusNav:
                Intent intent4 = new Intent(getApplicationContext(), ContactusActivity.class);
                startActivity(intent4);
                break;

            case R.id.aboutusTV:
            case R.id.aboutusNav:
                Intent intent6 = new Intent(getApplicationContext(), AboutusActivity.class);
                startActivity(intent6);
                break;

            case R.id.languagesTV:
            case R.id.languagesNav:
                Intent intent8 = new Intent(getApplicationContext(), LanguageActivity.class);
                startActivity(intent8);
                break;

            case R.id.regionTV:
            case R.id.regionNav:
                Intent intent10 = new Intent(getApplicationContext(), RegionActivity.class);
                startActivity(intent10);
                break;

            case R.id.medicalTV:
            case R.id.medicalNav:
                Intent intent11 = new Intent(getApplicationContext(), MedicalRecordsActivity.class);
                startActivity(intent11);
                break;

            case R.id.changepasswordNav:
            case R.id.changepasswordTV:
                Intent intent12 = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent12);
                break;
           /* case R.id.callNav:
            case R.id.callTV:
                Intent intent13 = new Intent(getApplicationContext(), CallActivity.class);
                startActivity(intent13);
                break;*/

            case R.id.logoutNav:
            case R.id.logoutTV:
                SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finishAffinity();
                    startActivity(in);
                    finish();
                }, 1000);
                break;
        }
    }

// dashboardResponse

    private void getDashBoardData() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            HomeDataRequest globalRequest = new HomeDataRequest();
            globalRequest.setStateId(24);
            globalRequest.setCityId(14);
            ApiClient.getRestAPI().getHomedata(globalRequest).enqueue(new Callback<HomeResponse>() {
                @Override
                public void onResponse(@NonNull Call<HomeResponse> call, @NonNull retrofit2.Response<HomeResponse> response) {
                    getHomeResponse(Objects.requireNonNull(response.body()));
                }
                @Override
                public void onFailure(@NonNull Call<HomeResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(DashBoardActivity.this);
        }
    }

    private void getHomeResponse(HomeResponse body) {

        try{
            specializations=body.getData().getSpecializations();
            cities=body.getData().getCities();
            providers=body.getData().getProviders();
            adResponse=body.getData().getAds();

            // adds
            String[] images = new String[adResponse.size()];

            for (int i = 0; i < adResponse.size(); i++) {
                ad = adResponse.get(i);
                images[i] = ad.getImage();
            }
            ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(DashBoardActivity.this, images,adResponse);
            //Adding the Adapter to the ViewPager
            activityDashBoardBinding.layoutDashboard.dashboardmainlayout.viewPagerMain.setAdapter(mViewPagerAdapter);



            specializationAdapter = new SpecializationAdapter(this,specializations);
            activityDashBoardBinding.layoutDashboard.dashboardmainlayout.rvSpecailist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            activityDashBoardBinding.layoutDashboard.dashboardmainlayout.rvSpecailist.setAdapter(specializationAdapter);


           //providers
            activityDashBoardBinding.layoutDashboard.dashboardmainlayout.rvTophospital.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            );


            HospitalAdapter adapter = new HospitalAdapter(this, providers);
            activityDashBoardBinding.layoutDashboard.dashboardmainlayout.rvTophospital.setAdapter(adapter);

            citieslist =new ArrayList<>(cities.size());
                 for(City city:cities)
                 {
                     citieslist.add(city.getCityName());
                 }

            activityDashBoardBinding.layoutDashboard.tvLocation.setOnClickListener(v ->
            {
                showDropdownPopup(activityDashBoardBinding.layoutDashboard.tvLocation, citieslist);
            });


//


        }catch (Exception exception)
        {
            exception.getMessage();
        }

    }

    private void showDropdownPopup(final TextView anchorView, final List<String> items) {
        // Inflate the dropdown layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.dropdown_list, null);

        ListView listView = popupView.findViewById(R.id.lvCities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item_black, items);
        listView.setAdapter(adapter);

        // Create PopupWindow
        final PopupWindow popupWindow = new PopupWindow(popupView, anchorView.getWidth(),
                WindowManager.LayoutParams.WRAP_CONTENT, true);

        // Handle item click
        listView.setOnItemClickListener((parent, view, position, id) -> {
            anchorView.setText(items.get(position));
            popupWindow.dismiss();
        });

        // Show dropdown below the TextView
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(anchorView, 0, 0);
    }


    private void getSpecalists() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            GlobalRequest globalRequest = new GlobalRequest();
            globalRequest.setUserId(userId);
            ApiClient.getRestAPI().specialities(globalRequest).enqueue(new Callback<SpecalitiesResponse>() {
                @Override
                public void onResponse(@NonNull Call<SpecalitiesResponse> call, @NonNull retrofit2.Response<SpecalitiesResponse> response) {
                    getSpecalistsResponse(Objects.requireNonNull(response.body()));
                }
                @Override
                public void onFailure(@NonNull Call<SpecalitiesResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(DashBoardActivity.this);
        }
    }

    private void getNotifications() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            AppointmentRequest appointmentRequest = new AppointmentRequest();
            appointmentRequest.setUserId(Integer.parseInt(userId));
            appointmentRequest.setUserType(1);
            ApiClient.getRestAPI().getNotifications(appointmentRequest).enqueue(new Callback<NotificationResponse>() {
                @Override
                public void onResponse(@NonNull Call<NotificationResponse> call, @NonNull retrofit2.Response<NotificationResponse> response) {
                    getNotificationResponse(Objects.requireNonNull(response.body()));
                }

                @Override
                public void onFailure(@NonNull Call<NotificationResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(DashBoardActivity.this);
        }
    }

    private void getSpecalistsResponse(SpecalitiesResponse specalitiesResponse) {
        dismissLoadingDialog();
        String description = specalitiesResponse.getMessage();
        if (specalitiesResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            dataItems.clear();
            dataItems.addAll(specalitiesResponse.getData());
            caregoryAdapter.notifyDataSetChanged();
        } else {
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void getNotificationResponse(NotificationResponse specalitiesResponse) {
        dismissLoadingDialog();
        String description = specalitiesResponse.getMessage();
        if (specalitiesResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            if (specalitiesResponse.getData().isEmpty()){
                activityDashBoardBinding.layoutDashboard.nodataimage.setVisibility(View.VISIBLE);
            }
            else {
                activityDashBoardBinding.layoutDashboard.nodataimage.setVisibility(View.GONE);
            }
            notificationList.addAll(specalitiesResponse.getData());
            notificationAdapter.notifyDataSetChanged();
        } else {
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }


}
