package top.wenyl.recyclerviewfresco;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MainActivity extends Activity {
    public static final String DOMAIN = "http://dn-assets-gitcafe-com.qbox.me";
    private static final String BASE_DIR = "Kaedea/Kaede-Assets/raw/gitcafe-pages/image";
    List<String> datas;
    MyAdapter adapter;
    private PtrFrameLayout mPtrFrameLayout;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
//    StaggeredGridLayoutManager layoutManager;
     LinearLayoutManager  layoutManager;
    boolean isLoading;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

//
    }
    public void init()
    {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        linearLayout = (LinearLayout)findViewById(R.id.ll_con);
        //init
//        layoutManager  = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
    layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(layoutManager);
        adapter= new MyAdapter(0);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        datas = new ImageHolder(DOMAIN, BASE_DIR,"jk","jk-",".jpg",30,"00").getUrls();


        adapter.setDatas(datas);

        //下拉加载刷新
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                // here check list view, not content.
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,linearLayout, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                    Log.i("Test","加載更多");

                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        datas.add(0,"http://t12.baidu.com/it/u=2784785666,765219454&fm=58");
                        adapter.updateDatas(datas);

                        mPtrFrameLayout.refreshComplete();//刷新完成
                    }
                }, 1800);
            }
        });


  /*  recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            int totalItemCount = mLayoutManager.getItemCount();
            //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
            // dy>0 表示向下滑动
            if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                if(isLoadingMore){
                    Log.d(TAG,"ignore manually update!");
                } else{
                    loadPage();//这里多线程也要手动控制isLoadingMore
                    isLoadingMore = false;
                }
            }
        }
    });*/

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("test", "StateChanged = " + newState);


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("test", "onScrolled");

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 4 == adapter.getItemCount()) {
                    Log.d("test", "loading executed");

//                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
//                    if (isRefreshing) {
//                        adapter.notifyItemRemoved(adapter.getItemCount());
//                        return;
//                    }
                 /*   if (!isLoading) {
                        isLoading = true;
                        Log.i("Test","加载更多");
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                getData();
//                                Log.d("test", "load more completed");
//                                isLoading = false;
//                            }
//                        }, 1000);
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //  adapter.addEndData("http://t12.baidu.com/it/u=2784785666,765219454&fm=58");
                                int s =datas.size();
                                for(int i=0;i<10;i++)
                                {
                                    datas.add(s+i,"http://t12.baidu.com/it/u=2784785666,765219454&fm=58");
                                }
                                adapter.updateDatas(datas);
                                isLoading = false;

                            }
                        }, 1000);

                    }*/
                }
            }
        });


    }

}
