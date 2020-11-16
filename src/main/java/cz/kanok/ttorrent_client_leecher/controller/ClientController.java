package cz.kanok.ttorrent_client_leecher.controller;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

@RestController
public class ClientController {

	private static final Logger logger = LoggerFactory.getLogger(ClientController.class.getName());

	//Path to .torrent folder
	private static final String PATH_TO_TORRENT_FOLDER = "c:/Ttorrent/Torrent/";
	//.torrent file name
	private static final String TORRENT_NAME = "bcprace.torrent";
	//Path to upload/download folder
	private static final String DOWNLOAD_FOLDER = "c:/Ttorrent/ClientLeecher/";

	//LEECHER PORT 8090
	private Client leecher;

	@GetMapping("start-client")
	public void startClient() throws IOException, NoSuchAlgorithmException {

		// First, instantiate the Client object.
		this.leecher = new Client(
				// This is the interface the client will listen on (you might need something
				// else than localhost here).
				InetAddress.getLocalHost(),

				// Load the torrent from the torrent file and use the given
				// output directory. Partials downloads are automatically recovered.
				SharedTorrent.fromFile(
						new File(PATH_TO_TORRENT_FOLDER + TORRENT_NAME),
						new File(DOWNLOAD_FOLDER)));

		// You can optionally set download/upload rate limits
		// in kB/second. Setting a limit to 0.0 disables rate
		// limits.
		leecher.setMaxDownloadRate(50.0);
		leecher.setMaxUploadRate(50.0);

		// At this point, can you either call download() to download the torrent and
		// stop immediately after...
		leecher.download();

		// Or call client.share(...) with a seed time in seconds:
		// leecher.share(3600);
		// Which would seed the torrent for an hour after the download is complete.

		// Downloading and seeding is done in background threads.
		// To wait for this process to finish, call:
		leecher.waitForCompletion();

		// At any time you can call client.stop() to interrupt the download.

	}

	@GetMapping("stop-client")
	public void stop() {
		this.leecher.stop();
	}

}
