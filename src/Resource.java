import java.awt.Color;

import com.nompor.gtk.GTK;
import com.nompor.gtk.GTKColor;
import com.nompor.gtk.GTKCycleMethod;
import com.nompor.gtk.GTKGradientParam;
import com.nompor.gtk.GTKMath;
import com.nompor.gtk.GTKPaint;
import com.nompor.gtk.image.GTKImage;

public class Resource {
	//背景画像の構築
	public static GTKImage background;
	public static GTKImage b_red;
	public static GTKImage b_green;
	public static GTKImage b_yellow;
	public static GTKImage b_orange;
	public static GTKImage b_blue;
	public static GTKImage b_skyblue;
	public static GTKImage b_pink;

	private static boolean isLoaded;

	//背景画像をプログラムで作成
	public static void load() {
		GTK.runLater(() -> {
			background = GTK.createImage(GTK.getWidth(), GTK.getHeight(), g -> {
				GTKPaint backColor = GTK.createLinearGradient(0, 0, 0, GTK.getHeight(), GTKCycleMethod.NO_CYCLE
						, new GTKGradientParam(0f, GTK.color(Color.BLACK))
						, new GTKGradientParam(1f, GTK.createIntColor(0,0,100))
				);
				g.setPaint(backColor);
				g.fillRect(0, 0, GTK.getWidth(), GTK.getHeight());

				//星をランダム描画
				GTKColor[] clr =  {
						GTK.color(Color.RED)
						,GTK.color(Color.BLUE)
						,GTK.color(Color.GREEN)
						,GTK.color(Color.YELLOW)
						,GTK.color(Color.ORANGE)
						,GTK.createIntColor(255,0,255)
				};
				for ( int i = 0;i < 200;i++ ) {
					g.setColor(clr[GTKMath.randBetween(0, clr.length - 1)]);
					g.fillOval(
							GTKMath.randBetween(0, GTK.getWidth())
							, GTKMath.randBetween(0, GTK.getHeight())
					, 2, 2);
				}
			});

			//ブロックの画像を作成
			b_red = createBlockImage(GTK.createIntColor(255, 0, 0));
			b_green = createBlockImage(GTK.createIntColor(0, 255, 0));
			b_yellow = createBlockImage(GTK.createIntColor(255, 255, 0));
			b_orange = createBlockImage(GTK.createIntColor(255, 150, 0));
			b_blue = createBlockImage(GTK.createIntColor(0, 0, 255));
			b_skyblue = createBlockImage(GTK.createIntColor(100, 255, 255));
			b_pink = createBlockImage(GTK.createIntColor(255, 0, 255));
			isLoaded = true;
		});
	}

	private static GTKImage createBlockImage(GTKColor clr) {
		return GTK.createImage(TetriMino.BLOCK_SIZE, TetriMino.BLOCK_SIZE, g -> {
			GTKColor whiteColor = GTK.createIntColor(255, 255, 255);
			GTKColor blackColor = GTK.createIntColor(0, 0, 0);
			GTKPaint backColor = GTK.createLinearGradient(0, 0, TetriMino.BLOCK_SIZE, TetriMino.BLOCK_SIZE, GTKCycleMethod.NO_CYCLE
					, new GTKGradientParam(0f, whiteColor)
					, new GTKGradientParam(0.1f, clr)
					, new GTKGradientParam(0.9f, clr)
					, new GTKGradientParam(1f, blackColor)
			);
			g.setPaint(backColor);
			g.fillRect(0, 0, TetriMino.BLOCK_SIZE, TetriMino.BLOCK_SIZE);
			g.setAlpha(0.5);
			g.setColor(GTK.createIntColor(0, 0, 0));
			g.drawRect(0, 0, TetriMino.BLOCK_SIZE, TetriMino.BLOCK_SIZE);
		});
	}

	public static boolean isLoaded() {
		return isLoaded;
	}
}
