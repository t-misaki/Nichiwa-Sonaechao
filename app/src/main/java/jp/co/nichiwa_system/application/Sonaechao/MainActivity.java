package jp.co.nichiwa_system.application.Sonaechao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

/*
 *   MainActivityクラス
 *   メイン画面の中の処理
 *   activity_main.xml の 内部の処理を行うクラスです
 */
public class MainActivity extends Activity {

    //非常食の種類
    final int MAX_HIJOUSYOKU = 12;
    final int MAX_BICHIKUHIN = 25;

    //非常食の項目を取得する
    ItemClass[] item = {
            // 食べ物
            new ItemClass("レトルトご飯", "retorutogohan_number", R.drawable.retoruto_gohan, true, "食", this),
            new ItemClass("缶詰（ご飯）", "kandume_number", R.drawable.kandume_gohan, true, "缶", this),
            new ItemClass("乾麺", "kanmen_number", R.drawable.kanmen, true, "袋", this),
            new ItemClass("乾パン", "kanpan_number", R.drawable.kanpan, true, "缶", this),
            new ItemClass("缶詰（肉・魚）", "kandume2_number", R.drawable.kandume, true, "缶", this),
            new ItemClass("レトルト食品", "retoruto_number", R.drawable.retoruto, true, "袋", this),
            new ItemClass("フリーズドライ", "furizu_dorai_number", R.drawable.furizu_dorai, true, "食", this),
            new ItemClass("カロリーメイト", "karori_meito_number", R.drawable.karori_meito, true, "箱", this),
            new ItemClass("お菓子", "okasi_number", R.drawable.okasi, true, "箱・袋", this),

            // 幼児用
            new ItemClass("離乳食", "rinyu_number", R.drawable.rinyu, true, "食", this),
            new ItemClass("粉ミルク", "konamilk_number", R.drawable.konamilk, true, "缶", this),

            // 水
            new ItemClass("水", "mizu_number", R.drawable.mizu, true, "ℓ", this),

            // 必需品
            new ItemClass("ガスコンロ・鍋", "gas_number", R.drawable.gas, false, "個", this),
            new ItemClass("ガスボンベ", "bombe_number", R.drawable.bombe, true, "本", this),
            new ItemClass("乾電池　※単三", "denti_number", R.drawable.denti, true, "×１０本", this),
            new ItemClass("ティッシュ・ウェットティッシュ", "tissue_number", R.drawable.thissyu, false, "箱", this),
            new ItemClass("大人下着", "shitagi_number", R.drawable.otona, false, "枚", this),
            new ItemClass("小人下着", "kodomo_number", R.drawable.kodomo, false, "枚", this),
            new ItemClass("アルミホイル", "almi_number", R.drawable.almi, false, "本", this),
            new ItemClass("ラップ", "rap_number", R.drawable.rappu, false, "本", this),
            new ItemClass("ビニール袋（ゴミ袋）", "bag_number", R.drawable.hukuro, false, "×１０枚", this),
            new ItemClass("割り箸", "hasi_number", R.drawable.hasi, false, "×１００膳", this),
            new ItemClass("スプーン", "supun_number", R.drawable.spoon, false, "×１００本", this),
            new ItemClass("タオル", "taoru_number", R.drawable.taoru, false, "枚", this),
            new ItemClass("コップ（プラスチック）", "koppu_number", R.drawable.koppu, false, "個", this),
            new ItemClass("器（プラスチック）", "utuwa_number", R.drawable.utuwa, false, "枚", this),
            new ItemClass("懐中電灯　※単三電池推奨", "kaityu_number", R.drawable.kaityu, false, "本", this),

            // 便利品
            new ItemClass("ラジオ　※単三電池推奨", "radio_number", R.drawable.radio, false, "個", this),
            new ItemClass("携帯電話充電器　※単三電池推奨", "judenki_number", R.drawable.judenti, false, "個", this),
            new ItemClass("笛（防犯ブザー）", "whistle_number", R.drawable.whistle, false, "個", this),
            new ItemClass("缶切り", "kankiri_number", R.drawable.kankiri, false, "個", this),
            new ItemClass("マッチ・ライター", "match_number", R.drawable.match, false, "箱", this),
            new ItemClass("寝袋", "nebukuro_number", R.drawable.nebukuro, false, "枚", this),
            new ItemClass("マスク", "mask_number", R.drawable.mask, false, "×１００枚", this),
            new ItemClass("軍手", "gunnte_number", R.drawable.gunnte, false, "対", this),

            // 幼児用
            new ItemClass("哺乳びん", "nyuji_number", R.drawable.bin, false, "本", this),
            new ItemClass("おむつ", "omutu_number", R.drawable.omutu, false, "枚", this)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初期設定（設定画面から起動する）
        SharedPreferences firstpref = getSharedPreferences("Preference", MODE_PRIVATE);
        if (firstpref.getInt("first_key", 0) == 0) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setClassName("jp.co.nichiwa_system.application.Sonaechao", "jp.co.nichiwa_system.application.Sonaechao.SubActivity");
            startActivity(intent);

            SharedPreferences.Editor e = firstpref.edit();
            e.putInt("first_key", 1);
            e.commit();
        }

        if (firstpref.getInt("first_key", 0) == 1) {
            AlertDialog.Builder fast = new AlertDialog.Builder(this);
            fast.setTitle("ホーム画面の説明");
            fast.setMessage("ここでは非常食と備蓄品がどれだけあるか％表示で確認できます。。\n" +
                    "要チェック欄の商品を選択することで備蓄数を確認、編集できます。\n");
            fast.setPositiveButton("次へ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AlertDialog.Builder fast = new AlertDialog.Builder(MainActivity.this);
                    fast.setTitle("ホーム画面の説明");
                    fast.setMessage("メーター上の非常食、備蓄品の文字色により備蓄不足か判断できます\n\n" +
                            "赤・・・備蓄不足\n" +"青・・・備蓄完了\n\n"+
                            "要チェック欄の項目がなくなると、備蓄はより完璧なものになります。\n\n"+
                            "※このメッセージは画面下のホームボタンを押すと再び表示されます。");
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

            SharedPreferences.Editor e = firstpref.edit();
            e.putInt("first_key", 2);
            e.commit();
        }

        //activity_main.xmlを使う場合これを宣言する
        setContentView(R.layout.activity_main);

        ImageButton DispBtn = (ImageButton) findViewById(R.id.settingbutton);//「設定」ボタン
        ImageButton Home = (ImageButton) findViewById(R.id.home);//「ホーム」ボタン
        ImageButton Stock = (ImageButton) findViewById(R.id.bichiku);//「備蓄」ボタン
        ImageButton hijousyoku = (ImageButton) findViewById(R.id.hijousyoku);//「非常食」ボタン

        ImageButton hijousyoku_ib = (ImageButton) findViewById(R.id.L_graph);
        ImageButton bichiku_ib = (ImageButton) findViewById(R.id.R_graph);

        Home.setBackgroundResource(R.drawable.style2);
        Home.setOnClickListener(new View.OnClickListener() { // 非常食ボタンを押した時の処理（説明ダイアログ）
            @Override
            public void onClick(View v) {
                AlertDialog.Builder fast = new AlertDialog.Builder(MainActivity.this);
                fast.setTitle("ホーム画面の説明");
                fast.setMessage("ここでは非常食と備蓄品がどれだけあるか％表示で確認できます。。\n" +
                        "要チェック欄の商品を選択することで備蓄数を確認、編集できます。\n");
                fast.setPositiveButton("次へ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlertDialog.Builder fast = new AlertDialog.Builder(MainActivity.this);
                        fast.setTitle("ホーム画面の説明");
                        fast.setMessage("メーター上の非常食、備蓄品の文字色により備蓄不足か判断できます\n\n" +
                                "赤・・・備蓄不足\n" + "青・・・備蓄完了\n\n" +
                                "要チェック欄の項目がなくなると、備蓄はより完璧なものになります。\n\n" +
                                "※このメッセージは画面下のホームボタンを押すと再び表示されます。");
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

        //非常食へ
        hijousyoku.setOnClickListener(new OnClickTransListenerClass(".Hijousyoku", this));
        hijousyoku_ib.setOnClickListener(new OnClickTransListenerClass(".Hijousyoku", this));
        //備蓄品へ
        Stock.setOnClickListener(new OnClickTransListenerClass(".Stock", this));
        bichiku_ib.setOnClickListener(new OnClickTransListenerClass(".Stock", this));
        //設定画面へ
        DispBtn.setOnClickListener(new OnClickTransListenerClass(".SubActivity", this));


        // 広告の設定
        // IDはあくまでテスト用なので、apkを出すときは外すように
        AdView adview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);

    }

    //アクティビティが実行される直前に行われる処理
    @Override
    protected void onResume() {
        super.onResume();//おなじみ

        Calendar cl = Calendar.getInstance();       //今日の日付の取得
        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);

        //各グラフの取得
        ImageButton R_button = (ImageButton) findViewById(R.id.R_graph);
        ImageButton L_button = (ImageButton) findViewById(R.id.L_graph);

        //  非常食、備蓄品、それぞれのパーセント配列
        int[] goukei = new int[2];
        //  非常食、備蓄品、それぞれの合計値配列
        int[] volume = new int[2];

        //要チェック
        //プレファレンスを生成して、設定画面のデータを取得する
        //pref = getSharedPreferences("Preferences",MODE_PRIVATE);
        int gou = pref.getInt("youji_people", 0) +
                pref.getInt("kobito_people", 0) +
                pref.getInt("otona_people", 0);

        //非常食の割合を取得
        goukei[0] = FoodOverKids() + FoodBaby() + RateWater();
        volume[0] = VolumeFoods();

        //左グラフの画像
        if (goukei[0] < 10) {
            L_button.setImageResource(R.drawable.l_graph0);
        } else if (10 <= goukei[0] && goukei[0] < 20) {
            L_button.setImageResource(R.drawable.l_graph1);
        } else if (20 <= goukei[0] && goukei[0] < 30) {
            L_button.setImageResource(R.drawable.l_graph2);
        } else if (30 <= goukei[0] && goukei[0] < 40) {
            L_button.setImageResource(R.drawable.l_graph3);
        } else if (40 <= goukei[0] && goukei[0] < 50) {
            L_button.setImageResource(R.drawable.l_graph4);
        } else if (50 <= goukei[0] && goukei[0] < 60) {
            L_button.setImageResource(R.drawable.l_graph5);
        } else if (60 <= goukei[0] && goukei[0] < 70) {
            L_button.setImageResource(R.drawable.l_graph6);
        } else if (70 <= goukei[0] && goukei[0] < 80) {
            L_button.setImageResource(R.drawable.l_graph7);
        } else if (80 <= goukei[0] && goukei[0] < 90) {
            L_button.setImageResource(R.drawable.l_graph8);
        } else if (90 <= goukei[0] && goukei[0] < 100) {
            L_button.setImageResource(R.drawable.l_graph9);
        } else if (100 >= goukei[0]) {
            L_button.setImageResource(R.drawable.l_graph10);
        }

        //グラフのパーセント値を表示する
        ((TextView) findViewById(R.id.hijousyoku_percent)).setText("非常食：" + String.valueOf(goukei[0]) + "%");

        //防犯グッズの値
        goukei[1] = RateStock();
        volume[1] = VolumeStock();

        //右グラフの画像
        if (goukei[1] < 10) {
            R_button.setImageResource(R.drawable.r_graph0);
        } else if (10 <= goukei[1] && goukei[1] < 20) {
            R_button.setImageResource(R.drawable.r_graph1);
        } else if (20 <= goukei[1] && goukei[1] < 30) {
            R_button.setImageResource(R.drawable.r_graph2);
        } else if (30 <= goukei[1] && goukei[1] < 40) {
            R_button.setImageResource(R.drawable.r_graph3);
        } else if (40 <= goukei[1] && goukei[1] < 50) {
            R_button.setImageResource(R.drawable.r_graph4);
        } else if (50 <= goukei[1] && goukei[1] < 60) {
            R_button.setImageResource(R.drawable.r_graph5);
        } else if (60 <= goukei[1] && goukei[1] < 70) {
            R_button.setImageResource(R.drawable.r_graph6);
        } else if (70 <= goukei[1] && goukei[1] < 80) {
            R_button.setImageResource(R.drawable.r_graph7);
        } else if (80 <= goukei[1] && goukei[1] < 90) {
            R_button.setImageResource(R.drawable.r_graph8);
        } else if (90 <= goukei[1] && goukei[1] < 100) {
            R_button.setImageResource(R.drawable.r_graph9);
        } else if (goukei[1] >= 100) {
            R_button.setImageResource(R.drawable.r_graph10);
        }

        //グラフのパーセント値を表示する(10%ずつ)
        ((TextView) findViewById(R.id.bichiku_percent)).setText("備蓄品：" + String.valueOf(goukei[1]) + "%");

        //ダイアログの生成
        DialogClass keikoku;

        //備蓄品の割合が60%未満且つ、非常食が0ではない且つ、設定人数が0の場合、警告を出す。
        if (goukei[0] < 60/* && !(volume[0] <= 0) && gou > 0*/) {
            TextView h_tv = (TextView)findViewById(R.id.textView17);
            h_tv.setTextColor(Color.RED);
           /* keikoku = new DialogClass("警告", "非常食が60%未満です\n" + "※要チェック欄か非常食ボタンから備蓄してください" , this);
            keikoku.setPositiveButton("確認する", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.setClassName("jp.co.nichiwa_system.application.Sonaechao", "jp.co.nichiwa_system.application.Sonaechao.Hijousyoku");
                    startActivity(intent);
                }
            });
            keikoku.setNegativeButton("後で", null);
            keikoku.Diarog_show();*/
        }
        else{
            TextView h_tv = (TextView)findViewById(R.id.textView17);
            h_tv.setTextColor(Color.BLUE);
        }

        //非常食の割合が60%未満且つ、備蓄品が0ではない且つ、設定人数が0の場合、警告を出す
        if (goukei[1] < 60 /*&& !(volume[1] <= 0) && gou > 0*/) {
            TextView b_tv = (TextView)findViewById(R.id.textView18);
            b_tv.setTextColor(Color.RED);
           /* keikoku = new DialogClass("警告", "　備蓄品が60%未満です", this);
            keikoku.setPositiveButton("確認する", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.setClassName("jp.co.nichiwa_system.application.Sonaechao", "jp.co.nichiwa_system.application.Sonaechao.Stock");
                    startActivity(intent);

                }
            });
            keikoku.setNegativeButton("後で", null);
            keikoku.Diarog_show();*/
        }
        else{
            TextView b_tv = (TextView)findViewById(R.id.textView18);
            b_tv.setTextColor(Color.BLUE);
        }

        //設定画面の合計値(gou)が0ならば警告ダイアログをだす
        if (gou == 0) {
            //ダイアログの表示
            keikoku = new DialogClass("人数の合計が0です！", "　人数の設定を行ってください！", this);
            keikoku.setPositiveButton("設定へ移動", new ListenerClass("jp.co.nichiwa_system.application.Sonaechao", "jp.co.nichiwa_system.application.Sonaechao.SubActivity", MainActivity.this));
//            dc.setNegativeButton("後で",null);
            keikoku.Diarog_show();
        }

        //要チェックに使用するTextViewを使用する
        TextView[] YouCheck_tv = new TextView[MAX_HIJOUSYOKU + MAX_BICHIKUHIN];

        //フラグメントのリニアレイアウトを取得
        TableLayout tl = (TableLayout) findViewById(R.id.CheckLayout);
        tl.removeAllViews();//中身を全部消去


        for (int i = 0; i < MAX_HIJOUSYOKU + MAX_BICHIKUHIN; i++) {
            YouCheck_tv[i] = new TextView(this);
            //警告文を取得する
            YouCheck_tv[i].setTextSize(30.0f);
            //if (pref.getInt(item[i].getPrefName(), 0) > 0) { // 要チェック欄とアイコン画像の喧嘩が起こるif文
            YouCheck_tv[i].setText(get_Number_of_days_Warning(item[i].getPrefName(), item[i].getName(), item[i].getCalender_flag()));

            if ( /*pref.getInt(item[i].getPrefName(), 0) > 0 &&*/ i == 9 || i == 10) {
                YouCheck_tv[i].setText(get_Number_of_days_Warning_b(item[i].getPrefName(), item[i].getName(), item[i].getCalender_flag()));
            }
            if ( /*pref.getInt(item[i].getPrefName(), 0) > 0 &&*/ i == 11) {
                YouCheck_tv[i].setText(get_Number_of_days_Warning_w(item[i].getPrefName(), item[i].getName(), item[i].getCalender_flag()));
            }
            if (i >= 12) {
                YouCheck_tv[i].setText(get_Number_of_days_Warning_bichiku(item[i].getPrefName(), item[i].getName(), item[i].getCalender_flag()));
            }

            //警告文を挿入する
            if (YouCheck_tv[i].getText().length() > 0) {

                //警告文を押すとダイアログが表示されるようにする
                YouCheck_tv[i].setOnClickListener(new DialogOnClickListenerClass(item[i]));

                //アイコンの設定
                get_Icon_Warning(item[i].getPrefName(), item[i]);
                YouCheck_tv[i].setCompoundDrawablesWithIntrinsicBounds(item[i].getIcon(), 0, 0, 0);
                if (item[i].getIcon() == R.drawable.batsu) {
                    YouCheck_tv[i].setTextColor(Color.RED);
                }
                if (item[i].getIcon() == R.drawable.bikkuri) {
                    YouCheck_tv[i].setTextColor(Color.BLUE);
                }
            }
        }

        int check = 0;
        for (int i = 0; i < MAX_HIJOUSYOKU + MAX_BICHIKUHIN; i++) {
            if (item[i].getIcon() == R.drawable.batsu || item[i].getIcon() == R.drawable.batsu_b || item[i].getIcon() == R.drawable.bikkuri || item[i].getIcon() == R.drawable.bikkuri_b) {
                check++;
            }
        }
        TextView check_tv = (TextView) findViewById(R.id.textView);
        check_tv.setText("要チェック欄:" + check + "件");

        /*******************************************************************************************
         // 要チェック欄に「～が足りていません」というメッセージを出す
         //
         // メッセージ出力の条件
         //   大人または小人が1以上、幼児が0　かつ　非常食の食べ物が50％未満
         //   大人または小人が1以上、幼児が1以上　かつ　食べ物25％未満、乳児用25％未満
         //   大人または小人が0、幼児が1以上　かつ　乳児用50％未満
         //   水は大人または小人または幼児が1以上の場合に50％未満だとメッセージが出ます
         *******************************************************************************************/
        SharedPreferences Preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        int otona = Preferences.getInt("otona_people", 0); // 大人の人数を取得
        int kobito = Preferences.getInt("kobito_people", 0); // 小人の人数を取得
        int youji = Preferences.getInt("youji_people", 0); // 幼児の人数を取得
        int setDays = pref.getInt("sitei_day", 3); // 設定日数を取得


        for (int i = 0; i < MAX_HIJOUSYOKU + MAX_BICHIKUHIN; i++) {
            YouCheck_tv[i].setText(get_Number_of_days_Warning(item[i].getPrefName(), item[i].getName(), item[i].getCalender_flag())); // 非常食品名 + が足りていません
            YouCheck_tv[i].setCompoundDrawablesWithIntrinsicBounds(item[i].getIcon(), 0, 0, 0);
            YouCheck_tv[i].setOnClickListener(new DialogOnClickListenerClass(item[i])); //警告文を押すとダイアログが表示されるようにする
            if (i == 9) {
                YouCheck_tv[9].setText(get_Number_of_days_Warning_b(item[9].getPrefName(), item[9].getName(), item[9].getCalender_flag())); // 離乳食が足りていません
                YouCheck_tv[9].setCompoundDrawablesWithIntrinsicBounds(item[9].getIcon(), 0, 0, 0);
                YouCheck_tv[9].setOnClickListener(new DialogOnClickListenerClass(item[9])); //警告文を押すとダイアログが表示されるようにする
            }
            if (i == 10) {
                YouCheck_tv[10].setText(get_Number_of_days_Warning_b(item[10].getPrefName(), item[10].getName(), item[10].getCalender_flag())); // 粉ミルクが足りていません
                YouCheck_tv[10].setCompoundDrawablesWithIntrinsicBounds(item[10].getIcon(), 0, 0, 0);
                YouCheck_tv[10].setOnClickListener(new DialogOnClickListenerClass(item[10])); //警告文を押すとダイアログが表示されるようにする
            }
            if (i == 11) {
                YouCheck_tv[11].setText(get_Number_of_days_Warning_w(item[11].getPrefName(), item[11].getName(), item[11].getCalender_flag())); // 水が足りていません
                YouCheck_tv[11].setCompoundDrawablesWithIntrinsicBounds(item[11].getIcon(), 0, 0, 0);
                YouCheck_tv[11].setOnClickListener(new DialogOnClickListenerClass(item[11])); //警告文を押すとダイアログが表示されるようにする
            }
            if (i >= 12) {
                YouCheck_tv[i].setText(get_Number_of_days_Warning_bichiku(item[i].getPrefName(), item[i].getName(), item[i].getCalender_flag())); // 備蓄品名 + が足りていません
                YouCheck_tv[i].setCompoundDrawablesWithIntrinsicBounds(item[i].getIcon(), 0, 0, 0);
                YouCheck_tv[i].setOnClickListener(new DialogOnClickListenerClass(item[i])); //警告文を押すとダイアログが表示されるようにする
            }

        }

        /*******************************************************************************************
         * 　ソートプログラム
         ******************************************************************************************/

        for (int i = 0; i < MAX_HIJOUSYOKU + MAX_BICHIKUHIN; i++) {
            //特に警告のないものは飛ばす
            if (YouCheck_tv[i].getText().length() > 0) {
                for (int k = MAX_HIJOUSYOKU + MAX_BICHIKUHIN - 1; k > i; k--) {
                    //同じく特に警告のないものは飛ばす
                    if (YouCheck_tv[k].getText().length() > 0) {
                        if (getDate(item[k].getPrefName()) < getDate(item[k - 1].getPrefName())) {
                            //場所を交換する
                            TextView tv = YouCheck_tv[k - 1];
                            YouCheck_tv[k - 1] = YouCheck_tv[k];
                            YouCheck_tv[k] = tv;

                            //アイテム
                            ItemClass ic = item[k - 1];
                            item[k - 1] = item[k];
                            item[k] = ic;
                        }

                        // 離乳食である
                        if (item[k].getName() == "離乳食") {
                            //場所を交換する
                            TextView tv = YouCheck_tv[k - 1];
                            YouCheck_tv[k - 1] = YouCheck_tv[k];
                            YouCheck_tv[k] = tv;

                            //アイテム
                            ItemClass ic = item[k - 1];
                            item[k - 1] = item[k];
                            item[k] = ic;
                        }

                        // 粉ミルクである
                        if (item[k].getName() == "粉ミルク") {
                            //場所を交換する
                            TextView tv = YouCheck_tv[k - 1];
                            YouCheck_tv[k - 1] = YouCheck_tv[k];
                            YouCheck_tv[k] = tv;

                            //アイテム
                            ItemClass ic = item[k - 1];
                            item[k - 1] = item[k];
                            item[k] = ic;
                        }

                        if (item[k].getIcon() == R.drawable.batsu) { //×ボタンである
                            //場所を交換する
                            TextView tv = YouCheck_tv[k - 1];
                            YouCheck_tv[k - 1] = YouCheck_tv[k];
                            YouCheck_tv[k] = tv;

                            //アイテム
                            ItemClass ic = item[k - 1];
                            item[k - 1] = item[k];
                            item[k] = ic;
                        }
                    }
                }
                //画面に表示する
                tl.addView(YouCheck_tv[i]);
            }
        }
 /*       for (int i = 0; i < MAX_BICHIKUHIN; i++) {
            //特に警告のないものは飛ばす
            if (YouCheck_tv[MAX_HIJOUSYOKU+i].getText().length() > 0) {
                for (int k = MAX_BICHIKUHIN - MAX_HIJOUSYOKU - 1; k > i; k--) {
                    //同じく特に警告のないものは飛ばす
                    if (YouCheck_tv[k].getText().length() > 0) {
                        if (getDate(item[k].getPrefName()) < getDate(item[k - 1].getPrefName())) {
                            //場所を交換する
                            TextView tv = YouCheck_tv[k - 1];
                            YouCheck_tv[k - 1] = YouCheck_tv[k];
                            YouCheck_tv[k] = tv;

                            //アイテム
                            ItemClass ic = item[k - 1];
                            item[k - 1] = item[k];
                            item[k] = ic;
                        }



                        if (item[k].getIcon() == R.drawable.batsu) { //×ボタンである
                            //場所を交換する
                            TextView tv = YouCheck_tv[k - 1];
                            YouCheck_tv[k - 1] = YouCheck_tv[k];
                            YouCheck_tv[k] = tv;

                            //アイテム
                            ItemClass ic = item[k - 1];
                            item[k - 1] = item[k];
                            item[k] = ic;
                        }
                    }
                }
                //画面に表示する
                tl.addView(YouCheck_tv[MAX_HIJOUSYOKU+i]);
            }
        }*/
        //最終入力日
        TextView b_tv = (TextView) findViewById(R.id.bichiku_nyuuryoku);
        TextView h_tv = (TextView) findViewById(R.id.hijousyoku_nyuuryoku);

        //備蓄品の最終入力日
        pref = getSharedPreferences("Stock_pref", MODE_PRIVATE);
        cl.set(pref.getInt("year", 2000), pref.getInt("month", 1), pref.getInt("day", 1));
        if (pref.getInt("year", 2000) == 2000 && pref.getInt("month", 1) == 1 && pref.getInt("day", 1) == 1) {
            b_tv.setText("備蓄品はまだ入力されていません");
        } else {
            b_tv.setText("最終入力日:" + cl.get(Calendar.YEAR) + "年" + (cl.get(Calendar.MONTH) + 1) + "月" + cl.get(Calendar.DAY_OF_MONTH) + "日");
        }
        //非常食の最終入力日
        pref = getSharedPreferences("Hijousyoku_pref", MODE_PRIVATE);
        cl.set(pref.getInt("year", 2000), pref.getInt("month", 1), pref.getInt("day", 1));
        if (pref.getInt("year", 2000) == 2000 && pref.getInt("month", 1) == 1 && pref.getInt("day", 1) == 1) {
            h_tv.setText("非常食はまだ入力されていません");
        } else {
            h_tv.setText("最終入力日:" + cl.get(Calendar.YEAR) + "年" + (cl.get(Calendar.MONTH) + 1) + "月" + cl.get(Calendar.DAY_OF_MONTH) + "日");
        }

    }

    /**
     * ******************************************************************
     * // 消費期限と設定した期日を計算して、それぞれのテキストを返す
     * // prefName ・・・ プレファレンス名
     * // ItemName ・・・ 項目の名前
     * *******************************************************************
     */
    public String get_Number_of_days_Warning(String prefName, String ItemName, boolean isCal) {
        String str = "";

        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);
        //残り日数を取得する
        int nokori = (int) getDate(prefName);
        //期日を取得する
        int nissu = (getSharedPreferences("Preferences", MODE_PRIVATE)).getInt("kiniti_day", 14);
        SharedPreferences Preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        int otona = Preferences.getInt("otona_people", 0); // 大人の人数を取得
        int kobito = Preferences.getInt("kobito_people", 0); // 小人の人数を取得
        int youji = Preferences.getInt("youji_people", 0); // 幼児の人数を取得


        if (youji >= 1 && (otona >= 1 || kobito >= 1)) { // 幼児が1人以上
            if (FoodOverKids() < 25) { // もし非常食が25％未満なら
                str = ItemName + "が足りていません";
            } else if (FoodOverKids() >= 25 && pref.getInt(prefName, 0 ) <= 0) {
                str = "";
            } else if (isCal == true && (otona >= 1 || kobito >= 1)) { // カレンダーが存在するもの
                if (nokori == 0) { // 備蓄数が1以上の消費期限表示
                    //消費期限が当日になったら表示
                    str = ItemName + "の消費期限が当日です";
                } else if (nokori < 0) {
                    //消費期限が切れたら表示
                    str = ItemName + "の消費期限が切れました";
                } else if (nokori <= nissu) {
                    //消費期限が期日に近づいたら表示
                    str = ItemName + "の消費期限が" + nokori + "日前です";
                }
            }
        } else if (youji <= 0 && (otona >= 1 || kobito >= 1)) { // 幼児がいない
            if (FoodOverKids() < 50) { // もし足りていなかったら
                str = ItemName + "が足りていません";
            } else if(FoodOverKids() >= 50 && pref.getInt(prefName,0) <= 0) {
                str = "";
            } else if (isCal == true && (otona >= 1 || kobito >= 1)) { // カレンダーが存在するもの
                if (nokori == 0) { // 備蓄数が1以上の消費期限表示
                    //消費期限が当日になったら表示
                    str = ItemName + "の消費期限が当日です";
                } else if (nokori < 0) {
                    //消費期限が切れたら表示
                    str = ItemName + "の消費期限が切れました";
                } else if (nokori <= nissu) {
                    //消費期限が期日に近づいたら表示
                    str = ItemName + "の消費期限が" + nokori + "日前です";
                }
            }
        } else {
            str = "";
        }
        return str;
    }

    /**
     * ******************************************************************
     * // 消費期限と設定した期日を計算して、それぞれのテキストを返す
     * // prefName ・・・ プレファレンス名
     * // ItemName ・・・ 項目の名前
     * *******************************************************************
     */
    public String get_Number_of_days_Warning_b(String prefName, String ItemName, boolean isCal) {
        String str = "";

        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);
        //残り日数を取得する
        int nokori = (int) getDate(prefName);
        //期日を取得する
        int nissu = (getSharedPreferences("Preferences", MODE_PRIVATE)).getInt("kiniti_day", 14);
        SharedPreferences Preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        int otona = Preferences.getInt("otona_people", 0); // 大人の人数を取得
        int kobito = Preferences.getInt("kobito_people", 0); // 小人の人数を取得
        int youji = Preferences.getInt("youji_people", 0); // 幼児の人数を取得


        if (youji >= 1 && (otona >= 1 || kobito >= 1)) { // 大人または小人が1人以上
            if (FoodBaby() < 25) { // もし足りていなかったら
                str = ItemName + "が足りていません";
            } else if (FoodBaby() >= 25 && pref.getInt(prefName, 0) <= 0 ) {
                str = "";
            } else if (isCal == true && youji >= 1) { // カレンダーが存在するもの
                if (nokori == 0) { // 備蓄数が1以上の消費期限表示
                    //消費期限が当日になったら表示
                    str = ItemName + "の消費期限が当日です";
                } else if (nokori < 0) {
                    //消費期限が切れたら表示
                    str = ItemName + "の消費期限が切れました";
                } else if (nokori <= nissu) {
                    //消費期限が期日に近づいたら表示
                    str = ItemName + "の消費期限が" + nokori + "日前です";
                }
            }
        } else if (youji >= 1 && (otona <= 0 && kobito <= 0)) { // 大人または小人がいない
            if (FoodBaby() < 50) { // もし足りていなかったら
                str = ItemName + "が足りていません";
            } else if (FoodBaby() > 50 && pref.getInt(prefName, 0) <= 0) {
                str = "";
            } else if (isCal == true && youji >= 1) { // カレンダーが存在するもの
                if (nokori == 0) { // 備蓄数が1以上の消費期限表示
                    //消費期限が当日になったら表示
                    str = ItemName + "の消費期限が当日です";
                } else if (nokori < 0) {
                    //消費期限が切れたら表示
                    str = ItemName + "の消費期限が切れました";
                } else if (nokori <= nissu) {
                    //消費期限が期日に近づいたら表示
                    str = ItemName + "の消費期限が" + nokori + "日前です";
                }
            }
        } else {
            str = "";
        }
        return str;
    }

    /**
     * ******************************************************************
     * // 消費期限と設定した期日を計算して、それぞれのテキストを返す
     * // prefName ・・・ プレファレンス名
     * // ItemName ・・・ 項目の名前
     * *******************************************************************
     */
    public String get_Number_of_days_Warning_w(String prefName, String ItemName, boolean isCal) {
        String str = "";

        //残り日数を取得する
        int nokori = (int) getDate(prefName);
        //期日を取得する
        int nissu = (getSharedPreferences("Preferences", MODE_PRIVATE)).getInt("kiniti_day", 14);

        if (RateWater() < 50) { // もし足りていなかったら
            str = ItemName + "が足りていません";
        }
        else if (isCal == true) { // カレンダーが存在するもの
            if (nokori == 0) { // 備蓄数が1以上の消費期限表示
                //消費期限が当日になったら表示
                str = ItemName + "の消費期限が当日です";
            } else if (nokori < 0) {
                //消費期限が切れたら表示
                str = ItemName + "の消費期限が切れました";
            } else if (nokori <= nissu) {
                //消費期限が期日に近づいたら表示
                str = ItemName + "の消費期限が" + nokori + "日前です";
            }
        } else {
            str = "";
        }
        return str;
    }

    public String get_Number_of_days_Warning_bichiku(String prefName, String ItemName, boolean isCal) {
        String str = "";
        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);

        //残り日数を取得する
        int nokori = (int) getDate(prefName);
        //期日を取得する
        int nissu = (getSharedPreferences("Preferences", MODE_PRIVATE)).getInt("kiniti_day", 14);
        SharedPreferences Preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        int otona = Preferences.getInt("otona_people", 0); // 大人の人数を取得
        int kobito = Preferences.getInt("kobito_people", 0); // 小人の人数を取得
        int youji = Preferences.getInt("youji_people", 0); // 幼児の人数を取得
        float need = pref.getInt(prefName, 0);
        int setDays = pref.getInt("sitei_day", 3);


        if (UsedFamilyStockneed(need, setDays, 1.0f, 1.0f, 1.0f) != 0) { // 大人または小人が1人以上
            str = ItemName + "が足りていません";
        } else {
            str = "";
        }
        if (ItemName == "ガスボンベ") {
            if (UsedFamilyStockneed(need, setDays, 1.0f, 2.0f, 5.0f) != 0) { // 大人または小人が1人以上
                str = ItemName + "が足りていません";
            } else if (UsedFamilyStockneed(need, setDays, 1.0f, 2.0f, 5.0f) == 0) {
                if (nokori == 0) { // 備蓄数が1以上の賞味期限表示
                    //消費期限が当日になったら表示
                    str = ItemName + "の消費期限が当日です";
                } else if (nokori < 0) {
                    //消費期限が切れたら表示
                    str = ItemName + "の消費期限が切れました";
                } else if (nokori <= nissu) {
                    //消費期限が期日に近づいたら表示
                    str = ItemName + "の消費期限が" + nokori + "日前です";
                } else {
                    str = "";
                }
            }
        }
        if (ItemName == "乾電池　※単三") {
            if (UsedFamilyStockneed(need, setDays, 2.0f, 2.0f, 4.0f) != 0) { // 大人または小人が1人以上
                str = ItemName + "が足りていません";
            } else if (UsedFamilyStockneed(need, setDays, 2.0f, 2.0f, 4.0f) == 0) {
                if (nokori == 0) { // 備蓄数が1以上の賞味期限表示
                    //消費期限が当日になったら表示
                    str = ItemName + "の消費期限が当日です";
                } else if (nokori < 0) {
                    //消費期限が切れたら表示
                    str = ItemName + "の消費期限が切れました";
                } else if (nokori <= nissu) {
                    //消費期限が期日に近づいたら表示
                    str = ItemName + "の消費期限が" + nokori + "日前です";
                } else {
                    str = "";
                }
            }
        }
        if (ItemName == "ティッシュ・ウェットティッシュ" || ItemName == "ラップ" || ItemName == "ビニール袋（ゴミ袋）") {
            if (UsedFamilyStockneed(need, setDays, 1.0f, 1.0f, 3.0f) != 0) { // 大人または小人が1人以上
                str = ItemName + "が足りていません";
            } else {
                str = "";
            }
        }
        if (ItemName == "アルミホイル") {
            if (UsedFamilyStockneed(need, setDays, 1.0f, 1.0f, 2.0f) != 0) { // 大人または小人が1人以上
                str = ItemName + "が足りていません";
            } else {
                str = "";
            }
        }
        if (ItemName == "缶切り") {
            if (UsedFamilyStockneed(need, setDays, 0.0f, 1.0f, 2.0f) != 0) { // 大人または小人が1人以上
                str = ItemName + "が足りていません";
            } else {
                str = "";
            }
        }
        if (ItemName == "大人下着") {
            if (UsedWearStock_adultneed(need, setDays, otona) != 0) {
                str = ItemName + "が足りていません";
            } else {
                str = "";
            }
        }
        if (ItemName == "小人下着") {
            if (UsedWearStock_childneed(need, setDays, kobito) != 0) {
                str = ItemName + "が足りていません";
            } else {
                str = "";
            }
        }
        if (ItemName == "タオル") {
            if (UsedOneStockOnlyTaoruneed(need, otona, kobito, youji, setDays) != 0) {
                str = ItemName + "が足りていません";
            } else {
                str = "";
            }
        }
        if (ItemName == "コップ（プラスチック）" || ItemName == "器（プラスチック）") {
            if (UsedOneStockneed(need, otona, kobito, youji) != 0) {
                str = ItemName + "が足りていません";
            } else {
                str = "";
            }
        }
        if (ItemName == "寝袋" || ItemName == "軍手") {
            if (UsedOneStockneed(need, otona, kobito, 0) != 0) {
                str = ItemName + "が足りていません";
            } else {
                str = "";
            }
        }
        if (ItemName == "哺乳びん") {
            if (UsedOneStockneed(need, 0, 0, youji) != 0) {
                str = ItemName + "が足りていません";
            } else {
                str = "";
            }
        }
        if (ItemName == "おむつ") {
            if (UsedBabyomutuneed(need, setDays, youji) != 0) {
                str = ItemName + "が足りていません";
            } else {
                str = "";
            }
        }

        if( ItemName ==  "マッチ・ライター" || ItemName ==  "笛（防犯ブザー）" || ItemName ==  "軍手"
                || ItemName == "ラジオ　※単三電池推奨" || ItemName == "缶切り" || ItemName == "マスク"
                || ItemName == "携帯電話充電器　※単三電池推奨"){
            if( RateStock()>=50){
                str = "";
            }
        }

        return str;
    }

    /**
     * ******************************************************************
     * //非常食の賞味期限と設定した期日を計算して、それぞれのアイコンを返す
     * // prefName       ・・・ プレファレンス名
     * // HijousyokuName ・・・ 非常食の名前
     * *******************************************************************
     */
    public void get_Icon_Warning(String prefName, ItemClass item) {
        // 非常食の備蓄数を取得する
        int BichikuSu = (getSharedPreferences("Preferences", MODE_PRIVATE)).getInt(prefName, 0);
        //残り日数を取得する
        int nokori = (int) getDate(prefName);
        //期日を取得する
        int nissu = (getSharedPreferences("Preferences", MODE_PRIVATE)).getInt("kiniti_day", 0);
        item.setIcon(R.drawable.batsu);
        if (BichikuSu < 1) { // もし備蓄されていなかったら
            item.setIcon(R.drawable.batsu);
        }

        if (nokori <= 0) {
            item.setIcon(R.drawable.batsu);
        } else if (nokori <= nissu) {
            item.setIcon(R.drawable.bikkuri);
        }
        if (nokori <= 0 && item.getName() == "離乳食" || nokori <= 0 && item.getName() == "粉ミルク") {
            item.setIcon(R.drawable.batsu_b);
        } else if (nokori <= nissu && item.getName() == "離乳食" || nokori <= nissu && item.getName() == "粉ミルク") {
            item.setIcon(R.drawable.bikkuri_b);
        }
    }

    /**
     * ******************************************************************
     * // 賞味期限の日付と現在の日付から引き出された残り日数を返す
     * // prefName ・・・　プレファレンス名
     * *******************************************************************
     */
    public long getDate(String prefName) {
        SharedPreferences pref2 = getSharedPreferences(prefName + "_pref", MODE_PRIVATE);
        //現在の時刻
        Calendar cl = Calendar.getInstance();
        //引数で指定した食品の賞味期限
        Calendar cl2 = Calendar.getInstance();
        cl2.set(pref2.getInt("year", cl.get(Calendar.YEAR)), pref2.getInt("month", cl.get(Calendar.MONTH)), pref2.getInt("day", cl.get(Calendar.DAY_OF_MONTH)));
        Date date1 = cl.getTime();
        Date date2 = cl2.getTime();

        long current_time = date1.getTime();
        long item_time = date2.getTime();

        long nokori = (item_time - current_time) / (1000 * 60 * 60 * 24);

        return nokori;
    }

    /**
     * ******************************************************************
     * // 人数と非常食の栄養量から割り出されたパーセンテージを返す
     * // return: s_w ・・・ 非常食の全体のパーセンテージ
     * *******************************************************************
     */
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

    /**
     * ************************************************************************************************
     * //  処理内容：備蓄品のパーセントの算出
     * //  概要  　：備蓄品の各パーセントを求めだし、それらを足し合わせたものをreturnしている。
     * //  作成日　：2015年6月2日
     * //  担当者　：岡田 洋介
     * //  備考　　：幼児がいる場合は、必需品5割、便利品4割、幼児品1割。
     * //          ：幼児がいない場合は、必需品6割、便利品4割。となる。（それぞれの最大値です）
     * *************************************************************************************************
     */
    public int RateStock() {
        int rateStockTotal;
        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);

        int gas = pref.getInt("gas_number", 0);
        int matti = pref.getInt("match_number", 0);
        int bombe = pref.getInt("bombe_number", 0);
        int fue = pref.getInt("whistle_number", 0);
        int aSitagi = pref.getInt("shitagi_number", 0);
        int kSitagi = pref.getInt("kodomo_number", 0);
        int tissue = pref.getInt("tissue_number", 0);
        int almi = pref.getInt("almi_number", 0);
        int rap = pref.getInt("rap_number", 0);
        int gunnte = pref.getInt("gunnte_number", 0);
        int mask = pref.getInt("mask_number", 0);
        int bag = pref.getInt("bag_number", 0);
        int kaichu = pref.getInt("kaityu_number", 0);
        int kankiri = pref.getInt("kankiri_number", 0);
        int radio = pref.getInt("radio_number", 0);
        int judenki = pref.getInt("judenki_number", 0);
        int spoon = pref.getInt("supun_number", 0);
        int hasi = pref.getInt("hasi_number", 0);
        int koppu = pref.getInt("koppu_number", 0);
        int bin = pref.getInt("nyuji_number", 0);
        int omutu = pref.getInt("omutu_number", 0);
        int denti = pref.getInt("denti_number", 0);
        int nebukuro = pref.getInt("nebukuro_number", 0);
        int utuwa = pref.getInt("utuwa_number", 0);
        int towel = pref.getInt("taoru_number", 0);

        //人数
        int adult = pref.getInt("otona_people", 0);
        int kids = pref.getInt("kobito_people", 0);
        int baby = pref.getInt("youji_people", 0);

        //  設定人数
        int setDays = pref.getInt("sitei_day", 3);

/***************************************************************************************************
 //  必需品の計算S
 ***************************************************************************************************/

        float rateNTotal = 0.0f;
        float rateNOKAll = 0.0f;
        float rateNBAll = 0.0f;
        float divStock = 15.0f; //  備蓄品専用の割り算。カテゴリーの数だけ割り算をするので、変更有。
        float divBStock = 2.0f;

        rateNOKAll += UsedFamilyStock(kaichu, setDays, 1.0f, 1.0f, 1.0f);
        rateNOKAll += UsedFamilyStock(almi, setDays, 1.0f, 1.0f, 2.0f);
        rateNOKAll += UsedFamilyStock(rap, setDays, 1.0f, 1.0f, 3.0f);
        rateNOKAll += UsedFamilyStock(bombe, setDays, 1.0f, 2.0f, 5.0f);
        rateNOKAll += UsedFamilyStock(gas, setDays, 1.0f, 1.0f, 1.0f);
        rateNOKAll += UsedFamilyStock(tissue, setDays, 1.0f, 1.0f, 3.0f);
        rateNOKAll += UsedFamilyStock(bag, setDays, 1.0f, 1.0f, 1.0f);
        rateNOKAll += UsedFamilyStock(spoon, setDays, 1.0f, 1.0f, 1.0f);
        rateNOKAll += UsedFamilyStock(hasi, setDays, 1.0f, 1.0f, 1.0f);
        rateNOKAll += UsedFamilyStock(denti, setDays, 2.0f, 2.0f, 4.0f);
        rateNOKAll += UsedOneStock(koppu, adult, kids, baby);
        rateNOKAll += UsedOneStock(utuwa, adult, kids, baby);
        rateNOKAll += UsedOneStockOnlyTaoru(towel, adult, kids, baby, setDays);
        if (kids > 0) {
            rateNOKAll += UsedWearStock(kSitagi, setDays, 1.0f, 1.0f, 2.0f);
        }
        if (!(setDays == 1) && adult > 0) {   //
            rateNOKAll += UsedWearStock(aSitagi, setDays, 0.0f, 1.0f, 2.0f);
        }
        //  この下の関数だけ、divStockが２つ分
        if (baby > 0) {
            rateNBAll += UsedBabyWearStock(bin, omutu, setDays);
        }


        if (setDays == 1 && adult > 0) {
            divStock -= 1.0f;
        }

        if (adult == 0) {
            divStock -= 1.0f;
        }

        if (kids == 0) {
            divStock -= 1.0f;
        }

        if (baby == 0) {
            divBStock -= 2.0f;
        }

        if ((adult + kids) > 0 && baby == 0) {
            rateNOKAll = (rateNOKAll / divStock) * 0.6f;
        } else if ((adult + kids) > 0 && baby > 0) {
            rateNOKAll = (rateNOKAll / divStock) * 0.5f;
            rateNBAll = (rateNBAll / divBStock) * 0.1f;
        }

        //  必需品の最終計
        rateNTotal = (rateNOKAll + rateNBAll);
        if ((adult + kids + baby) == 0) {
            rateNTotal = 0.0f;
        }

/**********************************************************************************/
//  便利品
/**********************************************************************************/
        //
        float rateUTotal = 0.0f;
        float rateUAll = 0.0f;
        float divUStock = 8.0f;     //  全8カテゴリー

        //  幼児は必要ないので無条件で０
        rateUAll += UsedOneStock(gunnte, adult, kids, 0);
        rateUAll += UsedOneStock(nebukuro, adult, kids, 0);

        rateUAll += UsedFamilyStock(fue, setDays, 1.0f, 1.0f, 1.0f);
        rateUAll += UsedFamilyStock(matti, setDays, 1.0f, 1.0f, 1.0f);
        rateUAll += UsedFamilyStock(radio, setDays, 1.0f, 1.0f, 1.0f);
        if (!(setDays == 1)) {   //  設定が1日だった場合、行わない。０除算防止。
            rateUAll += UsedFamilyStock(kankiri, setDays, 0.0f, 1.0f, 1.0f);
        }
        rateUAll += UsedFamilyStock(mask, setDays, 1.0f, 1.0f, 1.0f);
        rateUAll += UsedFamilyStock(judenki, setDays, 1.0f, 1.0f, 1.0f);

        if (setDays == 1) {
            divUStock -= 1.0f;
        }

        rateUTotal = (rateUAll / divUStock) * 0.4f;

/**********************************************************************************/
//  備蓄品、トータルパーセンテージ
/**********************************************************************************/
        rateStockTotal = ((int) ((rateNTotal + rateUTotal) * 100));

        if ((adult + kids + baby) == 0) {
            rateStockTotal = 0;
        }
        return rateStockTotal;
    }

    /**
     * ************************************************************************************************
     * //  備蓄品で必要な関数S
     * *************************************************************************************************
     */
    public float UsedOneStockOnlyTaoru(float taoru, int adult, int kids, int baby, int set) {
        float rate = 0.0f;
        switch (set) {
            case 1:
                rate = taoru / (adult + kids + baby);
                if (rate >= 1.0f) {
                    rate = 1.0f;
                }
                break;

            case 3:
                rate = taoru / (adult + kids + (baby * 2.0f));
                if (rate >= 1.0f) {
                    rate = 1.0f;
                }
                break;

            case 7:
                rate = taoru / ((adult * 3.0f) + (kids * 3.0f) + (baby * 6.0f));
                if (rate >= 1.0f) {
                    rate = 1.0f;
                }
                break;
        }
        return rate;
    }

    public float UsedOneStockOnlyTaoruneed(float taoru, int adult, int kids, int baby, int set) {
        float rate = 0.0f;
        switch (set) {
            case 1:
                rate = (adult + kids + baby) - taoru;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;

            case 3:
                rate = (adult + kids + (baby * 2.0f)) - taoru;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;

            case 7:
                rate = ((adult * 3.0f) + (kids * 3.0f) + (baby * 6.0f)) - taoru;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;
        }
        return rate;
    }

    public boolean Check_Day(String prefName) {
        int i = 0;
        //残り日数を取得する
        int nokori = (int) getDate(prefName);

        if (nokori <= 0) {
            //賞味期限が切れたら表示
            return false;
        }
        return true;
    }

    //  コップと器専用の計算関数
    public float UsedOneStock(float stock, int adult, int kids, int baby) {
        float rate;

        rate = stock / (adult + kids + baby);
        if (rate >= 1.0f) {
            rate = 1.0f;
        }

        return rate;
    }

    //  コップと器専用の計算関数
    public float UsedOneStockneed(float stock, int adult, int kids, int baby) {
        float rate;

        rate = (adult + kids + baby) - stock;
        if (rate <= 0.0f) {
            rate = 0.0f;
        }
        Math.ceil(rate);
        return rate;
    }

    //  関数
    public float UsedFamilyStock(float stock, int set, float x, float y, float z) {
        float rate = 0.0f;
        switch (set) {
            case 1:
                rate = stock / x;
                break;

            case 3:
                rate = stock / y;
                break;

            case 7:
                rate = stock / z;
                break;
        }

        if (rate >= 1.0f) {
            rate = 1.0f;
        }

        return rate;
    }

    public float UsedFamilyStockneed(float stock, int set, float x, float y, float z) {
        float rate = 0.0f;
        switch (set) {
            case 1:
                rate = x - stock;
                break;

            case 3:
                rate = y - stock;
                break;

            case 7:
                rate = z - stock;
                break;
        }

        if (rate <= 0.0f) {
            rate = 0.0f;
        }
        Math.ceil(rate);
        return rate;
    }

    //改正版
    public float UsedWearStock(float wear, int set, float x, float y, float z) {
        float rate = 0.0f;

        switch (set) {
            case 1:
                rate = wear / x;
                if (rate >= 1.0f) {
                    rate = 1.0f;
                }
                break;

            case 3:
                rate = wear / y;
                if (rate >= 1.0f) {
                    rate = 1.0f;
                }
                break;

            case 7:
                rate = wear / z;
                if (rate >= 1.0f) {
                    rate = 1.0f;
                }
                break;
        }

        return rate;
    }

    public float UsedWearStock_adultneed(float wear, int set, int adult) {
        float rate = 0.0f;

        switch (set) {
            case 1:
                rate = (adult * 0.0f) - wear;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;

            case 3:
                rate = (adult * 1.0f) - wear;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;
            case 7:
                rate = (adult * 2.0f) - wear;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;
        }
        return rate;
    }

    public float UsedWearStock_childneed(float wear, int set, int child) {
        float rate = 0.0f;

        switch (set) {
            case 1:
                rate = (child * 1.0f) - wear;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;

            case 3:
                rate = (child * 1.0f) - wear;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;
            case 7:
                rate = (child * 2.0f) - wear;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;
        }
        return rate;
    }

    public float UsedBabyWearStock(float bin, float omutu, int set) {
        float rateBin = 0.0f;
        float rateOmutu = 0.0f;
        float rateFinal;

        switch (set) {
            case 1:
                rateBin = bin / 1.0f;
                if (rateBin >= 1.0f) {
                    rateBin = 1.0f;
                }

                rateOmutu = omutu / 2.0f;
                if (rateOmutu >= 1.0f) {
                    rateOmutu = 1.0f;
                }
                break;

            case 3:
                rateBin = bin / 1.0f;
                if (rateBin >= 1.0f) {
                    rateBin = 1.0f;
                }

                rateOmutu = omutu / 5.0f;
                if (rateOmutu >= 1.0f) {
                    rateOmutu = 1.0f;
                }
                break;

            case 7:
                rateBin = bin / 1.0f;
                if (rateBin >= 1.0f) {
                    rateBin = 1.0f;
                }

                rateOmutu = omutu / 10.0f;
                if (rateOmutu >= 1.0f) {
                    rateOmutu = 1.0f;
                }
                break;
        }

        rateFinal = rateBin + rateOmutu;

        return rateFinal;
    }

    public float UsedBabyomutuneed(float omutu, int set, int baby) {
        float rate = 0.0f;

        switch (set) {
            case 1:
                rate = (baby * 2.0f) - omutu;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;

            case 3:
                rate = (baby * 5.0f) - omutu;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;

            case 7:
                rate = (baby * 10.0f) - omutu;
                if (rate <= 0.0f) {
                    rate = 0.0f;
                }
                Math.ceil(rate);
                break;
        }
        return rate;
    }


    /**
     * ************************************************************************************************
     * //  処理内容：非常食の合計値を求めている処理
     * //  概要  　：returnは警告ダイアログの条件式の一つとして使用される。
     * //  作成日　：2015年6月3日
     * //  担当者　：岡田 洋介
     * //  備考　　：特になし。
     * *************************************************************************************************
     */
    public int VolumeFoods() {
        int total;

        //  プレファレンスの呼び出し。
        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);

        int reto_g = pref.getInt("retorutogohan_number", 0);
        int kan = pref.getInt("kandume_number", 0);
        int kanmen = pref.getInt("kanmen_number", 0);
        int kanpan = pref.getInt("kanpan_number", 0);
        int kan2 = pref.getInt("kandume2_number", 0);
        int reto = pref.getInt("retoruto_number", 0);
        int furizu = pref.getInt("furizu_dorai_number", 0);
        int mizu = pref.getInt("mizu_number", 0);
        int rinyu = pref.getInt("rinyu_number", 0);
        int konamilk = pref.getInt("konamilk_number", 0);
        int karori = pref.getInt("karori_meito_number", 0);
        int okasi = pref.getInt("okasi_number", 0);

        total = reto_g + kan + kanmen + kanpan + kan2 + reto + furizu + mizu + rinyu + konamilk
                + karori + okasi;

        return total;
    }

    /**
     * ************************************************************************************************
     * //  処理内容：備蓄品の合計値を求めている処理
     * //  概要  　：returnは警告ダイアログの条件式の一つとして使用される。
     * //  作成日　：2015年6月3日
     * //  担当者　：岡田 洋介
     * //  備考　　：特になし。
     * *************************************************************************************************
     */
    public int VolumeStock() {
        int total;

        //  プレファレンスの呼び出し
        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);

        //  各備蓄品のデータ（個数）の呼び出し。
        int gas = pref.getInt("gas_number", 0);
        int matti = pref.getInt("match_number", 0);
        int bombe = pref.getInt("bombe_number", 0);
        int fue = pref.getInt("whistle_number", 0);
        int aSitagi = pref.getInt("shitagi_number", 0);
        int kSitagi = pref.getInt("kodomo_number", 0);
        int tissue = pref.getInt("tissue_number", 0);
        int almi = pref.getInt("almi_number", 0);
        int rap = pref.getInt("rap_number", 0);
        int gunnte = pref.getInt("gunnte_number", 0);
        int mask = pref.getInt("mask_number", 0);
        int bag = pref.getInt("bag_number", 0);
        int kaichu = pref.getInt("kaityu_number", 0);
        int kankiri = pref.getInt("kankiri_number", 0);
        int radio = pref.getInt("radio_number", 0);
        int judenki = pref.getInt("judenki_number", 0);
        int spoon = pref.getInt("supun_number", 0);
        int hasi = pref.getInt("hasi_number", 0);
        int koppu = pref.getInt("koppu_number", 0);
        int bin = pref.getInt("nyuji_number", 0);
        int omutu = pref.getInt("omutu_number", 0);
        int denti = pref.getInt("denti_number", 0);
        int nebukuro = pref.getInt("nebukuro_number", 0);
        int utuwa = pref.getInt("utuwa_number", 0);
        int towel = pref.getInt("taoru_number", 0);

        //  各備蓄品を足し合わせた合計値
        total = gas + matti + bombe + fue + aSitagi + kSitagi + tissue + almi + rap + gunnte + mask
                + bag + kaichu + kankiri + radio + judenki + spoon + hasi + koppu + bin + omutu
                + denti + nebukuro + utuwa + towel;

        return total;
    }

}
