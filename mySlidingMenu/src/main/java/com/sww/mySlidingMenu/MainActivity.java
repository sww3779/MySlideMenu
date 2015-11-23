package com.sww.mySlidingMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.sww.mySlidingMenu.view.SlideMenu;

public class MainActivity extends Activity implements View.OnClickListener {

    private SlideMenu slideMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        slideMenu = (SlideMenu) findViewById(R.id.slidemenu);
        findViewById(R.id.ib_back).setOnClickListener(this);

    }

    public void clickTab(View v){
        TextView tv= (TextView) v;
        Toast.makeText(MainActivity.this, tv.getText(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {
        boolean isShowMenu=slideMenu.isShowMenu();
        if(isShowMenu){
            slideMenu.hideMenu();
        }else{
            slideMenu.showMenu();
        }
    }
}
