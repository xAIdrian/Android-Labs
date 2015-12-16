package vandy.mooc.model.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import vandy.mooc.model.datamodel.ReplyMessage;
import vandy.mooc.model.datamodel.RequestMessage;
import vandy.mooc.utils.NetUtils;

/**
 * An IntentService that downloads an image requested via data in an
 * intent, stores the image in a local file on the local device, and
 * returns the image file's URI back to the ImageModelImpl's Handler
 * via the Messenger passed with the intent.
 */
public class DownloadImagesStartedService 
       extends IntentService {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * Key used to identify the RequestMessage within an Intent
     * "extra".
     */
    private static final String REQUEST_MESSAGE = "REQUEST_MESSAGE";

    /**
     * Constructor initializes the IntentService super class.
     */
    public DownloadImagesStartedService() {
    	super("DownloadImagesStartedService");
    }

    /**
     * Factory method that returns an explicit Intent for downloading
     * an image.
     */
    public static Intent makeIntent(Context context,
                                    int requestCode, 
                                    Uri url,
                                    Uri directoryPathname,
                                    Handler downloadHandler) {
        // Create an intent that will download the image from the web.
        // TODO -- you fill in here, replacing "null" with the proper
        // code, which involves
        //
        // (1) creating a RequestMessage
        // containing the various parameters passed into this method
        //
        // (2) storing this RequestMessage as a Message "extra" in
        // the Intent.


        RequestMessage requestMessage = RequestMessage.makeRequestMessage(
                requestCode, url, directoryPathname, new Messenger(downloadHandler));
        Intent intent = new Intent(context, DownloadImagesStartedService.class); //todo: correct class?
        intent.putExtra(REQUEST_MESSAGE, requestMessage.getMessage());

        return intent;
    }

    /**
     * Hook method dispatched by the IntentService framework to
     * download the image requested via data in an intent, store the
     * image in a local file on the local device, and return the image
     * file's URI back to the ImageModelImpl's Handler via the
     * Messenger passed with the intent.
     */
    @Override
    public void onHandleIntent(Intent intent) {
        // Extract the RequestMessage from the intent.

        final RequestMessage requestMessage =
            RequestMessage.makeRequestMessage
                ((Message) intent.getParcelableExtra(REQUEST_MESSAGE));

        // Extract the URL for the image to download.
        // Download the requested image.
        // TODO -- you fill in here.
        NetUtils.downloadImage(DownloadImagesStartedService.this,
                requestMessage.getImageURL(), requestMessage.getDirectoryPathname());

        // Extract the request code.
        // Extract the Messenger stored in the RequestMessage.
        // Send the path to the image file back to the
        // MainActivity via the messenger.
        // TODO -- you fill in here.
        sendPath(requestMessage.getMessenger(), requestMessage.getDirectoryPathname(),
                requestMessage.getImageURL(), requestMessage.getRequestCode());




    }

    /**
     * Send the @a pathToImageFile back to the ImageModelImp's Handler
     * via the @a Messenger.
     */
    private void sendPath(Messenger messenger,
                          Uri pathToImageFile,
                          Uri url,
                          int requestCode) {
        // Call the makeReplyMessage() factory method to create
        // Message.
        // TODO -- you fill in here.

        Log.e(TAG, "sendPath");
        try {
            // Send the path to the image file back to the
            // ImageModelImpl's Handler via the Messenger.
            // TODO -- you fill in here.

            Log.e(TAG, String.valueOf(messenger == null));

            messenger.send(ReplyMessage.makeReplyMessage(pathToImageFile, url, requestCode).getMessage());

        } catch (RemoteException e) {
            Log.e(getClass().getName(),
                    "Exception while sending reply message back to Activity.",
                    e);
        }
    }
}
