package id

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import org.hibernate.internal.CoreLogging
import org.hibernate.internal.CoreMessageLogger


class SnowflakeIdGenerator implements IdentifierGenerator {
    private static final CoreMessageLogger LOG = CoreLogging.messageLogger( SnowflakeIdGenerator.class );

    private static IdWorker idWorker = new IdWorker(-1, -1);

    public SnowflakeIdGenerator() {
    }

    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        String.valueOf(idWorker.nextId())
    }
}
