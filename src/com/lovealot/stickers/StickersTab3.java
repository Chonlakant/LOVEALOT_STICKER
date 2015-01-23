package com.lovealot.stickers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;

public class StickersTab3 extends Fragment {
	
	protected static final int SELECT_PHOTO = 0;
	List<String> imageList;
	ImageAdapter adpater;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmenttab3, container, false);
        
        GridView grid = (GridView) rootView.findViewById(R.id.gridView3);
    	try {
			imageList = getImage();
			adpater = new ImageAdapter(getActivity(), imageList);
			grid.setAdapter(adpater);
			
			grid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					Intent resultIntent = new Intent();
					resultIntent.putExtra("result", imageList.get(position));

					Log.d("CheckString", "" + imageList.get(position));
					getActivity().setResult(getActivity().RESULT_OK, resultIntent);
					getActivity().finish();
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
        
    	return rootView;
    }
    
	private List<String> getImage() throws IOException {
		List<String> it = new ArrayList<String>();
		AssetManager assetManager = getActivity().getAssets();
		String[] filelist = assetManager.list("test3");
		for (String filename : filelist) {
			Log.d("check", filename);
			it.add("test3/" + filename);
		}
		return it;
	}
    
}