package jp.co.nichiwa_system.yamashitamasaki.Sonaechao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.content.DialogInterface;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import jp.co.nichiwa_system.yamashitamasaki.Sonaechao.R;


public class SubActivity extends Activity {

    InputMethodManager inputMethodManager;
    RelativeLayout R_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //キーボードの状態を取得
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //レイアウトの取得
        R_layout = (RelativeLayout)findViewById(R.id.sub_Layout);

        //人数
        final CharSequence[] people = {"0","1","2","3","4","5","6","7","8","9"};
        //期日
        final CharSequence[] kijitu_day = { "14", "30", "60" };
        //備えちゃお日数
        final CharSequence[] nissuu_day = { "1", "3", "7" };

        //データの読み込み
        loadInt( (EditText)findViewById(R.id.EditText5) , "youji_people", 0 );   //幼児
        loadInt( (EditText)findViewById(R.id.EditText),   "otona_people", 0);    //大人
        loadInt( (EditText)findViewById(R.id.EditText2) , "kobito_people", 0);  //小人
        loadInt( (EditText)findViewById(R.id.EditText3) , "kiniti_day", 14);      //期日
        loadInt( (EditText)findViewById(R.id.EditText4) , "sitei_day", 3);       //備えちゃお日数

        //設定日数と期日のEditTextを取得
        EditText otona_et = (EditText)findViewById(R.id.EditText); //大人
        EditText kobito_et = (EditText)findViewById(R.id.EditText2); //小人
        EditText youji_et = (EditText)findViewById(R.id.EditText5); //幼児
        EditText kijitu_et = (EditText)findViewById(R.id.EditText3); //期日
        EditText settei_et = (EditText)findViewById(R.id.EditText4); //備えちゃお日数

        //クリックするとダイアログを表示

        otona_et.setOnClickListener( new OnClick( people, R.id.EditText ) );        //大人
        kobito_et.setOnClickListener( new OnClick( people, R.id.EditText2 ) );      //小人
        youji_et.setOnClickListener( new OnClick( people, R.id.EditText5 ) );       //幼児
        kijitu_et.setOnClickListener( new OnClick( kijitu_day, R.id.EditText3 ) );  //期日
        settei_et.setOnClickListener( new OnClick( nissuu_day, R.id.EditText4 ) );  //備えちゃお日数

        Button Home = (Button)findViewById(R.id.home);          //「ホーム」ボタン
        Button Stock = (Button)findViewById(R.id.bichiku);           //「備蓄」ボタン
        Button hijousyoku = (Button)findViewById(R.id.hijousyoku);  //「非常食」ボタン

        //移動
        Home.setOnClickListener( new OnClickListenerClass() );
        Stock.setOnClickListener( new OnClickListenerClass(".Stock", this) );
        hijousyoku.setOnClickListener( new OnClickListenerClass(".Hijousyoku", this) );

        //広告の設定
        /*
        AdView adview = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);*/
    }

    //画面をタッチしたときの処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //キーボードを非表示にする
        inputMethodManager.hideSoftInputFromWindow(R_layout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //フォーカスの更新
        //キーボードが非表示になっているはずなので、EditTextからフォーカスが外れる
        R_layout.requestFocus();
        return true;
    }

    //数字を選択するダイアログを生成するクラス
    class OnClick implements View.OnClickListener{
        Builder alert = new Builder(SubActivity.this); //ダイアログ
        CharSequence[] item = {"0"};    //アイテム
        int root;                      //部品の場所

        //コンストラクタ
        OnClick(CharSequence[] item, int root)
        {
            this.item = item;
            this.root = root;
        }

        @Override
        public void onClick(View v) {
            alert.setTitle("値を選択してください");
            //アイテムの表示
            alert.setItems(item, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //選択した値を格納する
                    String str = (String)item[which];
                    //備蓄日数を記入する
                    ((EditText) findViewById(root)).setText(str);
                }
            });
            alert.create();
            alert.show();
        }
    }

    //遷移クラス
    class OnClickListenerClass extends OnClickTransListenerClass {

        //コンストラクタ
        public OnClickListenerClass() {
            // スーパークラスであるOnClickTransListenerClassの
            // コンストラクタに引数を渡す
            super(SubActivity.this);
        }

        public OnClickListenerClass( String name, Activity act ) {
            super( name, act );
        }

        @Override
        public void onClick(View v) {

            //値に問題がないかチェック
            if(

                    saveInt( (EditText)findViewById(R.id.EditText5)  , "youji_people")   &&
                            saveInt( (EditText)findViewById(R.id.EditText)  , "otona_people")   &&
                            saveInt( (EditText)findViewById(R.id.EditText2) , "kobito_people")  &&
                            saveInt( (EditText)findViewById(R.id.EditText3) , "kiniti_day")     &&
                            saveInt( (EditText)findViewById(R.id.EditText4) , "sitei_day")
            ) {
                if(intent == null) {
                    //ホームボタンの時アクティビティを閉じる
                    finish();
                }else{
                    //それ以外はアクティビティを表示
                    startActivity(intent);
                }
            }
            else {
                //歯抜けがあれば、警告文を表示する
                Builder alertDialog = new Builder(SubActivity.this);

                alertDialog.setTitle("Error");
                alertDialog.setMessage("値が入力されていません");

                alertDialog.setPositiveButton("はい", null);

                alertDialog.create();
                alertDialog.show();
            }
        }
    }

    //値データを取り出す関数
    public void loadInt( EditText et , String name, int CreateNum)
    {
        SharedPreferences pref =
                getSharedPreferences("Preferences",MODE_PRIVATE);
        //(保存している値の名前,何も入っていなかった時の初期値)

        int i = CreateNum;
        i  = pref.getInt( name, i );
        //値を文字に変換して挿入
        String str = String.valueOf(i);
        et.setText(str);
    }

    //値データをプレファレンスで保存する関数 (エディットテキスト、名前)
    // true: 成功 false: 失敗
    public boolean saveInt( EditText et, String name )
    {
        //プレファレンスの生成
        SharedPreferences pref;
        pref = getSharedPreferences("Preferences",MODE_PRIVATE);//棚を作る
        Editor e = pref.edit();

        //int型に変換して挿入
        String str = et.getText().toString();
        int i = Integer.parseInt(str);

        //名前をつけて値を格納
        e.putInt( name, i );

        //Preferences棚に保管する
        e.commit();

        // 成功である
        return true;
    }
}