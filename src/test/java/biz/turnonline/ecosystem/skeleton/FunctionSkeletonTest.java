package biz.turnonline.ecosystem.skeleton;

import com.google.cloud.firestore.Firestore;
import com.google.events.cloud.firestore.v1.DocumentEventData;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Collectors;

/**
 * {@link FunctionSkeleton} unit testing.
 *
 * @author <a href="mailto:medvegy@turnonline.biz">Aurel Medvegy</a>
 */
@ExtendWith( MockitoExtension.class )
public class FunctionSkeletonTest
{
    @InjectMocks
    private FunctionSkeleton tested;

    @Mock( answer = Answers.RETURNS_DEEP_STUBS )
    private Firestore db;

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

    @Test
    public void validInputBody() throws InvalidProtocolBufferException
    {
        String json = readString( "firestore-event.json" );
        DocumentEventData.Builder builder = DocumentEventData.newBuilder();
        JsonFormat.parser().merge( json, builder );

        CloudEvent event = CloudEventBuilder.v1()
                .withId( "1" )
                .withType( "google.cloud.firestore.document.v1.updated" )
                .withSource( URI.create( "https://cloudevents.io" ) )
                .withData( "application/protobuf", builder.build().toByteArray() )
                .build();
        tested.accept( event );
    }
}
