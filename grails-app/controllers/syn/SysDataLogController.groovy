package syn


import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

@ReadOnly
class SysDataLogController {

    SysDataLogService sysDataLogService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond sysDataLogService.list(params), model:[sysDataLogCount: sysDataLogService.count()]
    }

    def show(Long id) {
        respond sysDataLogService.get(id)
    }

    @Transactional
    def save(SysDataLog sysDataLog) {
        if (sysDataLog == null) {
            render status: NOT_FOUND
            return
        }
        if (sysDataLog.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond sysDataLog.errors
            return
        }

        try {
            sysDataLogService.save(sysDataLog)
        } catch (ValidationException e) {
            respond sysDataLog.errors
            return
        }

        respond sysDataLog, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(SysDataLog sysDataLog) {
        if (sysDataLog == null) {
            render status: NOT_FOUND
            return
        }
        if (sysDataLog.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond sysDataLog.errors
            return
        }

        try {
            sysDataLogService.save(sysDataLog)
        } catch (ValidationException e) {
            respond sysDataLog.errors
            return
        }

        respond sysDataLog, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Long id) {
        if (id == null) {
            render status: NOT_FOUND
            return
        }

        sysDataLogService.delete(id)

        render status: NO_CONTENT
    }
}
