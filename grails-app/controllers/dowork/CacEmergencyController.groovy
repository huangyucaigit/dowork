package dowork

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import grails.validation.ValidationException
import pojo.SynBody
import syn.SynProcessorService
import syn.SysDataLogService

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import grails.gorm.transactions.Transactional

class CacEmergencyController {

    SynProcessorService synProcessorService
    SysDataLogService sysDataLogService

    CacEmergencyService cacEmergencyService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    // todo 实现自己发送方法
    def send(String id){
        def synResult = sysDataLogService.doSynchronizedByTaskId(cacEmergencyService.dataType(),id)
        respond(synResult)
    }
    // todo 实现自己的接收方法
    def receive() {
        def json = request.JSON
        println json
        log.info("===== 服务端：1 ========== 接收需要同步的数据" )
        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // <2>
        SynBody synBody = objectMapper.readValue(json.toString(), SynBody) // <3>
        def synResult = synProcessorService.execute(synBody.dataType,synBody)
        respond(synResult)
    }




    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond cacEmergencyService.list(params) , model:[cacEmergencyCount: cacEmergencyService.count()]
    }

    def show(Long id) {
        println id
        respond cacEmergencyService.get(id)
    }

    @Transactional
    def save(CacEmergency cacEmergency) {
        if (cacEmergency == null) {
            render status: NOT_FOUND
            return
        }
        if (cacEmergency.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond cacEmergency.errors
            return
        }

        try {
            cacEmergencyService.save(cacEmergency)
        } catch (ValidationException e) {
            respond cacEmergency.errors
            return
        }

        respond cacEmergency, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(CacEmergency cacEmergency) {
        if (cacEmergency == null) {
            render status: NOT_FOUND
            return
        }
        if (cacEmergency.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond cacEmergency.errors
            return
        }

        try {
            cacEmergencyService.save(cacEmergency)
        } catch (ValidationException e) {
            respond cacEmergency.errors
            return
        }

        respond cacEmergency, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Long id) {
        if (id == null) {
            render status: NOT_FOUND
            return
        }
        cacEmergencyService.delete(id)
        render status: NO_CONTENT
    }
}
