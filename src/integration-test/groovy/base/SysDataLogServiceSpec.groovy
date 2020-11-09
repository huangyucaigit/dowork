package base

import syn.SysDataLog
import syn.SysDataLogService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class SysDataLogServiceSpec extends Specification {

    SysDataLogService sysDataLogService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new SysDataLog(...).save(flush: true, failOnError: true)
        //new SysDataLog(...).save(flush: true, failOnError: true)
        //SysDataLog sysDataLog = new SysDataLog(...).save(flush: true, failOnError: true)
        //new SysDataLog(...).save(flush: true, failOnError: true)
        //new SysDataLog(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //sysDataLog.id
    }

    void "test get"() {
        setupData()

        expect:
        sysDataLogService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<SysDataLog> sysDataLogList = sysDataLogService.list(max: 2, offset: 2)

        then:
        sysDataLogList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        sysDataLogService.count() == 5
    }

    void "test delete"() {
        Long sysDataLogId = setupData()

        expect:
        sysDataLogService.count() == 5

        when:
        sysDataLogService.delete(sysDataLogId)
        sessionFactory.currentSession.flush()

        then:
        sysDataLogService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        SysDataLog sysDataLog = new SysDataLog()
        sysDataLogService.save(sysDataLog)

        then:
        sysDataLog.id != null
    }
}
