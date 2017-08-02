package com.mynew.fragment;

import android.support.v4.app.Fragment;

import com.mynew.R;
import com.mynew.http.Url;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by user on 2017/5/17.
 */
public class BaseFragment extends Fragment {

    //线程池
    protected Executor executor= Executors.newFixedThreadPool(10);

    //头条URL
    public String getTopUrl(int index){
        String topUrl= Url.TopUrl+Url.TopId+"/"+index+Url.endUrl;
        return  topUrl;
    }

    //其它版面URL
    public String getCommonUrl(String itemId,int index){
        String commonUrl=Url.CommonUrl+itemId+"/"+index+Url.endUrl;
        return commonUrl;
    }

    //本地URl
    public String getLocalUrl(String index, String itemId) {
        String urlString = Url.Local + itemId + "/" + index + Url.endUrl;
        return urlString;
    }

    public void overridePendingTransition(){
        getActivity().overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);
    }


}
