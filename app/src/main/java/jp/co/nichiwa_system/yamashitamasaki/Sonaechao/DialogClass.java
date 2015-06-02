package jp.co.nichiwa_system.yamashitamasaki.Sonaechao;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

/**
 * ダイアログを表示するクラス
  Created by yamashita.masaki on 2015/05/15.
 */
public class DialogClass extends AlertDialog.Builder{

    TextView tv;

    //コンストラクタ
    public DialogClass(String TitleName, String Message, Activity act)
    {
        super(act);
        setTitle(TitleName);

        //テキストビューの生成
        tv = new TextView(act);
        tv.setText(Message);

        //
        setView(tv);
    }

    public DialogClass(Activity act) {
        super(act);
        tv = new TextView(act);
    }

    public void setText(String str) {
        setMessage(str);
    }

    public void Diarog_show() {
        //
        setCancelable(true);
        AlertDialog alertDialog = this.create();
        //表示
        alertDialog.show();
    }

}
