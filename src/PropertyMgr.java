
import java.io.IOException;
import java.util.Properties;

class PropertyMgr {
	private static Properties props = new Properties();
	
	static {
		try {
			props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private PropertyMgr() {}

    static String getProperty(String key) {
		return props.getProperty(key);
	}
}
