package org.italiangrid.voms.store.impl;

import java.util.List;

import org.italiangrid.voms.store.UpdatingVOMSTrustStore;

public class VOMSTrustStores {

	public static UpdatingVOMSTrustStore newTrustStore(){
		
		return new DefaultUpdatingVOMSTrustStore();
	}
	
	public static UpdatingVOMSTrustStore newTrustStore(
			List<String> localTrustDirs, long updateFrequency) {
		return new DefaultUpdatingVOMSTrustStore(localTrustDirs,
				updateFrequency);
	}

	public static UpdatingVOMSTrustStore newTrustStore(
			long updateFrequency) {
		return new DefaultUpdatingVOMSTrustStore(updateFrequency);
	}

	
}
