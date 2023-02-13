package biz.turnonline.ecosystem.skeleton;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Entry point that listens for Firestore events.
 *
 * @author <a href="mailto:medvegy@turnonline.biz">Aurel Medvegy</a>
 */
public class FunctionSkeleton
        implements BackgroundFunction<FirestoreEvent>
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
    public void accept( FirestoreEvent event, Context context )
    {
        LOGGER.info( context.attributes().toString() );
        LOGGER.info( context.eventId() );
        LOGGER.info( context.eventType() );
        LOGGER.info( context.timestamp() );
        LOGGER.info( "Function triggered by change to: " + event );

        final String fullDocPath = context.resource();
        String[] strings = fullDocPath.split( "/" );
        LOGGER.info( "Doc path fragments " + Arrays.toString( strings ) );
    }

}