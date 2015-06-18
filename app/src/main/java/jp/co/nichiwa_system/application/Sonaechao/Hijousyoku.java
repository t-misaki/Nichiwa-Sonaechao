package jp.co.nichiwa_system.application.Sonaechao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.Date;

import jp.co.nichiwa_system.application.Sonaechao.R;


public class Hijousyoku extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hijousyoku);
        SharedPreferences pre = getSharedPreferences("Preference",MODE_PRIVATE);
        if(pre.getInt("fast_start_food",0)==0){
            /***処理***/
            AlertDialog.Builder fast = new AlertDialog.Builder(this);
            fast.setTitle("非常食画面の説明");
            fast.setMessage("ここでは非常食の備蓄が設定できます。\n" +
                    "備蓄したいものを選択し数量と消費期限を設定してみましょう。\n");
            fast.setPositiveButton("次へ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AlertDialog.Builder fast = new AlertDialog.Builder(Hijousyoku.this);
                    fast.setTitle("非常食画面の説明");
                    fast.setMessage("備蓄している個数を入力できます\n\n" +
                            "カレンダーのボタンを押すと消費期限が入力できます\n\n" +
                            "okボタンまたはカレンダーボタンを押さなければ備蓄個数は保存されないので注意してください。\n\n"+
                            "※このメッセージは画面下の非常食ボタンを押すと再び表示されます。");
                    fast.setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                    fast.show();
                }
            });


            fast.show();

            SharedPreferences.Editor e = pre.edit();
            e.putInt("fast_start_food", 1);
            e.commit();
        }

        // それぞれのボタンの情報を取得
        ImageButton Home = (ImageButton)findViewById(R.id.home);                  //「ホーム」ボタン
        ImageButton DispBtn = (ImageButton)findViewById(R.id.settingbutton);    //「設定」ボタン
        ImageButton Stock = (ImageButton)findViewById(R.id.bichiku);             //「備蓄」ボタン
        ImageButton food = (ImageButton)findViewById(R.id.hijousyoku);

        food.setBackgroundResource(R.drawable.style2);

        ImageView[] Hijousyoku_iv = new ImageView[12];

        //各イメージビューの取得
        Hijousyoku_iv[0] = (ImageView)findViewById(R.id.retoruto_gohan);
        Hijousyoku_iv[1] = (ImageView)findViewById(R.id.kandume);
        Hijousyoku_iv[2] = (ImageView)findViewById(R.id.kanmen);
        Hijousyoku_iv[3] = (ImageView)findViewById(R.id.kanpan);
        Hijousyoku_iv[4] = (ImageView)findViewById(R.id.kandume2);
        Hijousyoku_iv[5] = (ImageView)findViewById(R.id.retoruto);
        Hijousyoku_iv[6] = (ImageView)findViewById(R.id.furizu_dorai);
        Hijousyoku_iv[7] = (ImageView)findViewById(R.id.mizu);
        Hijousyoku_iv[8] = (ImageView)findViewById(R.id.karori_meito);
        Hijousyoku_iv[9] = (ImageView)findViewById(R.id.okasi);
        Hijousyoku_iv[10] = (ImageView)findViewById(R.id.rinyu);
        Hijousyoku_iv[11] = (ImageView)findViewById(R.id.konamilk);

        //非常食の項目を取得する
        ItemClass[] item = new ItemClass[12];
        item[0] = new ItemClass("レトルトご飯", "retorutogohan_number", R.drawable.retoruto_gohan, true,"食", this);
        item[1] = new ItemClass("缶詰（ご飯）", "kandume_number", R.drawable.kandume_gohan, true,"缶", this);
        item[2] = new ItemClass("乾麺", "kanmen_number", R.drawable.kanmen, true,"袋", this);
        item[3] = new ItemClass("乾パン", "kanpan_number", R.drawable.kanpan, true,"缶", this);
        item[4] = new ItemClass("缶詰（肉・魚）", "kandume2_number", R.drawable.kandume, true, "缶", this);
        item[5] = new ItemClass("レトルト食品", "retoruto_number", R.drawable.retoruto, true, "袋", this);
        item[6] = new ItemClass("フリーズドライ", "furizu_dorai_number", R.drawable.furizu_dorai, true, "食", this);
        item[7] = new ItemClass("水", "mizu_number", R.drawable.mizu, true, "ℓ",this);
        item[8] = new ItemClass("カロリーメイト", "karori_meito_number", R.drawable.karori_meito, true, "箱", this);
        item[9] = new ItemClass("菓子類", "okasi_number", R.drawable.okasi, true, "箱・袋", this);
        item[10] = new ItemClass("離乳食", "rinyu_number", R.drawable.rinyu, true, "食", this );
        item[11] = new ItemClass("粉ミルク", "konamilk_number", R.drawable.konamilk, true, "缶", this);

        //番号の振り分け
        for( int i = 0 ; i < 12 ; i++ ) {
            item[i].setNumber(i);
        }

        // 場所を指定する
        Home.setOnClickListener(new OnClickTransListenerClass(".MainActivity", this)); // ホーム画面へ
        Stock.setOnClickListener(new OnClickTransListenerClass(".Stock", this)); // 備蓄品画面へ
        DispBtn.setOnClickListener(new OnClickTransListenerClass(".SubActivity", this)); // 設定画面へ
        food.setOnClickListener(new View.OnClickListener() { // 非常食ボタンを押した時の処理（説明ダイアログ）
            @Override
            public void onClick(View v) {
                AlertDialog.Builder fast = new AlertDialog.Builder(Hijousyoku.this);
                fast.setTitle("非常食画面の説明");
                fast.setMessage("ここでは非常食の備蓄が設定できます。\n" +
                        "備蓄したいものを選択し数量と消費期限を設定してみましょう。\n");
                fast.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlertDialog.Builder fast = new AlertDialog.Builder(Hijousyoku.this);
                        fast.setTitle("非常食画面の説明");
                        fast.setMessage("備蓄している個数を入力できます\n\n" +
                                "カレンダーのボタンを押すと消費期限が入力できます\n\n" +
                                "okボタンまたはカレンダーボタンを押さなければ備蓄個数は保存されないので注意してください。\n\n"+
                                "※このメッセージは画面下の非常食ボタンを押すと再び表示されます。");
                        fast.setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                        fast.show();
                    }
                });
                fast.show();
            }
        });

        //枠線をつける
        for( int i = 0 ; i < 12 ; i++ ) {
            SharedPreferences pref =
                    getSharedPreferences("Preferences",MODE_PRIVATE);
            //ボタンアクションの処理
            Hijousyoku_iv[i].setOnClickListener( new DialogOnClickListenerClass( item[i]) );
            int r = pref.getInt(item[i].getPrefName(),0);
            //期限の切れているものは赤線を敷く
            if( !Check_Day(item[i].getPrefName()) && r!=0  ) {
                Hijousyoku_iv[i].setBackgroundResource(R.drawable.style2);
            }

            //非常食、幼児用非常食の備蓄％が50％未満なら水以外のボタンに赤枠を敷く
            if( FoodOverKids() + FoodBaby() < 50 && item[i].getName() != "水"){
                Hijousyoku_iv[i].setBackgroundResource(R.drawable.style2);
            }
            //水の備蓄％が50％未満なら、水ボタンに赤枠を敷く
            if( RateWater() < 50 && item[i].getName() == "水"){
                Hijousyoku_iv[i].setBackgroundResource(R.drawable.style2);
            }
            //非常食、幼児用非常食の備蓄％が50％以上なら水以外のボタンから赤枠をぬく
            if( FoodOverKids() + FoodBaby()  >= 50 && item[i].getName() != "水" ){
                Hijousyoku_iv[i].setBackgroundResource(R.drawable.style);
            }
            //水の備蓄％が５０％以上なら水の赤枠を抜く
            if( RateWater()  >= 50 && item[i].getName() == "水" ){
                Hijousyoku_iv[i].setBackgroundResource(R.drawable.style);
            }
            //期限の切れているものは赤線を敷く
            if (!Check_Day(item[i].getPrefName()) && r != 0) {
                Hijousyoku_iv[i].setBackgroundResource(R.drawable.style2);
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
        cl2.set( pref2.getInt("year", cl.get(Calendar.YEAR) ), pref2.getInt("month", cl.get(Calendar.MONTH) ), pref2.getInt("day", cl.get(Calendar.DAY_OF_MONTH) ) );
        Date date1 = cl.getTime();
        Date date2 = cl2.getTime();

        long current_time = date1.getTime();
        long item_time = date2.getTime();

        long nokori = (item_time - current_time) / ( 1000 * 60 * 60 * 24 );

        return nokori;
    }

    public int FoodOverKids() {
        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);

        int reto_g = pref.getInt("retorutogohan_number", 0);
        int kan = pref.getInt("kandume_number", 0);
        int kanmen = pref.getInt("kanmen_number", 0);
        int kanpan = pref.getInt("kanpan_number", 0);
        int kan2 = pref.getInt("kandume2_number", 0);
        int reto = pref.getInt("retoruto_number", 0);
        int furizu = pref.getInt("furizu_dorai_number", 0);
        int karori = pref.getInt("karori_meito_number", 0);
        int okasi = pref.getInt("okasi_number", 0);

        //人数
        int adult_n = pref.getInt("otona_people", 0);
        int child_n = pref.getInt("kobito_people", 0);
        int baby_n = pref.getInt("youji_people", 0);

        //  設定人数。訂正者：岡田
        int setDays = pref.getInt("sitei_day", 3);

        //各合計（大小のみ）
        int Hijousyoku_sum = reto_g + kan + kanmen + kanpan + kan2 + reto + furizu + karori + okasi;

        double okAll;
        double div = 2.0;

        //栄養価の計算。乾パンとカロリーメイトを抜いたものは栄養価１．乾パン、カロリーメイトは３。
        //  大小限定の栄養価総計。over kids All
        okAll = ((Hijousyoku_sum - kanpan - karori) * 1) + (kanpan + karori) * 3;

        //  大小の割合。
        double rateOK = okAll / (((adult_n * 3) + (child_n * 2)) * setDays);

        if (rateOK >= 1.0) {
            rateOK = 1.0;
        }

        //  幼児がいなかった場合
        if (!(baby_n > 0)) {
            div -= 1.0;
        }

        if (!(adult_n > 0 || child_n > 0)) {
            rateOK = 0.0;
        }

        if (adult_n > 0 || child_n > 0) {
            rateOK = (rateOK / div) * 50.0;
        }

        return (int) rateOK;
    }

    public int FoodBaby() {
        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);

        int rinyu = pref.getInt("rinyu_number", 0);
        int konamilk = pref.getInt("konamilk_number", 0);

        //人数
        int adult_n = pref.getInt("otona_people", 0);
        int child_n = pref.getInt("kobito_people", 0);
        int baby_n = pref.getInt("youji_people", 0);


        int setDays = pref.getInt("sitei_day", 3);

        double bAll;

        //  幼児限定の栄養価総計。baby All
        bAll = (konamilk * 3) + rinyu;

        double rate = bAll / ((baby_n * 3) * setDays);
        double div = 2.0;

        if (rate >= 1.0) {
            rate = 1.0;
        }

        //  大人・小人がいなかった場合
        if (!(adult_n > 0 || child_n > 0)) {
            div -= 1.0;
        }

        //  幼児がいなかった場合
        if (!(baby_n > 0)) {
            rate = 0.0;
        }

        //  幼児がいる場合計算を行う(念のため）
        if (baby_n > 0) {
            rate = (rate / div) * 50.0;
        }

        return (int) rate;
    }

    public int RateWater() {
        double rate;

        //水の必要数の計算
        //　水の部分が計算が異なっていたので修正。修正者：岡田
        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);

        double mizu = pref.getInt("mizu_number", 0);

        //人数
        int adult_n = pref.getInt("otona_people", 0);
        int child_n = pref.getInt("kobito_people", 0);
        int baby_n = pref.getInt("youji_people", 0);

        int setDays = pref.getInt("sitei_day", 3);

        double adult_w = adult_n * 3;
        double child_w = child_n * 2;
        double baby_w = baby_n * 2;

        //  水の必要値の算出。備えちゃお日数も追加。
        double total_w = (adult_w + child_w + baby_w) * setDays;

        //備蓄は何％あるか計算。最大50％
        rate = (mizu / total_w) * 50;

        //  水の最大値は50％までの設定
        if (rate >= 50) {
            rate = 50;
        }

        if ((adult_n + child_n + baby_n) == 0) {
            rate = 0.0;
        }

        return (int) rate;
    }
}