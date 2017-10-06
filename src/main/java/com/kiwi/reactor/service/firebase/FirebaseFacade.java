package com.kiwi.reactor.service.firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiwi.reactor.domain.SuscriberData;

@Service
public class FirebaseFacade {
	
	
	public FirebaseFacade(){}
	
	@PostConstruct
	/**
	 * Method in charge of loading the fuirebase configuration
	 */
	public void loadFBConfiguration() {
		ClassLoader classLoader = getClass().getClassLoader();
		//File file = new File(classLoader.getResource("trotalotest-firebase.json").getFile());
		File file = new File(classLoader.getResource("trotalotest-firebase.json").getFile());
		FileInputStream serviceAccount;
		try {
			serviceAccount = new FileInputStream(file);

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
					.setDatabaseUrl("https://trotalotest.firebaseio.com/").build();

			FirebaseApp.initializeApp(options);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Service method to create w new subscriber into fb
	 * @param suscriber
	 * @return
	 */
	public String createSuscriber(SuscriberData suscriber){
		DatabaseReference ref = FirebaseDatabase
    			.getInstance().getReference("suscribers");
		DatabaseReference newRef = ref.push();
		newRef.setValue(suscriber);
		return newRef.getKey();
	}
	
	/**
	 * Method to add secs to a given line on fb
	 * @param userId
	 * @param secs
	 */
	public void addBalance(String userId, long secs){
		DatabaseReference ref = FirebaseDatabase
    			.getInstance().getReference("suscribers/" + userId + "/balance");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
	        @Override
	        public void onDataChange(DataSnapshot dataSnapshot) {
	            long value =(long) dataSnapshot.getValue();
	            value = value + secs;
	            dataSnapshot.getRef().setValue(value);
	        }
	        @Override
			public void onCancelled(DatabaseError arg0) {
				
			}
	    });
	}
	
	public void removeBalance(String userId, long secs){
		DatabaseReference ref = FirebaseDatabase
    			.getInstance().getReference("suscribers/" + userId);
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
	        @Override
	        public void onDataChange(DataSnapshot dataSnapshot) {
	        	SuscriberData suscriber = dataSnapshot.getValue(SuscriberData.class);
	        	if(suscriber.getBalance()>secs){
	        		suscriber.balance(suscriber.getBalance() - secs);
		            suscriber.setConsumed(suscriber.getConsumed() + secs);
	        		dataSnapshot.getRef().setValue(suscriber);
	        	}
	        }
	        @Override
			public void onCancelled(DatabaseError arg0) {
				
			}
	    });
	}
	
	

}
