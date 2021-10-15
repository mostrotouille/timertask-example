package mx.com.mostrotuille.example.timertask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TimertaskApplication {
	protected static class RetryTask extends TimerTask {
		@Override
		public void run() {
			timer.cancel();

			printlnTimestampMessage("Retries: " + retries);

			final boolean response = invokeService();

			printlnTimestampMessage("Response: " + response);

			if (!response && retries < MAXIMUM_RETRIES) {
				timer = new Timer();
				timer.schedule(new RetryTask(),
						retries < RETRIES_DELAY_ARRAY.length ? RETRIES_DELAY_ARRAY[retries] : DEFAULT_RETRIES_DELAY);

				retries++;
			}
		}
	}

	protected static final int DEFAULT_RETRIES_DELAY = 12000;
	protected static final int MAXIMUM_RETRIES = 7;
	protected static int retries = 0;
	protected static final int[] RETRIES_DELAY_ARRAY = new int[] { 3000, 6000, 9000 };
	protected static Timer timer = null;

	public static boolean invokeService() {
		printlnTimestampMessage("invokeService(...)");

		return (new Random(Calendar.getInstance().getTimeInMillis())).nextBoolean();
	}

	public static void main(String[] ar) {
		timer = new Timer();
		timer.schedule(new RetryTask(), 0);
	}

	private static final void printlnTimestampMessage(String message) {
		System.out.println(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + " " + message);
	}
}