package com.lchb.epcsview;

import android.support.v4.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class AboutFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private TextView txtVersion;
        private Button btnUpdate;
		
		public AboutFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_about, container,false);
			return rootView;
		}
	    
		@Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        txtVersion=(TextView)getActivity().findViewById(R.id.txtVersion);
            btnUpdate=(Button)getActivity().findViewById(R.id.btnUpdate);
	        txtVersion.setText(getVersion());
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new UpdateManager(getActivity()).checkUpdate();
                }
            });
		}
		
		public String getVersion() {
			try {
				PackageManager manager = getActivity().getPackageManager();
				PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
				String version = info.versionName;
				return "版本号:V"+version;
			} catch (Exception e) {
				e.printStackTrace();
				return "版本号:V1.0";
			}
		}
				
	}
