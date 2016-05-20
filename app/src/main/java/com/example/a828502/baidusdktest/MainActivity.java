package com.example.a828502.baidusdktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.TransitRouteResult;

public class MainActivity extends AppCompatActivity {
    MapView mMapView = null;
    BaiduMap mBaiduMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        addOverlayOnMap();

    }
    public void addOverlayOnMap(){
        //定义Maker坐标点
        LatLng point = new LatLng(24.68661, 112.5818);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_launcher);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    //在公交线路规划回调方法中添加TransitRouteOverlay用于展示换乘信息
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            //未找到结果
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            //创建公交路线规划线路覆盖物
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
            //设置公交路线规划数据
            overlay.setData(result.getRouteLines().get(0));
            //将公交路线规划覆盖物添加到地图中
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }
    private class MyTransitRouteOverlay extends TransitRouteOverlay {
        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }
//        @Override
//        public boolean onPoiClick(int index) {
//            super.onPoiClick(index);
//            return true;
//        }
    }
}
