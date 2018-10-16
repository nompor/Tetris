import com.nompor.gtk.GTK;
import com.nompor.gtk.input.GTKKeyCode;

//ゲーム時のキー操作用クラス
public enum KeyAction {
	LEFT,RIGHT,DOWN,HARDDROP,L_ROTATE,R_ROTATE,HOLD;
	public static void initKey() {
		GTK.key.orRegist(LEFT, GTKKeyCode.A, GTKKeyCode.LEFT);
		GTK.key.orRegist(RIGHT, GTKKeyCode.D, GTKKeyCode.RIGHT);
		GTK.key.orRegist(DOWN, GTKKeyCode.S, GTKKeyCode.DOWN);
		GTK.key.orRegist(HARDDROP, GTKKeyCode.W, GTKKeyCode.UP);
		GTK.key.orRegist(L_ROTATE, GTKKeyCode.X, GTKKeyCode.K);
		GTK.key.orRegist(R_ROTATE, GTKKeyCode.Z, GTKKeyCode.L);
		GTK.key.orRegist(HOLD, GTKKeyCode.C, GTKKeyCode.SHIFT, GTKKeyCode.ENTER);
	}
	public static boolean isActive(KeyAction act) {
		return GTK.isKeyPress(act);
	}
	public static boolean isDown(KeyAction act) {
		return GTK.isKeyDown(act);
	}
}
