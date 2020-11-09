package pojo

import grails.compiler.GrailsCompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 处理结果
 */
@GrailsCompileStatic
@ToString(includes='taskId,error,message,timestamp', includeNames=true, includePackage=false)
class SynResult {
    String taskId
    String message
    String error
    Long timestamp = System.currentTimeMillis()
    def data


}
