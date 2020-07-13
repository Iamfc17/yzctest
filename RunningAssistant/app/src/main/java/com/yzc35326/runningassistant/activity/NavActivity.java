package com.yzc35326.runningassistant.activity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.MovingPointOverlay;
import com.yzc35326.runningassistant.R;
import com.yzc35326.runningassistant.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NavActivity extends CheckPermissionsActivity implements AMap.OnMyLocationChangeListener {
    private static final String TAG = "NavActivity";
    private boolean firstCount=true;
    private LatLng oldLocation,newLocation;
    private Button btStartRun, btStopRun;
    //移动轨迹坐标
    private List<LatLng> dataList = new ArrayList<>();

    private MyLocationStyle myLocationStyle;
    private MapView mapView;
    private Polyline polyline;
    private MovingPointOverlay smoothMarker;
    private Marker marker;

    //初始化地图控制器对象
    private AMap aMap;

    private Button button1;

    @Override
    public void onMyLocationChange(Location location) {
        if(location!=null){
            //保存经纬度
             newLocation = new LatLng(location.getLatitude(),location.getLongitude());
            if(firstCount){
                //记录第一次的位置信息
                oldLocation=newLocation;
                firstCount=false;
            }
            //位置有变化时
            if(oldLocation!=newLocation){
                //设置新的位置信息
                setUpMap(oldLocation,newLocation);
                //记录新的位置信息
                oldLocation=newLocation;
            }
        }
    }

    private void setUpMap(LatLng oldData,LatLng newData){
        //创建线段选项
        PolylineOptions polylineOptions = new PolylineOptions();
        //设置位置
        polylineOptions.add(oldData,newData);
        //设置宽度
        polylineOptions.width(3);
        //设置颜色
        polylineOptions.color(Color.argb(255,1,1,1));
        //地图中添加线段
        this.aMap.addPolyline(polylineOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        this.mapView = (MapView) this.findViewById(R.id.map_nav);
        this.mapView.onCreate(savedInstanceState);
        this.btStartRun = (Button) findViewById(R.id.bt_start_run);
        this.btStopRun = (Button) findViewById(R.id.bt_stop_run);
        initButtonEvents();

        //以下使用高德 地图 SDK
        this.aMap = this.mapView.getMap();

    }

    /**
     * 初始化两个按钮的点击事件
     */
    private void initButtonEvents() {
        btStartRun.setOnClickListener((v) -> {
            //启动定位
            UiSettings mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
            mUiSettings.setScaleControlsEnabled(true);
            mUiSettings.setCompassEnabled(true);
            this.aMap.setOnMyLocationChangeListener(this);
            firstLocation();
        });
        btStopRun.setOnClickListener((v) -> {
            //停止定位
//            List<LatLng> latLngs = new ArrayList<LatLng>();
//            latLngs.add(new LatLng(39.999391,116.135972));
//            latLngs.add(new LatLng(39.898323,116.057694));
//            latLngs.add(new LatLng(39.900430,116.265061));
//            latLngs.add(new LatLng(39.955192,116.140092));
//            Polyline polyline =this.aMap.addPolyline(new PolylineOptions().
//                    addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
//            this.mLocationClient.stopLocation();

        });
    }

    /**
     * 第一次地图显示
     */
    private void firstLocation() {
        myLocationStyle = new MyLocationStyle();
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(1000);
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setOnMyLocationChangeListener(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        this.aMap.showIndoorMap(true);     //true：显示室内地图；false：不显示；
        this.aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();


    }




    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mapView.onSaveInstanceState(outState);
    }

}
