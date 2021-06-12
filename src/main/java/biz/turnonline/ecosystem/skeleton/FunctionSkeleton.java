package biz.turnonline.ecosystem.skeleton;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Entry point that listens for Firestore events.
 *
 * @author <a href="mailto:medvegy@turnonline.biz">Aurel Medvegy</a>
 */
public class FunctionSkeleton
        implements BackgroundFunction<FunctionSkeleton.FirestoreEvent>
{
    private static final Logger LOGGER = LoggerFactory.getLogger( FunctionSkeleton.class.getName() );

    private final String DB_DEFAULT_PATH;

    private final Firestore db;

    private final Gson gson;

    public FunctionSkeleton()
    {
        this( FirestoreOptions.getDefaultInstance().getService(), FirestoreOptions.getDefaultInstance().getProjectId() );
    }

    @VisibleForTesting
    public FunctionSkeleton( Firestore db, String projectId )
    {
        this.db = db;
        this.gson = new Gson();
        DB_DEFAULT_PATH = "projects/" + projectId + "/databases/(default)/documents/";
    }

    @Override
    public void accept( FirestoreEvent event, Context context )
    {
        LOGGER.info( context.attributes().toString() );
        LOGGER.info( context.eventId() );
        LOGGER.info( context.eventType() );
        LOGGER.info( context.resource() );
        LOGGER.info( context.timestamp() );
        LOGGER.info( "Function triggered by change to: " + event );
        LOGGER.info( "DB_DEFAULT_PATH " + DB_DEFAULT_PATH );
    }

    public static class FirestoreEvent
    {
        FirestoreValue value;

        FirestoreValue oldValue;

        UpdateMask updateMask;

        @Override
        public String toString()
        {
            return new StringJoiner( ", ", FirestoreEvent.class.getSimpleName() + "[", "]" )
                    .add( "value=" + value )
                    .add( "oldValue=" + oldValue )
                    .add( "updateMask=" + updateMask )
                    .toString();
        }
    }

    public static class FirestoreValue
    {
        Date createTime;

        Map<String, Object> fields;

        String name;

        Date updateTime;

        @Override
        public String toString()
        {
            return new StringJoiner( ", ", FirestoreValue.class.getSimpleName() + "[", "]" )
                    .add( "createTime='" + createTime + "'" )
                    .add( "fields='" + fields + "'" )
                    .add( "name='" + name + "'" )
                    .add( "updateTime='" + updateTime + "'" )
                    .toString();
        }
    }

    public static class UpdateMask
    {
        List<String> fieldPaths;

        @Override
        public String toString()
        {
            return new StringJoiner( ", ", UpdateMask.class.getSimpleName() + "[", "]" )
                    .add( "fieldPaths=" + fieldPaths )
                    .toString();
        }
    }
}