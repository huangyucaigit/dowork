package dowork

import base.ISynchronized
import dictionary.DataOperation
import syn.SysDataLog
import client.ISendClient
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import pojo.SynBody
import pojo.SynResult

interface ICacEmergencyService {

    CacEmergency get(Serializable id)

    List<CacEmergency> list(Map args)

    Long count()

    void delete(Serializable id)

    CacEmergency save(CacEmergency cacEmergency)

}


@Service(CacEmergency)
abstract class  CacEmergencyService implements ICacEmergencyService, ISynchronized {

    @Autowired
    @Qualifier("cacEmergencyClient") // todo 改成自己的发送客户端
    ISendClient iSendClient


    String dataType(){
        return "应急事件下发"
    }



    /**
     * 1 获取最新数据
     * 2 发送给对方服务器处理
     * 3 获得处理结果修改日期结果
      */
    @Transactional
    SynResult doSynchronized(List<SysDataLog> sysDataLogList){
        log.info("========== 客户端：3 ========== 获取同步数据")
        def data = todo(sysDataLogList)
        def synBody = new SynBody(taskId:sysDataLogList.get(0)?.seqFirst,dataType:this.dataType(),data:data)
        log.info("========== 客户端：4 ========== 发送同步数据到服务端")
        return iSendClient.send(synBody)
    }


    /**
     * 实现自己的业务逻辑
     * 不同的业务获取不同表的数据
     * 通过
     * @param sysDataLogList
     * @return
     */
    def todo(List<SysDataLog> sysDataLogList){
        // todo 获取自己的业务数据
        def list = []
        for (SysDataLog sysDataLog : sysDataLogList){
            if(sysDataLog.tableName == "CAC_EMERGENCY" ){
                def domainData
                if(sysDataLog.keyId){
                    domainData = CacEmergency.get(sysDataLog.keyId)

                }
                list.add(
                        [
                                keyId:sysDataLog.keyId,
                                tableName:sysDataLog.tableName,
                                sysDataLog:sysDataLog,
                                domainData: domainData,
                        ]
                )
            }
        }
        log.info("========== 客户端：3-1 ========== 获取数据${list.size()}行")
        return list
    }

    @Transactional
    SynResult saveSynProcessor(SynBody synBody){
        // todo 根据业务不同 同步自己的业务数据
        println synBody.taskId
        println synBody.dataType
        if(synBody.data){
            if(synBody.data instanceof  List){
                List list = (List)synBody.data
                for(def map : list){
                    SysDataLog sysDataLog = new SysDataLog(map.sysDataLog)
                    CacEmergency cacEmergency  = new CacEmergency(map.domainData)
                    cacEmergency.setId(map.keyId)
                    CacEmergency dbCacEmergency =  CacEmergency.findById(map.keyId)
                    print dbCacEmergency.emergencyTitle
                    if(sysDataLog.operation == DataOperation.新增.getValue()){
                        if(dbCacEmergency){
                            log.warn("新增数据时，数据已经存在，同步数据")
                            map.domainData.each{it->
                                dbCacEmergency.setProperty(it.key,cacEmergency.getAt(it.key))
                            }
                            dbCacEmergency.save(flush:true,failOnError: true)
                        }else{
                            cacEmergency.save(flush:true,failOnError: true)
                        }
                    }else if(sysDataLog.operation == DataOperation.更新.getValue()){
                        if(dbCacEmergency){
                            log.info("修改数据，使用最新的数据覆盖原有的数据")
                            map.domainData.each{it->
                                dbCacEmergency.setProperty(it.key,cacEmergency.getAt(it.key))
                            }
                            dbCacEmergency.save(flush:true,failOnError: true)
                        }else{
                            log.warn("修改数据，数据不存在，新增数据")
                            cacEmergency.save(flush:true,failOnError: true)
                        }
                    }
                }
            }
        }

        log.info("===== 服务端：3 ========== 正在保存数据")
        return new SynResult(taskId: synBody.taskId,message: "成功")
    }

}