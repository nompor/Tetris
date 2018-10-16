import java.awt.Color;

import com.nompor.gtk.GTK;
import com.nompor.gtk.GTKColor;
import com.nompor.gtk.GTKFont;
import com.nompor.gtk.GTKView;
import com.nompor.gtk.draw.GTKGraphics;
import com.nompor.util.StringUtil;

//ロード画面
public class Initialize implements GTKView {
	private GTKColor clr;
	private GTKColor clr2;
	private GTKFont font;
	private int count;
	private boolean isChange;
	public void start() {
		clr = GTK.color(Color.WHITE);
		clr2 = GTK.color(Color.BLACK);
		font = GTK.createFont(40);
		Resource.load();
		KeyAction.initKey();
	}

	@Override
	public void draw(GTKGraphics g) {
		g.setColor(clr2);
		g.fillRect(0, 0, GTK.getWidth(), GTK.getHeight());
		g.setTextAntialias(true);
		g.setColor(clr);
		g.setFont(font);

		//※マルチスレッドでロードしているわけではないのでNOW LOADINGは実質飾りみたいなものです
		g.drawString("NOW LOADING"+StringUtil.repeat(".", count), 400, 500);
		count = ++count % 5;

		if ( Resource.isLoaded() && !isChange ) {
			isChange = true;
			GTK.runLater(()->GTK.changeViewDefaultAnimation(new TitleView()));
		}
	}
}
