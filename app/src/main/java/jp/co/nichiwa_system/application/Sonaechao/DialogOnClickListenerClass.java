package jp.co.nichiwa_system.application.Sonaechao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * *****************************************************************************
 * アイテムの個数や賞味期限を制御するクラス
 * ******************************************************************************
 */
public class DialogOnClickListenerClass implements View.OnClickListener {

    private String TitleName;       //ダイアログのタイトル
    private String DateName;        //プレファレンスに保管されている値の名前
    private int img_id;             //イメージ画像のID番号
    private Activity act;            //アクティビティ
    private boolean calendarshow; //カレンダー機能の有無
    private String tani;            //アイテムの単
    private int Number;

    //1日生きるために最低限必要な量
    //左から大人、小人、幼児。1以上だと表示。
    int[][] Hijou_num = {
            {1, 1, 0}, //レトルトご飯
            {1, 1, 0}, //缶詰ゴハン
            {1, 1, 0}, //乾麺
            {1, 1, 0}, //カンパン
            {1, 1, 0}, //缶詰め（肉・魚）
            {1, 1, 0}, //レトルト食品
            {1, 1, 0}, //フリーズドライ
            {3, 2, 2},  //水（1ℓで1栄養　ふつうのペットボトル相当）
            {1, 1, 0}, //カロリーメイト（一箱で3栄養）
            {1, 1, 0}, //お菓子類（1で1箱・1袋）
            {0, 0, 1}, //離乳食（1で80g）
            {0, 0, 1}, //粉ミルク（ 1000mlで3栄養 Lサイズのペットボトルに相当 ）
            {1, 1, 1}, //ガスコンロ
            {1, 1, 0}, //マッチ;ライター
            {1, 1, 1}, //ガスボンベ
            {1, 1, 1}, //笛
            {1, 0, 0}, //大人下着
            {0, 1, 0}, //小人下着
            {1, 1, 1}, //ティッシュ
            {1, 1, 1}, //アルミホイル
            {1, 1, 1}, //ラップ
            {1, 1, 1}, //軍手
            {1, 1, 1}, //マスク
            {1, 1, 1}, //ビニール袋
            {1, 1, 1}, //懐中電灯
            {1, 1, 1}, //缶切り
            {1, 1, 1}, //ラジオ
            {1, 1, 1}, //充電器
            {1, 1, 1}, //スプーン
            {1, 1, 1}, //割り箸
            {1, 1, 1}, //コップ
            {0, 0, 1}, //哺乳びん
            {0, 0, 1}, //おむつ
            {2, 2, 2}, //乾電池
            {1, 1, 0}, //寝袋
            {1, 1, 1}, //器
            {1, 1, 1}, //タオル
    };

    // 非常食の種類
    final int MAX_HIJOUSYOKU = 12;
    // 備蓄品の種類
    final int MAX_BICHIKUHIN = 25;

    //ItemClassのコンストラクタ
    public DialogOnClickListenerClass(ItemClass item) {
        //ずばっと
        this(item.getName(), item.getPrefName(), item.getDrawable_Location(), item.getCalender_flag(), item.getUnit(), item.getActivity());
        this.Number = item.getNumber();
    }

    public DialogOnClickListenerClass(String TitleName, String DateName, int img_id, boolean calendarshow, String tani, Activity act) {
        //他のものは下記のコンストラクタへ運ぶ
        this(TitleName, DateName, img_id, calendarshow, act);
        //単位を挿入
        this.tani = tani;
    }

    //コンストラクタ
    public DialogOnClickListenerClass(String TitleName, String DateName, int img_id, boolean calendarshow, Activity act) {
        this.TitleName = TitleName;
        this.DateName = DateName;
        this.img_id = img_id;
        this.calendarshow = calendarshow;
        this.act = act;
    }

    //クリックを押したときの処理
    @Override
    public void onClick(View v) {

        /*************************************************************************
         * 宣言部
         *************************************************************************/
        //ダイアログの生成
        AlertDialog.Builder alert = new AlertDialog.Builder(act);

        //元のアクティビティの場所を指定
        LayoutInflater inflater = LayoutInflater.from(act);
        //final その変数は変更不可能になる
        final View viw = inflater.inflate(R.layout.activity_popup, null);

        //イメージ画像を取得
        ImageView img = (ImageView) viw.findViewById(R.id.imageView);
        //画像を変更
        img.setImageResource(img_id);

        //文字を変える
        TextView tv = (TextView) viw.findViewById(R.id.textView26);
        tv.setText(tani);
        //
        //EditTextを取得する
        final EditText et = (EditText) viw.findViewById(R.id.Number);

        //クリアボタンの処理
        ImageView textclear = (ImageView) viw.findViewById(R.id.textclear);
        textclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });

        if (tani == "ℓ") {

            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        } else {
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        }


        //日付処理
        final TextView day = (TextView) viw.findViewById(R.id.popup_day);
        final Calendar cl = loadCalendar(DateName);       //日付の取得。インスタンス（実体）を取得

        //アイテムの賞味期限を取得
        if (loadInt2(DateName) == 0) {
            day.setText("備蓄数入力後消費期限を入力できます→");
        } else {
            day.setText("消費期限" + String.valueOf(cl.get(Calendar.YEAR)) + "年" + String.valueOf(cl.get(Calendar.MONTH) + 1) + "月" + String.valueOf(cl.get(Calendar.DAY_OF_MONTH)) + "日");
        }
        //テキストビュー
        TextView[] People_tv = new TextView[3];
        People_tv[0] = (TextView) viw.findViewById(R.id.otona_text);
        People_tv[1] = (TextView) viw.findViewById(R.id.kobito_text);
        People_tv[2] = (TextView) viw.findViewById(R.id.youji_text);

        //大人小人幼児の人数を明記
        People_tv[0].setText("大人" + loadInt("otona_people") + "人分");
        People_tv[1].setText("小人" + loadInt("kobito_people") + "人分");
        People_tv[2].setText("幼児" + loadInt("youji_people") + "人分");

        //非常食の推奨地値
        if (act.getClass() == Hijousyoku.class || act.getClass() == MainActivity.class) {
            TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
            if (TitleName == "水") {
                SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

                int mizu = pref.getInt("mizu_number", 0);

                //人数
                int adult_n = pref.getInt("otona_people", 0);
                int child_n = pref.getInt("kobito_people", 0);
                int baby_n = pref.getInt("youji_people", 0);

                int setDays = pref.getInt("sitei_day", 3);

                int adult_w = adult_n * 3;
                int child_w = child_n * 2;
                int baby_w = baby_n * 2;

                //  水の必要値の算出。備えちゃお日数も追加。
                int total_w = (adult_w + child_w + baby_w) * setDays;
                if (total_w - mizu <= 0) {
                    s_tv.setText("水は十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (toString().valueOf(total_w - mizu)) + tani + "備蓄してください");
                }
            } else if (TitleName == "離乳食" || TitleName == "粉ミルク") {
                SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

                int rinyu = pref.getInt("rinyu_number", 0);
                int konamilk = pref.getInt("konamilk_number", 0);

                //人数
                int baby_n = pref.getInt("youji_people", 0);
                int setDays = pref.getInt("sitei_day", 3);
                int bAll;

                //  幼児限定の栄養価総計。baby All
                bAll = (konamilk * 3) + rinyu;
                int total_b = (3 * baby_n) * setDays;
                double need_sum = total_b - bAll;
                if (need_sum <= 0) {
                    s_tv.setText("非常食は十分備蓄されています");
                } else if (TitleName == "粉ミルク") {
                    s_tv.setText("あと" + toString().valueOf(((int) Math.ceil(need_sum / 3)) + tani + "備蓄してください"));
                } else {
                    s_tv.setText("あと" + toString().valueOf(((int) need_sum) + tani + "備蓄してください"));
                }
            } else /*if(TitleName == "レトルトご飯" || TitleName == "缶詰（ご飯）" || TitleName == "乾麺"
                    || TitleName == "乾パン" || TitleName == "缶詰（肉・魚）" || TitleName == "レトルト食品"
                    || TitleName == "フリーズドライ" || TitleName == "カロリーメイト" || TitleName == "菓子類")*/ {
                SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

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
                //  設定人数。訂正者：岡田
                int setDays = pref.getInt("sitei_day", 3);
                //各合計（大小のみ）
                int Hijousyoku_sum = reto_g + kan + kanmen + kanpan + kan2 + reto + furizu + karori + okasi;
                int okAll;
                int div = 2;
                //栄養価の計算。乾パンとカロリーメイトを抜いたものは栄養価１．乾パン、カロリーメイトは３。
                //  大小限定の栄養価総計。over kids All
                okAll = ((Hijousyoku_sum - kanpan - karori) * 1) + (kanpan + karori) * 3;
                //  大小の割合。
                int rateOK = (((adult_n * 3) + (child_n * 2)) * setDays);
                double need_sum = rateOK - okAll;
                if (need_sum <= 0) {
                    s_tv.setText("非常食は十分備蓄されています");
                } else if (TitleName == "カロリーメイト" || TitleName == "乾パン") {
                    s_tv.setText("あと" + toString().valueOf(((int) Math.ceil(need_sum / 3)) + tani + "備蓄してください"));
                } else {
                    s_tv.setText("あと" + toString().valueOf(((int) need_sum) + tani + "備蓄してください"));
                }
                rateOK = 0;

            }
        }

        //備蓄品の推奨地
        if (act.getClass() == Stock.class || act.getClass() == MainActivity.class) {

            SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

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
            if (TitleName == "懐中電灯　※単三電池推奨") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                if (UsedFamilyStockneed(kaichu, setDays, 1.0f, 1.0f, 1.0f) <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) UsedFamilyStockneed(kaichu, setDays, 1.0f, 1.0f, 1.0f) + tani + "備蓄してください");
                }
            }
            if (TitleName == "アルミホイル") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                if (UsedFamilyStockneed(almi, setDays, 1.0f, 1.0f, 2.0f) <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) UsedFamilyStockneed(almi, setDays, 1.0f, 1.0f, 2.0f) + tani + "備蓄してください");
                }
            }
            if (TitleName == "ラップ") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                if (UsedFamilyStockneed(rap, setDays, 1.0f, 1.0f, 3.0f) <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) UsedFamilyStockneed(rap, setDays, 1.0f, 1.0f, 3.0f) + tani + "備蓄してください");
                }
            }
            if (TitleName == "ガスボンベ") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                if (UsedFamilyStockneed(bombe, setDays, 1.0f, 2.0f, 5.0f) <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) UsedFamilyStockneed(bombe, setDays, 1.0f, 2.0f, 5.0f) + tani + "備蓄してください");
                }
            }
            if (TitleName == "ガスコンロ・鍋") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(gas, setDays, 1.0f, 1.0f, 1.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "ティッシュ・ウェットティッシュ") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(tissue, setDays, 1.0f, 1.0f, 3.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "ビニール袋（ゴミ袋）") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(bag, setDays, 1.0f, 1.0f, 3.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "スプーン") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(spoon, setDays, 1.0f, 1.0f, 1.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "割り箸") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(hasi, setDays, 1.0f, 1.0f, 1.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "乾電池　※単三") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(denti, setDays, 2.0f, 2.0f, 4.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "コップ（プラスチック）") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedOneStockneed(koppu, adult, kids, baby);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "器（プラスチック）") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedOneStockneed(utuwa, adult, kids, baby);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "タオル") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedOneStockOnlyTaoruneed(towel, adult, kids, baby, setDays);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "軍手") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                People_tv[2].setVisibility(View.GONE);
                float x = UsedOneStockneed(gunnte, adult, kids, 0);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "寝袋") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                People_tv[2].setVisibility(View.GONE);
                float x = UsedOneStockneed(nebukuro, adult, kids, 0);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "笛（防犯ブザー）") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(fue, setDays, 1.0f, 1.0f, 1.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "マッチ・ライター") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(matti, setDays, 1.0f, 1.0f, 1.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "ラジオ　※単三電池推奨") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(radio, setDays, 1.0f, 1.0f, 1.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "缶切り") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(kankiri, setDays, 0.0f, 1.0f, 1.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "マスク") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(mask, setDays, 1.0f, 1.0f, 1.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "携帯電話充電器　※単三電池推奨") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                float x = UsedFamilyStockneed(judenki, setDays, 1.0f, 1.0f, 1.0f);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "おむつ") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                People_tv[0].setVisibility(View.GONE);
                People_tv[1].setVisibility(View.GONE);
                float x = UsedBabyomutuneed(omutu, setDays, baby);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "哺乳びん") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                People_tv[0].setVisibility(View.GONE);
                People_tv[1].setVisibility(View.GONE);
                float x = UsedOneStockneed(bin, 0, 0, baby);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }
            if (TitleName == "大人下着") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                People_tv[1].setVisibility(View.GONE);
                People_tv[2].setVisibility(View.GONE);
                float x = UsedWearStock_adultneed(aSitagi, setDays, adult);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }

            if (TitleName == "小人下着") {
                TextView s_tv = (TextView) viw.findViewById(R.id.suisyouti);
                People_tv[0].setVisibility(View.GONE);
                People_tv[2].setVisibility(View.GONE);
                float x = UsedWearStock_childneed(kSitagi, setDays, kids);
                if (x <= 0) {
                    s_tv.setText("十分備蓄されています");
                } else {
                    s_tv.setText("あと" + (int) x + tani + "備蓄してください");
                }
            }


        }
        //アイテムが不要な人は、ダイアログに表示させない
             /*   for (int i = 0; i < 3; i++) {
                    if (Hijou_num[Number][i] <= 0) {
                        People_tv[i].setVisibility(View.GONE);
                    }
                }*/

        //指定したEditTextの中に値を挿入する
        et.setText(loadInt(DateName));

        //カレンダーが必要ある時に行う処理
        if (calendarshow) {
            //単位を変える必要がある場合
         /*   if( tani != null ) {
                //文字を変える
            TextView tv = (TextView) viw.findViewById(R.id.textView26);
            tv.setText(tani);
        }*/

            //カレンダー画像の取得
            ImageView Clock_iv = (ImageView) viw.findViewById(R.id.imageCalender);

            //日付ダイアログの初期化処理
            final DatePickerDialog dpd = new DatePickerDialog(act,
                    new DatePickerDialog.OnDateSetListener() {
                        //「設定」ボタンを押すと発生するイベント
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            //設定した日付を格納する

                            day.setText("消費期限:" + String.valueOf(year) + "年" + (String.valueOf(monthOfYear + 1)) + "月" + String.valueOf(dayOfMonth) + "日");
                            cl.set(year, monthOfYear, dayOfMonth);

                        }
                    },
                    cl.get(Calendar.YEAR),
                    cl.get(Calendar.MONTH),
                    cl.get(Calendar.DAY_OF_MONTH)
            );

            /**********************************************************************************************
             * 処理内容：カレンダーアイコン押下時、入力された値が０ならカレンダーダイアログを表示させない
             * 制作日時：2015/06/09
             * 制作者：中山延雄
             * コメントアウト【//カレンダーアイコン入力時の処理はここまで】まで編集
             **********************************************************************************************/
            //カレンダーボタンが押された時の処理
            Clock_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //IF文テキストビュー内の文字数が０以下なら
                    if (et.getText().toString().length() <= 0) {
                        //カラッポである
                        et.setText("0");//テキストビューに０を挿入
                        saveInt(et, DateName);//プリファレンスに保存
                    }
                    //違うなら、入力されている値を保存
                    else {
                        saveInt(et, DateName);
                    }

                    //備蓄数が０なら警告表示
                    if (loadInt2(DateName) == 0) {
                        Toast.makeText(act, "備蓄数を入力してください", Toast.LENGTH_SHORT).show();
                        // 初期値を１年後に設定する
                        int firstyear = cl.get(Calendar.YEAR) + 1;
                        int firstmonth = cl.get(Calendar.MONTH);
                        int firstday = cl.get(Calendar.DAY_OF_MONTH);
                        dpd.updateDate(firstyear, firstmonth, firstday);
                    }
                    //０じゃなければカレンダーダイアログ表示
                    else {
                        dpd.show();
                    }
                }
            });//カレンダーアイコン入力時の処理はここまで

            // 消費期限の説明
            TextView limitmessage2 = (TextView) viw.findViewById(R.id.limitmessage2);
            limitmessage2.setText("複数個ある場合は消費期限が一番早いものの期限を設定して下さい");

            // カレンダーボタンの説明
            TextView calmessage = (TextView) viw.findViewById(R.id.calmessage);
            calmessage.setText("※カレンダーを押すと日付が設定できます");

        } else {
            //必要なければ非表示にする
            ImageView Clock_iv = (ImageView) viw.findViewById(R.id.imageCalender);
            Clock_iv.setVisibility(View.GONE);
            day.setVisibility(View.GONE);
        }

        // ダイアログ内に備えちゃお日数の表示
        TextView sonaechao_tv = (TextView) viw.findViewById(R.id.sonaechao_text);
        sonaechao_tv.setText(" 備えちゃお日数" + loadInt("sitei_day") + "日分");

        //タイトルの設定
        alert.setTitle(TitleName);

        //決定ボタンを押すと行われる処理
        alert.setPositiveButton("OK", null);
        //アラートダイアログ
        final AlertDialog m_dlg;
        m_dlg = alert.setView(viw).show();
        m_dlg.setCanceledOnTouchOutside(false);
        Button buttonOk = m_dlg.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //アクティビティがメイン画面の場合、以下の処理を行う
                if (act.getClass() == MainActivity.class) {
                    //非常食の項目を取得する
                    ItemClass[] item = {
                            // 食べ物
                            new ItemClass("レトルトご飯", "retorutogohan_number", R.drawable.retoruto_gohan, true, "食", act),
                            new ItemClass("缶詰（ご飯）", "kandume_number", R.drawable.kandume_gohan, true, "缶", act),
                            new ItemClass("乾麺", "kanmen_number", R.drawable.kanmen, true, "袋", act),
                            new ItemClass("乾パン", "kanpan_number", R.drawable.kanpan, true, "缶", act),
                            new ItemClass("缶詰（肉・魚）", "kandume2_number", R.drawable.kandume, true, "缶", act),
                            new ItemClass("レトルト食品", "retoruto_number", R.drawable.retoruto, true, "袋", act),
                            new ItemClass("フリーズドライ", "furizu_dorai_number", R.drawable.furizu_dorai, true, "食", act),
                            new ItemClass("カロリーメイト", "karori_meito_number", R.drawable.karori_meito, true, "箱", act),
                            new ItemClass("お菓子", "okasi_number", R.drawable.okasi, true, "箱・袋", act),

                            // 幼児用
                            new ItemClass("離乳食", "rinyu_number", R.drawable.rinyu, true, "食", act),
                            new ItemClass("粉ミルク", "konamilk_number", R.drawable.konamilk, true, "缶", act),

                            // 水
                            new ItemClass("水", "mizu_number", R.drawable.mizu, true, "ℓ", act),

                            // 必需品
                            new ItemClass("ガスコンロ・鍋", "gas_number", R.drawable.gas, false, "個", act),
                            new ItemClass("ガスボンベ", "bombe_number", R.drawable.bombe, true, "本", act),
                            new ItemClass("乾電池　※単三", "denti_number", R.drawable.denti, true, "×１０本", act),
                            new ItemClass("ティッシュ・ウェットティッシュ", "tissue_number", R.drawable.thissyu, false, "箱", act),
                            new ItemClass("大人下着", "shitagi_number", R.drawable.otona, false, "枚", act),
                            new ItemClass("小人下着", "kodomo_number", R.drawable.kodomo, false, "枚", act),
                            new ItemClass("アルミホイル", "almi_number", R.drawable.almi, false, "本", act),
                            new ItemClass("ラップ", "rap_number", R.drawable.rappu, false, "本", act),
                            new ItemClass("ビニール袋（ゴミ袋）", "bag_number", R.drawable.hukuro, false, "×１０枚", act),
                            new ItemClass("割り箸", "hasi_number", R.drawable.hasi, false, "×１００膳", act),
                            new ItemClass("スプーン", "supun_number", R.drawable.spoon, false, "×１００本", act),
                            new ItemClass("タオル", "taoru_number", R.drawable.taoru, false, "枚", act),
                            new ItemClass("コップ（プラスチック）", "koppu_number", R.drawable.koppu, false, "個", act),
                            new ItemClass("器（プラスチック）", "utuwa_number", R.drawable.utuwa, false, "枚", act),
                            new ItemClass("懐中電灯　※単三電池推奨", "kaityu_number", R.drawable.kaityu, false, "本", act),

                            // 便利品
                            new ItemClass("ラジオ　※単三電池推奨", "radio_number", R.drawable.radio, false, "個", act),
                            new ItemClass("携帯電話充電器　※単三電池推奨", "judenki_number", R.drawable.judenti, false, "個", act),
                            new ItemClass("笛（防犯ブザー）", "whistle_number", R.drawable.whistle, false, "個", act),
                            new ItemClass("缶切り", "kankiri_number", R.drawable.kankiri, false, "個", act),
                            new ItemClass("マッチ・ライター", "match_number", R.drawable.match, false, "箱", act),
                            new ItemClass("寝袋", "nebukuro_number", R.drawable.nebukuro, false, "枚", act),
                            new ItemClass("マスク", "mask_number", R.drawable.mask, false, "×１００枚", act),
                            new ItemClass("軍手", "gunnte_number", R.drawable.gunnte, false, "対", act),

                            // 幼児用
                            new ItemClass("哺乳びん", "nyuji_number", R.drawable.bin, false, "本", act),
                            new ItemClass("おむつ", "omutu_number", R.drawable.omutu, false, "枚", act)
                    };
                    for (int i = 0; i < item.length; i++) {
                        item[i].setNumber(i);
                    }
                    if (et.getText().toString().length() <= 0) {
                        //カラッポである
                        et.setText("0");
                        saveInt(et, DateName);
                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                        m_dlg.dismiss();
                    } else {
                        //正常なのでデータを保存する
                        saveInt(et, DateName);

                        //現在の日付を保存
                        saveCalendar(Calendar.getInstance(), act.getLocalClassName());


                        for (int i = 0; i < item.length; i++) {
                            if (TitleName == item[i].getName()) {
                                //賞味期限の保存
                                saveCalendar(cl, DateName);
                                if (calendarshow &&!Check_Day(item[i].getPrefName()) && loadInt2(DateName) != 0) {
                                    Toast.makeText(act, "消費期限が本日か過去の日付になっています", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                } else if (day.getText() == "備蓄数入力後消費期限を入力できます→"){
                                    Toast.makeText(act, "消費期限の入力をして下さい", Toast.LENGTH_SHORT).show(); // 【消費期限の入力をして下さい】とトースト表示
                                } else {
                                    if (calendarshow && loadInt2(DateName) != 0) {
                                        //日付を保存する
                                        saveCalendar(cl, DateName);
                                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                        m_dlg.dismiss();
                                    } else if (calendarshow && loadInt2(DateName) == 0) {
                                        Calendar cl = Calendar.getInstance();       //今日の日付の取得
                                        saveCalendar(cl, DateName);
                                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                        m_dlg.dismiss();
                                    }
                                    else{
                                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                        m_dlg.dismiss();
                                    }
                                }
                            }
                        }
                    }
                    Calendar cl = Calendar.getInstance();       //今日の日付の取得
                    SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

                    //各グラフの取得
                    ImageButton R_button = (ImageButton) act.findViewById(R.id.R_graph);
                    ImageButton L_button = (ImageButton) act.findViewById(R.id.L_graph);

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
                    ((TextView) act.findViewById(R.id.hijousyoku_percent)).setText("非常食：" + String.valueOf(goukei[0]) + "%");

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
                    ((TextView) act.findViewById(R.id.bichiku_percent)).setText("備蓄品：" + String.valueOf(goukei[1]) + "%");

                    //要チェックに使用するTextViewを使用する
                    TextView[] YouCheck_tv = new TextView[MAX_HIJOUSYOKU + MAX_BICHIKUHIN];
                    //フラグメントのリニアレイアウトを取得
                    TableLayout tl = (TableLayout) act.findViewById(R.id.CheckLayout);
                    tl.removeAllViews();//中身を全部消去

                    for (int i = 0; i < MAX_HIJOUSYOKU + MAX_BICHIKUHIN; i++) {
                        YouCheck_tv[i] = new TextView(act);
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
                    TextView check_tv = (TextView) act.findViewById(R.id.textView);
                    check_tv.setText("要チェック欄:" + check + "件");

                    /*******************************************************************************************
                     // 要チェック欄に「～が足りていません」というメッセージを出す
                     //
                     // メッセージ出力の条件
                     //   大人または小人が1以上、幼児が0　かつ　非常食の食べ物が50％未満
                     //   大人または小人が1以上、幼児が1以上　かつ　食べ物25％未満、乳児用25％未満
                     //   大人または小人が0、幼児が1以上　かつ　乳児用50％未満
                     //   水は大人または小人または幼児が1以上の場合に50％未満だとメッセージが出ます
                     ******************************************************************************************/
                    for (int i = 0; i < MAX_HIJOUSYOKU; i++) {
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
                            YouCheck_tv[i].setText(get_Number_of_days_Warning_bichiku(item[i].getPrefName(), item[i].getName(), item[i].getCalender_flag())); // 非常食品名 + が足りていません
                            YouCheck_tv[i].setCompoundDrawablesWithIntrinsicBounds(item[i].getIcon(), 0, 0, 0);
                            YouCheck_tv[i].setOnClickListener(new DialogOnClickListenerClass(item[i])); //警告文を押すとダイアログが表示されるようにする
                        }
                    }

                    /***************************************************************************
                     * 　ソートプログラム
                     **************************************************************************/
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

                    //最終入力日
                    TextView b_tv = (TextView) act.findViewById(R.id.bichiku_nyuuryoku);
                    TextView h_tv = (TextView) act.findViewById(R.id.hijousyoku_nyuuryoku);

                    //備蓄品の最終入力日
                    pref = act.getSharedPreferences("Stock_pref", act.MODE_PRIVATE);
                    cl.set(pref.getInt("year", 2000), pref.getInt("month", 1), pref.getInt("day", 1));
                    if (pref.getInt("year", 2000) == 2000 && pref.getInt("month", 1) == 1 && pref.getInt("day", 1) == 1) {
                        b_tv.setText("備蓄品はまだ入力されていません");
                    } else {
                        b_tv.setText("最終入力日:" + cl.get(Calendar.YEAR) + "年" + (cl.get(Calendar.MONTH) + 1) + "月" + cl.get(Calendar.DAY_OF_MONTH) + "日");
                    }
                    //非常食の最終入力日
                    pref = act.getSharedPreferences("Hijousyoku_pref", act.MODE_PRIVATE);
                    cl.set(pref.getInt("year", 2000), pref.getInt("month", 1), pref.getInt("day", 1));
                    if (pref.getInt("year", 2000) == 2000 && pref.getInt("month", 1) == 1 && pref.getInt("day", 1) == 1) {
                        h_tv.setText("非常食はまだ入力されていません");
                    } else {
                        h_tv.setText("最終入力日:" + cl.get(Calendar.YEAR) + "年" + (cl.get(Calendar.MONTH) + 1) + "月" + cl.get(Calendar.DAY_OF_MONTH) + "日");
                    }
                }

        // メイン画面処理　ここまで

                // アクティビティが非常食画面の場合、以下の処理を行う
                if (act.getClass() == Hijousyoku.class) {
                    ItemClass[] item2 = new ItemClass[12];
                    item2[0] = new ItemClass("レトルトご飯", "retorutogohan_number", R.drawable.retoruto_gohan, true, "食", act);
                    item2[1] = new ItemClass("缶詰（ご飯）", "kandume_number", R.drawable.kandume_gohan, true, "缶", act);
                    item2[2] = new ItemClass("乾麺", "kanmen_number", R.drawable.kanmen, true, "袋", act);
                    item2[3] = new ItemClass("乾パン", "kanpan_number", R.drawable.kanpan, true, "缶", act);
                    item2[4] = new ItemClass("缶詰（肉・魚）", "kandume2_number", R.drawable.kandume, true, "缶", act);
                    item2[5] = new ItemClass("レトルト食品", "retoruto_number", R.drawable.retoruto, true, "袋", act);
                    item2[6] = new ItemClass("フリーズドライ", "furizu_dorai_number", R.drawable.furizu_dorai, true, "食", act);
                    item2[7] = new ItemClass("水", "mizu_number", R.drawable.mizu, true, "ℓ", act);
                    item2[8] = new ItemClass("カロリーメイト", "karori_meito_number", R.drawable.karori_meito, true, "箱", act);
                    item2[9] = new ItemClass("菓子類", "okasi_number", R.drawable.okasi, true, "箱・袋", act);
                    item2[10] = new ItemClass("離乳食", "rinyu_number", R.drawable.rinyu, true, "食", act);
                    item2[11] = new ItemClass("粉ミルク", "konamilk_number", R.drawable.konamilk, true, "缶", act);

                    //番号の振り分け
                    for (int i = 0; i < 12; i++) {
                        item2[i].setNumber(i);
                    }
                    ImageView[] Hijousyoku_iv = new ImageView[12];

                    if (et.getText().toString().length() <= 0) {
                        //カラッポである
                        et.setText("0");
                        saveInt(et, DateName);
                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                        m_dlg.dismiss();
                    } else {
                        //正常なのでデータを保存する
                        saveInt(et, DateName);

                        //現在の日付を保存
                        saveCalendar(Calendar.getInstance(), act.getLocalClassName());



                        for (int i = 0; i < item2.length; i++) {
                            if (TitleName == item2[i].getName()) {
                                //賞味期限の保存
                                saveCalendar(cl, DateName);
                                if (calendarshow &&!Check_Day(item2[i].getPrefName()) && loadInt2(DateName) != 0) {
                                    Toast.makeText(act, "消費期限が本日か過去の日付になっています", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                } else if (day.getText() == "備蓄数入力後消費期限を入力できます→"){
                                    Toast.makeText(act, "消費期限の入力をして下さい", Toast.LENGTH_SHORT).show(); // 【消費期限の入力をして下さい】とトースト表示
                                } else {
                                    if (calendarshow && loadInt2(DateName) != 0) {
                                        //日付を保存する
                                        saveCalendar(cl, DateName);
                                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                        m_dlg.dismiss();
                                    } else if (calendarshow && loadInt2(DateName) == 0) {
                                        Calendar cl = Calendar.getInstance();       //今日の日付の取得
                                        saveCalendar(cl, DateName);
                                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                        m_dlg.dismiss();
                                    }
                                    else{
                                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                        m_dlg.dismiss();
                                    }
                                }
                            }
                        }
                    }

                    //各イメージビューの取得
                    Hijousyoku_iv[0] = (ImageView) act.findViewById(R.id.retoruto_gohan);
                    Hijousyoku_iv[1] = (ImageView) act.findViewById(R.id.kandume);
                    Hijousyoku_iv[2] = (ImageView) act.findViewById(R.id.kanmen);
                    Hijousyoku_iv[3] = (ImageView) act.findViewById(R.id.kanpan);
                    Hijousyoku_iv[4] = (ImageView) act.findViewById(R.id.kandume2);
                    Hijousyoku_iv[5] = (ImageView) act.findViewById(R.id.retoruto);
                    Hijousyoku_iv[6] = (ImageView) act.findViewById(R.id.furizu_dorai);
                    Hijousyoku_iv[7] = (ImageView) act.findViewById(R.id.mizu);
                    Hijousyoku_iv[8] = (ImageView) act.findViewById(R.id.karori_meito);
                    Hijousyoku_iv[9] = (ImageView) act.findViewById(R.id.okasi);
                    Hijousyoku_iv[10] = (ImageView) act.findViewById(R.id.rinyu);
                    Hijousyoku_iv[11] = (ImageView) act.findViewById(R.id.konamilk);

                    for (int i = 0; i < 12; i++) {
                        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);
                        //ボタンアクションの処理
                        Hijousyoku_iv[i].setOnClickListener(new DialogOnClickListenerClass(item2[i]));
                        int r = pref.getInt(item2[i].getPrefName(), 0);
                        //非常食、幼児用非常食の備蓄％が50％未満なら水以外のボタンに赤枠を敷く
                        if (FoodOverKids() + FoodBaby() < 50 && item2[i].getName() != "水") {
                            Hijousyoku_iv[i].setBackgroundResource(R.drawable.style2);
                        }
                        //水の備蓄％が50％未満なら、水ボタンに赤枠を敷く
                        if (RateWater() < 50 && item2[i].getName() == "水") {
                            Hijousyoku_iv[i].setBackgroundResource(R.drawable.style2);
                        }
                        //非常食、幼児用非常食の備蓄％が50％以上なら水以外のボタンから赤枠をぬく
                        if (FoodOverKids() + FoodBaby() >= 50 && item2[i].getName() != "水") {
                            Hijousyoku_iv[i].setBackgroundResource(R.drawable.style);
                        }
                        //水の備蓄％が５０％以上なら水の赤枠を抜く
                        if (RateWater() >= 50 && item2[i].getName() == "水") {
                            Hijousyoku_iv[i].setBackgroundResource(R.drawable.style);
                        }
                        //期限の切れているものは赤線を敷く
                        if (!Check_Day(item2[i].getPrefName()) && r != 0) {
                            Hijousyoku_iv[i].setBackgroundResource(R.drawable.style2);
                        }

                    }
                }
                // 非常食画面処理　ここまで

                // アクティビティが備蓄品画面の場合、以下の処理を行う
                if (act.getClass() == Stock.class) {
                    //各イメージビューの取得
                    ImageView[] Stock_iv = {
                            (ImageView) act.findViewById(R.id.gasView),
                            (ImageView) act.findViewById(R.id.matchView),
                            (ImageView) act.findViewById(R.id.bombeView),
                            (ImageView) act.findViewById(R.id.whistleView),
                            (ImageView) act.findViewById(R.id.shitagiView),
                            (ImageView) act.findViewById(R.id.kodomoView),
                            (ImageView) act.findViewById(R.id.tissueView),
                            (ImageView) act.findViewById(R.id.almiView),
                            (ImageView) act.findViewById(R.id.rapView),
                            (ImageView) act.findViewById(R.id.gunnteView),
                            (ImageView) act.findViewById(R.id.maskView),
                            (ImageView) act.findViewById(R.id.biniiruView),
                            (ImageView) act.findViewById(R.id.kaityuView),
                            (ImageView) act.findViewById(R.id.kankiriView),
                            (ImageView) act.findViewById(R.id.radioView),
                            (ImageView) act.findViewById(R.id.judenkiView),
                            (ImageView) act.findViewById(R.id.supunView),
                            (ImageView) act.findViewById(R.id.hasiView),
                            (ImageView) act.findViewById(R.id.koppuView),
                            (ImageView) act.findViewById(R.id.nyujiView),
                            (ImageView) act.findViewById(R.id.omutuView),
                            (ImageView) act.findViewById(R.id.dentiView),
                            (ImageView) act.findViewById(R.id.nebukuroView),
                            (ImageView) act.findViewById(R.id.utuwaView),
                            (ImageView) act.findViewById(R.id.taoruView),
                    };
                    //備蓄品の項目を取得する
                    ItemClass[] item = {
                            new ItemClass("ガスコンロ・鍋", "gas_number", R.drawable.gas, false, "個", act),
                            new ItemClass("マッチ・ライター", "match_number", R.drawable.match, false, "箱", act),
                            new ItemClass("ガスボンベ", "bombe_number", R.drawable.bombe, true, "本", act),
                            new ItemClass("笛（防犯ブザー）", "whistle_number", R.drawable.whistle, false, "個", act),
                            new ItemClass("大人下着", "shitagi_number", R.drawable.otona, false, "枚", act),
                            new ItemClass("小人下着", "kodomo_number", R.drawable.kodomo, false, "枚", act),
                            new ItemClass("ティッシュ・ウェットティッシュ", "tissue_number", R.drawable.thissyu, false, "箱", act),
                            new ItemClass("アルミホイル", "almi_number", R.drawable.almi, false, "本", act),
                            new ItemClass("ラップ", "rap_number", R.drawable.rappu, false, "本", act),
                            new ItemClass("軍手", "gunnte_number", R.drawable.gunnte, false, "対", act),
                            new ItemClass("マスク", "mask_number", R.drawable.mask, false, "×１００枚", act),
                            new ItemClass("ビニール袋（ゴミ袋）", "bag_number", R.drawable.hukuro, false, "×１０枚", act),
                            new ItemClass("懐中電灯　※単三電池推奨", "kaityu_number", R.drawable.kaityu, false, "本", act),
                            new ItemClass("缶切り", "kankiri_number", R.drawable.kankiri, false, "個", act),
                            new ItemClass("ラジオ　※単三電池推奨", "radio_number", R.drawable.radio, false, "個", act),
                            new ItemClass("携帯電話充電器　※単三電池推奨", "judenki_number", R.drawable.judenti, false, "個", act),
                            new ItemClass("スプーン", "supun_number", R.drawable.spoon, false, "×１００本", act),
                            new ItemClass("割り箸", "hasi_number", R.drawable.hasi, false, "×１００膳", act),
                            new ItemClass("コップ（プラスチック）", "koppu_number", R.drawable.koppu, false, "個", act),
                            new ItemClass("哺乳びん", "nyuji_number", R.drawable.bin, false, "本", act),
                            new ItemClass("おむつ", "omutu_number", R.drawable.omutu, false, "枚", act),
                            new ItemClass("乾電池　※単三", "denti_number", R.drawable.denti, true, "×１０本", act),
                            new ItemClass("寝袋", "nebukuro_number", R.drawable.nebukuro, false, "枚", act),
                            new ItemClass("器（プラスチック）", "utuwa_number", R.drawable.utuwa, false, "枚", act),
                            new ItemClass("タオル", "taoru_number", R.drawable.taoru, false, "枚", act),
                    };
                    //番号の振り分け + 非常食の分もプラスしています。
                    for (int i = 0; i < Stock_iv.length; i++) {
                        item[i].setNumber(i);
                    }

                    if (et.getText().toString().length() <= 0) {
                        //カラッポである
                        et.setText("0");
                        saveInt(et, DateName);
                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                        m_dlg.dismiss();
                    } else {
                        //正常なのでデータを保存する
                        saveInt(et, DateName);

                        //現在の日付を保存
                        saveCalendar(Calendar.getInstance(), act.getLocalClassName());



                        for (int i = 0; i < item.length; i++) {
                            if (TitleName == item[i].getName()) {
                                //賞味期限の保存
                                saveCalendar(cl, DateName);
                                if (calendarshow &&!Check_Day(item[i].getPrefName()) && loadInt2(DateName) != 0) {
                                    Toast.makeText(act, "消費期限が本日か過去の日付になっています", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                } else if (day.getText() == "備蓄数入力後消費期限を入力できます→"){
                                    Toast.makeText(act, "消費期限の入力をして下さい", Toast.LENGTH_SHORT).show(); // 【消費期限の入力をして下さい】とトースト表示
                                } else {
                                    if (calendarshow && loadInt2(DateName) != 0) {
                                        //日付を保存する
                                        saveCalendar(cl, DateName);
                                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                        m_dlg.dismiss();
                                    } else if (calendarshow && loadInt2(DateName) == 0) {
                                        Calendar cl = Calendar.getInstance();       //今日の日付の取得
                                        saveCalendar(cl, DateName);
                                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                        m_dlg.dismiss();
                                    }
                                    else{
                                        Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                                        m_dlg.dismiss();
                                    }
                                }
                            }
                        }
                    }


                    //枠線をつける
                    for (int i = 0; i < Stock_iv.length; i++) {
                        SharedPreferences pref =
                                act.getSharedPreferences("Preferences", act.MODE_PRIVATE);
                        int r = pref.getInt(item[i].getPrefName(), 0);
                        //ボタンアクションの処理
                        Stock_iv[i].setOnClickListener(new DialogOnClickListenerClass(item[i]));
                        //期限の切れているものは赤線を敷く
                        if (item[i].getCalender_flag() == true) {
                            if (!Check_Day(item[i].getPrefName()) && r != 0) {
                                Stock_iv[i].setBackgroundResource(R.drawable.style2);
                            } else {
                                Stock_iv[i].setBackgroundResource(R.drawable.style);
                            }
                        }
                    }
                }

            }

        });
        //alert.setView(viw);
        //alert.show();
    }

    /**
     * *************************************************質問の問題個所ここまで**********************************************************
     */

    //値データを取得する関数
    public String loadInt(String name) {
        //プリファレンスの生成
        SharedPreferences pref =
                act.getSharedPreferences("Preferences", act.MODE_PRIVATE);
        int i = 0;
        i = pref.getInt(name, 0);
        String str = String.valueOf(i);

        return str;
    }

    //値データを取得する関数
    public int loadInt2(String name) {
        //プリファレンスの生成
        SharedPreferences pref =
                act.getSharedPreferences("Preferences", act.MODE_PRIVATE);
        int i = pref.getInt(name, 0);
        return i;
    }

    //値データをプレファレンスで保存する関数 (エディットテキスト、名前)
    public void saveInt(EditText et, String name) {
        //プリファレンスの生成
        SharedPreferences pref =
                act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

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
    public Calendar loadCalendar(String prefName) {
        SharedPreferences pref =
                act.getSharedPreferences(prefName + "_pref", act.MODE_PRIVATE);
        Calendar cl = Calendar.getInstance();
        cl.set(pref.getInt("year", cl.get(Calendar.YEAR) + 1), pref.getInt("month", cl.get(Calendar.MONTH)), pref.getInt("day", cl.get(Calendar.DAY_OF_MONTH)));

        return cl;
    }

    //日付を格納する
    public void saveCalendar(Calendar cl, String prefName) {
        SharedPreferences pref =
                act.getSharedPreferences(prefName + "_pref", act.MODE_PRIVATE);
        Calendar cl_2 = Calendar.getInstance();

        SharedPreferences.Editor e = pref.edit();
        if (loadInt2(DateName) == 0) {
            e.putInt("year", cl_2.get(Calendar.YEAR) + 1);
            e.putInt("month", cl_2.get(Calendar.MONTH));
            e.putInt("day", cl_2.get(Calendar.DAY_OF_MONTH));

            e.commit();
        } else {
            e.putInt("year", cl.get(Calendar.YEAR));
            e.putInt("month", cl.get(Calendar.MONTH));
            e.putInt("day", cl.get(Calendar.DAY_OF_MONTH));

            e.commit();
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

        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);
        //残り日数を取得する
        int nokori = (int) getDate(prefName);
        //期日を取得する
        int nissu = (act.getSharedPreferences("Preferences", act.MODE_PRIVATE)).getInt("kiniti_day", 14);
        SharedPreferences Preferences = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);
        int otona = Preferences.getInt("otona_people", 0); // 大人の人数を取得
        int kobito = Preferences.getInt("kobito_people", 0); // 小人の人数を取得
        int youji = Preferences.getInt("youji_people", 0); // 幼児の人数を取得


        if (youji >= 1 && otona >= 1 || kobito >= 1) { // 幼児が1人以上
            if (FoodOverKids() < 25) { // もし非常食が25％未満なら
                str = ItemName + "が足りていません";
            } else if (FoodOverKids() >= 25 && pref.getInt(prefName, 0) <= 0) {
                str = "";
            } else if (isCal == true && otona >= 1 || kobito >= 1) { // カレンダーが存在するもの
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
        } else if (youji <= 0 && otona >= 1 || kobito >= 1) { // 幼児がいない
            if (FoodOverKids() < 50) { // もし足りていなかったら
                str = ItemName + "が足りていません";
            } else if (FoodOverKids() >= 50 && pref.getInt(prefName, 0) <= 0) {
                str = "";
            } else if (isCal == true && otona >= 1 || kobito >= 1) { // カレンダーが存在するもの
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

        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);
        //残り日数を取得する
        int nokori = (int) getDate(prefName);
        //期日を取得する
        int nissu = (act.getSharedPreferences("Preferences", act.MODE_PRIVATE)).getInt("kiniti_day", 14);
        SharedPreferences Preferences = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);
        int otona = Preferences.getInt("otona_people", 0); // 大人の人数を取得
        int kobito = Preferences.getInt("kobito_people", 0); // 小人の人数を取得
        int youji = Preferences.getInt("youji_people", 0); // 幼児の人数を取得


        if (youji >= 1 && otona >= 1 || kobito >= 1) { // 大人または小人が1人以上
            if (FoodBaby() < 25) { // もし足りていなかったら
                str = ItemName + "が足りていません";
            } else if (FoodBaby() >= 25 && pref.getInt(prefName, 0) <= 0) {
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
        } else if (youji >= 1 && otona <= 0 && kobito <= 0) { // 大人または小人がいない
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
        int nissu = (act.getSharedPreferences("Preferences", act.MODE_PRIVATE)).getInt("kiniti_day", 14);

        if (RateWater() < 50) { // もし足りていなかったら
            str = ItemName + "が足りていません";
        } else if (isCal == true) { // カレンダーが存在するもの
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
        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

        //残り日数を取得する
        int nokori = (int) getDate(prefName);
        //期日を取得する
        int nissu = (act.getSharedPreferences("Preferences", act.MODE_PRIVATE)).getInt("kiniti_day", 14);
        SharedPreferences Preferences = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);
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

        if (ItemName == "マッチ・ライター" || ItemName == "笛（防犯ブザー）" || ItemName == "軍手"
                || ItemName == "ラジオ　※単三電池推奨" || ItemName == "缶切り" || ItemName == "マスク"
                || ItemName == "携帯電話充電器　※単三電池推奨") {
            if (RateStock() >= 50) {
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
        int BichikuSu = (act.getSharedPreferences("Preferences", act.MODE_PRIVATE)).getInt(prefName, 0);
        //残り日数を取得する
        int nokori = (int) getDate(prefName);
        //期日を取得する
        int nissu = (act.getSharedPreferences("Preferences", act.MODE_PRIVATE)).getInt("kiniti_day", 0);

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
        SharedPreferences pref2 = act.getSharedPreferences(prefName + "_pref", act.MODE_PRIVATE);
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
        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

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
        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

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
        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

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
        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

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
        }else {
            return true;
        }
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
        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

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
        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

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
