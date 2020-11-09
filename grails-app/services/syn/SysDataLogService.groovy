package syn


import grails.gorm.services.Service
import org.hibernate.transform.Transformers
import org.hibernate.type.StandardBasicTypes
import org.springframework.beans.factory.annotation.Autowired
import pojo.SysDataGroup

interface ISysDataLogService {

    SysDataLog get(Serializable id)

    List<SysDataLog> list(Map args)

    Long count()

    void delete(Serializable id)

    SysDataLog save(SysDataLog sysDataLog)

    //执行同步
    void doSynchronized()

    /**
     * 执行单个同步
     * @param sysFunction 功能名称
     * @param sysDataLogList 功能日志  seqFirst相同的一组日期
     */
    def doSynchronizedByTaskId(String sysFunction,String taskId)

    // 一次找出10个未处理的同步任务
    //SELECT seq_first groupId,count(*) groupNum,min(sys_data_log_id) minId ,min(time_reg) minTimeReg FROM SYS_DATA_LOG WHERE  type_log   = '0' AND  is_success IN ('0','2')  GROUP BY seq_first ORDER BY max(sys_data_log_id)
    List<SysDataGroup> findAllSysDataGroup(int limit)

}

@Service(SysDataLog)
abstract class SysDataLogService implements ISysDataLogService {

    @Autowired
    SynSendService synSendService

    List<SysDataGroup>  findAllSysDataGroup(int limit){
         def list = SysDataLog.createCriteria().list {
            resultTransformer(Transformers.aliasToBean(SysDataGroup))
            projections {
                sqlGroupProjection 'sys_function dataType ,seq_first groupId,count(*) groupNum,min(sys_data_log_id) minId ,min(time_reg) minTimeReg', 'sys_function,seq_first',['dataType','groupId', 'groupNum','minId','minTimeReg'], [StandardBasicTypes.STRING,StandardBasicTypes.LONG, StandardBasicTypes.LONG, StandardBasicTypes.STRING, StandardBasicTypes.TIMESTAMP]
            }
            if(limit){
                maxResults(limit)
            }
        }
        return list
    }

    void doSynchronized(){
        List<SysDataGroup>  sysDataGroupList = findAllSysDataGroup(1)
        log.info("========== 客户端：start ========== ${sysDataGroupList.size()} 个任务正在发起同步")
        for (SysDataGroup sysDataGroup : sysDataGroupList){
            doSynchronizedByTaskId(sysDataGroup.dataType,sysDataGroup.groupId)
        }
    }
    def doSynchronizedByTaskId(String sysFunction,String taskId){
        List<SysDataLog> sysDataLogList = []
        if(taskId && taskId.isLong()){
            sysDataLogList = SysDataLog.findAllBySeqFirst(Long.valueOf(taskId),[order:"seqSecond"])
        }
        log.info("========== 客户端：1 ========== 获取《${sysFunction}》 SysDataLog.seqFirst =  ${taskId} 系统日志 ")
        return synSendService.execute(sysFunction,taskId,sysDataLogList)
    }
}