package com.fuelContractorAuth.apiQuery

class ApiQueryFactory(module: Module,param: ModuleParameter){

    val apiUrl = "https://esi.evetech.net/latest/${module.moduleName}/${module.moduleValue}$param"

}

class ModuleParameter()

abstract class Module(val moduleName: String, val moduleValue: Int)