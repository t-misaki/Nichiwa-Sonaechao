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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/********************************************************************************
 * アイテムの個数や賞味期限を制御するクラス
 ********************************************************************************/
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
            { 1, 1, 0 }, //レトルトご飯
            { 1, 1, 0 }, //缶詰ゴハン
            { 1, 1, 0 }, //乾麺
            { 1, 1, 0 }, //カンパン
            { 1, 1, 0 }, //缶詰め（肉・魚）
            { 1, 1, 0 }, //レトルト食品
            { 1, 1, 0 }, //フリーズドライ
            { 3, 2, 2 },  //水（1ℓで1栄養　ふつうのペットボトル相当）
            { 1, 1, 0 }, //カロリーメイト（一箱で3栄養）
            { 1, 1, 0 }, //お菓子類（1で1箱・1袋）
            { 0, 0, 1 }, //離乳食（1で80g）
            { 0, 0, 1 }, //粉ミルク（ 1000mlで3栄養 Lサイズのペットボトルに相当 ）
            { 1, 1, 1 }, //ガスコンロ
            { 1, 1, 0 }, //マッチ;ライター
            { 1, 1, 1 }, //ガスボンベ
            { 1, 1, 1 }, //笛
            { 1, 0, 0 }, //大人下着
            { 0, 1, 0 }, //小人下着
            { 1, 1, 1 }, //ティッシュ
            { 1, 1, 1 }, //アルミホイル
            { 1, 1, 1 }, //ラップ
            { 1, 1, 1 }, //軍手
            { 1, 1, 1 }, //マスク
            { 1, 1, 1 }, //ビニール袋
            { 1, 1, 1 }, //懐中電灯
            { 1, 1, 1 }, //缶切り
            { 1, 1, 1 }, //ラジオ
            { 1, 1, 1 }, //充電器
            { 1, 1, 1 }, //スプーン
            { 1, 1, 1 }, //割り箸
            { 1, 1, 1 }, //コップ
            { 0, 0, 1 }, //哺乳びん
            { 0, 0, 1 }, //おむつ
            { 2, 2, 2 }, //乾電池
            { 1, 1, 0 }, //寝袋
            { 1, 1, 1 }, //器
            { 1, 1, 1 }, //タオル
    };

    //非常食の種類
    final int MAX_HIJOUSYOKU = 12;

    //ItemClassのコンストラクタ
    public DialogOnClickListenerClass(ItemClass item){
        //ずばっと
        this(item.getName(),item.getPrefName(),item.getDrawable_Location(),item.getCalender_flag(),item.getUnit(),item.getActivity());
        this.Number = item.getNumber();
    }

    public DialogOnClickListenerClass(String TitleName, String DateName, int img_id, boolean calendarshow, String tani, Activity act){
        //他のものは下記のコンストラクタへ運ぶ
       this(TitleName,DateName,img_id,calendarshow,act);
        //単位を挿入
        this.tani = tani;
    }

    //コンストラクタ
   public DialogOnClickListenerClass(String TitleName, String DateName, int img_id, boolean calendarshow, Activity act) {
        this.TitleName     = TitleName;
        this.DateName      = DateName;
        this.img_id        = img_id;
        this.calendarshow = calendarshow;
        this.act            = act;
    }

    //クリックを押したときの処理
    @Override
   public void onClick(View v) {

        /*************************************************************************
         * 宣言部
         *************************************************************************/
        //ダイアログの生成
        AlertDialog.Builder alert = new AlertDialog.Builder(act);
        //タイトルの設定
        alert.setTitle(TitleName);

        //元のアクティビティの場所を指定
        LayoutInflater inflater = LayoutInflater.from(act);
        //final その変数は変更不可能になる
        final View viw = inflater.inflate(R.layout.activity_popup, null);

        //イメージ画像を取得
        ImageView img = (ImageView)viw.findViewById(R.id.imageView);
        //画像を変更
        img.setImageResource(img_id);

        //文字を変える
        TextView tv = (TextView) viw.findViewById(R.id.textView26);
        tv.setText(tani);

        //EditTextを取得する
        final EditText et = (EditText)viw.findViewById(R.id.Number);
        if(tani=="ℓ") {

            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        }
        else {
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        }
        //日付処理
        final TextView day = (TextView)viw.findViewById(R.id.popup_day);
        final Calendar cl = loadCalendar(DateName);       //日付の取得。インスタンス（実体）を取得

        //アイテムの賞味期限を取得
        day.setText( String.valueOf(cl.get(Calendar.YEAR)) + "年" + String.valueOf(cl.get(Calendar.MONTH)+1) + "月" + String.valueOf(cl.get(Calendar.DAY_OF_MONTH)) + "日" );

        //テキストビュー
        TextView[] People_tv = new TextView[3];
        People_tv[0] = (TextView)viw.findViewById(R.id.otona_text);
        People_tv[1] = (TextView)viw.findViewById(R.id.kobito_text);
        People_tv[2] = (TextView)viw.findViewById(R.id.youji_text);

        //大人小人幼児の人数を明記
        People_tv[0].setText("大人" + loadInt("otona_people") + "人分");
        People_tv[1].setText("小人" + loadInt("kobito_people") + "人分");
        People_tv[2].setText("幼児"  + loadInt("youji_people")  + "人分");

        //アイテムが不要な人は、ダイアログに表示させない
        for( int i = 0 ; i < 3 ; i++ ) {
            if( Hijou_num[Number][i] <= 0 ) {
                People_tv[i].setVisibility( View.GONE );
            }
        }

        //指定したEditTextの中に値を挿入する
            et.setText( loadInt(DateName) );

        //カレンダーが必要ある時に行う処理
         if( calendarshow )
        {
            //単位を変える必要がある場合
         /*   if( tani != null ) {
                //文字を変える
            TextView tv = (TextView) viw.findViewById(R.id.textView26);
            tv.setText(tani);
        }*/

            //カレンダー画像の取得
            ImageView Clock_iv = (ImageView)viw.findViewById(R.id.imageCalender);

            //日付ダイアログの初期化処理
            final DatePickerDialog dpd = new DatePickerDialog(act,
                    new DatePickerDialog.OnDateSetListener(){
                        //「設定」ボタンを押すと発生するイベント
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            //設定した日付を格納する
                            day.setText(String.valueOf(year) + "年" + ( String.valueOf(monthOfYear+1)  ) + "月" + String.valueOf(dayOfMonth) + "日");
                            cl.set(year,monthOfYear,dayOfMonth);
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
                    if(et.getText().toString().length() <= 0) {
                        //カラッポである
                        et.setText("0");//テキストビューに０を挿入
                        saveInt(et, DateName);//プリファレンスに保存
                    }
                    //違うなら、入力されている値を保存
                    else{
                        saveInt(et,DateName);
                    }

                    //備蓄数が０なら警告表示
                    if(loadInt2(DateName)==0){
                        Toast.makeText(act,"備蓄数を入力してください", Toast.LENGTH_SHORT ).show();

                    }
                    //０じゃなければカレンダーダイアログ表示
                    else {
                        dpd.show();
                    }
                }
            });//カレンダーアイコン入力時の処理はここまで

            // 消費期限の説明
            TextView limitmessage = (TextView)viw.findViewById(R.id.limitmessage);
            limitmessage.setText("消費期限：");
            TextView limitmessage2 = (TextView)viw.findViewById(R.id.limitmessage2);
            limitmessage2.setText("複数個ある場合は消費期限が一番早いものの期限を設定して下さい");

            // カレンダーボタンの説明
            TextView calmessage = (TextView)viw.findViewById(R.id.calmessage);
            calmessage.setText("※カレンダーを押すと日付が設定できます");

        }else{
            //必要なければ非表示にする
            ImageView Clock_iv = (ImageView)viw.findViewById(R.id.imageCalender);
            Clock_iv.setVisibility(View.GONE);
            day.setVisibility(View.GONE);
        }

        // ダイアログ内に備えちゃお日数の表示
        TextView sonaechao_tv = (TextView)viw.findViewById(R.id.sonaechao_text);
        sonaechao_tv.setText(" 備えちゃお日数" + loadInt("sitei_day") + "日分");

        //決定ボタンを押すと行われる処理
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //値が格納されてないか判定する
                if (et.getText().toString().length() <= 0) {
                    //カラッポである
                    et.setText("0");
                    saveInt(et, DateName);
                } else {
                    //正常なのでデータを保存する
                    saveInt(et, DateName);

                    //現在の日付を保存
                    saveCalendar(Calendar.getInstance(), act.getLocalClassName());

                    //賞味期限の保存
                    if (calendarshow) {
                        //日付を保存する
                        saveCalendar(cl, DateName);
                    }
                    Toast.makeText(act, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示

                    //アクティビティがメイン画面の場合、以下の処理を行う
                    if (act.getClass() == MainActivity.class) {
                        //非常食の項目を取得する
                        ItemClass[] item = {
                                new ItemClass("レトルトご飯", "retorutogohan_number", R.drawable.retoruto_gohan, true, "食", act),
                                new ItemClass("缶詰（ご飯）", "kandume_number", R.drawable.kandume_gohan, true, "缶", act),
                                new ItemClass("乾麺", "kanmen_number", R.drawable.kanmen, true, "袋", act),
                                new ItemClass("乾パン", "kanpan_number", R.drawable.kanpan, true, "缶", act),
                                new ItemClass("缶詰（肉・魚）", "kandume2_number", R.drawable.kandume, true, "缶", act),
                                new ItemClass("レトルト食品", "retoruto_number", R.drawable.retoruto, true, "袋", act),
                                new ItemClass("フリーズドライ", "furizu_dorai_number", R.drawable.furizu_dorai, true, "食", act),
                                new ItemClass("水", "mizu_number", R.drawable.mizu, true, "ℓ", act),
                                new ItemClass("カロリーメイト", "karori_meito_number", R.drawable.karori_meito, true, "箱", act),
                                new ItemClass("お菓子", "okasi_number", R.drawable.okasi, true, "箱・袋", act),
                                new ItemClass("離乳食", "rinyu_number", R.drawable.rinyu, true, "食", act),
                                new ItemClass("粉ミルク", "konamilk_number", R.drawable.konamilk, true, "缶", act)
                        };

                        Calendar cl = Calendar.getInstance();       //今日の日付の取得
                        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

                        //各グラフの取得
                        ImageButton R_button = (ImageButton)act.findViewById(R.id.R_graph);
                        ImageButton L_button = (ImageButton)act.findViewById(R.id.L_graph);

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
                        ((TextView)act.findViewById(R.id.hijousyoku_percent)).setText("非常食：" + String.valueOf(goukei[0]) + "%");

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
                        ((TextView)act.findViewById(R.id.bichiku_percent)).setText("備蓄品：" + String.valueOf(goukei[1]) + "%");

                        //要チェックに使用するTextViewを使用する
                        TextView[] Hijousyoku_tv = new TextView[MAX_HIJOUSYOKU];
                        //フラグメントのリニアレイアウトを取得
                        TableLayout tl = (TableLayout) act.findViewById(R.id.CheckLayout);
                        tl.removeAllViews();//中身を全部消去

                        for (int i = 0; i < MAX_HIJOUSYOKU; i++) {
                            Hijousyoku_tv[i] = new TextView(act);
                            //警告文を取得する
                            Hijousyoku_tv[i].setTextSize(18.0f);
                            if (loadInt2(item[i].getPrefName()) > 0) {
                                Hijousyoku_tv[i].setText(get_Number_of_days_Warning(item[i].getPrefName(), item[i].getName()));
                            }
                            //警告文を挿入する
                            if (Hijousyoku_tv[i].getText().length() > 0) {

                                //警告文を押すとダイアログが表示されるようにする
                                Hijousyoku_tv[i].setOnClickListener(new DialogOnClickListenerClass(item[i]));

                                //アイコンの設定
                                get_Icon_Warning(item[i].getPrefName(), item[i]);
                                Hijousyoku_tv[i].setCompoundDrawablesWithIntrinsicBounds(item[i].getIcon(), 0, 0, 0);
                                if (item[i].getIcon() == R.drawable.batsu) {
                                    Hijousyoku_tv[i].setTextColor(Color.RED);
                                }
                                if (item[i].getIcon() == R.drawable.bikkuri) {
                                    Hijousyoku_tv[i].setTextColor(Color.BLUE);
                                }
                            }
                        }

                        /*******************************************************************************************
                         // 要チェック欄に「～が備蓄されていません」というメッセージを出す
                         //
                         // メッセージ出力の条件
                         //     大人または小人が1以上、幼児が0　かつ　非常食の食べ物が50％未満
                         //     大人または小人が1以上、幼児が1以上　かつ　食べ物25％未満、乳児用25％未満
                         //     大人または小人が0、幼児が1以上　かつ　乳児用50％未満
                         //     水は大人または小人または幼児が1以上の場合に50％未満だとメッセージが出ます
                         *******************************************************************************************/

                        SharedPreferences Preferences = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);
                        int otona = Preferences.getInt("otona_people", 0); // 大人の人数を取得
                        int kobito = Preferences.getInt("kobito_people", 0); // 小人の人数を取得
                        int youji = Preferences.getInt("youji_people",0); // 幼児の人数を取得
                        if ( youji <= 0 ) { // 幼児がいない
                            if ( otona >= 1 || kobito >= 1 ) { // 大人または小人が1人以上
                                if ( FoodOverKids() < 50 ) { // 非常食が50％未満
                                    for ( int i = 0; i < MAX_HIJOUSYOKU - 2; i++ ) { // 幼児用の離乳食と粉ミルクは別の条件になるので-2しています
                                        if ( item[i].getName() != "水" ) { // 水を除く
                                            Hijousyoku_tv[i].setText(get_Number_of_days_Warning(item[i].getPrefName(), item[i].getName())); // 非常食品名 + が備蓄されていません
                                            Hijousyoku_tv[i].setCompoundDrawablesWithIntrinsicBounds(item[i].getIcon(), 0, 0, 0);
                                            Hijousyoku_tv[i].setOnClickListener(new DialogOnClickListenerClass(item[i])); //警告文を押すとダイアログが表示されるようにする
                                        }
                                    }
                                }
                                if ( RateWater() < 50 ) { // 水が50％未満
                                    Hijousyoku_tv[7].setText(get_Number_of_days_Warning(item[7].getPrefName(), item[7].getName())); // 水が備蓄されていません
                                    Hijousyoku_tv[7].setCompoundDrawablesWithIntrinsicBounds(item[7].getIcon(), 0, 0, 0);
                                    Hijousyoku_tv[7].setOnClickListener(new DialogOnClickListenerClass(item[7])); //警告文を押すとダイアログが表示されるようにする
                                }
                            }
                        }

                        if ( youji >= 1 ) { // 幼児が1人以上
                            if ( otona >= 1 || kobito >= 1 ) { // 大人または小人が1人以上
                                if ( FoodOverKids() < 25 ) { // 非常食が25％未満
                                    for ( int i = 0; i < MAX_HIJOUSYOKU - 2; i++ ) { // 幼児用の離乳食と粉ミルクは別の条件になるので-2しています
                                        if ( item[i].getName() != "水" ) { // 水を除く
                                            Hijousyoku_tv[i].setText(get_Number_of_days_Warning(item[i].getPrefName(), item[i].getName())); // 非常食品名 + が備蓄されていません
                                            Hijousyoku_tv[i].setCompoundDrawablesWithIntrinsicBounds(item[i].getIcon(), 0, 0, 0);
                                            Hijousyoku_tv[i].setOnClickListener(new DialogOnClickListenerClass(item[i])); //警告文を押すとダイアログが表示されるようにする
                                        }
                                    }
                                }
                                if ( RateWater() < 50 ) { // 水が50％未満
                                    Hijousyoku_tv[7].setText(get_Number_of_days_Warning(item[7].getPrefName(), item[7].getName())); // 水が備蓄されていません
                                    Hijousyoku_tv[7].setCompoundDrawablesWithIntrinsicBounds(item[7].getIcon(), 0, 0, 0);
                                    Hijousyoku_tv[7].setOnClickListener(new DialogOnClickListenerClass(item[7])); //警告文を押すとダイアログが表示されるようにする
                                }
                                if ( FoodBaby() < 25 ) { // 乳児用が25％未満
                                    Hijousyoku_tv[10].setText(get_Number_of_days_Warning(item[10].getPrefName(), item[10].getName())); // 離乳食が備蓄されていません
                                    Hijousyoku_tv[10].setCompoundDrawablesWithIntrinsicBounds(item[10].getIcon(), 0, 0, 0);
                                    Hijousyoku_tv[10].setOnClickListener(new DialogOnClickListenerClass(item[10])); //警告文を押すとダイアログが表示されるようにする
                                    Hijousyoku_tv[11].setText(get_Number_of_days_Warning(item[11].getPrefName(), item[11].getName())); // 粉ミルクが備蓄されていません
                                    Hijousyoku_tv[11].setCompoundDrawablesWithIntrinsicBounds(item[11].getIcon(), 0, 0, 0);
                                    Hijousyoku_tv[11].setOnClickListener(new DialogOnClickListenerClass(item[11])); //警告文を押すとダイアログが表示されるようにする
                                }
                            }
                        }

                        if ( otona <= 0 && kobito <= 0 ) { // 大人、小人がいない
                            if ( youji >= 1 ) { // 幼児が1人以上
                                if ( FoodBaby() < 50 ) { // 乳児用が25％未満
                                    Hijousyoku_tv[10].setText(get_Number_of_days_Warning(item[10].getPrefName(), item[10].getName())); // 離乳食が備蓄されていません
                                    Hijousyoku_tv[10].setCompoundDrawablesWithIntrinsicBounds(item[10].getIcon(), 0, 0, 0);
                                    Hijousyoku_tv[10].setOnClickListener(new DialogOnClickListenerClass(item[10])); //警告文を押すとダイアログが表示されるようにする
                                    Hijousyoku_tv[11].setText(get_Number_of_days_Warning(item[11].getPrefName(), item[11].getName())); // 粉ミルクが備蓄されていません
                                    Hijousyoku_tv[11].setCompoundDrawablesWithIntrinsicBounds(item[11].getIcon(), 0, 0, 0);
                                    Hijousyoku_tv[11].setOnClickListener(new DialogOnClickListenerClass(item[11])); //警告文を押すとダイアログが表示されるようにする
                                }
                                if ( RateWater() < 50 ) { // 水が50％未満
                                    Hijousyoku_tv[7].setText(get_Number_of_days_Warning(item[7].getPrefName(), item[7].getName())); // 水が備蓄されていません
                                    Hijousyoku_tv[7].setCompoundDrawablesWithIntrinsicBounds(item[7].getIcon(), 0, 0, 0);
                                    Hijousyoku_tv[7].setOnClickListener(new DialogOnClickListenerClass(item[7])); //警告文を押すとダイアログが表示されるようにする
                                }
                            }
                        }

/*
                        //幼児用のみテキストを変更
                        Hijousyoku_tv[10].setText( get_Child_Warning( item[10].getPrefName(),item[10].getName() ) );
                        Hijousyoku_tv[11].setText( get_Child_Warning( item[11].getPrefName(),item[11].getName() ) );
                        Hijousyoku_tv[10].setCompoundDrawablesWithIntrinsicBounds(item[10].getIcon(), 0, 0, 0);
                        Hijousyoku_tv[11].setCompoundDrawablesWithIntrinsicBounds(item[11].getIcon(), 0, 0, 0);
*/
                        for (int i = 0; i < MAX_HIJOUSYOKU; i++) {
                            //特に警告のないものは飛ばす
                            if (Hijousyoku_tv[i].getText().length() > 0) {
                                for (int k = MAX_HIJOUSYOKU - 1; k > i; k--) {
                                    //同じく特に警告のないものは飛ばす
                                    if (Hijousyoku_tv[k].getText().length() > 0) {
                                        if (getDate(item[k].getPrefName()) < getDate(item[k - 1].getPrefName())) {
                                            //場所を交換する
                                            TextView tv = Hijousyoku_tv[k - 1];
                                            Hijousyoku_tv[k - 1] = Hijousyoku_tv[k];
                                            Hijousyoku_tv[k] = tv;

                                            //アイテム
                                            ItemClass ic = item[k - 1];
                                            item[k - 1] = item[k];
                                            item[k] = ic;
                                        }

                                        // 離乳食である
                                        if (item[k].getName() == "離乳食") {
                                            //場所を交換する
                                            TextView tv = Hijousyoku_tv[k - 1];
                                            Hijousyoku_tv[k - 1] = Hijousyoku_tv[k];
                                            Hijousyoku_tv[k] = tv;

                                            //アイテム
                                            ItemClass ic = item[k - 1];
                                            item[k - 1] = item[k];
                                            item[k] = ic;
                                        }

                                        // 粉ミルクである
                                        if (item[k].getName() == "粉ミルク") {
                                            //場所を交換する
                                            TextView tv = Hijousyoku_tv[k - 1];
                                            Hijousyoku_tv[k - 1] = Hijousyoku_tv[k];
                                            Hijousyoku_tv[k] = tv;

                                            //アイテム
                                            ItemClass ic = item[k - 1];
                                            item[k - 1] = item[k];
                                            item[k] = ic;
                                        }

                                        if (item[k].getIcon() == R.drawable.batsu) { //×ボタンである
                                            //場所を交換する
                                            TextView tv = Hijousyoku_tv[k - 1];
                                            Hijousyoku_tv[k - 1] = Hijousyoku_tv[k];
                                            Hijousyoku_tv[k] = tv;

                                            //アイテム
                                            ItemClass ic = item[k - 1];
                                            item[k - 1] = item[k];
                                            item[k] = ic;
                                        }
                                    }
                                }
                                //画面に表示する
                                tl.addView(Hijousyoku_tv[i]);
                            }
                        }

                        //最終入力日
                        TextView b_tv = (TextView)act.findViewById(R.id.bichiku_nyuuryoku);
                        TextView h_tv = (TextView)act.findViewById(R.id.hijousyoku_nyuuryoku);

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
                    }// メイン画面処理　ここまで

                    // アクティビティが非常食画面の場合、以下の処理を行う
                    if (act.getClass() == Hijousyoku.class) {
                        // インテントで画面遷移し消費期限の赤枠線の更新を行う
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.setClassName("jp.co.nichiwa_system.application.Sonaechao", "jp.co.nichiwa_system.application.Sonaechao.Hijousyoku");
                        act.startActivity(intent);
                    } // 非常食画面処理　ここまで

                    // アクティビティが備蓄品画面の場合、以下の処理を行う
                    if (act.getClass() == Stock.class) {
                        // インテントで画面遷移し消費期限の赤枠線の更新を行う
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.setClassName("jp.co.nichiwa_system.application.Sonaechao", "jp.co.nichiwa_system.application.Sonaechao.Stock");
                        act.startActivity(intent);
                    }
                }
            }
        });
        alert.setView(viw);
        alert.show();
    }

    //値データを取得する関数
    public String loadInt( String name )
    {
        //プリファレンスの生成
        SharedPreferences pref =
                act.getSharedPreferences("Preferences",act.MODE_PRIVATE);
        int i = 0;
        i = pref.getInt(name,0);
        String str = String.valueOf(i);

        return str;
    }

    //値データを取得する関数
    public int loadInt2( String name )
    {
        //プリファレンスの生成
        SharedPreferences pref =
                act.getSharedPreferences("Preferences",act.MODE_PRIVATE);
        int i =  pref.getInt(name, 0);
        return i;
    }

    //値データをプレファレンスで保存する関数 (エディットテキスト、名前)
    public void saveInt( EditText et, String name )
    {
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
    public Calendar loadCalendar( String prefName )
    {
        SharedPreferences pref =
                act.getSharedPreferences( prefName + "_pref", act.MODE_PRIVATE);
        Calendar cl = Calendar.getInstance();
        cl.set(pref.getInt("year", cl.get(Calendar.YEAR)), pref.getInt("month", cl.get(Calendar.MONTH)), pref.getInt("day", cl.get(Calendar.DAY_OF_MONTH)));

        return cl;
    }

    //日付を格納する
    public void saveCalendar( Calendar cl , String prefName )
    {
        SharedPreferences pref =
                act.getSharedPreferences( prefName + "_pref", act.MODE_PRIVATE);

        SharedPreferences.Editor e = pref.edit();

        e.putInt("year", cl.get(Calendar.YEAR));
        e.putInt("month", cl.get(Calendar.MONTH) );
        e.putInt("day", cl.get(Calendar.DAY_OF_MONTH) );

        e.commit();
    }

    /*********************************************************************
     //非常食の賞味期限と設定した期日を計算して、それぞれのテキストを返す
     // prefName       ・・・ プレファレンス名
     // HijousyokuName ・・・ 非常食の名前
     *********************************************************************/
    public String get_Number_of_days_Warning( String prefName, String HijousyokuName )
    {
        String str = "";

        // 非常食の備蓄数を取得する
        int food_h = ( act.getSharedPreferences("Preferences",act.MODE_PRIVATE) ).getInt(prefName,0);
        //残り日数を取得する
        int nokori = (int)getDate(prefName);
        //期日を取得する
        int nissu =  ( act.getSharedPreferences("Preferences",act.MODE_PRIVATE) ).getInt("kiniti_day",14);

        if( food_h < 1 ) { // もし非常食が備蓄されていなかったら
            str = HijousyokuName + "が備蓄されていません";
        } else if(nokori == 0) { // 非常食の備蓄数が1以上の賞味期限表示
            //賞味期限が当日になったら表示
            str = HijousyokuName + "の賞味期限が当日です";
        } else if( nokori < 0 ) {
            //賞味期限が切れたら表示
            str = HijousyokuName + "の賞味期限が切れました";
        } else if( nokori <= nissu ) {
            //賞味期限が期日に近づいたら表示
            str = HijousyokuName + "の賞味期限が" + nokori + "日前です";
        }
        return str;
    }

/*    // 非常食が0の時、要チェック欄に「～が備蓄されていません」警告を表示（ifの条件文は上の出力の時にしてあります）
    public String get_Food_Warning( String dateName , String name )
    {
        String str = "";
        int food_h = ( getSharedPreferences("Preferences",MODE_PRIVATE) ).getInt(dateName,0);

        if( food_h < 1 ) { // もし非常食が備蓄されていなかったら
            str = name + "が備蓄されていません";
        }
        return str;
    }
*/

    /*********************************************************************
     //非常食の賞味期限と設定した期日を計算して、それぞれのアイコンを返す
     // prefName       ・・・ プレファレンス名
     // HijousyokuName ・・・ 非常食の名前
     *********************************************************************/
    public void get_Icon_Warning(String prefName ,ItemClass item)
    {
        //残り日数を取得する
        int nokori = (int)getDate(prefName);
        //期日を取得する
        int nissu =  ( act.getSharedPreferences("Preferences",act.MODE_PRIVATE) ).getInt("kiniti_day",0);
        if( nokori <= 0 ) {
            item.setIcon( R.drawable.batsu );
        } else if( nokori <= nissu ) {
            item.setIcon(R.drawable.bikkuri);
        }
        if( nokori <= 0 && item.getName() == "離乳食" || nokori <= 0 && item.getName() == "粉ミルク") {
            item.setIcon(R.drawable.batsu_b);
        } else if ( nokori <= nissu && item.getName() == "離乳食" || nokori <= nissu && item.getName() == "粉ミルク") {
            item.setIcon(R.drawable.bikkuri_b);
        }
    }

/*
    // 乳児一人以上で、なおかつ離乳食と粉ミルクが「0」のとき、警告を表示
    public String get_Child_Warning( String dateName , String name )
    {
        String str = "";
        int youji = ( getSharedPreferences("Preferences",MODE_PRIVATE) ).getInt("youji_people",0);
        int youji_h = ( getSharedPreferences("Preferences",MODE_PRIVATE) ).getInt(dateName,0);

        //幼児が一人以上の時
        if( youji >= 1  ) {
            //離乳食がない
            if( youji_h < 1 ) {
                str = name + "が備蓄されていません";
            }
        }

        return str;
    }
*/

    /*********************************************************************
     // 賞味期限の日付と現在の日付から引き出された残り日数を返す
     // prefName ・・・　プレファレンス名
     *********************************************************************/
    public long getDate(String prefName)
    {
        SharedPreferences pref2 = act.getSharedPreferences(prefName+"_pref",act.MODE_PRIVATE);
        //現在の時刻
        Calendar cl = Calendar.getInstance();
        //引数で指定した食品の賞味期限
        Calendar cl2 = Calendar.getInstance();
        cl2.set( pref2.getInt("year", cl.get(Calendar.YEAR) ), pref2.getInt("month", cl.get(Calendar.MONTH) ), pref2.getInt("day", cl.get(Calendar.DAY_OF_MONTH) ) );
        Date date1 = cl.getTime();
        Date date2 = cl2.getTime();

        long current_time = date1.getTime();
        long item_time = date2.getTime();

        long nokori = (item_time - current_time) / ( 1000 * 60 * 60 * 24 );

        return nokori;
    }

    /*********************************************************************
     // 人数と非常食の栄養量から割り出されたパーセンテージを返す
     // return: s_w ・・・ 非常食の全体のパーセンテージ
     *********************************************************************/
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
        int setDays = pref.getInt("sitei_day",3);

        //各合計（大小のみ）
        int Hijousyoku_sum = reto_g + kan + kanmen + kanpan + kan2 + reto + furizu + karori + okasi;

        double okAll;
        double div = 2.0;

        //栄養価の計算。乾パンとカロリーメイトを抜いたものは栄養価１．乾パン、カロリーメイトは３。
        //  大小限定の栄養価総計。over kids All
        okAll = ((Hijousyoku_sum - kanpan - karori) * 1) + (kanpan + karori) * 3;

        //  大小の割合。
        double rateOK = okAll / ((( adult_n * 3 ) + ( child_n * 2 )) * setDays );

        if(rateOK >= 1.0){
            rateOK = 1.0;
        }

        //  幼児がいなかった場合
        if(!(baby_n > 0)){
            div -= 1.0;
        }

        if(!( adult_n > 0 || child_n > 0 )){
            rateOK = 0.0;
        }

        if( adult_n > 0 || child_n > 0 ){
            rateOK = ( rateOK / div ) * 50.0;
        }

        return (int)rateOK;
    }

    public int FoodBaby(){
        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

        int rinyu = pref.getInt("rinyu_number", 0);
        int konamilk = pref.getInt("konamilk_number", 0);

        //人数
        int adult_n = pref.getInt("otona_people", 0);
        int child_n = pref.getInt("kobito_people", 0);
        int baby_n = pref.getInt("youji_people", 0);


        int setDays = pref.getInt("sitei_day",3);

        double bAll;

        //  幼児限定の栄養価総計。baby All
        bAll  = ( konamilk * 3 ) + rinyu;

        double rate  = bAll / (( baby_n * 3 ) * setDays );
        double div = 2.0;

        if(rate >= 1.0){
            rate = 1.0;
        }

        //  大人・小人がいなかった場合
        if(!( adult_n > 0 || child_n > 0 )){
            div -= 1.0;
        }

        //  幼児がいなかった場合
        if(!(baby_n > 0)){
            rate = 0.0;
        }

        //  幼児がいる場合計算を行う(念のため）
        if(baby_n > 0) {
            rate = (rate / div) * 50.0;
        }

        return (int)rate;
    }

    public int RateWater(){
        double rate;

        //水の必要数の計算
        //　水の部分が計算が異なっていたので修正。修正者：岡田
        SharedPreferences pref = act.getSharedPreferences("Preferences", act.MODE_PRIVATE);

        double mizu = pref.getInt("mizu_number", 0);

        //人数
        int adult_n = pref.getInt("otona_people", 0);
        int child_n = pref.getInt("kobito_people", 0);
        int baby_n = pref.getInt("youji_people", 0);

        int setDays = pref.getInt("sitei_day",3);

        double   adult_w = adult_n * 3;
        double   child_w = child_n * 2;
        double   baby_w  = baby_n  * 2;

        //  水の必要値の算出。備えちゃお日数も追加。
        double   total_w = (adult_w + child_w + baby_w) * setDays;

        //備蓄は何％あるか計算。最大50％
        rate = ( mizu / total_w ) * 50;

        //  水の最大値は50％までの設定
        if( rate >= 50 ){
            rate = 50;
        }

        if(( adult_n + child_n + baby_n ) == 0){
            rate = 0.0;
        }

        return (int)rate;
    }

    /***************************************************************************************************
     //  処理内容：備蓄品のパーセントの算出
     //  概要  　：備蓄品の各パーセントを求めだし、それらを足し合わせたものをreturnしている。
     //  作成日　：2015年6月2日
     //  担当者　：岡田 洋介
     //  備考　　：幼児がいる場合は、必需品5割、便利品4割、幼児品1割。
     //          ：幼児がいない場合は、必需品6割、便利品4割。となる。（それぞれの最大値です）
     ***************************************************************************************************/
    public int RateStock()
    {
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
        int setDays = pref.getInt("sitei_day",3);

/***************************************************************************************************
 //  必需品の計算S
 ***************************************************************************************************/

        float rateNTotal = 0.0f;
        float rateNOKAll = 0.0f;
        float rateNBAll = 0.0f;
        float divStock = 15.0f; //  備蓄品専用の割り算。カテゴリーの数だけ割り算をするので、変更有。
        float divBStock = 2.0f;

        rateNOKAll += UsedFamilyStock( kaichu, setDays, 1.0f, 1.0f, 1.0f);
        rateNOKAll += UsedFamilyStock( almi, setDays, 1.0f, 1.0f, 2.0f);
        rateNOKAll += UsedFamilyStock( rap, setDays, 1.0f, 1.0f, 3.0f);
        rateNOKAll += UsedFamilyStock( bombe, setDays, 1.0f, 2.0f, 5.0f);
        rateNOKAll += UsedFamilyStock( gas, setDays, 1.0f, 1.0f, 1.0f);
        rateNOKAll += UsedFamilyStock( tissue, setDays, 1.0f, 1.0f, 3.0f);
        rateNOKAll += UsedFamilyStock( bag, setDays, 1.0f, 1.0f, 1.0f);
        rateNOKAll += UsedFamilyStock( spoon, setDays, 1.0f, 1.0f, 1.0f);
        rateNOKAll += UsedFamilyStock( hasi, setDays, 1.0f, 1.0f, 1.0f);
        rateNOKAll += UsedFamilyStock( denti, setDays, 2.0f, 2.0f, 4.0f);
        rateNOKAll += UsedOneStock( koppu, adult, kids, baby);
        rateNOKAll += UsedOneStock( utuwa, adult, kids, baby);
        rateNOKAll += UsedOneStockOnlyTaoru( towel, adult, kids, baby, setDays);
        if(kids > 0) {
            rateNOKAll += UsedWearStock( kSitagi, setDays, 1.0f, 1.0f, 2.0f);
        }
        if(!(setDays == 1) && adult > 0) {   //
            rateNOKAll += UsedWearStock(aSitagi, setDays, 0.0f, 1.0f, 2.0f);
        }
        //  この下の関数だけ、divStockが２つ分
        if(baby > 0) {
            rateNBAll += UsedBabyWearStock(bin, omutu, setDays);
        }


        if(setDays == 1 && adult > 0){
            divStock -= 1.0f;
        }

        if(adult == 0){
            divStock -= 1.0f;
        }

        if(kids == 0){
            divStock -= 1.0f;
        }

        if (baby == 0){
            divBStock -= 2.0f;
        }

        if((adult + kids) > 0 && baby == 0){
            rateNOKAll = ( rateNOKAll / divStock ) * 0.6f;
        } else if ((adult + kids) > 0 && baby > 0){
            rateNOKAll = ( rateNOKAll /  divStock ) * 0.5f;
            rateNBAll  = ( rateNBAll  / divBStock ) * 0.1f;
        }

        //  必需品の最終計
        rateNTotal = (rateNOKAll + rateNBAll) ;
        if ((adult + kids + baby ) == 0){
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
        rateUAll += UsedOneStock( gunnte, adult, kids, 0);
        rateUAll += UsedOneStock( nebukuro, adult, kids, 0);

        rateUAll += UsedFamilyStock( fue, setDays, 1.0f, 1.0f, 1.0f);
        rateUAll += UsedFamilyStock( matti, setDays, 1.0f, 1.0f, 1.0f);
        rateUAll += UsedFamilyStock( radio, setDays, 1.0f, 1.0f, 1.0f);
        if(!(setDays == 1)) {   //  設定が1日だった場合、行わない。０除算防止。
            rateUAll += UsedFamilyStock(kankiri, setDays, 0.0f, 1.0f, 1.0f);
        }
        rateUAll += UsedFamilyStock( mask, setDays, 1.0f, 1.0f, 1.0f);
        rateUAll += UsedFamilyStock( judenki, setDays, 1.0f, 1.0f, 1.0f);

        if(setDays == 1){
            divUStock -= 1.0f;
        }

        rateUTotal = (rateUAll / divUStock ) * 0.4f;

/**********************************************************************************/
//  備蓄品、トータルパーセンテージ
/**********************************************************************************/
        rateStockTotal = ( (int)((rateNTotal + rateUTotal) * 100) );

        if( (adult + kids + baby) == 0 ){
            rateStockTotal = 0;
        }
        return rateStockTotal;
    }

    /***************************************************************************************************
     //  備蓄品で必要な関数S
     ***************************************************************************************************/
    public float UsedOneStockOnlyTaoru(float taoru, int adult, int kids, int baby, int set){
        float rate = 0.0f;
        switch (set){
            case 1:
                rate = taoru / (adult + kids + baby);
                if (rate >= 1.0f){
                    rate = 1.0f;
                }
                break;

            case 3:
                rate = taoru / ( adult + kids + (baby * 2.0f) );
                if (rate >= 1.0f){
                    rate = 1.0f;
                }
                break;

            case 7:
                rate = taoru / ( (adult * 3.0f) + (kids * 3.0f) + (baby * 6.0f) );
                if (rate >= 1.0f){
                    rate = 1.0f;
                }
                break;
        }
        return rate;
    }

    //  コップと器専用の計算関数
    public float UsedOneStock(float stock, int adult, int kids, int baby){
        float rate;

        rate = stock / ( adult + kids + baby );
        if(rate >= 1.0f){
            rate = 1.0f;
        }

        return rate;
    }

    //  関数
    public float UsedFamilyStock(float stock, int set, float x, float y, float z){
        float rate = 0.0f;
        switch (set){
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

        if(rate >= 1.0f){
            rate = 1.0f;
        }

        return rate;
    }
    //改正版
    public float UsedWearStock(float wear, int set, float x, float y, float z){
        float rate = 0.0f;

        switch (set){
            case 1:
                rate = wear / x;
                if(rate >= 1.0f){
                    rate = 1.0f;
                }
                break;

            case 3:
                rate = wear / y;
                if(rate >= 1.0f){
                    rate = 1.0f;
                }
                break;

            case 7:
                rate = wear / z;
                if(rate >= 1.0f){
                    rate = 1.0f;
                }
                break;
        }

        return rate;
    }

    public float UsedBabyWearStock(float bin, float omutu, int set){
        float rateBin = 0.0f;
        float rateOmutu = 0.0f;
        float rateFinal;

        switch (set){
            case 1:
                rateBin = bin / 1.0f;
                if( rateBin >= 1.0f){
                    rateBin = 1.0f;
                }

                rateOmutu = omutu / 2.0f;
                if (rateOmutu >= 1.0f){
                    rateOmutu = 1.0f;
                }
                break;

            case 3:
                rateBin = bin / 1.0f;
                if( rateBin >= 1.0f){
                    rateBin = 1.0f;
                }

                rateOmutu = omutu / 5.0f;
                if (rateOmutu >= 1.0f){
                    rateOmutu = 1.0f;
                }
                break;

            case 7:
                rateBin = bin / 1.0f;
                if( rateBin >= 1.0f){
                    rateBin = 1.0f;
                }

                rateOmutu = omutu / 10.0f;
                if (rateOmutu >= 1.0f){
                    rateOmutu = 1.0f;
                }
                break;
        }

        rateFinal = rateBin + rateOmutu;

        return rateFinal;
    }

    /***************************************************************************************************
     //  処理内容：非常食の合計値を求めている処理
     //  概要  　：returnは警告ダイアログの条件式の一つとして使用される。
     //  作成日　：2015年6月3日
     //  担当者　：岡田 洋介
     //  備考　　：特になし。
     ***************************************************************************************************/
    public int VolumeFoods()
    {
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

    /***************************************************************************************************
     //  処理内容：備蓄品の合計値を求めている処理
     //  概要  　：returnは警告ダイアログの条件式の一つとして使用される。
     //  作成日　：2015年6月3日
     //  担当者　：岡田 洋介
     //  備考　　：特になし。
     ***************************************************************************************************/
    public int VolumeStock()
    {
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