package dowork


import grails.rest.Resource

/**
 * 应急事件
 */
class CacEmergency {
    String id		// 事件ID
    String cacEmergencyNo		// 事件编号
    String emergencyTitle		// 事件标题
    String emergencyContent		// 事件内容
    String emergencyLevel		// 事件等级
    String emergencyType		// 事件类型
    String emergencySendOfficeCode		// 事件发起单
    String emergencySendUserCode		// 事件发起人
    Date emergencySendTime		// 事件发送时间
    String emergencyReceiveOfficeCode		// 事件接收单位
    Date emergencyReceiveTime		// 事件接收时间
    String emergencyStatus		// 事件状态

    Date createDate  // 创建时间
    String createId  // 创建人id
    String createBy  //
    Date updateDate  //
    String updateId  // 修改人
    String updateBy


    static mapping = {
        version false
        id generator:'assigned',column: 'cac_emergency_id',maxSize: 20 // 主键字段定义为cac_emergency_id

    }
    static constraints = {
        emergencyReceiveOfficeCode nullable: true // nullable: true  表示允许为空
        emergencySendTime nullable: true
        emergencyReceiveTime nullable: true
    }
}
