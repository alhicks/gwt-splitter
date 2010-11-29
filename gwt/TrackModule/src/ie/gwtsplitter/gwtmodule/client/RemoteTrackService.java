package ie.gwtsplitter.gwtmodule.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("track")
public interface RemoteTrackService extends RemoteService {

	public boolean recordTrack(String trackId);
	
}
