import java.awt.Color;

import com.nompor.gtk.GTK;
import com.nompor.gtk.GTKColor;
import com.nompor.gtk.GTKFont;
import com.nompor.gtk.GTKView;
import com.nompor.gtk.draw.GTKGraphics;
import com.nompor.gtk.event.GTKKeyEvent;

public class TitleView implements GTKView {

	//フォントや色
	private GTKFont font;
	private GTKFont font2;
	private GTKColor white;
	private GTKColor orange;

	//選択フラグ
	private int selectFlg=GAME;
	private static final int GAME=0;
	private static final int END=1;
	private String[] selTx = {
			"Game Start"
			,"End"
	};

	private String title = "TETRIS";
	public TitleView() {
		font = GTK.createFont(80);
		font2 = GTK.createFont(30);
		white = GTK.color(Color.WHITE);
		orange = GTK.color(Color.ORANGE);
	}

	public void process() {
		Background.update();
	}

	//タイトルの描画処理
	@Override
	public void draw(GTKGraphics g) {
		g.setTextAntialias(true);

		//背景描画
		Background.draw(g);
		g.setFont(font);
		int width = (int) g.stringWidth(title);

		//タイトルの描画
		g.setColor(white);
		g.drawString(title, GTK.getWidth() / 2 - width / 2, 180);

		g.setFont(font2);
		for (int i = 0;i < selTx.length;i++) {
			String s = selTx[i];
			width = (int) g.stringWidth(s);
			if ( selectFlg == i ) {
				g.setColor(orange);
				g.drawString(s, GTK.getWidth() / 2 - width / 2, 350+i*80);
			} else {
				g.setColor(white);
				g.drawString(s, GTK.getWidth() / 2 - width / 2, 350+i*80);
			}
		}
	}

	//タイトル画面のキー操作
	@Override
	public void keyPressed(GTKKeyEvent evt) {
		switch(evt.code) {
		case Z:
		case ENTER:
		case L:
			//決定ボタン
			switch ( selectFlg ) {
			case GAME:GTK.changeViewDefaultAnimation(new GameView());break;
			case END:System.exit(0);
			}
			break;
		case S:
		case DOWN:
			//下を選択
			if ( selectFlg < selTx.length - 1 ) selectFlg++;
			else selectFlg = 0;
			break;
		case W:
		case UP:
			//上を選択
			if ( selectFlg > 0 ) selectFlg--;
			else selectFlg = selTx.length - 1;
			break;
		default:break;
		}
	}
}