package ie.gwtsplitter.gwtmodule.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

interface RemoteTrackServiceAsync {

	public void recordTrack(String trackId, AsyncCallback<Boolean> callback);

}