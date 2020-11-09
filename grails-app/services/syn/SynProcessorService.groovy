package syn

import base.ISynchronized
import dowork.CacEmergencyService
import org.springframework.beans.factory.annotation.Autowired
import pojo.SynBody
import pojo.SynResult

import javax.annotation.PostConstruct

/**
 * 接收同步任务 委派给实际任务执行者 (处理接收到的数据)
 * @see SynSendService
 */
class SynProcessorService {

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
     * 执行同步任务
     * @param dataType       任务功能名称
     * @param sysDataLogList    当前组数据
     * @return
     */
    SynResult execute(String dataType, SynBody synBody) {
        ISynchronized iSynchronized = targets.get(dataType)
        if(iSynchronized){
            log.info("===== 服务端：2 任务执行者 ==> ${iSynchronized}")
            try{
                def synResult =  iSynchronized.saveSynProcessor(synBody)
                log.info("===== 服务端：4 ========== 返回处理结果给调用端")
                return synResult
            }catch(e){
                log.error("执行《${dataType}》任务异常")
                e.printStackTrace()
                return new SynResult(taskId:synBody.taskId,message:"任务异常 " + e.getMessage(),error: "500")
            }
        }else{
            log.warn("===== 服务端：2-end ==========  未找到任务执行者 dataType ==> ${dataType}")
            return new SynResult(taskId:synBody.taskId,message:"未注册的同步任务",error: "404")
        }
    }
}
