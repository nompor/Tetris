import java.util.ArrayList;
import java.util.Collections;

import com.nompor.gtk.APIType;
import com.nompor.gtk.GTK;
import com.nompor.gtk.GTKColor;
import com.nompor.gtk.GTKView;
import com.nompor.gtk.draw.GTKGraphics;

public class Test5 {

	public static void main(String[] args) {
		GTKView gv = new GTKView() {

			//テトリミノ（ブロックの塊）
			class Mino{

				//1次元目=回転、2次元目=縦、3次元目=横
				int[][][] minos;

				Mino(int[][][] minos){
					this.minos = minos;
				}
			}

			static final int BLOCK_SIZE=25;//ブロックのピクセルサイズ
			static final int W=10,H=20;//フィールドの大きさ10×20
			GTKColor black,gray,white;
			Mino currentMino = null;
			Mino hold = null;
			ArrayList<Mino> nextMinos = new ArrayList<>();
			ArrayList<Mino> minos = new ArrayList<>();
			int[][] fields = new int[H][W];
			int fallFrame;
			int fallFrameCheck;
			int x,y,r;
			boolean isAlreadyHold;
			boolean isHarddrop;

			public void start() {
				black = GTK.createIntColor(0, 0, 0);
				gray = GTK.createColor(0.5, 0.5, 0.5);
				white = GTK.createColor(1, 1, 1);

				//定義したミノの種類をリストに登録していく

				//I
				minos.add(new Mino(new int[][][] {
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
				}));

				//L
				minos.add(new Mino(new int[][][] {
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
				}));

				//J
				minos.add(new Mino(new int[][][] {
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
				}));

				//T
				minos.add(new Mino(new int[][][] {
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
				}));

				//O
				minos.add(new Mino(new int[][][] {
					{
						 {1,1}
						,{1,1}
					}
				}));

				//S
				minos.add(new Mino(new int[][][] {
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
				}));

				//Z
				minos.add(new Mino(new int[][][] {
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
				}));

				//テトリミノ全種をシャッフルし、二巡分だけnextに挿入しておく
				Collections.shuffle(minos);
				minos.stream().forEach(nextMinos::add);
				Collections.shuffle(minos);
				minos.stream().forEach(nextMinos::add);

				//現在のテトリミノを取得
				currentMino = nextMinos.remove(0);
			}

			public void process() {
				if ( currentMino == null ) return;
				int yy = y;
				int xx = x;
				int rr = r;

				if (KeyAction.isActive(KeyAction.HARDDROP) || isHarddrop) {
					isHarddrop = true;
					yy++;
				} else if ( KeyAction.isActive(KeyAction.RIGHT) ) {
					xx++;
				} else if (KeyAction.isActive(KeyAction.LEFT)) {
					xx--;
				} else if (KeyAction.isActive(KeyAction.R_ROTATE)) {
					rr++;
					if ( rr>=currentMino.minos.length ) {
						rr = 0;
					}
				} else if (KeyAction.isActive(KeyAction.L_ROTATE)) {
					rr--;
					if ( rr<0 ) {
						rr = currentMino.minos.length - 1;
					}
				//HOLD
				}else if ( KeyAction.isActive(KeyAction.HOLD) && !isAlreadyHold ) {
						if ( hold != null ) {
							nextMinos.add(0, hold);
						}
						isAlreadyHold = true;
						hold = currentMino;
						nextInit();
						return;
				}

				if ( KeyAction.isDown(KeyAction.DOWN) ) {
					fallFrameCheck = 2;
				} else {
					fallFrameCheck = 60;
				}

				//自由落下の処理
				fallFrame++;
				if ( fallFrame >= fallFrameCheck ) {
					fallFrame = 0;
					yy++;
				}


				//yが加算されたときは配置完了判定
				int[][][] minos = currentMino.minos;
				int rrr = rr % minos.length;
				boolean isUpdate = true;
				label:
				for ( int i = 0;i < minos[rrr].length;i++ ) {
					for ( int j = 0;j < minos[rrr][i].length;j++ ) {
						if ( minos[rrr][i][j] != 0 ) {
							int fx = xx + j;
							int fy = yy + i;
							//移動、回転後にブロックがあれば更新不可能フラグを立てる
							if ( fy >= H || fx < 0 || fx >= W || fields[fy][fx] != 0 ) {
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
					r = rr;
				} else {
					//移動不能でyが加算されたときは配置完了判定
					if ( yy > y && xx == x && rrr == rr ) {
						for ( int i = 0;i < minos[rrr].length;i++ ) {
							for ( int j = 0;j < minos[rrr][i].length;j++ ) {
								int fx = x + j;
								int fy = y + i;
								//ブロックをフィールドに置く
								if ( minos[rrr][i][j] != 0 ) fields[fy][fx] = 1;
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
							if ( cnt == W ) {
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
						isHarddrop = false;
					}
				}
				if ( isHarddrop ) process();
			}

			public void draw(GTKGraphics g) {
				if ( currentMino == null ) return;

				//画面の描画
				g.setColor(black);
				g.fillRect(0, 0, W*BLOCK_SIZE, GTK.getHeight());
				g.setColor(gray);
				g.fillRect(W*BLOCK_SIZE, 0, GTK.getWidth() - W*BLOCK_SIZE, GTK.getHeight());
				for ( int i = 0;i < fields.length;i++ ) {
					for ( int j = 0;j < fields[i].length;j++ ) {
						//ブロックの描画
						if ( fields[i][j] != 0 ) {
							g.drawImage(Resource.b_green, j*BLOCK_SIZE, i*BLOCK_SIZE);
						}
					}
				}


				//HOLD領域の描画
				g.setColor(white);
				g.drawString("HOLD", 300,30);
				if ( hold != null ) {
					int nextXOff = 300;
					int nextYOff = 50;
					for ( int i = 0;i < hold.minos[0].length;i++ ) {
						for ( int j = 0;j < hold.minos[0][i].length;j++ ) {
							if ( hold.minos[0][i][j] != 0 ) {
								g.drawImage(
										Resource.b_orange
										,nextXOff+j*BLOCK_SIZE
										,nextYOff+i*BLOCK_SIZE
								);
							}
						}
					}
				}

				//NEXT領域の描画
				g.setColor(white);
				g.drawString("NEXT", 300, 180);
				for ( int k = 0;k < 3;k++ ) {
					int nextXOff = 300;
					int nextYOff = 200+k*100;
					Mino mino = nextMinos.get(k);
					int[][][] minos = mino.minos;
					for ( int i = 0;i < minos[0].length;i++ ) {
						for ( int j = 0;j < minos[0][i].length;j++ ) {
							if ( minos[0][i][j] != 0 ) {
								g.drawImage(
										Resource.b_orange
										,nextXOff+j*BLOCK_SIZE
										,nextYOff+i*BLOCK_SIZE
								);
							}
						}
					}
				}

				//現在落下中のミノの描画
				for ( int i = 0;i < currentMino.minos[r].length;i++ ) {
					for ( int j = 0;j < currentMino.minos[0][i].length;j++ ) {
						if ( currentMino.minos[r][i][j] != 0 ) {
							g.drawImage(Resource.b_orange,j*BLOCK_SIZE+x*BLOCK_SIZE, i*BLOCK_SIZE+y*BLOCK_SIZE);
						}
					}
				}
			}

			private void nextInit() {

				r = 0;
				x = 4;
				y = 0;
				currentMino = nextMinos.remove(0);

				//出現予定テトリミノが減ってきたらシャッフルして次の一巡分を挿入
				if ( nextMinos.size() < 7 ) {
					Collections.shuffle(this.minos);
					this.minos.stream().forEach(nextMinos::add);
				}
			}
		};

		GTK.start("TETRIS", 500, 500, new GTKView() {
			public void start() {
				Resource.load();
				KeyAction.initKey();
				GTK.runLater(()->{
					GTK.changeView(gv);
				});
			}
		},APIType.SWING);
	}
}
