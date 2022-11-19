package com.techno71.fireservice.View;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;
import com.techno71.fireservice.Model.MyLocation;
import com.techno71.fireservice.R;
import com.techno71.fireservice.databinding.ActivityUserMapBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserMapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        LocationListener, android.location.LocationListener, DirectionCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SharedPreferences sharedPreferences_type;
    private String access_token = "";
    private String phone = "";
    private ArrayList markerPoints = new ArrayList();
    protected LatLng start = null;
    protected LatLng end = null;

    private ActivityUserMapBinding binding;
    //polyline object
    private List<Polyline> polylines = null;
    private DatabaseReference databaseReference;
    private LatLng userLocationLat;
    private LatLng myLocationLat;

    //working on demo
    int[] icons_fire_list = {
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
            R.drawable.fire_14
    };
    int[] icons_car_burn_list = {
            R.drawable.car_brurn_1,
            R.drawable.car_brurn_2,
            R.drawable.car_brurn_3,
            R.drawable.car_brurn_4,
            R.drawable.car_brurn_5,
            R.drawable.car_brurn_6,
            R.drawable.car_brurn_7,
            R.drawable.car_brurn_8,
            R.drawable.car_brurn_9,
            R.drawable.car_brurn_10,
            R.drawable.car_brurn_11,
            R.drawable.car_brurn_12,
            R.drawable.car_brurn_13,
            R.drawable.car_brurn_14,
            R.drawable.car_brurn_15,
            R.drawable.car_brurn_16
    };
    int[] icons_ship_burn_list = {
            R.drawable.ship_burn_1,
            R.drawable.ship_burn_2,
            R.drawable.ship_burn_3,
            R.drawable.ship_burn_4,
            R.drawable.ship_burn_5,
            R.drawable.ship_burn_6,
            R.drawable.ship_burn_7,
            R.drawable.ship_burn_8,
            R.drawable.ship_burn_9,
            R.drawable.ship_burn_10,
            R.drawable.ship_burn_11,
            R.drawable.ship_burn_12,
            R.drawable.ship_burn_13,
            R.drawable.ship_burn_14,
    };
    int[] icons_building_burn_list = {
            R.drawable.house_burn_1,
            R.drawable.house_burn_2,
            R.drawable.house_burn_3,
            R.drawable.house_burn_4,
            R.drawable.house_burn_5,
            R.drawable.house_burn_6,
            R.drawable.house_burn_7,
            R.drawable.house_burn_8,
            R.drawable.house_burn_9,
            R.drawable.house_burn_10,
            R.drawable.house_burn_11,
            R.drawable.house_burn_12,
            R.drawable.house_burn_13,
            R.drawable.house_burn_14,
            R.drawable.house_burn_15,
    };
    int[] icons_siron_list = {
            R.drawable.siran_1,
            R.drawable.siran_2,
            R.drawable.siran_3,
            R.drawable.siran_4,
            R.drawable.siran_5,
            R.drawable.siran_6,
            R.drawable.siran_7,
            R.drawable.siran_8,
            R.drawable.siran_9,
            R.drawable.siran_10,
            R.drawable.siran_11,
            R.drawable.siran_12,
            R.drawable.siran_13,
    };
    boolean timer_running = false;
    Timer timer = new Timer();
    int current_icon = 0;
    String type = "fire";

    public UserMapActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences_type = getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        databaseReference = FirebaseDatabase.getInstance().getReference("FireService").child("Agent_Location_Details");
        access_token = sharedPreferences_type.getString("access_token", "default_access_token001");
        type = sharedPreferences_type.getString("alert_type", "fire");
        Log.d("taf", "type is " + type);
        phone = sharedPreferences_type.getString("phone", "01700000000");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        double latitude = Double.parseDouble("" + sharedPreferences_type.getFloat("latitude001", 00.00f));
        double longitude = Double.parseDouble("" + sharedPreferences_type.getFloat("longitude001", 00.00f));

        //userLocaton
        userLocationLat = new LatLng(latitude, longitude);

        checkLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_user);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        binding.kmTv.setVisibility(View.GONE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            checkLocationPermission();

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        // Enable / Disable my location button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(@NonNull Location location) {

                MyLocation myLocation = new MyLocation(
                        "" + location.getLatitude(),
                        "" + location.getLongitude(),
                        "" + access_token,
                        "" + phone

                );
                databaseReference.child(access_token).setValue(myLocation);
                myLocationLat = new LatLng(location.getLatitude(), location.getLongitude());
                getLocationMap();
            }
        });


    }


    private String[] colors = {"#7fff7272", "#7f31c7c5", "#7fff8a00"};
    private Double distance;

    private boolean isActivityStart = true;

    @SuppressLint("DefaultLocale")
    private void getLocationMap() {

        if (myLocationLat == null) {

        } else {

            if (userLocationLat == null) {

                //mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocationLat));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

            } else {
                distance = SphericalUtil.computeDistanceBetween(myLocationLat, userLocationLat);
                //Toast.makeText(this, "Distance \n " + String.format("%.2f", distance / 1000) + "km", Toast.LENGTH_SHORT).show();

                if (isActivityStart) {
                    isActivityStart = false;
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocationLat));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                }
                mMap.setMinZoomPreference(6.0f);
                mMap.setMaxZoomPreference(17.5f);
                MarkerOptions markerOptions1 = new MarkerOptions();
                markerOptions1.position(userLocationLat);
                markerOptions1.title("User Position");
                if ((sharedPreferences_type.getString("imgType", null)) != null) {
                    if (!timer_running) {
                        timer_running=true;
                        if (sharedPreferences_type.getString("imgType", null).contains("'s on fire")) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (type.equalsIgnoreCase("fire")) {
                                        if (current_icon < icons_fire_list.length - 1) {
                                            UserMapActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    // update UI here
                                                    mMap.clear();
                                                    mMap.addMarker(new MarkerOptions().position(userLocationLat).icon(BitmapDescriptorFactory.fromResource(icons_fire_list[current_icon])));
                                                }
                                            });

                                            Log.d("tag", "location updated.......");
                                            current_icon++;
                                        } else {
                                            current_icon = 0;
                                        }
                                    } else {
                                        Log.d("dddd", "not fire type is   " + type);
                                    }
                                }
                            }, 0, 300);
                        } else if (sharedPreferences_type.getString("imgType", null).contains("Lunch")) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (type.equalsIgnoreCase("fire")) {
                                        if (current_icon < icons_ship_burn_list.length - 1) {
                                            UserMapActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    // update UI here
                                                    mMap.clear();
                                                    mMap.addMarker(new MarkerOptions().position(userLocationLat).icon(BitmapDescriptorFactory.fromResource(icons_ship_burn_list[current_icon])));
                                                }
                                            });

                                            Log.d("tag", "location updated.......");
                                            current_icon++;
                                        } else {
                                            current_icon = 0;
                                        }
                                    } else {
                                        Log.d("dddd", "not fire type is   " + type);
                                    }
                                }
                            }, 0, 300);

                        } else if (sharedPreferences_type.getString("imgType", null).contains("The car")) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (type.equalsIgnoreCase("fire")) {
                                        if (current_icon < icons_car_burn_list.length - 1) {
                                            UserMapActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    // update UI here
                                                    mMap.clear();
                                                    mMap.addMarker(new MarkerOptions().position(userLocationLat).icon(BitmapDescriptorFactory.fromResource(icons_car_burn_list[current_icon])));
                                                }
                                            });

                                            Log.d("tag", "location updated.......");
                                            current_icon++;
                                        } else {
                                            current_icon = 0;
                                        }
                                    } else {
                                        Log.d("dddd", "not fire type is   " + type);
                                    }
                                }
                            }, 0, 300);
                        } else if (sharedPreferences_type.getString("imgType", null).contains("The Bulding")) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (type.equalsIgnoreCase("fire")) {
                                        if (current_icon < icons_building_burn_list.length - 1) {
                                            UserMapActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    // update UI here
                                                    mMap.clear();
                                                    mMap.addMarker(new MarkerOptions().position(userLocationLat).icon(BitmapDescriptorFactory.fromResource(icons_building_burn_list[current_icon])));
                                                }
                                            });

                                            Log.d("tag", "location updated.......");
                                            current_icon++;
                                        } else {
                                            current_icon = 0;
                                        }
                                    } else {
                                        Log.d("dddd", "not fire type is   " + type);
                                    }
                                }
                            }, 0, 300);
                        } else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (type.equalsIgnoreCase("fire")) {
                                        if (current_icon < icons_siron_list.length - 1) {
                                            UserMapActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    // update UI here
                                                    mMap.clear();
                                                    mMap.addMarker(new MarkerOptions().position(userLocationLat).icon(BitmapDescriptorFactory.fromResource(icons_siron_list[current_icon])));
                                                }
                                            });

                                            Log.d("tag", "location updated.......");
                                            current_icon++;
                                        } else {
                                            current_icon = 0;
                                        }
                                    } else {
                                        Log.d("dddd", "not fire type is   " + type);
                                    }
                                }
                            }, 0, 300);
                        }
                    } else {
                        Log.d(TAG, "timer already running..... on user and company side");
                    }

                } else {
                    if (sharedPreferences_type.getString("img", null) == null) {
                        markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    } else {
                        markerOptions1.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromLayout(R.layout.map_gif_load, "" + sharedPreferences_type.getString("img", null), 0)));
                    }
                    positionMarker = mMap.addMarker(markerOptions1);
                }
                Log.d("TAg", "imge type issssssssssss   " + sharedPreferences_type.getString("imgType", null));


                GoogleDirection.withServerKey(getResources().getString(R.string.google_map_key))
                        .from(myLocationLat)
                        .to(userLocationLat)
                        .avoid(AvoidType.HIGHWAYS)
                        .transportMode(TransportMode.WALKING)
                        //.alternativeRoute(true)
                        .execute(this);
            }
        }


    }

    private Marker positionMarker;

    @SuppressLint("SetTextI18n")
    @Override
    public void onDirectionSuccess(@Nullable Direction direction) {
        assert direction != null;

        if (direction.isOK()) {

            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.position(userLocationLat);
            markerOptions1.title("User Position");

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    myLocationLat, 16f);

            new CountDownTimer(500, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (positionMarker != null) {
                        positionMarker.remove();
                        positionMarker = null;
                    }
                    if (sharedPreferences_type.getString("img", null) == null) {
                        markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        positionMarker = mMap.addMarker(markerOptions1);
                    } else {
                        markerOptions1.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromLayout(R.layout.map_gif_load, "" + sharedPreferences_type.getString("img", null), 0)));
                        positionMarker = mMap.addMarker(markerOptions1);

                    }

                }

                @Override
                public void onFinish() {

                }
            }.start();
            //mMap.addMarker(markerOptions);

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));

            binding.kmTv.setVisibility(View.VISIBLE);
            binding.kmTv.setText("Distance \n " + String.format("%.2f", distance / 1000) + "km");
        }
    }

    private int isUrlCheckPosition = 1;

    private Bitmap getBitmapFromLayout(int layout, String url, int id) {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layout, null);
        ImageView img = view.findViewById(R.id.img);
        //GifImageView gifImageView=view.findViewById(R.id.gif);
        Glide.with(getApplicationContext()).load(url).into(img);


        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

        Drawable drawable = view.getBackground();
        if (drawable != null)

            drawable.draw(canvas);

        view.draw(canvas);


        //Setup anim with desired properties
        return bitmap;
    }

    @Override
    public void onDirectionFailure(@NonNull Throwable t) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    private void getLocation() {
        try {
            LocationManager locationManager = null;
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0.1f, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            getLocation();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted. Do the
                // contacts-related task you need to do.
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    assert mapFragment != null;
                    mapFragment.getMapAsync(this);
                }

            } else {

                checkLocationPermission();
                // Permission denied, Disable the functionality that depends on this permission.
                //Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityStart = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isActivityStart = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityStart = true;
    }


}