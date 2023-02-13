package biz.turnonline.ecosystem.skeleton;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Firestore event java model.
 *
 * @author <a href="mailto:medvegy@turnonline.biz">Aurel Medvegy</a>
 */
public class FirestoreEvent
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
