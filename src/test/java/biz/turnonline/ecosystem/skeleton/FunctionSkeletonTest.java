package biz.turnonline.ecosystem.skeleton;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.functions.Context;
import com.google.gson.Gson;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * {@link FunctionSkeleton} unit testing.
 *
 * @author <a href="mailto:medvegy@turnonline.biz">Aurel Medvegy</a>
 */
public class FunctionSkeletonTest
{
    private static final String GOOGLE_CLOUD_PROJECT;

    static
    {
        // Google Project ID setting to the value taken from firestore-event.json
        FunctionSkeleton.FirestoreEvent event = readEvent();
        GOOGLE_CLOUD_PROJECT = event.value.name.split( "/" )[1];
        System.setProperty( "GOOGLE_CLOUD_PROJECT", GOOGLE_CLOUD_PROJECT );
    }

    @Tested
    private FunctionSkeleton tested;

    @Injectable
    private final String projectId = GOOGLE_CLOUD_PROJECT;

    @Injectable
    private Firestore db;

    @Mocked
    private Context context;

    private static FunctionSkeleton.FirestoreEvent readEvent()
    {
        String json = readString( "firestore-event.json" );
        return new Gson().fromJson( json, FunctionSkeleton.FirestoreEvent.class );
    }

    /**
     * Reads the content of the file in the same package as this test and converts it into a string.
     *
     * @param filename the file name to be read
     * @return the string content of the file
     */
    private static String readString( String filename )
    {
        InputStream stream = FunctionSkeletonTest.class.getResourceAsStream( filename );
        if ( stream == null )
        {
            throw new IllegalArgumentException( filename + " not found" );
        }
        return new BufferedReader( new InputStreamReader( stream ) )
                .lines()
                .collect( Collectors.joining( System.lineSeparator() ) );
    }

    @BeforeEach
    public void before()
    {
        new Expectations()
        {
            {
                context.attributes();
                result = new HashMap<>();

                context.eventId();
                result = "5c196d9d-8e36-8414-3q98-3dfd871e9b42-9";

                context.eventType();
                result = "providers/cloud.firestore/eventTypes/document.write";

                context.resource();
                result = "projects/test-1abc/databases/(default)/documents/accounts/Uyg...Nfvg/orders/VTb...SN";

                context.timestamp();
                result = "2020-12-18T12:44:23.883567Z";
            }
        };
    }

    @Test
    public void validInputBody()
    {
        String json = readString( "firestore-event.json" );
        FunctionSkeleton.FirestoreEvent message = new Gson().fromJson( json, FunctionSkeleton.FirestoreEvent.class );

        tested.accept( message, context );
    }
}
