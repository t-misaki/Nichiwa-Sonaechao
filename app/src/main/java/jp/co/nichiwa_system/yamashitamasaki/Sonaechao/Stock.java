package jp.co.nichiwa_system.yamashitamasaki.Sonaechao;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.reflect.GenericArrayType;
import java.util.Calendar;
import java.util.Date;

import jp.co.nichiwa_system.yamashitamasaki.Sonaechao.R;


public class Stock extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        //各イメージビューの取得
        ImageView[] Stock_iv = {
                (ImageView)findViewById(R.id.gasView),
                (ImageView)findViewById(R.id.matchView),
                (ImageView)findViewById(R.id.bombeView),
                (ImageView)findViewById(R.id.whistleView),
                (ImageView)findViewById(R.id.shitagiView),
                (ImageView)findViewById(R.id.tissueView),
                (ImageView)findViewById(R.id.almiView),
                (ImageView)findViewById(R.id.gunnteView),
                (ImageView)findViewById(R.id.maskView),
                (ImageView)findViewById(R.id.biniiruView),
                (ImageView)findViewById(R.id.kaityuView),
                (ImageView)findViewById(R.id.kankiriView),
                (ImageView)findViewById(R.id.radioView),
                (ImageView)findViewById(R.id.judenkiView),
                (ImageView)findViewById(R.id.supunView),
                (ImageView)findViewById(R.id.koppuView),
                (ImageView)findViewById(R.id.kyuugoView),
                (ImageView)findViewById(R.id.nyujiView),
                (ImageView)findViewById(R.id.dentiView),
                (ImageView)findViewById(R.id.nebukuroView),
        };
        //備蓄品の項目を取得する
        ItemClass[] item = {
                new ItemClass("ガスコンロ", "gas_number", R.drawable.gas, false, this),
                new ItemClass("マッチ・ライター", "match_number", R.drawable.match, false, this),
                new ItemClass("ガスボンベ", "bombe_number", R.drawable.bombe, true, this),
                new ItemClass("笛", "whistle_number", R.drawable.whistle, false, this),
                new ItemClass("下着", "shitagi_number", R.drawable.shitagi, false, this),
                new ItemClass("ティッシュ", "tissue_number", R.drawable.tissue, false, this),
                new ItemClass("アルミホイル", "almi_number", R.drawable.almi, false, this),
                new ItemClass("軍手", "gunnte_number", R.drawable.gunnte, false, this),
                new ItemClass("マスク", "mask_number", R.drawable.mask, false, this),
                new ItemClass("ビニール袋", "mask_number", R.drawable.biniiru, false, this),
                new ItemClass("懐中電灯", "kaityu_number", R.drawable.kaityu, false, this),
                new ItemClass("缶切り", "kankiri_number", R.drawable.kankiri, false, this),
                new ItemClass("ラジオ", "radio_number", R.drawable.radio, false, this),
                new ItemClass("充電器", "judenki_number", R.drawable.judenti, true, this),
                new ItemClass("器・スプーン", "supun_number", R.drawable.supun, false, this),
                new ItemClass("食器・コップ", "koppu_number", R.drawable.koppu, false, this),
                new ItemClass("救護セット", "kyuugo_number", R.drawable.kyuugo, false, this),
                new ItemClass("乳児セット", "nyuji_number", R.drawable.nyuji, false, this),
                new ItemClass("電池", "denti_number", R.drawable.denti, true, this),
                new ItemClass("寝袋", "nebukuro_number", R.drawable.nebukuro, true, this),
        };

        // 戻る画面
        Button Home = (Button)findViewById(R.id.home);//「ホーム」ボタン
        Button DispBtn = (Button)findViewById(R.id.settingbutton);//「設定」ボタン
        Button hijousyoku = (Button)findViewById(R.id.hijousyoku);//「非常食」ボタン

        Home.setOnClickListener( new OnClickTransListenerClass(this) );
        hijousyoku.setOnClickListener( new OnClickTransListenerClass(".Hijousyoku",this ) );
        DispBtn.setOnClickListener( new OnClickTransListenerClass(".SubActivity",this ) );

        //枠線をつける
        for( int i = 0 ; i < Stock_iv.length ; i++ ) {
            //ボタンアクションの処理
            Stock_iv[i].setOnClickListener(new DialogOnClickListenerClass(item[i]));
            //期限の切れているものは赤線を敷く
            if( Check_Day(item[i].getPrefName()) ) {
                Stock_iv[i].setBackgroundResource(R.drawable.style2);
            }
        }

        //広告の設定
        AdView adview = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
    }

    //賞味期限を切れているかどうか判定する関数
    public boolean Check_Day( String prefName )
    {
        int i = 0;
        //残り日数を取得する
        int nokori = (int)getDate(prefName);

        if( nokori <= 0 ) {
            //賞味期限が切れたら表示
            return false;
        }
        return true;
    }

    /*********************************************************************
     // 賞味期限の日付と現在の日付から引き出された残り日数を返す
     // prefName ・・・　プレファレンス名
     *********************************************************************/
    public long getDate(String prefName)
    {
        SharedPreferences pref2 = getSharedPreferences( prefName + "_pref" ,MODE_PRIVATE);
        //現在の時刻
        Calendar cl = Calendar.getInstance();
        //引数で指定した非常食の賞味期限
        Calendar cl2 = Calendar.getInstance();
        cl2.set(pref2.getInt("year", cl.get(Calendar.YEAR)), pref2.getInt("month", cl.get(Calendar.MONTH)), pref2.getInt("day", cl.get(Calendar.DAY_OF_MONTH)));
        Date date1 = cl.getTime();
        Date date2 = cl2.getTime();

        long current_time = date1.getTime();
        long item_time = date2.getTime();

        long nokori = (item_time - current_time) / ( 1000 * 60 * 60 * 24 );

        return nokori;
    }
}