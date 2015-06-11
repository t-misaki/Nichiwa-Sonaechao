package jp.co.nichiwa_system.application.Sonaechao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.content.DialogInterface;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import jp.co.nichiwa_system.application.Sonaechao.R;


public class SubActivity extends Activity {
    //SharedPreferences pref = getSharedPreferences("Preference",MODE_PRIVATE);
    InputMethodManager inputMethodManager;
    RelativeLayout R_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        SharedPreferences pre = getSharedPreferences("Preference",MODE_PRIVATE);
        if(pre.getInt("fast_start_sub",0)==0){
            /***処理***/
            AlertDialog.Builder fast = new AlertDialog.Builder(this);
            fast.setTitle("設定画面の説明");
            fast.setMessage("ここでは大人、小人、幼児等の設定ができます！\n" +
                    "人数やお知らせ期日、備えちゃお日数を設定して災害に備えちゃお！\n"
            );

            fast.setPositiveButton("次へ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   // dialog.dismiss();
                    AlertDialog.Builder fast = new AlertDialog.Builder(SubActivity.this);
                    fast.setTitle("設定画面の説明");
                    fast.setMessage("大人、小人、幼児の人数を入力できます。\n\n" +
                            "お知らせ期日は賞味期限何日前にお知らせをだすか設定できます。\n\n"+"備えちゃお日数は何日分の備蓄をするのか設定できます。\n\n"+
                            "※このメッセージは画面下の設定ボタンを押すと再び表示されます。");
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
            e.putInt("fast_start_sub", 1);
            e.commit();
        }

        //バージョン情報出力
       /* PackageInfo packageInfo = null;
        TextView tv = (TextView)findViewById(R.id.version);
        try {
            packageInfo = getPackageManager().getPackageInfo("jp.co.nichiwa_system.application.Sonaechao", PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tv.setText("versionCode : "+packageInfo.versionCode+" / "+"versionName : "+packageInfo.versionName);
        SharedPreferences pref= getSharedPreferences("preferense",MODE_PRIVATE);
        TextView tv = (TextView)findViewById(R.id.version);
        tv.setText(String.valueOf(pref.getInt("save_d",0)));*/



                //キーボードの状態を取得
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //レイアウトの取得
        R_layout = (RelativeLayout)findViewById(R.id.sub_Layout);

        //人数
        final CharSequence[] people = {"0","1","2","3","4","5","6","7","8","9"};
        //期日
        final CharSequence[] kijitu_day = { "14", "30", "60" ,"90","180","365"};
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

        ImageButton Home = (ImageButton)findViewById(R.id.home);          //「ホーム」ボタン
        final ImageButton Stock = (ImageButton)findViewById(R.id.bichiku);           //「備蓄」ボタン
        ImageButton hijousyoku = (ImageButton)findViewById(R.id.hijousyoku);  //「非常食」ボタン
        ImageButton set = (ImageButton)findViewById(R.id.settingbutton);

        //移動
       /* Home.setOnClickListener( new OnClickListenerClass(".MainActivity", this) ); // ホーム画面へ
        Stock.setOnClickListener( new OnClickListenerClass(".Stock", this) ); // 備蓄品画面へ
        hijousyoku.setOnClickListener( new OnClickListenerClass(".Hijousyoku", this) ); // 非常食画面へ*/


        /**********************************************************************************************
         * 処理内容：大人子供幼児お知らせ期日備えちゃお日数を編集後、
         *           保存せずに画面遷移しようとしたときの処理
         * 制作日時：2015/06/10
         * 制作者：中山延雄
         * コメントアウト【//非常食ボタン押下時の処理はここまで】まで編集
         * ********************************************************************************************
         * 更新日時：2015/06/11
         * 更新者：中山延雄
         * 更新内容：大人子供幼児の総数が０の時、画面遷移しないようにした。
         * コメントアウト【//非常食ボタン押下時の処理はここまで】まで編集
         **********************************************************************************************/
        //ホームボタン押下時の処理
        Home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("Preferences",MODE_PRIVATE);
                int gou = pref.getInt("youji_people",0) +
                        pref.getInt("kobito_people",0) +
                        pref.getInt("otona_people",0);
                TextView debag_tv = (TextView)findViewById(R.id.debag);
                debag_tv.setText(toString().valueOf(gou));

                if(save_dialog()==true){//save_dialogクラスがtrueをreturnしてきたときの処理
                    AlertDialog.Builder fast = new AlertDialog.Builder(SubActivity.this);
                    fast.setTitle("※値が変更されている可能性があります※");//ここから
                    fast.setMessage("保存しなくてもよろしいですか？\n\n"+
                                    "※保存は右の保存ボタンより行えます。"
                    );//ここまでの　内容のダイアログ出力

                    //ここからはダイアログ内のはいorいいえ押下時の処理
                    fast.setPositiveButton("はい", new DialogInterface.OnClickListener() {//はい押下時
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences pref =
                                    getSharedPreferences("Preferences",MODE_PRIVATE);
                            Editor save_d = pref.edit();
                            save_d.putInt("save_d",0);
                            save_d.commit();//save_dに0を挿入

                            Intent intent=new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.setClassName("jp.co.nichiwa_system.application.Sonaechao","jp.co.nichiwa_system.application.Sonaechao.MainActivity");
                            startActivity(intent);//ホーム画面に遷移
                        }
                    });//ここまでがはい押下時の処理
                    fast.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {//いいえ押下時の処理
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();//ダイアログ消去
                        }
                    });//いいえ押下時の処理はここまで
                    fast.show();//ダイアログfast出力
                }//ここまでがtrue時の処理
                if(gou==0) {
                    //ダイアログの表示
                    AlertDialog.Builder fast = new AlertDialog.Builder(SubActivity.this);
                    fast.setTitle("※人数の合計が０です※");//ここから
                    fast.setMessage("人数が０だと備蓄を行えません！\n" +
                                    "人数の入力を行ってください。\n\n" +
                                    "例：家族構成等・・・"
                    );
                    fast.setPositiveButton("ok", new DialogInterface.OnClickListener() {//はい押下時
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    fast.show();
                }
                else{
                    Intent intent=new Intent();
                    intent.setClassName("jp.co.nichiwa_system.application.Sonaechao","jp.co.nichiwa_system.application.Sonaechao.MainActivity");
                    startActivity(intent);//ホーム画面に遷移
                }
            }
        });


        //備蓄品ボタン押下時の処理
        Stock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("Preferences",MODE_PRIVATE);
                int gou = pref.getInt("youji_people",0) +
                        pref.getInt("kobito_people",0) +
                        pref.getInt("otona_people",0);
                TextView debag_tv = (TextView)findViewById(R.id.debag);
                debag_tv.setText(toString().valueOf(gou));

                if(save_dialog()==true){//save_dialogクラスがtrueをreturnしてきたときの処理
                    AlertDialog.Builder fast = new AlertDialog.Builder(SubActivity.this);
                    fast.setTitle("※値が変更されている可能性があります※");//ここから
                    fast.setMessage("保存しなくてもよろしいですか？\n\n"+
                                    "※保存は右の保存ボタンより行えます。"
                    );//ここまでの　内容のダイアログ出力

                    //ここからはダイアログ内のはいorいいえ押下時の処理
                    fast.setPositiveButton("はい", new DialogInterface.OnClickListener() {//はい押下時
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences pref =
                                    getSharedPreferences("Preferences",MODE_PRIVATE);
                            Editor save_d = pref.edit();
                            save_d.putInt("save_d",0);
                            save_d.commit();//save_dに0を挿入

                            Intent intent=new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.setClassName("jp.co.nichiwa_system.application.Sonaechao","jp.co.nichiwa_system.application.Sonaechao.MainActivity");
                            startActivity(intent);//ホーム画面に遷移
                        }
                    });//ここまでがはい押下時の処理
                    fast.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {//いいえ押下時の処理
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();//ダイアログ消去
                        }
                    });//いいえ押下時の処理はここまで
                    fast.show();//ダイアログfast出力
                }//ここまでがtrue時の処理
                if(gou==0) {
                    //ダイアログの表示
                    AlertDialog.Builder fast = new AlertDialog.Builder(SubActivity.this);
                    fast.setTitle("※人数の合計が０です※");//ここから
                    fast.setMessage("人数が０だと備蓄を行えません！\n" +
                                    "人数の入力を行ってください。\n\n" +
                                    "例：家族構成等・・・"
                    );
                    fast.setPositiveButton("ok", new DialogInterface.OnClickListener() {//はい押下時
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    fast.show();
                }
                else{
                    Intent intent=new Intent();
                    intent.setClassName("jp.co.nichiwa_system.application.Sonaechao","jp.co.nichiwa_system.application.Sonaechao.MainActivity");
                    startActivity(intent);//ホーム画面に遷移
                }
            }
        });//ここまでが備蓄品ボタン押下時の処理


        //非常食ボタン押下時の処理
        hijousyoku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("Preferences",MODE_PRIVATE);
                int gou = pref.getInt("youji_people",0) +
                        pref.getInt("kobito_people",0) +
                        pref.getInt("otona_people",0);
                TextView debag_tv = (TextView)findViewById(R.id.debag);
                debag_tv.setText(toString().valueOf(gou));

                if(save_dialog()==true){//save_dialogクラスがtrueをreturnしてきたときの処理
                    AlertDialog.Builder fast = new AlertDialog.Builder(SubActivity.this);
                    fast.setTitle("※値が変更されている可能性があります※");//ここから
                    fast.setMessage("保存しなくてもよろしいですか？\n\n"+
                                    "※保存は右の保存ボタンより行えます。"
                    );//ここまでの　内容のダイアログ出力

                    //ここからはダイアログ内のはいorいいえ押下時の処理
                    fast.setPositiveButton("はい", new DialogInterface.OnClickListener() {//はい押下時
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences pref =
                                    getSharedPreferences("Preferences",MODE_PRIVATE);
                            Editor save_d = pref.edit();
                            save_d.putInt("save_d",0);
                            save_d.commit();//save_dに0を挿入

                            Intent intent=new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.setClassName("jp.co.nichiwa_system.application.Sonaechao","jp.co.nichiwa_system.application.Sonaechao.MainActivity");
                            startActivity(intent);//ホーム画面に遷移
                        }
                    });//ここまでがはい押下時の処理
                    fast.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {//いいえ押下時の処理
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();//ダイアログ消去
                        }
                    });//いいえ押下時の処理はここまで
                    fast.show();//ダイアログfast出力
                }//ここまでがtrue時の処理
                if(gou==0) {
                    //ダイアログの表示
                    AlertDialog.Builder fast = new AlertDialog.Builder(SubActivity.this);
                    fast.setTitle("※人数の合計が０です※");//ここから
                    fast.setMessage("人数が０だと備蓄を行えません！\n" +
                                    "人数の入力を行ってください。\n\n" +
                                    "例：家族構成等・・・"
                    );
                    fast.setPositiveButton("ok", new DialogInterface.OnClickListener() {//はい押下時
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    fast.show();
                }
                else{
                    Intent intent=new Intent();
                    intent.setClassName("jp.co.nichiwa_system.application.Sonaechao","jp.co.nichiwa_system.application.Sonaechao.MainActivity");
                    startActivity(intent);//ホーム画面に遷移
                }
            }
        });//非常食ボタン押下時の処理はここまで


        set.setOnClickListener(new View.OnClickListener() { // 設定ボタンを押した時の処理（説明ダイアログ）
            @Override
            public void onClick(View v) {
                /***処理***/
                AlertDialog.Builder fast = new AlertDialog.Builder(SubActivity.this);
                fast.setTitle("設定画面の説明");
                fast.setMessage("ここでは大人、小人、幼児等の設定ができます！\n" +
                                "人数やお知らせ期日、備えちゃお日数を設定して災害に備えちゃお！\n"
                );

                fast.setPositiveButton("次へ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog.dismiss();
                        AlertDialog.Builder fast = new AlertDialog.Builder(SubActivity.this);
                        fast.setTitle("設定画面の説明");
                        fast.setMessage("大人、小人、幼児の人数を入力できます。\n\n" +
                                "お知らせ期日は賞味期限何日前にお知らせをだすか設定できます。\n\n"+"備えちゃお日数は何日分の備蓄をするのか設定できます。\n\n"+
                                "※このメッセージは画面下の設定ボタンを押すと再び表示されます。");
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

        set.setBackgroundResource(R.drawable.style2);

        /**********************************************************************************************
         * 処理内容：保存ボタン押下時、大人、小人、幼児、お知らせ期日、備えちゃお日数を保存
         * 制作日時：2015/06/09
         * 制作者：中山延雄
         * コメントアウト【//保存ボタン押下時の挙動ここまで】まで編集
         **********************************************************************************************/
        //保存ボタン押下時の挙動
        final ImageView save = (ImageView)findViewById(R.id.save_Button);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder fast = new AlertDialog.Builder(SubActivity.this);
                fast.setTitle("※注意※");
                fast.setMessage("保存すると、ホームのメーターや必要数が増減する可能性があります。\n" +
                        "本当に保存しますか？");
                fast.setPositiveButton("はい", new DialogInterface.OnClickListener() {//はいボタン押下時
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences pref =
                                getSharedPreferences("Preferences",MODE_PRIVATE);
                        Editor save_d = pref.edit();
                        save_d.putInt("save_d",0);
                        save_d.commit();

                        saveInt( (EditText)findViewById(R.id.EditText5)  , "youji_people");//幼児保存
                        saveInt( (EditText)findViewById(R.id.EditText)  , "otona_people");//大人保存
                        saveInt( (EditText)findViewById(R.id.EditText2) , "kobito_people");//小人保存
                        saveInt( (EditText)findViewById(R.id.EditText3) , "kiniti_day");//お知らせ期日保存
                        saveInt( (EditText)findViewById(R.id.EditText4) , "sitei_day");//備えちゃお日数保存
                        dialog.dismiss();//ダイアログを閉じる


                        Toast.makeText(SubActivity.this, "保存しました", Toast.LENGTH_SHORT).show();//【保存しました】とトースト表示
                    }
                });
                fast.setNegativeButton("いいえ", new DialogInterface.OnClickListener(){//いいえボタン押下
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//ダイアログを閉じる
                        Toast.makeText(SubActivity.this, "保存をキャンセルしました", Toast.LENGTH_SHORT).show();//【保存をキャンセルしました】とトースト表示
                    }

                });
                fast.show();//ダイアログ【fast】出力
            }
        });//保存ボタン押下時の挙動ここまで

        //広告の設定
        AdView adview = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
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
            //alert.setTitle("値を選択してください");
            //アイテムの表示
            alert.setItems(item, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences pref =
                            getSharedPreferences("Preferences",MODE_PRIVATE);
                    Editor save_d = pref.edit();
                    save_d.putInt("save_d",1);
                    save_d.commit();

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
            SharedPreferences pref =
                    getSharedPreferences("Preferences",MODE_PRIVATE);
            Editor save_d = pref.edit();
            save_d.putInt("save_d",1);
            save_d.commit();

            if(intent == null) {
                    //ホームボタンの時アクティビティを閉じる
                    finish();
                }else{

                    //それ以外はアクティビティを表示
                    startActivity(intent);
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

    public boolean save_dialog(){
        SharedPreferences pref =
                getSharedPreferences("Preferences",MODE_PRIVATE);
        int save_d=pref.getInt("save_d",0);
        if(save_d>=1){//1度でも大人子供幼児設定期日を編集した場合
            return true;
        }
        else {//編集されてない場合
            return false;
        }
    }
}