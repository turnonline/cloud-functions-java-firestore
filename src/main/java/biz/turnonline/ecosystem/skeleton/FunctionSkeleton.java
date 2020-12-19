package biz.turnonline.ecosystem.skeleton;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.gson.Gson;

import java.util.Date;
import java.util.StringJoiner;
import java.util.logging.Logger;

/**
 * Entry point that listens for Firestore events.
 *
 * @author <a href="mailto:medvegy@turnonline.biz">Aurel Medvegy</a>
 */
public class FunctionSkeleton
        implements BackgroundFunction<FunctionSkeleton.FirestoreEvent>
{
    private static final Logger LOGGER = Logger.getLogger( FunctionSkeleton.class.getName() );

    private final Gson gson;

    public FunctionSkeleton()
    {
        this.gson = new Gson();
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
    }

    public static class FirestoreEvent
    {
        FirestoreValue value;

        FirestoreValue oldValue;

        @Override
        public String toString()
        {
            return new StringJoiner( ", ", FirestoreEvent.class.getSimpleName() + "[", "]" )
                    .add( "value=" + value )
                    .add( "oldValue=" + oldValue )
                    .toString();
        }
    }

    public static class FirestoreValue
    {
        Date createTime;

        String name;

        @Override
        public String toString()
        {
            return new StringJoiner( ", ", FirestoreValue.class.getSimpleName() + "[", "]" )
                    .add( "createTime='" + createTime + "'" )
                    .add( "name='" + name + "'" )
                    .toString();
        }
    }
}