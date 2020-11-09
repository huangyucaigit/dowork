package base

import pojo.SynBody
import pojo.SynResult
import syn.SysDataLog

interface ISynchronized {
    /**
     * 功能名称 对应数据库 SysDataLog.sysFunction 字段
     * @return
     */
    String dataType()

    /**
     * 将需要同步的数据发送至  saveSynProcessor 处理
     * @return
     */
    SynResult doSynchronized(List<SysDataLog> sysDataLog)
    /**
     * 接收数据 同步到数据库
     * @param sysDataLog
     */
    SynResult saveSynProcessor(SynBody synBody)



}