import java.util.ArrayList;
import java.util.Collections;

import com.nompor.gtk.APIType;
import com.nompor.gtk.GTK;
import com.nompor.gtk.GTKColor;
import com.nompor.gtk.GTKView;
import com.nompor.gtk.draw.GTKGraphics;

public class Test3 {

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

			static final int BLOCK_SIZE=25;
			GTKColor black;
			Mino currentMino = null;
			ArrayList<Mino> nextMinos = new ArrayList<>();
			ArrayList<Mino> minos = new ArrayList<>();
			int[][] fields = new int[20][10];
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

				if ( KeyAction.isActive(KeyAction.RIGHT) ) {
					x++;
				} else if (KeyAction.isActive(KeyAction.LEFT)) {
					x--;
				} else if (KeyAction.isActive(KeyAction.R_ROTATE)) {
					r++;
					if ( r>=currentMino.minos.length ) {
						r = 0;
					}
				} else if (KeyAction.isActive(KeyAction.L_ROTATE)) {
					r--;
					if ( r<0 ) {
						r = currentMino.minos.length - 1;
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
					if ( y+currentMino.minos.length >= fields.length ) {
						y = 0;
						x = 0;
						r = 0;
						currentMino = nextMinos.remove(0);
						//出現予定テトリミノが減ってきたらシャッフルして次の一巡分を挿入
						if ( nextMinos.size() < 7 ) {
							Collections.shuffle(this.minos);
							this.minos.stream().forEach(nextMinos::add);
						}

					} else {
						y++;
					}
				}

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
				if ( currentMino != null ) {
					int[][][] minos = currentMino.minos;
					for ( int i = 0;i < minos[r].length;i++ ) {
						for ( int j = 0;j < minos[0][i].length;j++ ) {
							if ( minos[r][i][j] != 0 ) {
								g.drawImage(Resource.b_green,j*BLOCK_SIZE+x*BLOCK_SIZE, i*BLOCK_SIZE+y*BLOCK_SIZE);
							}
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
