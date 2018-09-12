package com.potato.zhbj.fragment;

import android.view.View;

import com.potato.zhbj.R;

/**
 * Created by li.zhirong on 2018/9/12/012 15:58
 */
public class ContentFragment extends BaseFragment {
    @Override
    protected void initData() {

    }

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_content, null);
    }
}
