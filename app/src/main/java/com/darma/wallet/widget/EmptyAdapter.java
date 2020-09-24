package com.darma.wallet.widget;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public abstract class EmptyAdapter<T> extends MultiItemTypeAdapter<T>
{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    private boolean emptyView=false;

    public static final int VIEW_TYPE_EMPTY = -1;
    public EmptyAdapter(final Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                if(EmptyAdapter.super.getItemCount()==0&&emptyView){
                    return false;
                }
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                EmptyAdapter.this.convert(holder, t, position);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (super.getItemCount()==0&&emptyView) {
            return 1;
        }
        return super.getItemCount();
    }
    @Override
    public int getItemViewType(int position) {
        if (super.getItemCount()==0&&emptyView) {
            return mItemViewDelegateManager.getItemViewType(null, position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (super.getItemCount()==0&&emptyView) {
            convert(holder,null);
            return ;
        }
        convert(holder, mDatas.get(position));
    }

    public void setEmptyView(final int  layoutId, final ConvertEmptyView mConvertEmptyView){

        emptyView=true;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {

                if(EmptyAdapter.super.getItemCount()==0){
                    return true;
                }
                return false;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                mConvertEmptyView.convertEmptyView(holder);

            }
        });

    }

    protected abstract void convert(ViewHolder holder, T t, int position);

    public interface ConvertEmptyView{
        void convertEmptyView(ViewHolder holder);
    }

}
