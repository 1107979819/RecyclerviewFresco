package top.wenyl.recyclerviewfresco;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.List;

public class MainActivitybak extends Activity {
    public static final String DOMAIN = "http://dn-assets-gitcafe-com.qbox.me";
    private static final String BASE_DIR = "Kaedea/Kaede-Assets/raw/gitcafe-pages/image";
    List<String> datas;
    MyAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                datas.add(0,"http://t12.baidu.com/it/u=2784785666,765219454&fm=58");
//                adapter.updateDatas(datas);
//            }
//        });
    }
    public void init()
    {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);

        //init
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter= new MyAdapter(0);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        datas = new ImageHolder(DOMAIN, BASE_DIR,"jk","jk-",".jpg",30,"00").getUrls();


        adapter.setDatas(datas);

    }

}
