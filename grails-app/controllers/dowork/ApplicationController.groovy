package dowork

import grails.converters.XML
import grails.core.GrailsApplication
import grails.plugins.*
import pojo.SysDataGroup
import syn.SysDataLogService

class ApplicationController implements PluginManagerAware {

    GrailsApplication grailsApplication
    GrailsPluginManager pluginManager
    SysDataLogService sysDataLogService

    def taskList(){
        List<SysDataGroup> sysDataGroupList = sysDataLogService.findAllSysDataGroup(0)
        render([sysDataGroupCount:sysDataGroupList.size(),sysDataGroupList:sysDataGroupList] as XML)
    }

    def index() {
        List<SysDataGroup> sysDataGroupList = sysDataLogService.findAllSysDataGroup(100)
        [sysDataGroupList: sysDataGroupList,grailsApplication:grailsApplication, pluginManager: pluginManager]
    }
}
