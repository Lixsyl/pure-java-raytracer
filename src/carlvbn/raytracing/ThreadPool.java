package carlvbn.raytracing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class ThreadPool {
	private ArrayBlockingQueue<Runnable> queue;
	private List<Thread> threads = new ArrayList<>();
	
	private class Worker implements Runnable {
		public void run() {
			while (!Thread.interrupted()) {
				Runnable r = queue.poll();
				r.run();
			}
		}
	}
	
	public ThreadPool (int qsize, int N) {
		queue = new ArrayBlockingQueue<>(qsize);
		for (int i = 0; i < N; i++) {
			Thread t = new Thread(new Worker());
			t.start();
			threads.add(t);
		}
	}
	
	public void submit (Runnable r) {
		queue.add(r);
	}
	
	public void shutdown () {
		for (Thread t : threads) {
			try {
				t.interrupt();
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		threads.clear();
		queue.clear();
	}

	public List<Thread> getThreads() {
		return threads;
	}
}
