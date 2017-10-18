import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class PICompute {
	public static void main(String[] args) {
		double i = 0, j = 0, precent = 0;
		
		try {
			i = inputMethod("Pls input how many times u won?");
			j = inputMethod("Pls input how many times u lost?");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		i -= 1; j -= 1;
		
		System.out.println("The current rate of ur scores is " + i / (i + j));
		
		for (int looptimes = 0; ; looptimes ++, i ++, j ++, precent = i / (i + j)) {
			if (precent >= 0.4d) {
				System.out.println("U need 2 continuous win " + looptimes + " times 2 40%!");
				
				System.out.println("i: " + i);
				System.out.println("j: " + j);
				System.out.println("i + j: " + (i + j));
				System.out.println("Precent: " + precent);
				
				break;
			}
		}
	}
	
	public static double inputMethod(String noticemsg) throws IOException {
		double result = 0;
		
		boolean DONE = Boolean.FALSE;
		
		while (!DONE) {
			DONE = Boolean.TRUE;
			
			BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println(noticemsg);
			
			try {
				result = Double.valueOf(br1.readLine());
			} catch (Exception e) {
				DONE = Boolean.FALSE;
				
				System.out.println("Sorry, ur inputed was not a number!");
			}
		}
		
		return result;
	}
}