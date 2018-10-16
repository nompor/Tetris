import com.nompor.gtk.APIType;
import com.nompor.gtk.GTK;
import com.nompor.gtk.GTKColor;
import com.nompor.gtk.GTKView;
import com.nompor.gtk.draw.GTKGraphics;

public class Test1 {

	public static void main(String[] args) {
		GTK.start("TETRIS", 250, 500, new GTKView() {

			static final int BLOCK_SIZE=25;

			GTKColor red;
			GTKColor sky;
			GTKColor black;

			public void start() {
				red = GTK.createIntColor(255, 0, 0);
				sky = GTK.createIntColor(100, 255, 255);
				black = GTK.createIntColor(0, 0, 0);
			}

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
					,{0,0,1,0,0,0,0,0,0,0}
					,{0,0,0,2,0,0,0,0,0,0}
					,{1,2,1,2,1,1,1,1,1,2}//20
			};
			//色の定数
			static final int RED = 1;
			static final int SKYBLUE = 2;

			public void draw(GTKGraphics g) {
				g.setColor(black);
				g.fillRect(0, 0, GTK.getWidth(), GTK.getHeight());
				for ( int i = 0;i < fields.length;i++ ) {
					for ( int j = 0;j < fields[i].length;j++ ) {
						//色を特定
						switch(fields[i][j]) {
						case RED:g.setColor(red);break;
						case SKYBLUE:g.setColor(sky);break;
						}
						//ブロックの描画
						if ( fields[i][j] != 0 ) {
							g.fillRect(j*BLOCK_SIZE, i*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
							g.setColor(black);
							g.drawRect(j*BLOCK_SIZE, i*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
						}
					}
				}
			}
		},APIType.SWING);
	}
}
