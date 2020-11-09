package pojo

import grails.compiler.GrailsCompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@GrailsCompileStatic
@EqualsAndHashCode(includes='groupId')
@ToString(includes='dataType,groupId,groupNum,minTimeReg', includeNames=true, includePackage=false)
class SysDataGroup {
    Long groupId
    Long groupNum
    String minId
    Date minTimeReg
    String dataType


}
