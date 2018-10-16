import com.nompor.gtk.APIType;
import com.nompor.gtk.GTK;

public class TetrisStarter {

	public static void main(String[] args) {
		GTK.start("TETRIS", 800, 600, new Initialize(),APIType.AUTO);
	}
}
