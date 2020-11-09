package pojo


import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

/**
 * 处理结果
 */
@GrailsCompileStatic
@ToString(includes='taskId,dataType,timestamp', includeNames=true, includePackage=false)
class SynBody {
    String taskId
    String dataType
    String orgCode
    String digest
    def data
    Long timestamp = System.currentTimeMillis()

}
