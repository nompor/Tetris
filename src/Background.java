import com.nompor.gtk.GTK;
import com.nompor.gtk.draw.GTKGraphics;

//背景
public class Background {
	static int x;
	public static void update() {
		int xx = x-2;
		x = xx % GTK.getWidth();
	}
	public static void draw(GTKGraphics g) {
		int w = GTK.getWidth();
		int h = GTK.getHeight();
		g.drawImage(Resource.background, -x, 0, w + x, h, 0, 0, w + x, h);
		g.drawImage(Resource.background, 0, 0, -x, h, w + x, 0, -x, h);
	}
}
