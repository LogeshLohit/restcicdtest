package com.logesh.threadTest;

public class ThreadTest {

	public static void main(String[] args) {

		Main main = new Main();
		Seconday sec = new Seconday();
		main.callMe(sec);

		Thread t1 = new Thread(main);
		t1.start();
	}

}

class Main extends Thread {

	public void run() {
		System.out.println("Inside main's run method...");
		callMe(new Seconday());
	}

	void callMe(Seconday sec) {
		System.out.println("Inside main's CalleMe mthod..");
		System.out.println(holdsLock(sec));

		synchronized (sec) {
			System.out.println(holdsLock(sec));
			sec.callMe(new Main());
			sec.syncMtd();
		}
		System.out.println(holdsLock(sec));

	}

	synchronized void synMtd() {
		// TODO Auto-generated method stub
		System.out.println("INside sync main..");
	}

	@Override
	public String toString() {
		return "Main []";
	}
	
	
}

class Seconday extends Thread {

	synchronized void callMe(Main sec) {
		System.out.println("Sec::callMe::" + holdsLock(sec));
		System.out.println("Inside Seconday's CalleMe mthod..");
		
		sec.synMtd();
		
	}

	synchronized void syncMtd() {
		// TODO Auto-generated method stub
		System.out.println("Sec sync");
	}
}