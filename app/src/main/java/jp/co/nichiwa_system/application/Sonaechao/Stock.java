package jp.co.nichiwa_system.application.Sonaechao;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.reflect.GenericArrayType;
import java.util.Calendar;
import java.util.Date;

import jp.co.nichiwa_system.application.Sonaechao.R;


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
                (ImageView)findViewById(R.id.kodomoView),
                (ImageView)findViewById(R.id.tissueView),
                (ImageView)findViewById(R.id.almiView),
                (ImageView)findViewById(R.id.rapView),
                (ImageView)findViewById(R.id.gunnteView),
                (ImageView)findViewById(R.id.maskView),
                (ImageView)findViewById(R.id.biniiruView),
                (ImageView)findViewById(R.id.kaityuView),
                (ImageView)findViewById(R.id.kankiriView),
                (ImageView)findViewById(R.id.radioView),
                (ImageView)findViewById(R.id.judenkiView),
                (ImageView)findViewById(R.id.supunView),
                (ImageView)findViewById(R.id.hasiView),
                (ImageView)findViewById(R.id.koppuView),
                (ImageView)findViewById(R.id.nyujiView),
                (ImageView)findViewById(R.id.omutuView),
                (ImageView)findViewById(R.id.dentiView),
                (ImageView)findViewById(R.id.nebukuroView),
                (ImageView)findViewById(R.id.utuwaView),
                (ImageView)findViewById(R.id.taoruView),
        };
        //備蓄品の項目を取得する
        ItemClass[] item = {
                new ItemClass("ガスコンロ・鍋", "gas_number", R.drawable.gas, false, "個",this),
                new ItemClass("マッチ・ライター", "match_number", R.drawable.match, false,"箱", this),
                new ItemClass("ガスボンベ", "bombe_number", R.drawable.bombe, true,"本", this),
                new ItemClass("笛（防犯ブザー）", "whistle_number", R.drawable.whistle, false,"個", this),
                new ItemClass("大人下着", "shitagi_number", R.drawable.otona, false,"枚", this),
                new ItemClass("小人下着", "kodomo_number", R.drawable.kodomo, false,"枚", this),
                new ItemClass("ティッシュ・ウェットティッシュ", "tissue_number", R.drawable.thissyu, false,"箱", this),
                new ItemClass("アルミホイル", "almi_number", R.drawable.almi, false,"本", this),
                new ItemClass("ラップ", "rap_number", R.drawable.rappu, false,"本", this),
                new ItemClass("軍手", "gunnte_number", R.drawable.gunnte, false,"対", this),
                new ItemClass("マスク", "mask_number", R.drawable.mask, false,"×１００枚", this),
                new ItemClass("ビニール袋（ゴミ袋）", "bag_number", R.drawable.hukuro, false,"×１０枚", this),
                new ItemClass("懐中電灯　※単三電池推奨", "kaityu_number", R.drawable.kaityu, false,"本", this),
                new ItemClass("缶切り", "kankiri_number", R.drawable.kankiri, false,"個", this),
                new ItemClass("ラジオ　※単三電池推奨", "radio_number", R.drawable.radio, false,"個", this),
                new ItemClass("携帯電話充電器　※単三電池推奨", "judenki_number", R.drawable.judenti, false,"個", this),
                new ItemClass("スプーン", "supun_number", R.drawable.spoon, false,"×１００本", this),
                new ItemClass("割り箸", "hasi_number", R.drawable.hasi, false,"×１００膳", this),
                new ItemClass("コップ（プラスチック）", "koppu_number", R.drawable.koppu, false,"個", this),
                new ItemClass("哺乳びん", "nyuji_number", R.drawable.bin, false,"本", this),
                new ItemClass("おむつ", "omutu_number", R.drawable.omutu, false,"枚", this),
                new ItemClass("乾電池　※単三", "denti_number", R.drawable.denti, true,"×１０本", this),
                new ItemClass("寝袋", "nebukuro_number", R.drawable.nebukuro, false,"枚", this),
                new ItemClass("器（プラスチック）", "utuwa_number", R.drawable.utuwa, false,"枚", this),
                new ItemClass("タオル", "taoru_number", R.drawable.taoru, false,"枚", this),
        };

        // 戻る画面
        ImageButton Home = (ImageButton)findViewById(R.id.home);//「ホーム」ボタン
        ImageButton DispBtn = (ImageButton)findViewById(R.id.settingbutton);//「設定」ボタン
        ImageButton hijousyoku = (ImageButton)findViewById(R.id.hijousyoku);//「非常食」ボタン

        Home.setOnClickListener( new OnClickTransListenerClass(this) );
        hijousyoku.setOnClickListener( new OnClickTransListenerClass(".Hijousyoku",this ) );
        DispBtn.setOnClickListener( new OnClickTransListenerClass(".SubActivity",this ) );

        //枠線をつける
       for( int i = 0 ; i < Stock_iv.length ; i++ ) {
            //ボタンアクションの処理
           Stock_iv[i].setOnClickListener(new DialogOnClickListenerClass(item[i]));
           //期限の切れているものは赤線を敷く
           if( item[i].getCalender_flag() == true ) {
               if (!Check_Day(item[i].getPrefName())) {
                   Stock_iv[i].setBackgroundResource(R.drawable.style2);
               }
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