package com.yzc35326.runningassistant.activity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.yzc35326.runningassistant.R;
import com.yzc35326.runningassistant.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationActivity extends CheckPermissionsActivity implements AMap.OnMyLocationChangeListener, View.OnClickListener {
    //地图类型
    private int currentMapType = AMap.MAP_TYPE_NORMAL;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private boolean firstLocation = false;
    private double lat, lon;

    private static final String TAG = "LocationActivity";
    private TextView tvLocationDetail;
    private GeocodeSearch geocodeSearch;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;

    private MyLocationStyle myLocationStyle;
    private MapView mapView;

    //初始化地图控制器对象
    private AMap aMap;

    private Button button1, button2, button3, button4, button5;


    private AMapLocationListener myLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (null != aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    StringBuffer sb = new StringBuffer();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(aMapLocation.getTime());
                    sb.append("定位时间：").append(df.format(date)).append("\n");
                    sb.append("定位类型：");
                    switch (aMapLocation.getLocationType()) {
                        case 1:
                            sb.append("GPS定位结果").append("\n");
                            break;
                        case 2:
                            sb.append("前次定位结果").append("\n");
                            break;
                        case 4:
                            sb.append("缓存定位结果").append("\n");
                            break;
                        case 5:
                            sb.append("WIFI定位结果").append("\n");
                            break;
                        case 6:
                            sb.append("基站定位结果").append("\n");
                            break;
                        case 8:
                            sb.append("离线定位结果").append("\n");
                            break;
                    }

                    //获得纬度
                    sb.append("纬度：").append(aMapLocation.getLatitude()).append("\n");
                    sb.append("经度：").append(aMapLocation.getLongitude()).append("\n");
                    sb.append("速度：").append(aMapLocation.getSpeed()).append("米/秒").append("\n");
                    sb.append("精度：").append(aMapLocation.getAccuracy()).append("米").append("\n");
                    sb.append("地址信息：").append(aMapLocation.getAddress()).append("\n");
                    sb.append("国家：").append(aMapLocation.getCountry()).append("\n");
                    sb.append("省份：").append(aMapLocation.getProvince()).append("\n");
                    sb.append("城市：").append(aMapLocation.getCity()).append("\n");
                    sb.append("城区：").append(aMapLocation.getDistrict()).append("\n");
                    sb.append("街道：").append(aMapLocation.getStreet()).append("\n");
                    sb.append("街道门牌号：").append(aMapLocation.getStreetNum()).append("\n");
                    sb.append("AOI信息：").append(aMapLocation.getAoiName()).append("\n");
                    sb.append("POI信息：").append(aMapLocation.getPoiName()).append("\n");
                    sb.append("室内定位的建筑物Id:").append(aMapLocation.getBuildingId()).append("\n");
                    sb.append("当前室内定位的楼层：").append(aMapLocation.getFloor()).append("\n");
                    sb.append("当前的卫星信号状态：");
                    switch (aMapLocation.getGpsAccuracyStatus()) {
                        case AMapLocation.GPS_ACCURACY_GOOD:
                            sb.append("强");
                            break;
                        case AMapLocation.GPS_ACCURACY_BAD:
                            sb.append("弱");
                            break;
                        case AMapLocation.GPS_ACCURACY_UNKNOWN:
                            sb.append("未知");
                            break;
                    }
                    sb.append("\n");
                    tvLocationDetail.setText(sb.toString());
                } else {
                    //定位失败时，可通过错误码信息来确定失败的原因，errInfo是
                    //错误信息
                    LogUtil.e(TAG, "location error,ErrCode: " + aMapLocation
                            .getErrorCode() + ",errInfo:" +
                            aMapLocation.getErrorInfo());
                }
            }
        }
    };

    /**
     * 地图位置发生变化时候的回调
     *
     * @param location 新的位置
     */
    @Override
    public void onMyLocationChange(Location location) {
        // Toast.makeText(this, "经度为："+location.getLongitude()+",纬度 为："+location.getLatitude(), Toast.LENGTH_SHORT).show();
        // LogUtil.d("1","经度为："+location.getLongitude()+",纬度 为："+location.getLatitude());
        // aMap.moveCamera();
    }


    /**
     * 按钮点击事件监听，切换图层
     *
     * @param view 被点击的View
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_nav_map:
                if (this.currentMapType != AMap.MAP_TYPE_NAVI) {
                    LogUtil.d(TAG, "1");
                    this.aMap.setMapType(AMap.MAP_TYPE_NAVI);
                    this.currentMapType = AMap.MAP_TYPE_NAVI;
                    location();
                }

                break;
            case R.id.bt_normal_map:
                if (this.currentMapType != AMap.MAP_TYPE_NORMAL) {
                    this.aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                    this.currentMapType = AMap.MAP_TYPE_NORMAL;
                    location();
                }

                break;
            case R.id.bt_satellite_map:
                if (this.currentMapType != AMap.MAP_TYPE_SATELLITE) {
                    this.aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                    this.currentMapType = AMap.MAP_TYPE_SATELLITE;
                    location();
                }
                break;
            case R.id.bt_nignt_map:
                if (this.currentMapType != AMap.MAP_TYPE_NIGHT) {
                    this.aMap.setMapType(AMap.MAP_TYPE_NIGHT);
                    this.currentMapType = AMap.MAP_TYPE_NIGHT;
                    location();
                }
                break;
            case R.id.bt_bus_map:
                if (this.currentMapType != AMap.MAP_TYPE_BUS) {
                    this.currentMapType = AMap.MAP_TYPE_BUS;

                    this.aMap.setTrafficEnabled(true);
                    location();
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //地图显示和初始化
        this.mapView = (MapView) findViewById(R.id.map_location2);
        this.mapView.onCreate(savedInstanceState);

        //按钮初始化和点击事件注册
        button1 = (Button) findViewById(R.id.bt_nav_map);
        button2 = (Button) findViewById(R.id.bt_normal_map);
        button3 = (Button) findViewById(R.id.bt_satellite_map);
        button4 = (Button) findViewById(R.id.bt_nignt_map);
        button5 = (Button) findViewById(R.id.bt_bus_map);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);

        this.tvLocationDetail = (TextView) findViewById(R.id.tv_location_detail);


        /**
         * 定位相关
         */
        //初始化定位
        this.mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        this.mLocationClient.setLocationListener(myLocationListener);
        //该对象用来设置发起定位的模式和相关参数
        this.mLocationOption = new AMapLocationClientOption();
        //高精度定位模式
        this.mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //定位间隔
        this.mLocationOption.setInterval(1000);
        //需要返回地址信息
        this.mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(false);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        // LogUtil.d("地图", "地图");
        //设置定位参数
        this.mLocationClient.setLocationOption(this.mLocationOption);
        //启动定位
        this.mLocationClient.startLocation();


        /**
         * 地图显示相关
         */
        this.aMap = this.mapView.getMap();
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        this.mUiSettings.setScaleControlsEnabled(true);
        this.mUiSettings.setCompassEnabled(true);
        this.aMap.setOnMyLocationChangeListener(this);
        firstLocation();
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
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        this.aMap.showIndoorMap(true);     //true：显示室内地图；false：不显示；
        this.aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
    }

    /**
     * 地图显示
     */
    private void location() {
        myLocationStyle = new MyLocationStyle();
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(1000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        this.aMap.showIndoorMap(true);     //true：显示室内地图；false：不显示；
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
        //销毁定位客户端
        this.mLocationClient.onDestroy();
//        this.mLocationClient.stopLocation();
//        this.mLocationClient.onDestroy();
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
    protected void onStop() {
        super.onStop();
        //停止定位
        this.mLocationClient.stopLocation();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mapView.onSaveInstanceState(outState);
    }
}
