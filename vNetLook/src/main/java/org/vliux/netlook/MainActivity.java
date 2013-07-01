package org.vliux.netlook;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.vliux.netlook.db.DbManager;
import org.vliux.netlook.model.AppNetUse;
import org.vliux.netlook.model.TotalNetUse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity {
    private ListView mNetUseListView;
    private ImageButton mRefreshBtn;
    private TextView mSummaryTextView;

    SimpleAdapter mAdapter;
    List<Map<String, Object>> mDataSource = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNetUseListView = (ListView)findViewById(R.id.main_netuse_listview);
        mRefreshBtn = (ImageButton)findViewById(R.id.main_refresh);
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadNetUseAsyncTask(new DbManager(MainActivity.this), getPackageManager(), mHandler).execute();
            }
        });
        mSummaryTextView = (TextView)findViewById(R.id.main_summary);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadNetUseAsyncTask(new DbManager(this), getPackageManager(), mHandler).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void updateDataSource(List<AppNetUse> appUses){
        if(null == appUses){
            return;
        }else{
            mDataSource.clear();
            for(AppNetUse au : appUses){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", au.getmPackageName());
                long rx = au.getmRxBytes();
                long tx = au.getmTxBytes();
                long total = au.getTotalBytes();
                map.put("use", String.format(Locale.US, "%.2fk", (double)total/1024));

                map.put("icon", au.getmIcon());
                mDataSource.add(map);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case LoadNetUseAsyncTask.MSG_COMPLETED:
                    TotalNetUse totalUse = (TotalNetUse)msg.obj;
                    mAdapter = new SimpleAdapter(MainActivity.this,
                            mDataSource,
                            R.layout.item_netuse,
                            new String[]{"icon", "name", "use"},
                            new int[]{R.id.item_netuse_icon, R.id.item_netuse_name, R.id.item_netuse_use}
                    );
                    mAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Object o, String s) {
                            if(view instanceof ImageView && o instanceof Drawable){
                                ImageView iv = (ImageView)view;
                                Drawable icon = (Drawable)o;
                                iv.setImageDrawable(icon);
                                return true;
                            }else{
                                return false;
                            }
                        }
                    });
                    mNetUseListView.setAdapter(mAdapter);
                    updateDataSource(totalUse.getmAppNetUses());
                    mSummaryTextView.setText(String.format(Locale.US,
                            "Total %.2fk\nMobile %.2fk",
                            (double)(totalUse.getmRxBytes() + totalUse.getmTxBytes())/1024,
                            (double)totalUse.getMobileBytes()/1024));
                    break;
            }
        }


    };
    
}
