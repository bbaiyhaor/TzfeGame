package com.example.administrator.tzfegame;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/17.
 */
public class GameView extends GridLayout {
    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    private void initGameView() {
        //  固定四列
        setColumnCount(4);
        //  设置背景颜色
        setBackgroundColor(0xffbbada0);

        setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeLeft();
                            } else if (offsetX > 5) {
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeUp();
                            } else if (offsetY > 5) {
                                swipeDown();
                            }
                        }
                        break;
                }
                //  只能返回 true
                return true;
            }
        });
    }

    //  当布局发生变化(比如手机旋转)可以知道当前宽高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //  onSizeChanged 会执行两次
        //  如果不判断 oldw 和 oldh 等于 0
        //  就会绘制图像两次, 出现 bug
        if ((oldw == 0) && (oldh == 0)) {
            int cardWidth = (Math.min(w, h) - 10) / 4;
            //  正方形
            addCards(cardWidth, cardWidth);
            startGame();
        }
    }

    public void addCards(int cardWidth, int cardHeight){
        Card c;
        for (int y = 0; y < 4; y++){
            for (int x = 0; x < 4; x++){
                c = new Card(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);
                cardsMap[x][y] = c;
            }
        }

    }

    private void startGame(){
        //  清空计分器
        MainActivity.getMainActivity().clearScore();
        for (int y = 0; y < 4; y++){
            for (int x = 0; x < 4; x++){
                cardsMap[x][y].setNum(0);
            }
        }

        //  添加随机数
        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum(){
        emptyPoints.clear();

        for (int y = 0; y < 4; y++){
            for (int x = 0; x < 4; x++){
                if (cardsMap[x][y].getNum() <= 0){
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

            Point p = emptyPoints.remove((int)(Math.random() * emptyPoints.size()));
            cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }
       private void swipeLeft(){
           boolean merge = false;
           for (int y = 0; y < 4; y++){
               for (int x = 0; x < 4; x++){

                   for (int x1 = x + 1; x1 < 4; x1++){
                       if (cardsMap[x1][y].getNum() > 0){

                           if (cardsMap[x][y].getNum() <= 0){
                               cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                               cardsMap[x1][y].setNum(0);
                               x--;
                               merge = true;
                           }else if (cardsMap[x][y].equals(cardsMap[x1][y])){
                               cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                               cardsMap[x1][y].setNum(0);
                               MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                               merge = true;
                           }
                           break;
                       }
                   }
               }
           }

           //   如果有移动就添加新的随机数
           if (merge){
               addRandomNum();
               checkComplete();
           }
       }

       private void swipeRight(){
           boolean merge = false;
           for (int y = 0; y < 4; y++){
               for (int x = 3; x >= 0; x--){

                   for (int x1 = x - 1; x1 >= 0; x1--){
                       if (cardsMap[x1][y].getNum() > 0){

                           if (cardsMap[x][y].getNum() <= 0){
                               cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                               cardsMap[x1][y].setNum(0);
                               x++;
                               merge = true;
                           }else if (cardsMap[x][y].equals(cardsMap[x1][y])){
                               cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                               cardsMap[x1][y].setNum(0);
                               MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                               merge = true;
                           }
                           break;
                       }
                   }
               }
           }
           if (merge){
               addRandomNum();
               checkComplete();
           }
       }

       private void swipeUp(){
           boolean merge = false;
           for (int x = 0; x < 4; x++){
               for (int y = 0; y < 4; y++){

                   for (int y1 = y + 1; y1 < 4; y1++){
                       if (cardsMap[x][y1].getNum() > 0){

                           if (cardsMap[x][y].getNum() <= 0){
                               cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                               cardsMap[x][y1].setNum(0);
                               y--;
                               merge = true;
                           }else if (cardsMap[x][y].equals(cardsMap[x][y1])){
                               cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                               cardsMap[x][y1].setNum(0);
                               MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                               merge = true;
                           }
                           break;
                       }
                   }
               }
           }
           if (merge){
               addRandomNum();
               checkComplete();
           }
       }

       private void swipeDown(){
           boolean merge = false;
           for (int x = 0; x < 4; x++){
               for (int y = 3; y >= 0; y--){

                   for (int y1 = y +-1; y1 >= 0; y1--){
                       if (cardsMap[x][y1].getNum() > 0){

                           if (cardsMap[x][y].getNum() <= 0){
                               cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                               cardsMap[x][y1].setNum(0);
                               y++;
                               merge = true;
                           }else if (cardsMap[x][y].equals(cardsMap[x][y1])){
                               cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                               cardsMap[x][y1].setNum(0);
                               MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                               merge = true;
                           }
                           break;
                       }
                   }
               }
           }
           if (merge){
               addRandomNum();
               checkComplete();
           }
       }

    private boolean haveRepeated(int x, int y){
        if ((x > 0) && (cardsMap[x][y].equals(cardsMap[x - 1][y])))
            return  true;
        if ((x < 3) && (cardsMap[x][y].equals(cardsMap[x + 1][y])))
            return  true;
        if ((y > 0) && (cardsMap[x][y].equals(cardsMap[x][y - 1])))
            return  true;
        if ((y < 3) && (cardsMap[x][y].equals(cardsMap[x][y + 1])))
            return  true;
        return false;
    }

    private void checkComplete(){
        boolean complete = true;

        ALL:
        for (int y = 0; y < 4; y++){
            for (int x = 0; x < 4; x++){
                if ((cardsMap[x][y].getNum() == 0) ||
                    haveRepeated(x, y)) {
                    complete = false;
                    break ALL;
                }
            }
        }
        if (complete){
            new AlertDialog.Builder(getContext()).setTitle("你好！").setMessage("游戏结束")
                    .setPositiveButton("重来", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    }).show();
        }
    }
    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();
}








