package org.vliux.netlook;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNetUseListView = (ListView)findViewById(R.id.main_netuse_listview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadNetUseAsyncTask asyncTask = new LoadNetUseAsyncTask(getPackageManager(), mHandler);
        asyncTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case LoadNetUseAsyncTask.MSG_COMPLETED:
                    TotalNetUse totalUse = (TotalNetUse)msg.obj;
                    SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,
                            getData(totalUse.getmAppNetUses()),
                            R.layout.item_netuse,
                            new String[]{"icon", "name", "use"},
                            new int[]{R.id.item_netuse_icon, R.id.item_netuse_name, R.id.item_netuse_use}
                    );
                    mNetUseListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

        private List<Map<String, Object>> getData(List<AppNetUse> appUses){
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            if(null == appUses){
                return data;
            }

            for(AppNetUse au : appUses){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", au.getmPackageName());
                map.put("use", String.format(Locale.US, "RX:%d, TX:%d", au.getmRxBytes(), au.getmTxBytes()));

                map.put("icon", au.getmIcon());
                data.add(map);
            }
            return data;
        }
    };
    
}
