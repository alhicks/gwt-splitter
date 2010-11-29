package ie.gwtsplitter.gwtmodule.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TrackModule implements EntryPoint {
	
	private final RemoteTrackServiceAsync remoteService = 
		GWT.create(RemoteTrackService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {}
	
	
	public void track(String trackId) {
		remoteService.recordTrack(trackId,
			new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					System.out.println("failure");
				}
				public void onSuccess(Boolean result) {
					//System.out.println("success");
				}
			});
	}
}
