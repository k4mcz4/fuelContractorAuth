package com.fuelContractorAuth.apiQuery

class test {
    fun test() {
        println()
    }
}

enum class ModuleParameterValues(val moduleName: String, val moduleParam: ModuleParam) {
    CHARACTER("character", ModuleParam(listOf("")))

}

data class ModuleParam(val param: List<String>) {

}

enum class character {
    assets, bookmarks, calendar, agents_research, blueprints, contracts, industry, mining, mail, orders, wallet
}