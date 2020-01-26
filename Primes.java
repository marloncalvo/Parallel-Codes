import java.util.*;
import java.io.*;

class SieveWorker extends Thread {

	public final List<Integer> primes;
	public final BitSet bits;
	public final int low;
	public final int high;
	public int index = 0;

	public final static int MAX_SQRT = (int) Math.ceil(Math.sqrt(Integer.MAX_VALUE));

	public SieveWorker(List<Integer> primes, int low, int high) {
		this.low = low;
		this.high = high;
		this.primes = primes;

		final int size = high - low + 1;
		this.bits = new BitSet(size+1);

		threadDebug("Low="+low+",High="+high);
	}

	@Override
	public void run() {

		for (int i = 0; i < bits.size(); i++) {
			bits.set(i, true);
		}

		while(true) {

			if (index >= primes.size()) {
				//threadDebug("waiting");
				try {
					Thread.sleep(100);
				} catch (Exception e) {

				}
				continue;
			}

			int p = primes.get(index);
			if (p == -1) {
				threadDebug("finished - terminating");
				return;
			}

			int start = ((int) Math.ceil((double) low / p)) * p;
			for (int n = start; n <= high; n+=p) {
				//threadDebug(""+n);
				bits.set(n-low, false);
//				if (Thread.currentThread().getName().contains("1"))
//					System.out.println(n-low);
			}

			//threadDebug("finished prime " + primes.get(index));
			index++;
		}
	}

	public static void threadDebug(String info) {
		if (Primes.DEBUG)
			System.out.println(Thread.currentThread().getName() + ": " + info);
	}
}

class SieveController extends Thread {

	public final ArrayList<Integer> primes;
	public final BitSet bits;

	public final int numPrimes;
	public final int size;

	public final SieveWorker [] workers;

	public SieveController(int numPrimes, int numWorkers) {

		this.size = (int) Math.ceil((double) numPrimes / numWorkers);

		// Use +1 for normal indexing.
		this.bits = new BitSet(size+1);
		this.primes = new ArrayList<Integer>(size+1);
		this.numPrimes = numPrimes;

		// We will be a worker as well.
		this.workers = new SieveWorker[(numWorkers-1)];
	}

	@Override
	public void run() {

		// add these now, so that workers can get a head start.
		preInitialization();

		threadDebug("job - starting workers (started)");
		initializeWorkers(size-1);
		threadDebug("job - starting workers (finished)");

		int initialPrimesSize = (int) Math.ceil(Math.sqrt(numPrimes)) + 1;

		threadDebug("job - calculating initial primes (started)");
		calculateInitialPrimes(initialPrimesSize);
		threadDebug("job - calculating initial primes (finished)");

		// We need to finish our part of the sieve.
		threadDebug("job - performing sieve (started)");
		doSieve(initialPrimesSize + 1);
		threadDebug("job - performing sieve (finished)");

		// We're done, let's destroy so we can collect data.
		threadDebug("waiting on workers");
		for (int i = 0; i < workers.length; i++) {
			try {
				workers[i].join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		threadDebug("workers done");

		threadDebug("job - testing primes (started)");
		//testPrimes();
		threadDebug("job - testing primes (finished)");
	}

	public void doSieve(int low) {
		int start;
		int high = size;
		for (int i = 0; i < primes.size(); i++) {
			int p = primes.get(i);
			if (p == -1)
				return;

			start = ((int) Math.ceil((double) low / p)) * p;
			for (int n = start; n <= high; n+=p) {
				//threadDebug(""+n);
				bits.set(n, false);
			}
		}
	}

	// This assumes that the sqrt(limit) <= capacity.
	// For this problem, that's ok.
	public void calculateInitialPrimes(int limit) {
		for (int i = 0; i < bits.size(); i++) {
			bits.set(i, true);
		}

		for (int n = 2; n <= limit; n++) {
			if (bits.get(n)) {
				primes.add(n);
				for (int n2 = n*n; n2 <= limit; n2+=n) {
					bits.set(n2, false);
				}
			}
		}

		primes.add(-1);
	}

	public void testPrimes() {

		try {

			FileWriter writer = new FileWriter("primes.out");

			writer.write(2+"\n");
			writer.write(3+"\n");

			ArrayList<BitSet> sets = new ArrayList<>();
			sets.add(bits);

			// Test if we got the correct data.
			for (int w = 0; w < workers.length; w++) {
				sets.add(workers[w].bits);
			}

			for (int i = 6; i <= numPrimes; i+=6) {

				int a = (i-1) / size, ai = (i-1) % size;
				int b = (i+1) / size, bi = (i+1) % size;
				
				if (sets.get(a).get(ai))
					writer.write((i-1)+"\n");
				if (sets.get(b).get(bi))
					writer.write((i+1)+"\n");
			}

			writer.close();

		}  catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void preInitialization() {
		primes.add(2);
		primes.add(3);
		primes.add(5);
		primes.add(7);
	}

	public void initializeWorkers(int startLow) {
		int low, high;

		// Start prevHigh as my own's max sieve block.
		// Since this thread will do [0 ... size]
		int prevHigh = startLow;
		for (int i = 0; i < workers.length; i++) {
			low = prevHigh + 1;
			prevHigh = high = low + size - 1;
			workers[i] = new SieveWorker(primes, low, high);
			workers[i].start();
		}
	}

	public static void threadDebug(String info) {
		if (Primes.DEBUG)
			System.out.println("SieveController: " + info);
	}
}

public class Primes {

	public final static boolean DEBUG = false;

	public final static int NUM_PRIMES = 100000000;
	public final static int NUM_WORKERS = 8;

	public static void main(String[] args) throws Exception {
		Thread controller = new SieveController(NUM_PRIMES, NUM_WORKERS);
		controller.start();
		controller.join();
	}
}