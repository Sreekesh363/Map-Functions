package mappins.sreekesh.com.mappins;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
<<<<<<< HEAD
import android.os.Handler;
=======
>>>>>>> 794d65945a412deac1cfedab69b98795fe437c4f
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import mappins.sreekesh.com.mappins.model.Contract;
import mappins.sreekesh.com.mappins.model.PlaceMarker;
import mappins.sreekesh.com.mappins.utils.DBHelper;
import mappins.sreekesh.com.mappins.utils.PrefHelper;
import mappins.sreekesh.com.mappins.utils.ZoomOutPageTransformer;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerDragListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowLongClickListener, GoogleMap.OnMyLocationChangeListener, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor>, ActivityCompat.OnRequestPermissionsResultCallback, DetailsFragment.DetailsFragmentCallbacks {

    private static final String[] MAP_DATA_PROJECTION = {
            Contract.MapDataEntry._ID,
            Contract.MapDataEntry.COLUMN_MAP_DATA_ID,
            Contract.MapDataEntry.COLUMN_MAP_DATA_NAME,
            Contract.MapDataEntry.COLUMN_MAP_DATA_LATITUDE,
            Contract.MapDataEntry.COLUMN_MAP_DATA_LONGITUDE,
            Contract.MapDataEntry.COLUMN_MAP_DATA_ADDRESS,
    };

    public static final int COL_MAP_DATA_ID = 1;
    public static final int COL_MAP_DATA_NAME = 2;
    public static final int COL_MAP_DATA_LATITUDE = 3;
    public static final int COL_MAP_DATA_LONGITUDE = 4;
    public static final int COL_MAP_DATA_ADDRESS = 5;

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 21;
    public static final int PIN_LOADER = 1;

    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_RESOLVE_ERROR = 2;
    private static final String DIALOG_ERROR = "dialog_error_play_services";
    private static final String DIALOG_TAG = "dialog_tag_error_play_services";

    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    private ErrorDialogFragment mDialogFragment;
    private LocationRequest mLocationRequestBalancedPowerAccuracy;

    private Dialog dialog;
    private GoogleMap mMap;
    private ArrayList<PlaceMarker> markerDetailsList;
    private HashMap<Marker,PlaceMarker> mapMarkerList;
    private Marker draggingMarker;
    private PlaceMarker draggingMarkerItem;
    private PrefHelper prefHelper;
    private Location mLastLocation;
    private RelativeLayout progressBar;
    private TextView progressBarText;
    private ViewPager mViewPager;
    private SamplePagerAdapter mPagerAdapter;
    private SlidingUpPanelLayout slideLayout;
    private TextView noDataText;
    private ImageView progressBarImage;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        enableLocationServices();
        markerDetailsList = new ArrayList<>();
        mapMarkerList = new HashMap<>();
        prefHelper = new PrefHelper(this);
        initMembers();
        mPagerAdapter = new SamplePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        Toolbar mToolbar = (Toolbar) findViewById(R.id.header_toolbar);
        ImageButton clearMap = (ImageButton) mToolbar.findViewById(R.id.clear_map);
        clearMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder details_builder = new android.support.v7.app.AlertDialog.Builder(MapsActivity.this);
                LayoutInflater details_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View no_pin_prompt = details_inflater.inflate(R.layout.clear_maps_dialog, null);
                details_builder.setView(no_pin_prompt);
                Button okButton = (Button) no_pin_prompt.findViewById(R.id.confirm_button);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelDialog();
                        DBHelper dbHelper = new DBHelper(MapsActivity.this);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("delete from " + Contract.MapDataEntry.TABLE_NAME);
                        db.close();
                        clearMap();
                        getContentResolver().notifyChange(Contract.MapDataEntry.CONTENT_URI,null);
                    }
                });
                Button cancelButton = (Button) no_pin_prompt.findViewById(R.id.cancel_button);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelDialog();
                    }
                });
                dialog = details_builder.show();
            }
        });
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(progressBarImage);
        Glide.with(this).load(R.drawable.gps).into(imageViewTarget);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initMembers() {
        progressBar = (RelativeLayout) findViewById(R.id.progressBarHolder);
        progressBarText = (TextView) findViewById(R.id.progressBarText);
        progressBarImage = (ImageView) findViewById(R.id.progressBarImage);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        noDataText = (TextView) findViewById(R.id.no_data);
        slideLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
    }

    @Override
    public void zoomToPin(LatLng position) {
        try {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                    .tilt(0)
                    .bearing(0f)
                    .zoom(15.75f)
                    .target(position)
                    .build()), 1000, null);
            slideLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }catch (Exception e){
            Log.e("Zoom to Next Pin","Exception:" + e.toString());
        }
    }

    public class SamplePagerAdapter extends FragmentStatePagerAdapter {

        SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            DetailsFragment fragment = new DetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id",markerDetailsList.get(position).getId());
            bundle.putString("name",markerDetailsList.get(position).getName());
            bundle.putDouble("latitude",markerDetailsList.get(position).getLatitude());
            bundle.putDouble("longitude",markerDetailsList.get(position).getLongitude());
            bundle.putString("address",markerDetailsList.get(position).getAddress());
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return markerDetailsList.size();
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(), "Enable location services for seamless functioning", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Location services unavailable on this device, functionality may be affected", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case REQUEST_RESOLVE_ERROR:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mResolvingError = false;
                        if (mDialogFragment != null) {
                            mDialogFragment.dismiss();
                            mDialogFragment = null;
                        }
                        if (!mGoogleApiClient.isConnecting() &&
                                !mGoogleApiClient.isConnected()) {
                            mGoogleApiClient.connect();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        if (mDialogFragment != null) {
                            mDialogFragment.dismiss();
                            mDialogFragment = null;
                        }
                        Toast.makeText(getApplicationContext(), "Please install google play services and try again", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    default:
                        if (mDialogFragment != null) {
                            mDialogFragment.dismiss();
                            mDialogFragment = null;
                        }
                        Toast.makeText(getApplicationContext(), "Unsupported device", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void enableLocationServices() {
        buildGoogleApiClient();
        setUpLocationRequests();
    }

    public void initiateLocation() {
        if (mGoogleApiClient.isConnected()){
            Location mLastLocation;
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }else{
                return;
            }
            if(mLastLocation!=null) {
                onMyLocationChange(mLastLocation);
            }else{
                Log.e(TAG,"Last location is null");
            }
        }
    }

    private void setUpLocationRequests() {
        mLocationRequestBalancedPowerAccuracy = new LocationRequest();
        mLocationRequestBalancedPowerAccuracy.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        } else {
            mResolvingError = true;
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequestBalancedPowerAccuracy)
                .setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Location mLastLocation = new Location("dummyProvider");
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        }
                        if(mLastLocation!=null) {
                            onMyLocationChange(mLastLocation);
                        }
                        return;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MapsActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        Toast.makeText(getApplicationContext(), "Location services not available. Please enable them for proceeding further.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                String text = "Please allow the application to access the LOCATION permission";
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                        .setDuration(Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        });
                snackbar.setActionTextColor(getResources().getColor(R.color.snackbar_teel));
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(getResources().getColor(R.color.snackbar_yellow));
                snackbar.show();
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setTiltGesturesEnabled(false);
            googleMap.setOnMyLocationChangeListener(this);
            googleMap.setIndoorEnabled(false);
            initiateLocation();
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    View mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                    TextView title = (TextView) mWindow.findViewById(R.id.name);
                    TextView locality = (TextView) mWindow.findViewById(R.id.address);
                    title.setText(mapMarkerList.get(marker).getName());
                    locality.setText(mapMarkerList.get(marker).getAddress());
                    return mWindow;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View mContents = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                    TextView title = (TextView) mContents.findViewById(R.id.name);
                    TextView locality = (TextView) mContents.findViewById(R.id.address);
                    title.setText(mapMarkerList.get(marker).getName());
                    locality.setText(mapMarkerList.get(marker).getAddress());
                    return mContents;
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (mMap != null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
                        mMap.getUiSettings().setCompassEnabled(false);
                        mMap.setOnMyLocationChangeListener(this);
                        mMap.getUiSettings().setTiltGesturesEnabled(false);
                        mMap.setIndoorEnabled(false);
                        initiateLocation();
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker marker) {
                                View mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                                TextView title = (TextView) mWindow.findViewById(R.id.name);
                                TextView locality = (TextView) mWindow.findViewById(R.id.address);
                                title.setText(mapMarkerList.get(marker).getName());
                                locality.setText(mapMarkerList.get(marker).getAddress());
                                return mWindow;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                View mContents = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                                TextView title = (TextView) mContents.findViewById(R.id.name);
                                TextView locality = (TextView) mContents.findViewById(R.id.address);
                                title.setText(mapMarkerList.get(marker).getName());
                                locality.setText(mapMarkerList.get(marker).getAddress());
                                return mContents;
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Contract.MapDataEntry.CONTENT_URI,
                MAP_DATA_PROJECTION,
                null,
                null,
                Contract.MapDataEntry.COLUMN_MAP_DATA_NAME + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(loader.getId()==PIN_LOADER) {
            if (data != null && data.moveToFirst()) {
                noDataText.setVisibility(View.GONE);
                markerDetailsList.clear();
                do {
                    markerDetailsList.add(new PlaceMarker(data.getInt(COL_MAP_DATA_ID),
                            data.getString(COL_MAP_DATA_NAME),
                            data.getDouble(COL_MAP_DATA_LATITUDE),
                            data.getDouble(COL_MAP_DATA_LONGITUDE),
                            data.getString(COL_MAP_DATA_ADDRESS)));
                } while (data.moveToNext());
                insertMarkers(markerDetailsList);
                if(mPagerAdapter==null){
                    mPagerAdapter = new SamplePagerAdapter(getSupportFragmentManager());
                    mViewPager.setAdapter(mPagerAdapter);
                }else {
                    mPagerAdapter.notifyDataSetChanged();
                }
                mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            }else{
                noDataText.setVisibility(View.VISIBLE);
                android.support.v7.app.AlertDialog.Builder details_builder = new android.support.v7.app.AlertDialog.Builder(this);
                LayoutInflater details_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View no_pin_prompt = details_inflater.inflate(R.layout.no_pin_dialog, null);
                details_builder.setView(no_pin_prompt);
                Button okButton = (Button) no_pin_prompt.findViewById(R.id.ok_button);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelDialog();
                    }
                });
                dialog = details_builder.show();
                if(mViewPager.getAdapter()!=null){
                    mViewPager.setAdapter(null);
                    mPagerAdapter = null;
                }
            }
        }else{
            Log.i(TAG,"Loader is invalid");
        }
    }

    private void insertMarkers(ArrayList<PlaceMarker> markerList) {
        try{
            if(markerList!=null && markerList.size()>0){
                if(mapMarkerList.size()>0) {
                    ArrayList<PlaceMarker> removeList = new ArrayList<>();
                    for (PlaceMarker mapMarker : mapMarkerList.values()) {
                        if (!markerDetailsList.contains(mapMarker)) {
                            removeList.add(mapMarker);
                        }
                    }
                    if(removeList.size()>0) {
                        ArrayList<Marker> removeMarkerList = new ArrayList<>();
                        for (Marker markerInMap : mapMarkerList.keySet()) {
                            if (removeList.contains(mapMarkerList.get(markerInMap))) {
                                removeMarkerList.add(markerInMap);
                                markerInMap.remove();
                            }
                        }
                        for(Marker markerRemoved: removeMarkerList){
                            mapMarkerList.remove(markerRemoved);
                        }
                    }
                }

                for(PlaceMarker marker: markerList) {
                    if (!mapMarkerList.containsValue(marker)) {
                        Marker generatedMarker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
                                        R.drawable.marker)))
                                .position(new LatLng(marker.getLatitude(),marker.getLongitude()))
                                .draggable(true)
                                .anchor(.5f, 1f));
                        mapMarkerList.put(generatedMarker,marker);
                    }
                }
                mPagerAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onMyLocationChange(Location location) {
        try {
            if (location != null) {
                if (mLastLocation == null) {
                    mLastLocation = location;
                    setProgressbarText("Scouting Your Location");
                    setProgressbar(true);

                    zoomIntoLocation(location, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            try {
                                setProgressbar(false);
                                setUpListeners();
                                getSupportLoaderManager().restartLoader(PIN_LOADER,null,MapsActivity.this);
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                                        .tilt(0)
                                        .bearing(0f)
                                        .zoom(15.75f)
                                        .target(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()))
                                        .build()), 2000, null);
                            }catch(Exception e){
                                e.printStackTrace();
                                Log.e(TAG,"Zoom into location finish exception :"+e.toString());
                                setProgressbar(false);
                            }
                        }

                        @Override
                        public void onCancel() {
                            try {
                                setProgressbar(false);
                            }catch(Exception e){
                                e.printStackTrace();
                                Log.e(TAG,"Zoom into location cancel exception :"+e.toString());
                                setProgressbar(false);
                                setUpListeners();
                            }
                        }
                    });
                } else {
                    mLastLocation = location;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,e.toString());
            setProgressbar(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_map:
                clearMap();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        try {
            draggingMarker = marker;
            draggingMarkerItem = mapMarkerList.get(marker);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        try {
            PlaceMarker mapMarker = mapMarkerList.get(marker);
            mapMarker.setLatitude(marker.getPosition().latitude);
            mapMarker.setLongitude(marker.getPosition().longitude);
            mapMarkerList.remove(draggingMarker);
            mapMarkerList.put(marker, mapMarker);
            int position = markerDetailsList.indexOf(draggingMarkerItem);
            markerDetailsList.remove(draggingMarkerItem);
            markerDetailsList.add(position, mapMarker);
            ContentValues cv = new ContentValues();
            cv.put(Contract.MapDataEntry.COLUMN_MAP_DATA_LATITUDE, marker.getPosition().latitude);
            cv.put(Contract.MapDataEntry.COLUMN_MAP_DATA_LONGITUDE, marker.getPosition().longitude);
            getContentResolver().update(Contract.MapDataEntry.CONTENT_URI,
                    cv,
                    Contract.MapDataEntry.COLUMN_MAP_DATA_ID + " = " + mapMarker.getId(),
                    null);
            mPagerAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }
    }

    @Override
    public void onInfoWindowLongClick(final Marker marker) {
        android.support.v7.app.AlertDialog.Builder details_builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater details_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View no_pin_prompt = details_inflater.inflate(R.layout.remove_pin_dialog, null);
        details_builder.setView(no_pin_prompt);
        Button okButton = (Button) no_pin_prompt.findViewById(R.id.confirm_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeMarker(marker);
                cancelDialog();
            }
        });
        Button cancelButton = (Button) no_pin_prompt.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog();
            }
        });
        dialog = details_builder.show();
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {
        android.support.v7.app.AlertDialog.Builder details_builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater details_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View select_place_prompt = details_inflater.inflate(R.layout.address_dialog, null);
        details_builder.setView(select_place_prompt);
        final EditText body=(EditText) select_place_prompt.findViewById(R.id.address);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(body, InputMethodManager.SHOW_IMPLICIT);
        Button okButton = (Button) select_place_prompt.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String address = body.getText().toString();
                    Random rand = new Random();
                    Integer id = rand.nextInt();
                    ContentValues cv = new ContentValues();
                    cv.put(Contract.MapDataEntry.COLUMN_MAP_DATA_ID,id);
                    cv.put(Contract.MapDataEntry.COLUMN_MAP_DATA_NAME,GenerateString(prefHelper.getMapPinNameSerialNumber()));
                    cv.put(Contract.MapDataEntry.COLUMN_MAP_DATA_LATITUDE,latLng.latitude);
                    cv.put(Contract.MapDataEntry.COLUMN_MAP_DATA_LONGITUDE,latLng.longitude);
                    cv.put(Contract.MapDataEntry.COLUMN_MAP_DATA_ADDRESS,address);
                    getContentResolver().insert(Contract.MapDataEntry.CONTENT_URI,cv);
                    prefHelper.setMapPinNameSerialNumber(prefHelper.getMapPinNameSerialNumber()+1);
                    getSupportLoaderManager().restartLoader(PIN_LOADER,null,MapsActivity.this);
                    cancelDialog();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(TAG,e.toString());
                }
            }
        });
        Button cancelButton = (Button) select_place_prompt.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog();
            }
        });
        dialog = details_builder.show();
    }

    private void setUpListeners() {
        mMap.setOnInfoWindowLongClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }

    public void zoomIntoLocation(Location location, GoogleMap.CancelableCallback callback){
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                .tilt(45)
                .bearing(0f)
                .zoom(17)
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .build()), 2000, callback);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void setProgressbar(boolean show) {
        try {
            if (progressBar != null) {
                if (show) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            Log.e("SetProgressBar Except", e.toString());
        }
    }

    public void setProgressbarText(String text) {
        if (progressBarText != null) {
            progressBarText.setText(text);
        }
    }

    //Method to Generate next alphabet in the sequence A,B,C...AA, AB, AC ....AAA, AAB, AAC etc..
    private String GenerateString(int i) {
        return i < 0 ? "" : GenerateString((i / 26) - 1) + (char)(65 + i % 26);
    }

    private void cancelDialog(){
        if(dialog!=null) {
            dialog.dismiss();
            dialog.cancel();
        }
    }

    private void clearMap() {
        try {
            markerDetailsList.clear();
            mPagerAdapter.notifyDataSetChanged();
            mMap.clear();
            mapMarkerList.clear();
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }
    }

    private void removeMarker(Marker marker){
        try{
            markerDetailsList.remove(mapMarkerList.get(marker));
            mPagerAdapter.notifyDataSetChanged();
            getContentResolver().delete(Contract.MapDataEntry.CONTENT_URI,
                    Contract.MapDataEntry.COLUMN_MAP_DATA_ID+" = "+mapMarkerList.get(marker).getId(),
                    null);
            getSupportLoaderManager().restartLoader(PIN_LOADER,null,MapsActivity.this);
            marker.remove();
            mapMarkerList.remove(marker);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }
    }

    /*public void printPins(){
        for(PlaceMarker marker: markerDetailsList){
            Log.e(TAG,"Name:"+marker.getName()+", Id:"+marker.getId()+", Address:"+marker.getAddress());
        }
    }*/

    private void showErrorDialog(int errorCode) {
        if (mDialogFragment == null) {
            mDialogFragment = new ErrorDialogFragment();
            Bundle args = new Bundle();
            args.putInt(DIALOG_ERROR, errorCode);
            mDialogFragment.setArguments(args);
            mDialogFragment.setCancelable(false);
            mDialogFragment.show(getSupportFragmentManager(), DIALOG_TAG);
        }
    }

    public static class ErrorDialogFragment extends DialogFragment {

        public ErrorDialogFragment() {
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }
    }
}

