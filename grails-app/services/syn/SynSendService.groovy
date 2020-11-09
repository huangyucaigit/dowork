package syn

import base.ISynchronized
import dowork.CacEmergencyService
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.springframework.beans.factory.annotation.Autowired
import pojo.SynResult

import javax.annotation.PostConstruct

/**
 * 接收sysDataLog委派给实际任务执行者
 * @see SynProcessorService
 */
class SynSendService {

    static  Map<String, ISynchronized> targets = new HashMap<>()

    @Autowired
    CacEmergencyService cacEmergencyService

    static {

    }
    @PostConstruct
    private void init() {
        targets.put(cacEmergencyService.dataType(), cacEmergencyService)

    }

    /**
     * 获取数据发送给服务端去执行数据保存操作
     * @param dataType       任务功能名称
     * @param sysDataLogList    当前组数据
     * @return
     */
    def execute(String dataType,String taskId,List<SysDataLog> sysDataLogList) {
        if(sysDataLogList == null || sysDataLogList.isEmpty()){
            log.warn("《${dataType}》taskId=${taskId} 没有同步日志 ")
            return new SynResult(error:"99",message: "《${dataType}》taskId=${taskId} 没有同步日志",taskId:taskId)
        }
        if(dataType != sysDataLogList.get(0).sysFunction){
            return new SynResult(error:"98",message: "不能在《${dataType}》中执行《${sysDataLogList.get(0).sysFunction}》的任务 taskId=${taskId}",taskId:taskId)
        }
        ISynchronized iSynchronized = targets.get(dataType)
        if(iSynchronized){
            log.info("========== 客户端：2 任务执行者 ==> ${iSynchronized}")
            try{
                SynResult synResult = iSynchronized.doSynchronized(sysDataLogList)
                log.info("========== 客户端：5 ========== 保存同步结果 ==> ${synResult}")
                return  synResult
            }catch( e){
                e.printStackTrace()
                log.error("执行《${dataType}》任务异常 " + e.getMessage())
                if(e instanceof  HttpClientResponseException){
                    return  new SynResult(error:"500",message: "客户端：2 执行《${dataType}》任务异常 " + e.getMessage(),taskId:taskId)
                }else{
                    return  new SynResult(error:"500",message: "客户端：2 执行《${dataType}》任务异常 " + e.getMessage(),taskId:taskId)
                }
            }
        }else{
            log.warn("========== 客户端：2-end 未找到任务执行者 dataType ==> ${dataType}")
            return  new SynResult(error:"404",message: "客户端：2 未找到任务执行者",taskId:taskId)
        }
    }
}
