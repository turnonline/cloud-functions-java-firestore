package biz.turnonline.ecosystem.skeleton;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.functions.CloudEventsFunction;
import com.google.common.annotations.VisibleForTesting;
import com.google.events.cloud.firestore.v1.DocumentEventData;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ProtocolStringList;
import com.google.protobuf.util.JsonFormat;
import io.cloudevents.CloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point that listens for Firestore events.
 *
 * @author <a href="mailto:medvegy@turnonline.biz">Aurel Medvegy</a>
 */
public class FunctionSkeleton
        implements CloudEventsFunction
{
    private static final Logger LOGGER = LoggerFactory.getLogger( FunctionSkeleton.class );

    private final Firestore db;

    private final Gson gson;

    @SuppressWarnings( "unused" )
    public FunctionSkeleton()
    {
        this( FirestoreOptions.getDefaultInstance().getService() );
    }

    @VisibleForTesting
    public FunctionSkeleton( Firestore db )
    {
        this.db = db;
        this.gson = new Gson();
    }

    @Override
    public void accept( CloudEvent event ) throws InvalidProtocolBufferException
    {
        if ( event.getData() == null )
        {
            LOGGER.warn( "Event's data is not provided [{}]", event );
            return;
        }

        DocumentEventData data = DocumentEventData.parseFrom( event.getData().toBytes() );
        ProtocolStringList whatHasChanged = data.getUpdateMask().getFieldPathsList();
        LOGGER.info( "What has changed: {}", whatHasChanged );

        // Parse the Protobuf data into a DocumentEventData object
        DocumentEventData.Builder builder = DocumentEventData.newBuilder();
        builder.mergeFrom( event.getData().toBytes() );
        // Convert Protobuf object to JSON,
        String json = JsonFormat.printer()
                // This ensures that no extra whitespace is added, resulting in an effective copy in the log
                .omittingInsignificantWhitespace()
                .alwaysPrintFieldsWithNoPresence()
                .print( builder );

        LOGGER.info( json );
    }

}