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
		/*
		Let's prevent deadlock by always dropping our chopstick,
		if we are unsuccesful of grabbing both chopsticks.

		This can lead to starvation, if the neighbors of some philosopher P
		are cycling in some manner, where P-1 unlocks P's left chopstick,
		but P+1 locks their left chopstick, causing P to drop both.
		*/ 
		try {

			while (true) {

				// Let's wait until we can grab a chopstick.
				leftChopstick.lock();

				// After grabbing a chopstick, check if we can grab another.
				// If we can't, let's letgo of both chopsticks, prevent
				// deadlock.
				if (!rightChopstick.tryLock()) {
					leftChopstick.unlock();
				} else {

					// We have both chopsticks, we are eating.
					return;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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
			long timeInActivity = (int)(Math.random() * 200) + 100;
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

public class PhilosopherStarving {
	public static void main(String[] args) {
		int size = 5;
		Lock [] chopsticks = new Lock[size];
		Philosopher [] philosophers = new Philosopher[size];

		for (int i = 0; i < chopsticks.length; i++)
			chopsticks[i] = new ReentrantLock();

		for (int i = 0; i < philosophers.length; i++) {
			philosophers[i] = new Philosopher(i, chopsticks[i%size], chopsticks[(i+1)%size]);
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