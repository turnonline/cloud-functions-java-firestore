package biz.turnonline.ecosystem.skeleton;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.functions.Context;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

/**
 * {@link FunctionSkeleton} unit testing.
 *
 * @author <a href="mailto:medvegy@turnonline.biz">Aurel Medvegy</a>
 */
@ExtendWith( MockitoExtension.class )
public class FunctionSkeletonTest
{
    private static final String GOOGLE_CLOUD_PROJECT;

    static
    {
        // Google Project ID setting to the value taken from firestore-event.json
        FirestoreEvent event = readEvent();
        GOOGLE_CLOUD_PROJECT = event.value.name.split( "/" )[1];
        System.setProperty( "GOOGLE_CLOUD_PROJECT", GOOGLE_CLOUD_PROJECT );
    }

    @InjectMocks
    private FunctionSkeleton tested;

    private final String projectId = GOOGLE_CLOUD_PROJECT;

    @Mock( answer = Answers.RETURNS_DEEP_STUBS )
    private Firestore db;

    @Mock
    private Context context;

    private static FirestoreEvent readEvent()
    {
        String json = readString( "firestore-event.json" );
        return new Gson().fromJson( json, FirestoreEvent.class );
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
        when( context.attributes() ).thenReturn( new HashMap<>() );
        when( context.eventId() ).thenReturn( "5c196d9d-8e36-8414-3q98-3dfd871e9b42-9" );
        when( context.eventType() ).thenReturn( "providers/cloud.firestore/eventTypes/document.write" );
        when( context.resource() ).thenReturn( "projects/test-1abc/databases/(default)/documents/accounts/Uyg...Nfvg/orders/VTb...SN" );
        when( context.timestamp() ).thenReturn( "2020-12-18T12:44:23.883567Z" );
    }

    @Test
    public void validInputBody()
    {
        String json = readString( "firestore-event.json" );
        FirestoreEvent message = new Gson().fromJson( json, FirestoreEvent.class );

        tested.accept( message, context );
    }
}
