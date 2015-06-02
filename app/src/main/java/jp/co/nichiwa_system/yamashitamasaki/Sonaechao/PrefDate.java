package jp.co.nichiwa_system.yamashitamasaki.Sonaechao;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by yamashita.masaki on 2015/05/11.
 */
//TODO:クラスの作成
//プレファレンスの操作クラス
public class PrefDate extends Activity{

    String prefName;

    //コンストラクタ
    public PrefDate(String prefName) {
        this.prefName = prefName;
    }

    public void setPref(String prefName) {
        this.prefName = prefName;
    }

    //値データを取得する関数
    public int loadInt( String name )
    {
        //プリファレンスの生成
        SharedPreferences pref =
                getSharedPreferences(prefName, MODE_PRIVATE);
        int i = pref.getInt(name,0);

        return i;
    }

    //値データをプレファレンスで保存する関数 (エディットテキスト、名前)
    public void saveInt( EditText et, String name )
    {
        //プリファレンスの生成
        SharedPreferences pref =
                getSharedPreferences(prefName, MODE_PRIVATE);

        //挿入できるようにする
        SharedPreferences.Editor e = pref.edit();

        //String型で文字列を入手
        String str = et.getText().toString();

        //文字列を値に変換
        int i = Integer.parseInt(str);

        //値を挿入
        e.putInt(name, i);

        //保存
        e.commit();
    }

    //日付を取得する
    public Calendar loadCalendar( String name )
    {
        SharedPreferences pref =
                getSharedPreferences( name + "_pref", MODE_PRIVATE);
        Calendar cl = Calendar.getInstance();
        cl.set( pref.getInt("year", cl.get(Calendar.YEAR) ), pref.getInt("month", cl.get(Calendar.MONTH) ), pref.getInt("day", cl.get(Calendar.DAY_OF_MONTH) ) );

        return cl;
    }

    //日付を格納する
    public void saveCalendar( Calendar cl , String prefName )
    {
        SharedPreferences pref =
                getSharedPreferences( prefName + "_pref", MODE_PRIVATE);

        SharedPreferences.Editor e = pref.edit();

        e.putInt("year", cl.get(Calendar.YEAR));
        e.putInt("month", cl.get(Calendar.MONTH) );
        e.putInt("day", cl.get(Calendar.DAY_OF_MONTH) );

        e.commit();

    }
}
