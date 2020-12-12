package com.logesh;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

	@Async
	void callAsyncMtd() {
		asyncCall();
	}
	
	
	private void asyncCall() {
		// TODO Auto-generated method stub
		for(int i=0;i<10;i++) {
			System.out.print(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
