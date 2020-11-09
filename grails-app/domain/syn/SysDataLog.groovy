package syn

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='id')
@ToString(includes='id,tableName,keyId,sysFunction,userCode,seqFirst,seqSecond', includeNames=true, includePackage=false)
class SysDataLog {

     String id		// 系统数据变更日志ID
     String userCode		// 用户编码
     String sysFunction		// 系统功能
     String terminal		// 终端
     String source		// 来源
     Long typeLog		// 日志类型（0:上报 1:下达）
     String operation		// 操作， 新增、 更新、 删除
     String tableName		// 表名
     String keyId		// 主键ID
     Long seqFirst		// 大顺序
     Long seqSecond		// 小顺序
     Date timeReg		// 登记时间
     Long cntIntegration		// 集成次数，用于记录与第三方系统集成同步数据的次数
     Long isSuccess		// 上传状态（0.待上传/下达  1.成功 2.失败）
     String logText		// 日志
     Date timeBack		// 回复时间


     static mapping = {
          version false
          id generator:'id.SnowflakeIdGenerator',column: 'sys_data_log_id',maxSize: 20 // 主键字段定义为cac_emergency_id
     }
    static constraints = {
         source nullable: true
         logText nullable: true, column: 'log'
         timeBack nullable: true
    }


}
