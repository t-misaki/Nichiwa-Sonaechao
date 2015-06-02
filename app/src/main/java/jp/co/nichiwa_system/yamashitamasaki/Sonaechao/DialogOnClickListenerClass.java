package jp.co.nichiwa_system.yamashitamasaki.Sonaechao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TableLayout;
import android.widget.TextView;

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
    };

    //ItemClassのコンストラクタ
    public DialogOnClickListenerClass(ItemClass item){
        //ずばっと
        this(item.getName(),item.getPrefName(),item.getDrawable_Location(),item.getCalender_flag(),item.getActivity());
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

        //EditTextを取得する
        final EditText et = (EditText)viw.findViewById(R.id.Number);

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

        //指定したEditTextの中に値を挿入する
        et.setText( loadInt(DateName) );

        //カレンダーが必要ある時に行う処理
         if( calendarshow )
        {
            //単位を変える必要がある場合
            if( tani != null ) {
                //文字を変える
                TextView tv = (TextView) viw.findViewById(R.id.textView26);
                tv.setText(tani);
            }

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

            Clock_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dpd.show();
                }
            });

        }else{
            //必要なければ非表示にする
            ImageView Clock_iv = (ImageView)viw.findViewById(R.id.imageCalender);
            Clock_iv.setVisibility(View.GONE);
            day.setVisibility(View.GONE);
        }

        //アイテムが不要な人は、ダイアログに表示させない
        for( int i = 0 ; i < 3 ; i++ ) {
            if( Hijou_num[Number][i] <= 0 ) {
                People_tv[i].setVisibility( View.GONE );
            }
        }

        //決定ボタンを押すと行われる処理
        alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //値が格納されてないか判定する
                if(et.getText().toString().length() <= 0) {
                    //カラッポである
                    et.setText("0");
                    saveInt(et, DateName);
                }
                else {
                    //正常なのでデータを保存する
                    saveInt(et, DateName);

                    //現在の日付を保存
                    saveCalendar( Calendar.getInstance(), act.getLocalClassName() );

                    //賞味期限の保存
                    if( calendarshow ) {
                        //日付を保存する
                        saveCalendar(cl, DateName);
                    }

                    //アクティビティがメイン画面の場合、以下の処理を行う
                    if( act.getClass() == MainActivity.class )
                    {
                        //非常食の項目を取得する
                        ItemClass[] item  = {
                                new ItemClass("レトルトごはん", "retorutogohan_number", R.drawable.retoruto_gohan, true,"袋", act),
                                new ItemClass("缶詰（ごはん）", "kandume_number", R.drawable.kandume_gohan, true,"缶", act),
                                new ItemClass("乾麺", "kanmen_number", R.drawable.kanmen, true,"袋", act),
                                new ItemClass("カンパン", "kanpan_number", R.drawable.kanpan, true,"缶", act),
                                new ItemClass("缶詰（肉・魚）", "kandume2_number", R.drawable.kandume, true, "缶", act),
                                new ItemClass("レトルト食品", "retoruto_number", R.drawable.retoruto, true, "袋", act),
                                new ItemClass("フリーズドライ", "furizu_dorai_number", R.drawable.furizu_dorai, true, "塊", act),
                                new ItemClass("水", "mizu_number", R.drawable.mizu, true, "ℓ",act),
                                new ItemClass("カロリーメイト", "karori_meito_number", R.drawable.karori_meito, true, "箱", act),
                                new ItemClass("お菓子", "okasi_number", R.drawable.okasi, true, "箱・袋", act),
                                new ItemClass("離乳食", "rinyu_number", R.drawable.rinyu, true, act ),
                                new ItemClass("粉ミルク", "konamilk_number", R.drawable.konamilk, true, act)
                        };

                        //要チェックに使用するTextViewを使用する
                        TextView[] Hijousyoku_tv = new TextView[12];
                        //フラグメントのリニアレイアウトを取得
                        TableLayout tl  = (TableLayout)act.findViewById(R.id.CheckLayout);
                        tl.removeAllViews();//中身を全部消去

                        for( int i = 0 ; i < 12 ; i++ ) {
                            Hijousyoku_tv[i] = new TextView(act);
                            //警告文を取得する
                            Hijousyoku_tv[i].setText(get_Number_of_days_Warning(item[i].getPrefName(), item[i].getName()));
                            //警告文を挿入する
                            if( Hijousyoku_tv[i].getText().length() > 0 ) {

                                //警告文を押すとダイアログが表示されるようにする
                                Hijousyoku_tv[i].setOnClickListener( new DialogOnClickListenerClass(item[i]));

                                //アイコンの設定
                                get_Icon_Warning(item[i].getPrefName(), item[i]);
                                Hijousyoku_tv[i].setCompoundDrawablesWithIntrinsicBounds(item[i].getIcon(), 0, 0, 0);
                                Hijousyoku_tv[i].setTextColor(Color.RED);
                            }
                        }

                        //幼児用のみテキストを変更
                        Hijousyoku_tv[10].setText( get_Child_Warning( item[10].getPrefName(),item[10].getName() ) );
                        Hijousyoku_tv[11].setText( get_Child_Warning( item[11].getPrefName(),item[11].getName() ) );
                        Hijousyoku_tv[10].setCompoundDrawablesWithIntrinsicBounds(item[10].getIcon(), 0, 0, 0);
                        Hijousyoku_tv[11].setCompoundDrawablesWithIntrinsicBounds(item[11].getIcon(), 0, 0, 0);

                        for( int i = 0 ; i < 12 ; i++ ) {
                            //特に警告のないものは飛ばす
                            if (Hijousyoku_tv[i].getText().length() > 0) {
                                for( int k = 12-1 ; k > i ; k-- ) {
                                    //同じく特に警告のないものは飛ばす
                                    if (Hijousyoku_tv[k].getText().length() > 0) {
                                        //乳児用の食料である
                                        if(item[k].getName() == "離乳食" || item[k].getName() == "粉ミルク") {
                                            //場所を交換する
                                            TextView tv = Hijousyoku_tv[k - 1];
                                            Hijousyoku_tv[k - 1] = Hijousyoku_tv[k];
                                            Hijousyoku_tv[k] = tv;

                                            //アイテム
                                            ItemClass ic = item[k-1];
                                            item[k-1] = item[k];
                                            item[k] = ic;
                                        }else if (item[k].getIcon() == R.drawable.batsu || Hijousyoku_tv[k-1].getText().length() < 0) { //×ボタン または 空白 である
                                            //場所を交換する
                                            TextView tv = Hijousyoku_tv[k - 1];
                                            Hijousyoku_tv[k - 1] = Hijousyoku_tv[k];
                                            Hijousyoku_tv[k] = tv;

                                            //アイテム
                                            ItemClass ic = item[k-1];
                                            item[k-1] = item[k];
                                            item[k] = ic;
                                        }
                                    }
                                }
                                //画面に表示する
                                tl.addView(Hijousyoku_tv[i]);
                            }
                        }
                    }// メイン画面処理　ここまで
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
        cl.set( pref.getInt("year", cl.get(Calendar.YEAR) ), pref.getInt("month", cl.get(Calendar.MONTH) ), pref.getInt("day", cl.get(Calendar.DAY_OF_MONTH) ) );

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
        //残り日数を取得する
        int nokori = (int)getDate(prefName);
        //期日を取得する
        int nissu =  ( act.getSharedPreferences("Preferences",act.MODE_PRIVATE) ).getInt("kiniti_day",0);
        if( nokori <= 0 ) {
            //賞味期限が切れたら表示
            str = HijousyokuName + "の賞味期限が切れました";
        } else if( nokori <= nissu ) {
            //賞味期限が期日に近づいたら表示
            str = HijousyokuName + "の賞味期限が" + nokori + "日前です";
            if(nokori == 1) {
                //賞味期限が当日になったら表示
                str = HijousyokuName + "の賞味期限が当日です";
            }
        }

        return str;
    }

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
        int nissu =  ( act.getSharedPreferences("Preferences",act.MODE_PRIVATE) ).getInt("kiniti_day", 0);
        if( nokori <= 0 ) {
            item.setIcon( R.drawable.batsu );
        } else if( nokori <= nissu ) {
            item.setIcon(R.drawable.bikkuri);
        }
    }

    // 乳児一人以上で、なおかつ離乳食と粉ミルクが「0」のとき、警告を表示
    public String get_Child_Warning( String dateName , String name )
    {
        String str = "";
        int youji = ( act.getSharedPreferences("Preferences",act.MODE_PRIVATE) ).getInt("youji_people", 0);
        int youji_h = ( act.getSharedPreferences("Preferences",act.MODE_PRIVATE) ).getInt(dateName,0);

        //幼児が一人以上の時
        if( youji >= 1  ) {
            //離乳食がない
            if( youji_h < 1 ) {
                str = name + "が備蓄されていません";
            }
        }

        return str;
    }

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
}