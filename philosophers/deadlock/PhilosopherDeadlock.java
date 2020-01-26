import java.util.*;
import java.util.concurrent.locks.*;

class Philosopher extends Thread {

	public final Lock leftChopstick;
	public final Lock rightChopstick;

	public final int id;

	public Philosopher(int id, Lock leftChopstick, Lock rightChopstick) {
		this.id = id;
		this.leftChopstick = leftChopstick;
		this.rightChopstick = rightChopstick;
	}

	public void eat() {
		leftChopstick.lock();
		rightChopstick.lock();
	}
	public void think() {
		leftChopstick.unlock();
		rightChopstick.unlock();
	}

	@Override
	public void run() {

		boolean needsToEat = true;
		while(true) {
			// Somewhere between .5 to 1.5 seconds.
			long timeInActivity = 0;
			if (needsToEat) {
				System.out.printf("%d is now hungry\n", id);
				eat();
				System.out.printf("%d is now eating\n", id);
				needsToEat = false;
			} else {
				think();
				System.out.printf("%d is now thinking\n", id);
				needsToEat = true;
			}

			try {
				Thread.sleep(timeInActivity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

public class PhilosopherDeadlock {
	public static void main(String[] args) {
		int size = 5;
		Lock [] chopsticks = new Lock[size];
		Philosopher [] philosophers = new Philosopher[size];

		for (int i = 0; i < chopsticks.length; i++)
			chopsticks[i] = new ReentrantLock();

		for (int i = 0; i < philosophers.length; i++) {
			philosophers[i] = new Philosopher(i, chopsticks[i%5], chopsticks[(i+1)%5]);
			philosophers[i].start();
		}

		Scanner scan = new Scanner(System.in);
		String inp = "";
		while (!inp.contentEquals("n")) {
			inp = scan.next();			
		}

		System.exit(0);
	}
}