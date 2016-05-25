package top.wenyl.recyclerviewfresco;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by songhang on 16/4/25.
 * 适配器 包含 Fresco 、 Header 、Footer 功能
 */
public class SmartRecyclerAdapter extends  RecyclerView.Adapter<SmartRecyclerAdapter.ViewHolder>{
//        RecyclerViewAdapterWrapper {
    public static final int TYPE_HEADER = 111;
    public static final int TYPE_FOOTER = 112;

    private RecyclerView.LayoutManager layoutManager;

    private View headerView, footerView;
    int index;

    List<String> datas = new ArrayList<>();
    Map<String,Integer> heightMap = new HashMap<>();
    static Map<String,Integer> widthMap = new HashMap<>();

    public SmartRecyclerAdapter() {

    }

    public List<String> getDatas() {
        return datas;
    }
    public void setDatas(List<String> datas) {
        if (datas!=null&&datas.size()>0){
            this.datas.clear();
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }
    }
    public void addHeadData(int pos, String url)
    {
        datas.add(pos,url);
        notifyItemInserted(pos);
    }

    public void addHeadData(String url)
    {
        datas.add(0,url);
        notifyDataSetChanged();
    }
    public void addEndData(String url)
    {
        int s =datas.size();
        datas.add(s,url);
        notifyItemInserted(s);
    }

    public void updateDatas(List<String> datas)
    {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }






    private float getTargetHeight(float width,float height,View view, String url){
        View child = view.findViewById(R.id.draweeview);
        float widthTarget;
        if (widthMap.containsKey(url)) widthTarget = widthMap.get(url);
        else {
            widthTarget = child.getMeasuredWidth();
            if (widthTarget>0){
                widthMap.put(url, (int) widthTarget);
            }
        }

        FLog.i("kaede","child.getMeasuredWidth() = " + widthTarget);
		/*int getWidth = child.getWidth();
		int getMeasuredWidth = child.getMeasuredWidth();
		int getLayoutParamsWidth = child.getLayoutParams().width;
		if (getWidth==0||getMeasuredWidth==0||getLayoutParamsWidth==0){
			FLog.i("kaede","child.getWidth() = " + getWidth);
			FLog.i("kaede","child.getMeasuredWidth() = " + getMeasuredWidth);
			FLog.i("kaede","child.getLayoutParams().width = " + getLayoutParamsWidth);
		}*/
        return height * (widthTarget /width);
    }

    private void updateItemtHeight(int height, View view) {
        CardView cardView = (CardView) view.findViewById(R.id.cardview);
        View child = view.findViewById(R.id.draweeview);
        CardView.LayoutParams layoutParams = (CardView.LayoutParams) child.getLayoutParams();
        layoutParams.height = height;
        cardView.updateViewLayout(child,layoutParams);
    }

//    @Override
//    public int getItemCount() {
//        return datas.size();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView draweeView;

        public ViewHolder(View itemView) {
            super(itemView);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.draweeview);
        }
    }

    public void setHeaderView(View view) {
        headerView = view;
       notifyDataSetChanged();
    }

    public void removeHeaderView() {
        headerView = null;
        notifyDataSetChanged();
    }

    public void setFooterView(View view) {
        footerView = view;
        notifyDataSetChanged();
    }

    public void removeFooterView() {
        footerView = null;
       notifyDataSetChanged();
    }

    private void setGridHeaderFooter(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isShowHeader = (position == 0 && hasHeader());
                    boolean isShowFooter = (position == getItemCount() - 1 && hasFooter());
                    if (isShowFooter || isShowHeader) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    private boolean hasHeader() {
        return headerView != null;
    }

    private boolean hasFooter() {
        return footerView != null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        layoutManager = recyclerView.getLayoutManager();
        setGridHeaderFooter(layoutManager);
    }

    @Override
    public int getItemCount() {
        return datas.size()+ (hasHeader() ? 1 : 0) + (hasFooter() ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader() && position == 0) {
            return TYPE_HEADER;
        }

        if (hasFooter() && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return super.getItemViewType(hasHeader() ? position - 1 : position);
    }

//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
//        return new ViewHolder(view);
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        View itemView = null;
        if (viewType == TYPE_HEADER) {
            itemView = headerView;
        } else if (viewType == TYPE_FOOTER) {
            itemView = footerView;
        }
        if (itemView != null) {
            //set StaggeredGridLayoutManager header & footer view
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                ViewGroup.LayoutParams targetParams = itemView.getLayoutParams();
                StaggeredGridLayoutManager.LayoutParams StaggerLayoutParams;
                if (targetParams !=  null) {
                    StaggerLayoutParams = new StaggeredGridLayoutManager.LayoutParams(targetParams.width, targetParams.height);
                } else {
                    StaggerLayoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                StaggerLayoutParams.setFullSpan(true);
                itemView.setLayoutParams(StaggerLayoutParams);
            }
            return new ViewHolder(itemView) {
            };
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER) {
            //if you need get header & footer state , do here
            return;
        }
        onMBindViewHolder(holder, hasHeader() ? position - 1 : position);
    }

    private void onMBindViewHolder(final ViewHolder holder, int position) {
        final String url = getDatas().get(position);
        if (heightMap.containsKey(url)){
            int height = heightMap.get(url);
            FLog.i("kaede", url+ "'s height = " + height);
            if (height>0){
                updateItemtHeight(height,holder.itemView);
                holder.draweeView.setImageURI(Uri.parse(url));

                return;
            }

        }
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                if (qualityInfo.isOfGoodEnoughQuality()){
                    int heightTarget = (int) getTargetHeight(imageInfo.getWidth(),imageInfo.getHeight(),holder.itemView,url);
                    FLog.i("kaede", "heightTarget = " + heightTarget);
                    if (heightTarget<=0)return;
                    heightMap.put(url,heightTarget);
                    updateItemtHeight(heightTarget, holder.itemView);
                }
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setControllerListener(controllerListener)
                .setTapToRetryEnabled(true)
                .build();
        holder.draweeView.setController(controller);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
