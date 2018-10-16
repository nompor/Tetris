import java.util.ArrayList;
import java.util.Collections;

import com.nompor.gtk.APIType;
import com.nompor.gtk.GTK;
import com.nompor.gtk.GTKColor;
import com.nompor.gtk.GTKView;
import com.nompor.gtk.draw.GTKGraphics;

public class Test4 {

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
			GTKColor black;
			Mino currentMino = null;
			ArrayList<Mino> nextMinos = new ArrayList<>();
			ArrayList<Mino> minos = new ArrayList<>();
			int[][] fields = new int[H][W];
			int fallFrame;
			int fallFrameCheck;
			int x,y,r;

			public void start() {
				black = GTK.createIntColor(0, 0, 0);


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

			public void draw(GTKGraphics g) {
				if ( currentMino == null ) return;
				int yy = y;
				int xx = x;
				int rr = r;

				if ( KeyAction.isActive(KeyAction.RIGHT) ) {
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


				//画面の描画
				g.setColor(black);
				g.fillRect(0, 0, GTK.getWidth(), GTK.getHeight());
				for ( int i = 0;i < fields.length;i++ ) {
					for ( int j = 0;j < fields[i].length;j++ ) {
						//ブロックの描画
						if ( fields[i][j] != 0 ) {
							g.drawImage(Resource.b_green, j*BLOCK_SIZE, i*BLOCK_SIZE);
						}
					}
				}

				//現在落下中のミノの描画
				for ( int i = 0;i < minos[rrr].length;i++ ) {
					for ( int j = 0;j < minos[0][i].length;j++ ) {
						if ( minos[rrr][i][j] != 0 ) {
							g.drawImage(Resource.b_green,j*BLOCK_SIZE+x*BLOCK_SIZE, i*BLOCK_SIZE+y*BLOCK_SIZE);
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
				}
			}

		};

		GTK.start("TETRIS", 250, 500, new GTKView() {
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
