package com.example.carrotstreetpuzzle;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.content.Context;
import java.util.*;

public class MainActivity extends Activity {
 @Override public void onCreate(Bundle b){super.onCreate(b); getWindow().setStatusBarColor(Color.rgb(7,18,10)); setContentView(new GameView(this));}
}

class GameView extends View {
 Paint p=new Paint(3); Random rnd=new Random(4); int cols=9, rows=11, level=1, score=0; int px=1,py=9; float cell; boolean won=false; long lastMove=0;
 boolean[][] wall=new boolean[rows][cols]; boolean[][] carrot=new boolean[rows][cols];
 ArrayList<Point> cars=new ArrayList<>(); int[][] dirs={{1,0},{-1,0},{0,1},{0,-1}};
 float downX,downY;
 GameView(Context c){super(c); p.setTypeface(Typeface.create("sans",Typeface.BOLD)); newLevel(); setFocusable(true);}
 void newLevel(){won=false; px=1;py=rows-2; for(int y=0;y<rows;y++)for(int x=0;x<cols;x++){wall[y][x]=false;carrot[y][x]=false;}
  // border
  for(int x=0;x<cols;x++){wall[0][x]=true;wall[rows-1][x]=true;} for(int y=0;y<rows;y++){wall[y][0]=true;wall[y][cols-1]=true;}
  // deterministic maze-like streets
  for(int y=2;y<rows-2;y+=2) for(int x=2;x<cols-1;x++) if((x+y+level)%4!=0) wall[y][x]=true;
  for(int x=3;x<cols-2;x+=3) for(int y=1;y<rows-1;y++) if((x*y+level)%5==0) wall[y][x]=true;
  wall[py][px]=false; wall[1][cols-2]=false;
  int n=5+level; for(int i=0;i<n;i++){int x,y; do{x=1+rnd.nextInt(cols-2);y=1+rnd.nextInt(rows-2);}while(wall[y][x]||(x==px&&y==py)||carrot[y][x]);carrot[y][x]=true;}
  cars.clear(); for(int i=0;i<Math.min(2+level/2,4);i++){int x,y; do{x=1+rnd.nextInt(cols-2);y=1+rnd.nextInt(rows-2);}while(wall[y][x]||(x==px&&y==py)||carrot[y][x]);cars.add(new Point(x,y));}
  invalidate(); }
 protected void onDraw(Canvas c){super.onDraw(c); float w=getWidth(),h=getHeight(); c.drawColor(Color.rgb(6,15,9)); cell=Math.min(w/cols,(h-190)/rows); float ox=(w-cols*cell)/2f, oy=35;
  p.setTextAlign(Paint.Align.CENTER); p.setTypeface(Typeface.DEFAULT_BOLD); p.setTextSize(25); p.setColor(Color.WHITE); c.drawText("CARROT STREET",w/2,27,p);
  for(int y=0;y<rows;y++)for(int x=0;x<cols;x++){float l=ox+x*cell,t=oy+y*cell; p.setStyle(Paint.Style.FILL);p.setColor(wall[y][x]?Color.rgb(43,61,45):Color.rgb(27,39,29));c.drawRect(l+2,t+2,l+cell-2,t+cell-2,p); if(!wall[y][x]){p.setColor(Color.rgb(62,91,65));p.setStrokeWidth(1);p.setStyle(Paint.Style.STROKE);c.drawRect(l+2,t+2,l+cell-2,t+cell-2,p);p.setStyle(Paint.Style.FILL);}}
  // carrots
  for(int y=0;y<rows;y++)for(int x=0;x<cols;x++)if(carrot[y][x])drawCarrot(c,ox+x*cell+cell/2,oy+y*cell+cell/2,cell*.35f);
  // cars as moving hazards
  for(Point q:cars) drawCar(c,ox+q.x*cell+cell/2,oy+q.y*cell+cell/2,cell*.72f);
  drawPlayer(c,ox+px*cell+cell/2,oy+py*cell+cell/2,cell*.7f);
  // bottom controls
  float cy=h-75, bx=w/2, s=58; drawButton(c,bx,cy-s*.95f,"▲",s);drawButton(c,bx,cy+s*.95f,"▼",s);drawButton(c,bx-s*1.25f,cy,"◀",s);drawButton(c,bx+s*1.25f,cy,"▶",s);
  p.setColor(Color.WHITE);p.setTextSize(18);p.setTextAlign(Paint.Align.LEFT);c.drawText("Уровень "+level+"   Морковки: "+countCarrots()+"   Счёт: "+score,16,h-12,p);
  if(won){p.setColor(0xCC06100A);c.drawRect(0,0,w,h,p);p.setColor(Color.WHITE);p.setTextAlign(Paint.Align.CENTER);p.setTextSize(34);c.drawText("УРОВЕНЬ ПРОЙДЕН!",w/2,h/2-20,p);p.setTextSize(20);c.drawText("Нажми, чтобы продолжить",w/2,h/2+25,p);}
 }
 int countCarrots(){int n=0;for(int y=0;y<rows;y++)for(int x=0;x<cols;x++)if(carrot[y][x])n++;return n;}
 void drawCarrot(Canvas c,float x,float y,float s){p.setColor(0xFFFF7A22);Path q=new Path();q.moveTo(x,y+s*.65f);q.lineTo(x-s*.35f,y-s*.25f);q.quadTo(x,y-s*.55f,x+s*.35f,y-s*.25f);q.close();c.drawPath(q,p);p.setColor(0xFF63D66A);p.setStrokeWidth(s*.18f);c.drawLine(x,y-s*.2f,x-s*.05f,y-s*.62f,p);c.drawLine(x,y-s*.2f,x+s*.22f,y-s*.55f,p);}
 void drawPlayer(Canvas c,float x,float y,float s){p.setColor(0xFF6FCF65);c.drawCircle(x,y,s*.48f,p);p.setColor(Color.WHITE);c.drawCircle(x-s*.17f,y-s*.08f,s*.07f,p);c.drawCircle(x+s*.17f,y-s*.08f,s*.07f,p);p.setColor(0xFF1B281B);c.drawCircle(x-s*.17f,y-s*.08f,s*.035f,p);c.drawCircle(x+s*.17f,y-s*.08f,s*.035f,p);}
 void drawCar(Canvas c,float x,float y,float s){p.setColor(0xFF4A8ED8);c.drawRoundRect(x-s*.5f,y-s*.3f,x+s*.5f,y+s*.3f,s*.12f,s*.12f,p);p.setColor(0xFFBEE8FF);c.drawRect(x-s*.25f,y-s*.2f,x+s*.25f,y+s*.02f,p);}
 void drawButton(Canvas c,float x,float y,String t,float s){p.setColor(0xFF315A37);c.drawCircle(x,y,s/2,p);p.setColor(Color.WHITE);p.setTextSize(26);p.setTextAlign(Paint.Align.CENTER);c.drawText(t,x,y+9,p);}
 public boolean onTouchEvent(android.view.MotionEvent e){if(e.getAction()==0){downX=e.getX();downY=e.getY();return true;}if(e.getAction()==1){float dx=e.getX()-downX,dy=e.getY()-downY;if(won){level++;newLevel();return true;}int mx=0,my=0;if(Math.max(Math.abs(dx),Math.abs(dy))>25){if(Math.abs(dx)>Math.abs(dy))mx=dx>0?1:-1;else my=dy>0?1:-1;}else{float cx=getWidth()/2f,cy=getHeight()-75;if(Math.abs(e.getX()-cx)<50&&e.getY()<cy)my=-1;else if(Math.abs(e.getX()-cx)<50&&e.getY()>cy)my=1;else if(e.getX()<cx)mx=-1;else mx=1;}move(mx,my);return true;}return true;}
 void move(int dx,int dy){if(System.currentTimeMillis()-lastMove<90)return;lastMove=System.currentTimeMillis();int nx=px+dx,ny=py+dy;if(nx<0||nx>=cols||ny<0||ny>=rows||wall[ny][nx])return;for(Point q:cars)if(q.x==nx&&q.y==ny)return;px=nx;py=ny;if(carrot[py][px]){carrot[py][px]=false;score+=10;}if(countCarrots()==0){won=true;}invalidate();}
}
