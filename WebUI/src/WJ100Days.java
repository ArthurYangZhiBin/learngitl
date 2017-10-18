import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WJ100Days {
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
	
	public static void main(String[] args) {
		Date today = new Date();
		Date WJsBthday = new Date();
		
		try {
			WJsBthday = sdf.parse("2012-08-1");	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		long diff = today.getTime() - WJsBthday.getTime();
		
		System.out.println(diff);
		System.out.println(diff / (1000 * 60 * 60 * 60 * 24));
	}
}