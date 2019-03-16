package com.tokyonth.carnet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokyonth.carnet.R;
import com.tokyonth.carnet.adapter.UrgentAdapter;
import com.tokyonth.carnet.bean.Contacts;
import com.tokyonth.carnet.ui.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;

public class EmergencyWarning extends ListFragment {

    private List<Contacts> mFoodList;
    private Button btn_contacts;

    private CustomDialog customDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFoodList = new ArrayList<>();
        InitData();
        UrgentAdapter adapter = new UrgentAdapter(getContext(),R.layout.contacts_list,mFoodList);
        setListAdapter(adapter);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_emergency,container,false);
            btn_contacts = (Button) view.findViewById(R.id.add_contacts);
            btn_contacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog = new CustomDialog(getContext());
                    customDialog.setTitle("提示");
                    customDialog.setMessage(" i 行");
                    customDialog.setYesOnclickListener("添加", new CustomDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            customDialog.dismiss();
                        }
                    });
                    customDialog.setNoOnclickListener("取消", new CustomDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            customDialog.dismiss();
                        }
                    });
                    customDialog.setCanceledOnTouchOutside(false);
                    customDialog.show();
                }
            });
            return view;
        }

        private void InitData() {
           mFoodList.add(new Contacts("联系人1",R.drawable.ic_export));
           mFoodList.add(new Contacts("联系人2",R.drawable.ic_hierarchy));
           mFoodList.add(new Contacts("联系人1",R.drawable.ic_export));
        }

}
