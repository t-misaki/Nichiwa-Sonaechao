package jp.co.nichiwa_system.yamashitamasaki.Sonaechao;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.co.nichiwa_system.yamashitamasaki.Sonaechao.R;

public class WorthChecking extends Fragment {

    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //container.removeAllViews();
        //View rootView = inflater.inflate(R.layout.fragment_worth_checking, container, false);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_worth_checking, container, false);
    }
    public void SetTextView(String str){
        TextView txtHoge = (TextView) rootView.findViewById(R.id.textView);
        txtHoge.setText(str);
    }

}
