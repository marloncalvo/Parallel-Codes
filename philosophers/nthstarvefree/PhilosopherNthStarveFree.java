import java.util.*;
import java.util.concurrent.locks.*;

class Philosopher extends Thread {

	public final Lock leftChopstick;
	public final Lock rightChopstick;

	public final static Lock globalLock = new ReentrantLock();

	public final int id;

	public Philosopher(int id, Lock leftChopstick, Lock rightChopstick) {
		this.id = id;
		this.leftChopstick = leftChopstick;
		this.rightChopstick = rightChopstick;
	}

	public void eat() {
		/*
		When the i'th developer wants to eat, he will be k away 
		from obtaining the lock. Before he obtains the lock, he will
		wait, doing nothing. Once he obtains the lock, and only one 
		philosopher can have the lock; he will be the only philosopher
		with the ability to grab chopsticks.

		At this time there are only two possibilities, either one or both 
		of the chopsticks are current in use. Or there are none in use.
		If the chopsticks are in use, we know that no one else can grab
		the lock, since only this philosopher can grab the lock; eventually,
		other philosophers must stop eating to think, which will eventually
		be the two chopsticks we need.
		In the case where chopsticks are unused, we simple lock those chopsticks.
		
		Since after grabbing the general lock, no one else can grab any other chopsticks, and whoever is holding chopsticks must think, releasing 
		their chopsticks locks; we are guaranteed to have the lock to both
		chopsticks. This rule will extend to each next philosopher with a lock.
		*/ 
		Philosopher.globalLock.lock();
		leftChopstick.lock();
		rightChopstick.lock();
		Philosopher.globalLock.unlock();
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

public class PhilosopherNthStarveFree {
	public static void main(String[] args) {
		int size = Integer.valueOf(args[0]);
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
