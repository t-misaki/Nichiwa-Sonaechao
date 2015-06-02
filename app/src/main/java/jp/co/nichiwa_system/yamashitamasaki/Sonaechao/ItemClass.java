package jp.co.nichiwa_system.yamashitamasaki.Sonaechao;

import android.app.Activity;

/**
 * Created by yamashita.masaki on 2015/05/15.
 */
public class ItemClass {
    private int        Number;                    //番号
    private String      ItemName;              //アイテムの名前
    private String      prefName;              //プレファレンス名
    private int         Drawable_Location;   //画像の場所
    private String      Unit;                   //単位数
    private boolean    Calendar_flag;        //フラグ
    private Activity    act;                    //アクティビティ
    private int         Drawable_Icon;        //アイコン

    public ItemClass() {}

    public ItemClass(String ItemName, String prefName, int Drawable_Location, boolean Calendar_flag, String Unit, int Number , Activity act ) {
        this( ItemName, prefName, Drawable_Location, Calendar_flag, Unit , act );
        this.Number = Number;
    }


    public ItemClass(String ItemName, String prefName, int Drawable_Location, boolean Calendar_flag, String Unit, Activity act ) {
        this( ItemName, prefName, Drawable_Location, Calendar_flag, act );
        this.Unit = Unit;
    }

    public  ItemClass(String ItemName, String prefName, int Drawable_Location, boolean Calendar_flag,Activity act ) {
        this.ItemName = ItemName;
        this.prefName = prefName;
        this.Drawable_Location = Drawable_Location;
        this.Calendar_flag = Calendar_flag;
        this.act = act;
    }
    public void setIcon( int Drawable_Icon ) {
        this.Drawable_Icon = Drawable_Icon;
    }

    public int getNumber() { return Number; }
    public  String getName() { return ItemName; }
    public String getPrefName(){ return prefName; }
    public int getDrawable_Location(){ return Drawable_Location; }
    public String getUnit(){ return Unit; }
    public boolean getCalender_flag(){ return Calendar_flag; };
    public Activity getActivity(){ return act; }
    public int getIcon() {
        return Drawable_Icon;
    }

    public void setNumber( int Number ){
        this.Number = Number;
    }

}