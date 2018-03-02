package ru.johnlife.lifetools.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ru.johnlife.lifetools.R;
import ru.johnlife.lifetools.adapter.BaseAdapter;
import ru.johnlife.lifetools.data.AbstractData;

public abstract class BaseListFragment<T extends AbstractData> extends BaseAbstractFragment {

	private Context context;

	private RecyclerView list;
	private BaseAdapter<T> adapter;
	private View emptyView;

	private RecyclerView.AdapterDataObserver adapterObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            emptyView.setVisibility(adapter.getCount() == 0 ? View.VISIBLE : View.GONE);
        }

		@Override
		public void onItemRangeChanged(int positionStart, int itemCount) {
			super.onItemRangeChanged(positionStart, itemCount);
			onChanged();
		}

		@Override
		public void onItemRangeInserted(int positionStart, int itemCount) {
			super.onItemRangeInserted(positionStart, itemCount);
			onChanged();
		}

		@Override
		public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
			super.onItemRangeMoved(fromPosition, toPosition, itemCount);
			onChanged();
		}
	};

	protected abstract BaseAdapter<T> instantiateAdapter(Context context);

	protected int getLayoutId() {
		return R.layout.fragment_list;
	}

	public BaseAdapter<T> getAdapter(){
		return adapter;
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = inflater.getContext();
		View view = inflater.inflate(getLayoutId(), container, true);
		list = (RecyclerView) view.findViewById(getListId());
		if (null != list) {
			list.setHasFixedSize(true);
			list.setLayoutManager(getListLayoutManager());
			adapter = instantiateAdapter(context);
			list.setAdapter(adapter);
		}
		emptyView = view.findViewById(R.id.list_empty);
		adapter.registerAdapterDataObserver(adapterObserver);
		adapterObserver.onChanged();
		return view;
	}

	@Override
	public void onDestroyView() {
		adapter.unregisterAdapterDataObserver(adapterObserver);
		super.onDestroyView();
	}

	protected int getListId() {
		return R.id.list;
	}

	@NonNull
	protected LinearLayoutManager getListLayoutManager() {
		return new LinearLayoutManager(list.getContext());
	}

	public RecyclerView getList() {
		return list;
	}

	@Override
	public Context getContext() {
		return context;
	}
}
