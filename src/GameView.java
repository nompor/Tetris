import java.util.ArrayList;
import java.util.Collections;

import com.nompor.gtk.GTK;
import com.nompor.gtk.GTKColor;
import com.nompor.gtk.GTKFont;
import com.nompor.gtk.GTKView;
import com.nompor.gtk.draw.GTKGraphics;

public class GameView implements GTKView {


	GTKColor clr = GTK.createIntColor(0,80,150);
	GTKColor clr2 = GTK.createIntColor(0,0,0);
	GTKColor clr3 = GTK.createIntColor(255,255,255);

	GTKFont f1 = GTK.createFont(15);
	GTKFont f2 = GTK.createFont(40);

	//フィールドの広さ
	final int HEIGHT = 20;
	final int WIDTH = 10;
	//デバッグ用に配列宣言は直書きにしている
	int[][] fields = {
			 {0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}//5
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}//10
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}//15
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}
			,{0,0,0,0,0,0,0,0,0,0}//20
	};
	static final int RED = 1;
	static final int SKYBLUE = 2;
	static final int BLUE = 3;
	static final int YELLOW = 4;
	static final int GREEN = 5;
	static final int ORANGE = 6;
	static final int PINK = 7;

	final int exox = GTK.getWidth() / 2 - 250;
	final int exoy = GTK.getHeight() / 2 - 300;
	final int ox = 125 + exox;
	final int oy = 50 + exoy;

	ArrayList<TetriMino> nextMinos = new ArrayList<>();
	ArrayList<TetriMino> minos = new ArrayList<>();
	TetriMino currentMino = null;
	TetriMino hold = null;
	int x,y;
	int rotate;
	int fallFrame;
	int highFallFrame;
	int moveFrame;
	boolean isHardDropMode;
	boolean isAlreadyHold;
	boolean isGameOver;

	public GameView() {
		//定義したミノの種類をリストに登録していく

		//I
		minos.add(new TetriMino(new int[][][] {
			{
				{0,0,0,0}
				,{1,1,1,1}
				,{0,0,0,0}
				,{0,0,0,0}
			}
			,{
				{0,1,0,0}
				,{0,1,0,0}
				,{0,1,0,0}
				,{0,1,0,0}
			}
		}, SKYBLUE));

		//L
		minos.add(new TetriMino(new int[][][] {
			{
				 {0,0,0,0}
				,{0,1,1,1}
				,{0,1,0,0}
				,{0,0,0,0}
			}
			,{
				 {0,0,0,0}
				,{0,1,1,0}
				,{0,0,1,0}
				,{0,0,1,0}
			}
			,{
				 {0,0,0,0}
				,{0,0,1,0}
				,{1,1,1,0}
				,{0,0,0,0}
			}
			,{
				 {0,1,0,0}
				,{0,1,0,0}
				,{0,1,1,0}
				,{0,0,0,0}
			}
		}, ORANGE));

		//J
		minos.add(new TetriMino(new int[][][] {
			{
				 {0,0,0,0}
				,{0,1,0,0}
				,{0,1,1,1}
				,{0,0,0,0}
			}
			,{
				 {0,0,0,0}
				,{0,1,1,0}
				,{0,1,0,0}
				,{0,1,0,0}
			}
			,{
				 {0,0,0,0}
				,{1,1,1,0}
				,{0,0,1,0}
				,{0,0,0,0}
			}
			,{
				 {0,0,1,0}
				,{0,0,1,0}
				,{0,1,1,0}
				,{0,0,0,0}
			}
		}, BLUE));

		//T
		minos.add(new TetriMino(new int[][][] {
			{
				 {1,1,1}
				,{0,1,0}
				,{0,0,0}
			}
			,{
				 {0,1,0}
				,{1,1,0}
				,{0,1,0}
			}
			,{
				 {0,1,0}
				,{1,1,1}
				,{0,0,0}
			}
			,{
				 {0,1,0}
				,{0,1,1}
				,{0,1,0}
			}
		}, PINK));

		//O
		minos.add(new TetriMino(new int[][][] {
			{
				 {1,1}
				,{1,1}
			}
		}, YELLOW));

		//S
		minos.add(new TetriMino(new int[][][] {
			{
				 {0,1,1}
				,{1,1,0}
				,{0,0,0}
			}
			,{
				 {1,0,0}
				,{1,1,0}
				,{0,1,0}
			}
		}, RED));

		//Z
		minos.add(new TetriMino(new int[][][] {
			{
				 {1,1,0}
				,{0,1,1}
				,{0,0,0}
			}
			,{
				 {0,1,0}
				,{1,1,0}
				,{1,0,0}
			}
		}, GREEN));

		//テトリミノ全種をシャッフルし、二巡分だけnextに挿入しておく
		Collections.shuffle(minos);
		minos.stream().forEach(nextMinos::add);
		Collections.shuffle(minos);
		minos.stream().forEach(nextMinos::add);

		//現在のテトリミノを取得
		currentMino = nextMinos.remove(0);

		//テトリミノ出現座標
		x = 4;
		y = 0;
	}

	public void process() {
		Background.update();

		if ( currentMino == null ) return;

		//ゲームオーバーならゲームの処理をしない
		if ( isGameOver ) {

			//特定のキー押下でタイトルに戻る
			if ( KeyAction.isActive(KeyAction.R_ROTATE) || KeyAction.isActive(KeyAction.HOLD) ) {
				GTK.changeViewDefaultAnimation(new TitleView());
			}
			return;
		}

		int yy = y;
		int xx = x;
		int rr = rotate;

		//ハードドロップ
		if ( KeyAction.isActive(KeyAction.HARDDROP) || isHardDropMode ) {
			isHardDropMode = true;
			fallFrame = 0;
			yy++;
		} else {

			boolean isDown =  KeyAction.isDown(KeyAction.DOWN);

			//右回転
			if ( KeyAction.isActive(KeyAction.R_ROTATE) ) {
				rr++;
			}
			//左回転
			else if ( KeyAction.isActive(KeyAction.L_ROTATE) ) {
				if (rr > 0) {
					rr--;
				} else {
					rr = currentMino.minos.length - 1;
				}
			}
			//左移動
			else if ( KeyAction.isActive(KeyAction.LEFT) ) {
				xx--;
				if ( moveFrame != 0 ) {
					moveFrame = 0;
				}
			}
			//右移動
			else if ( KeyAction.isActive(KeyAction.RIGHT) ) {
				xx++;
				if ( moveFrame != 0 ) {
					moveFrame = 0;
				}
			}
			//左押し続け
			else if ( KeyAction.isDown(KeyAction.LEFT) ) {
				moveFrame--;
				if ( moveFrame <= -12 ) {
					if ( moveFrame % 2 == 0 ) {
						xx--;
					}
				}
			}
			//右押し続け
			else if ( KeyAction.isDown(KeyAction.RIGHT) ) {
				moveFrame++;
				if ( moveFrame >= 12 ) {
					if ( moveFrame % 2 == 0 ) {
						xx++;
					}
				}
			}
			//HOLD
			else if ( KeyAction.isActive(KeyAction.HOLD) && !isAlreadyHold ) {
				if ( hold != null ) {
					nextMinos.add(0, hold);
				}
				isAlreadyHold = true;
				hold = currentMino;
				nextInit();
				return;
			}
			else {
				//下移動
				if ( isDown ) {
					if ( highFallFrame == 0 ) {
						yy++;
					}
					highFallFrame++;
					if ( highFallFrame > 2 ) {
						highFallFrame = 0;
					}
					fallFrame = 0;
				}
			}
			if ( !isDown ) {
				highFallFrame = 0;
			}
		}

		//自由落下の処理
		fallFrame++;
		if ( fallFrame >= 60 ) {
			fallFrame = 0;
			if ( yy+1 >= fields.length ) {
				yy = 0;
			} else {
				yy++;
			}
		}

		//yが加算されたときは配置完了判定
		int[][][] minos = currentMino.minos;
		int r = rr % minos.length;
		boolean isUpdate = true;
		label:
		for ( int i = 0;i < minos[r].length;i++ ) {
			for ( int j = 0;j < minos[r][i].length;j++ ) {
				if ( minos[r][i][j] != 0 ) {
					int fx = xx + j;
					int fy = yy + i;
					if ( fy >= HEIGHT || fx < 0 || fx >= WIDTH || fields[fy][fx] != 0 ) {
						isUpdate = false;
						break label;
					}
				}
			}
		}

		//座標更新処理
		if (isUpdate) {
			x = xx;
			y = yy;
			rotate = rr;
		} else {
			//移動不能でyが加算されたときは配置完了判定
			if ( yy > y && xx == x && rotate == rr ) {
				for ( int i = 0;i < minos[r].length;i++ ) {
					for ( int j = 0;j < minos[r][i].length;j++ ) {
						int fx = x + j;
						int fy = y + i;
						if ( minos[r][i][j] != 0 ) fields[fy][fx] = currentMino.color;
					}
				}

				//フィールドをチェックし、揃ったラインを削除する
				for ( int i = 0;i < fields.length;i++ ) {
					int cnt = 0;
					//ブロックが置かれている数をカウント
					for ( int j = 0;j < fields[i].length;j++ ) {
						if ( fields[i][j] == 0 ) {
							break;
						}
						cnt++;
					}
					//ラインが揃っていたら削除して上にあるテトリミノをすべて一段下へずらす
					if ( cnt == WIDTH ) {
						//ライン削除
						for ( int j = 0;j < fields[i].length;j++ ) {
							fields[i][j] = 0;
						}

						//上にあるテトリミノをすべて一段下へずらす
						for ( int k = i - 1;k >= 0;k-- ) {
							for ( int j = 0;j < fields[i].length;j++ ) {
								fields[k+1][j] = fields[k][j];
							}
						}
					}
				}

				nextInit();
				isAlreadyHold = false;
			}
		}
		if ( isHardDropMode ) {
			process();
		}
	}

	private void nextInit() {
		isHardDropMode = false;
		highFallFrame = 0;
		fallFrame = 0;
		rotate = 0;
		x = 4;
		y = 0;
		currentMino = nextMinos.remove(0);

		//出現予定テトリミノが減ってきたらシャッフルして次の一巡分を挿入
		if ( nextMinos.size() < 7 ) {
			Collections.shuffle(this.minos);
			this.minos.stream().forEach(nextMinos::add);
		}

		//1列目か2列目にブロックがあった時点でゲームオーバーにする
		for ( int i = 0;i < 2;i++ ) {
			for ( int j = 0;j < fields[0].length;j++ ) {
				if ( fields[i][j] != 0 ) {
					isGameOver = true;
				}
			}
		}
	}


	@Override
	public void draw(GTKGraphics g) {
		if ( currentMino == null ) return;
		g.setFont(f1);

		//背景類の描画
		Background.draw(g);
		g.setColor(clr);
		g.fillRect(exox,exoy,500,600);

		//フィールドの描画
		g.setColor(clr2);
		g.fillRect(ox,oy,250,500);
		for ( int i = 0;i < fields.length;i++ ) {
			for ( int j = 0;j < fields[i].length;j++ ) {
				b_draw_idx(g,j,i,fields[i][j]);
			}
		}

		//HOLD領域の描画
		g.setColor(clr3);
		g.drawString("HOLD", exox+10,exoy+80);
		g.setColor(clr2);
		g.fillRect(exox+10,exoy+100,100,80);
		if ( hold != null ) {
			for ( int i = 0;i < hold.minos[0].length;i++ ) {
				for ( int j = 0;j < hold.minos[0][i].length;j++ ) {
					if ( hold.minos[0][i][j] != 0 ) {
						b_draw(
								g
								,j*TetriMino.BLOCK_SIZE+exox+10+50-(hold.initW+1)*TetriMino.BLOCK_SIZE/2-hold.initX*TetriMino.BLOCK_SIZE
								,i*TetriMino.BLOCK_SIZE+exoy+100+40-(hold.initH+1)*TetriMino.BLOCK_SIZE/2-hold.initY*TetriMino.BLOCK_SIZE
								,hold.color
						);
					}
				}
			}
		}

		//NEXT領域の描画
		g.setColor(clr3);
		g.drawString("NEXT", exox+390,exoy+80);
		g.setColor(clr2);
		for ( int k = 0;k < 4;k++ ) {
			int nextXOff = exox+390;
			int nextYOff = exoy+100+100*k;
			g.fillRect(nextXOff,nextYOff,100,80);
			TetriMino mino = nextMinos.get(k);
			int[][][] minos = mino.minos;
			for ( int i = 0;i < minos[0].length;i++ ) {
				for ( int j = 0;j < minos[0][i].length;j++ ) {
					if ( minos[0][i][j] != 0 ) {
						b_draw(
								g
								,j*TetriMino.BLOCK_SIZE+nextXOff+50-(mino.initW+1)*TetriMino.BLOCK_SIZE/2-mino.initX*TetriMino.BLOCK_SIZE
								,i*TetriMino.BLOCK_SIZE+nextYOff+40-(mino.initH+1)*TetriMino.BLOCK_SIZE/2-mino.initY*TetriMino.BLOCK_SIZE
								,mino.color
						);
					}
				}
			}
		}

		//現在落下中のミノの描画
		int[][][] minos = currentMino.minos;
		int r = rotate % minos.length;
		for ( int i = 0;i < minos[r].length;i++ ) {
			for ( int j = 0;j < minos[r][i].length;j++ ) {
				if ( minos[rotate % minos.length][i][j] != 0 ) {
					b_draw_idx(g,x+j,y+i,currentMino.color);
				}
			}
		}

		//ゲームオーバー描画
		if ( isGameOver ) {
			g.setColor(clr2);
			g.setAlpha(0.5);
			g.fillRect(ox,oy,250,500);
			g.setColor(clr3);
			g.setAlpha(1);
			String tx = "GAMEOVER";
			g.setFont(f2);
			int w = (int) g.stringWidth(tx);
			int h = (int) g.stringHeight(tx);
			g.drawString(tx, ox+125-w/2, oy+250-h/2);
		}

	}

	//ブロック描画
	private void b_draw_idx(GTKGraphics g, int x, int y, int color) {
		b_draw(g, x*TetriMino.BLOCK_SIZE+ox, y*TetriMino.BLOCK_SIZE+oy,color);
	}

	private void b_draw(GTKGraphics g, int x, int y, int color) {
		switch(color) {
		case RED:g.drawImage(Resource.b_red, x, y);break;
		case SKYBLUE:g.drawImage(Resource.b_skyblue, x, y);break;
		case BLUE:g.drawImage(Resource.b_blue, x, y);break;
		case YELLOW:g.drawImage(Resource.b_yellow, x, y);break;
		case GREEN:g.drawImage(Resource.b_green, x, y);break;
		case ORANGE:g.drawImage(Resource.b_orange, x, y);break;
		case PINK:g.drawImage(Resource.b_pink, x, y);break;
		}
	}
}
