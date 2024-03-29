package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Circle;
import com.techno71.fireservice.CircleAnimation;
import com.techno71.fireservice.Controller.Location_wishStorageController;
import com.techno71.fireservice.Model.AllComment;
import com.techno71.fireservice.Model.Divisoin_model;
import com.techno71.fireservice.Model.LocationWithStorageShow;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import me.tankery.lib.circularseekbar.CircularSeekBar;


public class CompanyMapsActivity extends AppCompatActivity
        implements OnMapReadyCallback
        , Animation.AnimationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    //working on demo
    int[] icons_demo_list = {
            R.drawable.fire_1,
            R.drawable.fire_2,
            R.drawable.fire_3,
            R.drawable.fire_4,
            R.drawable.fire_5,
            R.drawable.fire_6,
            R.drawable.fire_7,
            R.drawable.fire_7,
            R.drawable.fire_8,
            R.drawable.fire_7,
            R.drawable.fire_9,
            R.drawable.fire_10,
            R.drawable.fire_11,
            R.drawable.fire_12,
            R.drawable.fire_13,
            R.drawable.fire_14,
    };
    Timer timer = new Timer();
    int current_icon = 0;

    private ProgressDialog progressDialog;

    private GoogleMap mMap;
    private RecyclerView recyclerView_locationStorag;
    private List<LocationWithStorageShow> locationWithStorageShowList = new ArrayList<>();
    private Location_wishStorageController location_wishStorageController;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private double myLatitud, myLongitude;
    public Circle curve;
    private LinearLayout taplayout;
    private boolean cancel = false;
    private View view;

    private LayoutInflater layoutInflater;

    double latitude_stg, longitude_stg;

    private List<MarkerOptions> markerList = new ArrayList<>();
    private HashMap<String, String> locationId = new HashMap<>();

    private ArrayList<String> arrayList_spinnerColor = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter_spinnerColors;

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    private ActionBarDrawerToggle toggleButton;

    private BottomSheetDialog bottomSheetAlam;

    private EditText editText_tile, editText_message;

    private CircularSeekBar circularSeekBar;

    private CountDownTimer countDownTimer;

    private CheckBox checkBox;
    private String alertTypeName = "";

    int max = 5000;
    private SharedPreferences sharedPreferences_type;

    private View view_navigation;

    private TextView textView_userName;

    private CircleImageView circleImageView;

    private String user_infor = Main_Url.ROOT_URL + "api/get-user-info";

    private String locatinUpdate = Main_Url.ROOT_URL + "api/update-map-location";

    private String locatinWith_storage = Main_Url.ROOT_URL + "api/show-all-loaction-wise-storage";

    private String userWith_storage = Main_Url.ROOT_URL + "api/show-all-storage-loaction";

    private String user_blockcheck = Main_Url.ROOT_URL + "api/check-user-block-or-not";

    private String alertType_url = Main_Url.ROOT_URL + "api/show-alert-type";

    private String send_alert_url = Main_Url.ROOT_URL + "api/send-alert";

    private String marker_tag = "", user_Address;

    private AppCompatActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_maps);
        context = this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        sharedPreferences_type = getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        toolbar = (Toolbar) findViewById(R.id.toolbar_company);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_company);
        navigationView = (NavigationView) findViewById(R.id.navigation_compani);

        toggleButton = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggleButton.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        drawerLayout.addDrawerListener(toggleButton);
        toggleButton.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.sideCom_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.Side_ComlogOut:

                        Intent intent = new Intent(CompanyMapsActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(CompanyMapsActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        sharedPreferences_type.edit().clear().commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.side_ComchangePassword:

                        Intent intent_pass = new Intent(CompanyMapsActivity.this, InformationLoddingActivity.class);
                        intent_pass.putExtra("Techno", "userPass");
                        startActivity(intent_pass);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.SideCom_mycomment:

                        Intent intent_comm = new Intent(CompanyMapsActivity.this, InformationLoddingActivity.class);
                        intent_comm.putExtra("Techno", "comment");
                        startActivity(intent_comm);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.sideCom_profile:

                        Intent intent_prf = new Intent(CompanyMapsActivity.this, InformationLoddingActivity.class);
                        intent_prf.putExtra("Techno", "userPrf");
                        startActivity(intent_prf);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.SideCom_setting:

                        Intent intent_setting = new Intent(CompanyMapsActivity.this, Setting_Activity.class);
                        startActivity(intent_setting);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.SideCom_news:

                        Intent intent_news = new Intent(CompanyMapsActivity.this, InformationLoddingActivity.class);
                        intent_news.putExtra("Techno", "news");
                        startActivity(intent_news);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.SideCom_addStorage:

                        Intent intent_add = new Intent(CompanyMapsActivity.this, StorageAddActivity.class);
                        startActivity(intent_add);
                        /*String status = sharedPreferences_type.getString("status", "Status Not Found");

                        if (status.contains("2")) {

                            Intent intent_add = new Intent(CompanyMapsActivity.this, StorageAddActivity.class);
                            startActivity(intent_add);

                        }
                        if (status.contains("1")) {

                            Toast.makeText(CompanyMapsActivity.this, "Processing", Toast.LENGTH_SHORT).show();

                        }
                        if (status.contains("0")) {

                            MaterialDialog mDialog = new MaterialDialog.Builder(CompanyMapsActivity.this)
                                    .setTitle("Company Verify Notices")
                                    // .setAnimation(R.raw.congratulations)
                                    .setMessage("Are you sure you want to Company Verify??")
                                    .setCancelable(false)
                                    .setPositiveButton("Got it", new AbstractDialog.OnClickListener() {
                                        @Override
                                        public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                            dialogInterface.dismiss();

                                            Intent intent_comp = new Intent(CompanyMapsActivity.this, CompanyVerify_Activity.class);
                                            intent_comp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent_comp);

                                        }
                                    })
                                    .setNegativeButton("Cancel", new AbstractDialog.OnClickListener() {
                                        @Override
                                        public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                            dialogInterface.dismiss();

                                        }
                                    })
                                    .build();
                            mDialog.show();

                        }
                        if (status.contains("Status Not Found")) {

                            Toast.makeText(CompanyMapsActivity.this, "User Type Not Found!!", Toast.LENGTH_SHORT).show();

                        }*/
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.Sidecom_call:

                        Intent intent_call = new Intent(CompanyMapsActivity.this, InformationLoddingActivity.class);
                        intent_call.putExtra("Techno", "call");
                        startActivity(intent_call);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.sideCom_Bookmarks:

              /*          Intent intent_book=new Intent(CompanyMapsActivity.this,InformationLoddingActivity.class);
                        intent_book.putExtra("Techno","bookMark");
                        startActivity(intent_book);*/

                        Intent intent_book = new Intent(CompanyMapsActivity.this, BookMarkActivity.class);
                        startActivity(intent_book);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
                return false;
            }
        });

        view_navigation = navigationView.inflateHeaderView(R.layout.comp_header);
        textView_userName = view_navigation.findViewById(R.id.textNavComName);

        circleImageView = view_navigation.findViewById(R.id.text_userComImage);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);
        else Toast.makeText(this, "No Map Found...", Toast.LENGTH_SHORT).show();


        language_model language = new language_model(this);
        language.loadLanguage();

    }

    @Override
    protected void onStart() {
        super.onStart();

        language_model language = new language_model(this);
        language.loadLanguage();

        getUserInfromation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        verify_Account();

    }


  /*  @Override
  protected void onStop() {
     super.onStop();

   getUserInfromation();

    }
*/

    private void verify_Account() {

        String accessTocken = sharedPreferences_type.getString("access_token", "access_token found");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, user_blockcheck, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain = new JSONObject(response);

                    if (jsonObjectMain.getString("message").contains("Blocked")) {

                        Intent intent = new Intent(CompanyMapsActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        sharedPreferences_type.edit().clear().commit();

                        Toast.makeText(CompanyMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                    }/*else if(jsonObjectMain.getString("message").contains("Sry Access Token Not match")){

                        Intent intent=new Intent(CompanyMapsActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        sharedPreferences_type.edit().clear().commit();
                        Toast.makeText(CompanyMapsActivity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                    }*/ else if (!jsonObjectMain.getString("message").contains("Active")) {

                        Toast.makeText(CompanyMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                netWorkError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();

                hashMap.put("security_error", "tec71");
                hashMap.put("axcess_token", accessTocken);

                return hashMap;
            }
        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(CompanyMapsActivity.this);
        requestQueue1.add(stringRequest);
    }

    private void UpdateLocation(double latitude, double longtude) {

        String accessTocken = sharedPreferences_type.getString("access_token", "access_token found");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, locatinUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain = new JSONObject(response);

                    if (jsonObjectMain.getString("message").contains("Successfully Update Location")) {

                        Toast.makeText(CompanyMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();

                hashMap.put("security_error", "tec71");
                hashMap.put("axcess_token", accessTocken);
                hashMap.put("latitude", String.valueOf(latitude));
                hashMap.put("longtude", String.valueOf(longtude));

                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CompanyMapsActivity.this);
        requestQueue.add(stringRequest);
    }


    private void getUserInfromation() {

        final ProgressDialog progressDialog = new ProgressDialog(CompanyMapsActivity.this);

        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String accessTocken = sharedPreferences_type.getString("access_token", "access_token found");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, user_infor, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain = new JSONObject(response);

                    if (jsonObjectMain.getString("message").contains("Data is available!")) {
                        progressDialog.dismiss();

                        JSONArray jsonArray = jsonObjectMain.getJSONArray("UserInfo");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // jsonObject.getString("mobile_no");

                            sharedPreferences_type.edit().putString("status", jsonObject.getString("company_verify_status")).commit();
                            sharedPreferences_type.edit().putString("user_type", jsonObject.getString("user_type")).commit();
                            sharedPreferences_type.edit().putString("user_id", jsonObject.getString("user_id")).commit();

                            textView_userName.setText("Name:" + jsonObject.getString("user_name"));

                            String image = "https://fifaar.com/public/" + jsonObject.getString("user_picture");

                            Picasso.get().load(image).into(circleImageView);

                        }

                    } else if (jsonObjectMain.getString("message").contains("Data is Not available!")) {
                        sharedPreferences_type.edit().remove("status");
                        sharedPreferences_type.edit().remove("user_type");

                        Toast.makeText(CompanyMapsActivity.this, "User Information Not Found..", Toast.LENGTH_LONG).show();

                    } else if (jsonObjectMain.getString("message").contains("Sry Access Token Not match")) {

                        Intent intent = new Intent(CompanyMapsActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        sharedPreferences_type.edit().clear().commit();
                        Toast.makeText(CompanyMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(CompanyMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                netWorkError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();

                hashMap.put("security_error", "tec71");
                hashMap.put("axcess_token", accessTocken);

                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CompanyMapsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(CompanyMapsActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(CompanyMapsActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(CompanyMapsActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(CompanyMapsActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(CompanyMapsActivity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(CompanyMapsActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }

    private Marker dragMerker;
    private Marker invisibleDragMerker;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Showing / hiding your current location
        // Enable / Disable zooming controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        // Enable / Disable my location button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (dragMerker != null) {
                    dragMerker.remove();
                    dragMerker = null;
                }

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Select Position");
                markerOptions.draggable(true);
//                markerOptions.flat(true);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                markerOptions.visible(true);
                dragMerker = mMap.addMarker(markerOptions);
                if (invisibleDragMerker != null) {
                    invisibleDragMerker.remove();
                    invisibleDragMerker = null;
                }
                MarkerOptions invisibleMarkerOptions = new MarkerOptions();
                invisibleMarkerOptions.position(latLng);
                invisibleMarkerOptions.title("Select Position");
                invisibleMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.invisible_marker));
                invisibleMarkerOptions.visible(true);
                invisibleMarkerOptions.alpha(0f);
                invisibleDragMerker = mMap.addMarker(invisibleMarkerOptions);


            }
        });


    }

    private BottomSheetDialog bottomSheetDialog;
    private String storage_add = Main_Url.ROOT_URL + "api/user-add-storage";
    private String addCommentAPI = Main_Url.ROOT_URL + "api/storage-add-comment";

    private void setComments() {

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.user_comments_unwnon);

        EditText editTextTitle = bottomSheetDialog.findViewById(R.id.editText_userCompanyTitle);
        EditText editTextDetails = bottomSheetDialog.findViewById(R.id.editText_userCompanyDetails);

        Spinner spinnerColor = bottomSheetDialog.findViewById(R.id.spinner_Usercolor);
        Spinner spinnerFloor = bottomSheetDialog.findViewById(R.id.spinner_UserFloor);

        Button button_submit = bottomSheetDialog.findViewById(R.id.userComment_submit);
        Button button_close = bottomSheetDialog.findViewById(R.id.userComment_close);


        if (spinnerColor != null) {
            String[] colorData = new String[]{
                    "Select--Danger--Variation--", "Green", "Red"
            };

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    CompanyMapsActivity.this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    colorData
            );
            spinnerColor.setAdapter(adapter);
        }

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "" + editTextTitle.getText();
                String details = "" + editTextDetails.getText();
                String color = "" + spinnerColor.getSelectedItem();
                String floor = "" + spinnerFloor.getSelectedItem();
                String address = "" + getCompleteAddressString(dragMerker.getPosition().latitude, dragMerker.getPosition().longitude);
                String latitude = "" + dragMerker.getPosition().latitude;
                String longitude = "" + dragMerker.getPosition().longitude;
                String accessTocken = "" + sharedPreferences_type.getString("access_token", "access_token found");

                if (title.isEmpty()) {
                    editTextTitle.setError("Requaird");
                    editTextTitle.requestFocus();
                    return;
                }
                if (details.isEmpty()) {
                    editTextDetails.setError("Requaird");
                    editTextDetails.requestFocus();
                    return;
                }

                if (color.equals("Select--Colors--")) {
                    Toast.makeText(context, "Colors is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (floor.equals("Select--Floor--")) {
                    Toast.makeText(context, "Floor is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                storage_add_comment(title, details, color, floor, address, latitude, longitude, accessTocken);
            }
        });

        bottomSheetDialog.show();
    }

    private void storage_add_comment(String title, String details, String color, String floor, String address, String latitude, String longitude, String accessTocken) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, storage_add, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain = new JSONObject(response);

                    if (jsonObjectMain.getString("message").contains("Comment Successfully!")) {
                        bottomSheetDialog.dismiss();
                        Toast.makeText(context, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(context, "error:" + jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkError(error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error", "tec71");
                hashMap.put("axcess_token", accessTocken);
                hashMap.put("title", title);
                hashMap.put("company_latitude", latitude);
                hashMap.put("company_longtude", longitude);
                hashMap.put("address", address);
                hashMap.put("floor", floor);
                hashMap.put("company_detils", details);
                hashMap.put("alert_tag", color);
                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    private String getCompleteAddressString(double latitude, double longitude) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }


    private void ShowStorage(double latitude, double longitude) {

        String accessTocken = sharedPreferences_type.getString("access_token", "access_token found");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, userWith_storage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObjectMain = new JSONObject(response);

                    if (jsonObjectMain.getString("message").contains("Data is available!")) {

                        JSONArray jsonArray = jsonObjectMain.getJSONArray("alllocations");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.getString("status").contains("1")) {

                                latitude_stg = Double.parseDouble(jsonObject.getString("latitude"));
                                longitude_stg = Double.parseDouble(String.valueOf(jsonObject.getDouble("longtude")));
                                String id = jsonObject.getString("id");

                                LatLng latLng_storag = new LatLng(latitude_stg, longitude_stg);

                                MarkerOptions markerStorage = new MarkerOptions();

                                marker_tag = jsonObject.getString("tag_color");

                                if (marker_tag.equalsIgnoreCase("Yellow")) {

                                    markerStorage.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                } else if (marker_tag.equalsIgnoreCase("Red")) {
                                    markerStorage.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                } else {
                                    //selectColors=HUE_GREEN;
                                    markerStorage.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                }
                                String locationIdKey = latitude_stg + "" + longitude_stg;
                                locationId.put(locationIdKey, id);

                                //markerStorage.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                markerStorage.position(latLng_storag).title(jsonObject.getString("title"));
                                markerList.add(markerStorage);
                            }

                        }
                        showAllMarkers(mMap, true);

                    } else {
                        showAllMarkers(mMap, false);
                        Toast.makeText(CompanyMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkError(error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error", "tec71");
                hashMap.put("latitude", String.valueOf(latitude));
                hashMap.put("longtude", String.valueOf(longitude));
                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CompanyMapsActivity.this);
        requestQueue.add(stringRequest);
    }

    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();
    }

    public void Alown_Compuny_user(View view) {
/*
        bottomSheetAlam = new BottomSheetDialog(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alarm_bottom_sheet_dialog, null);

        CheckBox checkBox = view1.findViewById(R.id.check_Terms_Conditions);

        Spinner spinner_alertType=(Spinner)view1.findViewById(R.id.alertType_spinner);
        ArrayList<String> arrayList_alertType= new ArrayList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, alertType_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    arrayList_alertType.clear();

                    arrayList_alertType.add("Select--Alert--Type--");

                    if(jsonObjectMain.getString("message").contains("Data is available!")){

                        JSONArray jsonArray= jsonObjectMain.getJSONArray("alerType");

                        for (int i = 0; i <jsonArray.length() ;i++) {

                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            arrayList_alertType.add(jsonObject.getString("name"));
                        }

                        ArrayAdapter<String> arrayAdapter_alert=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList_alertType);
                        spinner_alertType.setAdapter(arrayAdapter_alert);


                        spinner_alertType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                alertTypeName= arrayList_alertType.get(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{

                        Toast.makeText(CompanyMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                netWorkError(error);

            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error","tec71");

                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(CompanyMapsActivity.this);
        requestQueue.add(stringRequest);

        editText_message = view1.findViewById(R.id.edit_userMessag);

        curve = view1.findViewById(R.id.curve_alarm);
        taplayout = view1.findViewById(R.id.layouttap);
        final CircleAnimation circleAnimation = new CircleAnimation(curve, 127.0f);
        this.curve.setCurveColor(ContextCompat.getColor(this, R.color.Fuchsia), ContextCompat.getColor(this, R.color.tap_dark));
        circleAnimation.setDuration(2000);
        circleAnimation.setAnimationListener(this);
        taplayout.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!checkBox.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Select Terms Conditions", Toast.LENGTH_SHORT).show();
                    checkBox.setError("Terms Conditions!!");

                }else if(spinner_alertType.getSelectedItem().toString().contains("Select--Alert--Type--")){

                    Toast.makeText(CompanyMapsActivity.this, "Please Your Alet Type", Toast.LENGTH_SHORT).show();

                }  else{

                    // String message=  editText_message.getText().toString().trim();
                    switch (motionEvent.getAction()) {
                        case 0:
                            curve.startAnimation(circleAnimation);
                            cancel = false;

                            //  Toast.makeText(MainActivity.this, "start", Toast.LENGTH_SHORT).show();
                            return true;
                        case 1:
                        case 3:
                            // Toast.makeText(MainActivity.this, "SuccessFull", Toast.LENGTH_SHORT).show();
                            circleAnimation.cancel();
                            cancel = true;
                            return true;
                        default:
                            return false;
                    }
                }
                return true;
            }
        });

        bottomSheetAlam.setContentView(view1);
        bottomSheetAlam.show();*/

        //   OpenAlert();
    }

    //open alert
    private void OpenAlert() {

        bottomSheetAlam = new BottomSheetDialog(this);
        LayoutInflater Inflater = getLayoutInflater();
        View view1 = Inflater.inflate(R.layout.alarm_bottom_sheet_dialog, null);

        CheckBox checkBox = view1.findViewById(R.id.check_Terms_Conditions);

        Spinner spinner_alertType = (Spinner) view1.findViewById(R.id.alertType_spinner);
        ArrayList<String> arrayList_alertType = new ArrayList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, alertType_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain = new JSONObject(response);

                    arrayList_alertType.clear();

                    arrayList_alertType.add("Select--Alert--Type--");

                    if (jsonObjectMain.getString("message").contains("Data is available!")) {

                        JSONArray jsonArray = jsonObjectMain.getJSONArray("alerType");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            arrayList_alertType.add(jsonObject.getString("name"));
                        }

                        ArrayAdapter<String> arrayAdapter_alert = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList_alertType);
                        spinner_alertType.setAdapter(arrayAdapter_alert);


                        spinner_alertType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                alertTypeName = arrayList_alertType.get(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {
                        Toast.makeText(CompanyMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                netWorkError(error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error", "tec71");

                return hashMap;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(CompanyMapsActivity.this);
        requestQueue.add(stringRequest);

        editText_message = view1.findViewById(R.id.edit_userMessag);

        curve = view1.findViewById(R.id.curve_alarm);
        taplayout = view1.findViewById(R.id.layouttap);
        final CircleAnimation circleAnimation = new CircleAnimation(curve, 127.0f);
        this.curve.setCurveColor(ContextCompat.getColor(this, R.color.Fuchsia), ContextCompat.getColor(this, R.color.tap_dark));
        circleAnimation.setDuration(2000);
        circleAnimation.setAnimationListener(this);

        taplayout.setOnTouchListener((view, motionEvent) -> {
            if (!checkBox.isChecked()) {
                Toast.makeText(getApplicationContext(), "Select Terms Conditions", Toast.LENGTH_SHORT).show();
                checkBox.setError("Terms Conditions!!");
            } else if (spinner_alertType.getSelectedItem().toString().contains("Select--Alert--Type--")) {

                Toast.makeText(CompanyMapsActivity.this, "Please Your Alet Type", Toast.LENGTH_SHORT).show();

            } else {

                // String message=  editText_message.getText().toString().trim();
                switch (motionEvent.getAction()) {
                    case 0:
                        curve.startAnimation(circleAnimation);
                        cancel = false;

                        //  Toast.makeText(MainActivity.this, "start", Toast.LENGTH_SHORT).show();
                        return true;
                    case 1:
                    case 3:
                        // Toast.makeText(MainActivity.this, "SuccessFull", Toast.LENGTH_SHORT).show();
                        circleAnimation.cancel();
                        cancel = true;
                        return true;
                    default:
                        return false;
                }
            }
            return true;
        });

        bottomSheetAlam.setContentView(view1);
        bottomSheetAlam.show();

    }

    private void showAllMarkers(GoogleMap mMap, boolean isMarker) {

        if (isMarker) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (MarkerOptions m : markerList) {
                builder.include(m.getPosition());
                // add the parkers to the map
                mMap.addMarker(m);
                //mMap.animateCamera(CameraUpdateFactory.zoomBy(14));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 12));
            }

            /*LatLngBounds bounds = builder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.30);
            // Zoom and animate the google map to show all markers
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mMap.animateCamera(cu);
            mMap.animateCamera(CameraUpdateFactory.zoomBy(25));*/


        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String title = marker.getTitle();
                if (isMarker) {

                    merkerClick(marker.getPosition().latitude, marker.getPosition().longitude, title);


                } else {
                    if (dragMerker == null) {

                    } else {
                        if (dragMerker.getPosition().latitude == marker.getPosition().latitude ||
                                dragMerker.getPosition().longitude == marker.getPosition().longitude) {
                            merkerClick(marker.getPosition().latitude, marker.getPosition().longitude, title);

                        } else {

                        }
                    }
                }

                return false;

            }
        });

    }

    private void merkerClick(double latitude, double longitude, String title) {
        vibrator(getApplicationContext());
        BottomSheetDialog bottomSheet_option = new BottomSheetDialog(CompanyMapsActivity.this);
        LayoutInflater Inflater = getLayoutInflater();
        View view = Inflater.inflate(R.layout.alert_option_select, null);
        bottomSheet_option.setContentView(view);
        bottomSheet_option.show();
        Button button_flat = view.findViewById(R.id.button_flat_submit);

        /*if (title.equalsIgnoreCase("Select Position"))
            button_flat.setText("Danger");
        else
            button_flat.setText("Flat");*/

        button_flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dragMerker == null) {
                    Show_loaction_wise_Storage(latitude, longitude);

                } else {
                    if (dragMerker.getPosition().latitude == latitude || dragMerker.getPosition().longitude == longitude) {
                        setComments();
                    } else {
                        Show_loaction_wise_Storage(latitude, longitude);

                    }
                }
                bottomSheet_option.dismiss();
            }
        });

        Button button_alert = view.findViewById(R.id.floating_alert);


        button_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenAlert();

                LatLng latLng_drag = new LatLng(latitude, longitude);

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {

                    List<Address> addresses = geocoder.getFromLocation(latLng_drag.latitude, latLng_drag.longitude, 1);
                    if (addresses.size() > 0) {

                        String address = addresses.get(0).getAddressLine(0);
                        sharedPreferences_type.edit().putString("user_Address", address).commit();
                        sharedPreferences_type.edit().putString("latitude", String.valueOf(latLng_drag.latitude)).commit();
                        sharedPreferences_type.edit().putString("longitude", String.valueOf(latLng_drag.longitude)).commit();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bottomSheet_option.dismiss();
            }
        });

    }

    public static void vibrator(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(50);
        }
    }

    private void sendAlet(String alertTypeName) {


        String accessTocken = sharedPreferences_type.getString("access_token", "access_token found");

        String user_Address = sharedPreferences_type.getString("user_Address", "user_Address found");
        String latitude_storage = sharedPreferences_type.getString("latitude", "user_Address found");
        String longitude_storage = sharedPreferences_type.getString("longitude", "user_Address found");

        String storage_details = user_Address + "" + latitude_storage + "" + longitude_storage;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, send_alert_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain = new JSONObject(response);

                    if (jsonObjectMain.getString("message").contains("Alert Send Successfully!")) {

                        MaterialDialog mDialog = new MaterialDialog.Builder(CompanyMapsActivity.this)
                                .setTitle("Successfully Send Notification..")
                                .setAnimation(R.raw.successful)
                                .setCancelable(false)
                                .setPositiveButton("Ok", new AbstractDialog.OnClickListener() {
                                    @Override
                                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                        dialogInterface.dismiss();

                                    }
                                })
                                .setNegativeButton("Cancel", new AbstractDialog.OnClickListener() {
                                    @Override
                                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .build();
                        mDialog.show();

                        Toast.makeText(CompanyMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(CompanyMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkError(error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error", "tec71");
                hashMap.put("axcess_token", accessTocken);
                hashMap.put("alert_type", alertTypeName);
                hashMap.put("detils", storage_details);
                hashMap.put("latitude", latitude_storage);
                hashMap.put("longtude", longitude_storage);
                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CompanyMapsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void Show_loaction_wise_Storage(double latitude, double longitude) {
        progressDialog.show();
        String access_token = sharedPreferences_type.getString("access_token", "default_access_token001");

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater layoutInflater1 = getLayoutInflater();
        View view1 = layoutInflater1.inflate(R.layout.user_location_storag_show, null);
        bottomSheetDialog.setContentView(view1);

        recyclerView_locationStorag = (RecyclerView) view1.findViewById(R.id.recyclerView_locationStorag);
        recyclerView_locationStorag.setHasFixedSize(true);
        recyclerView_locationStorag.setLayoutManager(new LinearLayoutManager(this));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, locatinWith_storage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain = new JSONObject(response);

                    if (jsonObjectMain.getString("message").contains("Data is available!")) {

                        JSONArray jsonArray = jsonObjectMain.getJSONArray("districts");

                        locationWithStorageShowList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            JSONArray comments = jsonObject.getJSONArray("allcomment");
                            List<AllComment> commentList = new ArrayList<>();
                            for (int j = 0; j < comments.length(); j++) {
                                JSONObject comment = comments.getJSONObject(j);
                                if (comment.getString("status").equals("1")) {
                                    commentList.add(new AllComment(
                                            comment.getString("id"),
                                            comment.getString("store_id"),
                                            comment.getString("user_id"),
                                            comment.getString("comment"),
                                            comment.getString("alert_tag"),
                                            comment.getString("status"),
                                            comment.getString("loc_id"),
                                            comment.getString("created_at"),
                                            comment.getString("updated_at")

                                    ));
                                }
                            }

                            String image = "https://fifaar.com/public/" + jsonObject.getString("storage_img");

                            if (jsonObject.getString("status").contains("1")) {

                                LocationWithStorageShow withStorageShow =
                                        new LocationWithStorageShow(jsonObject.getString("id"),
                                                jsonObject.getString("floor"),
                                                jsonObject.getString("company_name"),
                                                jsonObject.getString("company_owner"),
                                                jsonObject.getString("license_approved_date"),
                                                jsonObject.getString("license_renew_date"),
                                                jsonObject.getString("address"),
                                                jsonObject.getString("division"),
                                                jsonObject.getString("distric"),
                                                jsonObject.getString("thana"),
                                                jsonObject.getString("company_type"),
                                                jsonObject.getString("company_detils"),
                                                image,
                                                jsonObject.getString("alert_tag"),
                                                jsonObject.getString("status"),
                                                commentList);

                                locationWithStorageShowList.add(withStorageShow);
                            }

                        }
                        String l_id = locationId.get(latitude + "" + longitude);
                        location_wishStorageController = new Location_wishStorageController(l_id, locationWithStorageShowList, CompanyMapsActivity.this, true);
                        recyclerView_locationStorag.setAdapter(location_wishStorageController);
                        //location_wishStorageController.notifyDataSetChanged();

                        Button submitButton = view1.findViewById(R.id.submit_btn);
                        Spinner floorSpinner = view1.findViewById(R.id.floor_spinner);
                        Spinner colorSpinner = view1.findViewById(R.id.color_spiner);
                        EditText commentEt = view1.findViewById(R.id.comment_edittext);

                        String[] colorData = new String[]{
                                "Select--Danger--Variation--", "Green", "Red"
                        };

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                CompanyMapsActivity.this,
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                colorData
                        );
                        colorSpinner.setAdapter(adapter);
                        submitButton.setOnClickListener(view2 -> {
                            String floor = floorSpinner.getSelectedItem().toString();
                            String color = colorSpinner.getSelectedItem().toString();
                            String comment = commentEt.getText().toString();
                            if (floor.equals("Select--Floor--")) {
                                Toast.makeText(CompanyMapsActivity.this, "Select a floor...", Toast.LENGTH_SHORT).show();
                            } else if (color.equals("Select color…")) {
                                Toast.makeText(CompanyMapsActivity.this, "Select a color...", Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(comment)) {
                                commentEt.setError("Enter a comment...");
                            } else {
                                /*String id = "-001";
                                for (LocationWithStorageShow item : locationWithStorageShowList) {
                                    if (floor.equalsIgnoreCase(item.getFloor())) {
                                        id = item.getId();
                                    }
                                }*/
                                addComment(l_id, comment, color, floor, longitude, latitude);
                                /*if (id.equals("-001"))
                                    Toast.makeText(CompanyMapsActivity.this, "Select a valid floor...", Toast.LENGTH_SHORT).show();
                                else addComment(id, comment, color, floor);*/
                            }
                        });

                        bottomSheetDialog.show();

                    } else {
                        Toast.makeText(CompanyMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    }

                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CompanyMapsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkError(error);
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error", "tec71");
                hashMap.put("axcess_token", access_token);
                hashMap.put("latitude", String.valueOf(latitude));
                hashMap.put("longtude", String.valueOf(longitude));
                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CompanyMapsActivity.this);
        requestQueue.add(stringRequest);

    }

    private void addComment(String id, String comment, String color, String floor, double longitude, double latitude) {
        progressDialog.show();
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);
        String accessTocken = sp.getString("access_token", "access_token found");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, addCommentAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain = new JSONObject(response);

                    if (jsonObjectMain.getString("message").contains("Successfully")) {
                        //bottomSheetDialog.dismiss();
                        Toast.makeText(context, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(context, "" + jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    Show_loaction_wise_Storage(latitude, longitude);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkError(error);
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error", "tec71");
                hashMap.put("location_id", id);
                hashMap.put("axcess_token", accessTocken);
                hashMap.put("comment", comment);
                hashMap.put("alert_tag", color);
                hashMap.put("floor", floor);
                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finishAffinity();
                    }
                }).show();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (cancel) {

            Log.d("anim", "Incomplete");

        } else {

            sendAlet(alertTypeName);
            bottomSheetAlam.dismiss();

        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);
        // locationRequest.setFastestInterval(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        LatLng latLng_curent = new LatLng(location.getLatitude(), location.getLongitude());

        myLatitud = location.getLatitude();
        myLongitude = location.getLongitude();
        //mMap.setMyLocationEnabled(false);
        sharedPreferences_type.edit().putString("myLatitud", String.valueOf(location.getLatitude())).commit();
        sharedPreferences_type.edit().putString("myLongitude", String.valueOf(location.getLongitude())).commit();
        try {

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng_curent);
            markerOptions.title("Current Location");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            mMap.addMarker(markerOptions);

           /* timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (current_icon < icons_demo_list.length-1) {
                        CompanyMapsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                // update UI here
                                mMap.clear();
                                //Bitmap bitmap = AnimationDr.markerWithDuration(InCityBooking.this, "0");//String.valueOf(Math.round(duration)
                                //gmp.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                                mMap.addMarker(new MarkerOptions().position(latLng_curent).icon(BitmapDescriptorFactory.fromResource(icons_demo_list[current_icon])));
                            }
                        });
                        Log.d("tag", "location updated.......");
                        current_icon++;
                    } else {
                        current_icon = 0;
                    }
                }
            }, 0, 300);*/

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_curent, 17));
        } catch (Exception exception) {

            Toast.makeText(getApplicationContext(), "error:" + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }

        UpdateLocation(location.getLatitude(), location.getLongitude());
        ShowStorage(location.getLatitude(), location.getLongitude());
    }


}