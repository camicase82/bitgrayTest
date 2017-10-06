package com.kiwi.reactor.service.balance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiwi.reactor.domain.SuscriberData;
import com.kiwi.reactor.service.firebase.FirebaseFacade;

@Service
@Transactional
public class BalanceManagerService {

	@Autowired
	private FirebaseFacade firebaseFacade;
	
    private final Logger log = LoggerFactory.getLogger(BalanceManagerService.class);
    
    private Map<String, Long> balance = new ConcurrentHashMap<String, Long>();;
    
    public BalanceManagerService(){
    	
    }
    
    @PostConstruct
    /**
     * Method that loads listeners for the exisitng users, this lsiteners are used to update
     * the balance info proactively from the db, removing the need to query when retriving the
     * balance
     */
    public void loadBalanceListeners(){
    	/*with firebase, a conbination of addListenerForSingleValueEvent and 
    	 * addChildEventListener helps control de pre/post load of information,
    	 * on load, addChildEventListener is called for each registry on the db
    	 * when the load is finished is called addListenerForSingleValueEvent*/ 
    	DatabaseReference ref = FirebaseDatabase
    			.getInstance().getReference("suscribers/");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				System.out.println("We're done loading the initial " + snapshot.getChildrenCount() + " items");
			}
			
			@Override
			public void onCancelled(DatabaseError arg0) {
			}
		});
		
		ref.addChildEventListener(new ChildEventListener() {
			
			@Override
			public void onChildRemoved(DataSnapshot snapshot) {}
			
			@Override
			public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
			
			@Override
			public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
				//we update the local map
				SuscriberData suscriberData = snapshot.getValue(SuscriberData.class);
				balance.put(snapshot.getKey(), suscriberData.getBalance());
				
			}
			
			@Override
			public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
				SuscriberData suscriberData = snapshot.getValue(SuscriberData.class);
				balance.put(snapshot.getKey(), suscriberData.getBalance());
			}
			
			@Override
			public void onCancelled(DatabaseError error) {

				
			}
		});
    }
    
    /**
     * Retrieves the minutes left in a user account
     * @param userId
     * @return
     */
    public long getSecsLeft(String userId){
    	return balance.get(userId);
    }
    
    /**
     * Registers balance consmtion from the user
     * @param userId
     * @param secs
     */
    public void removeBalance(String userId, int secs){
    	firebaseFacade.removeBalance(userId, secs);
    }
    
    /**
     * Ads balance to a user account
     * @param userId
     * @param secs
     */
    public void addBalance(String userId, long secs){
    	firebaseFacade.addBalance(userId, secs);
    }

}
