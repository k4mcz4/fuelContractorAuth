package com.fuelContractorAuth.apiQuery

import com.fuelContractorAuth.dataClasses.CharacterModel

enum class Modules {
    character,
    corporation;
}

enum class CharacterFirstParameters {

    bookmarks,
    calendar,
    agents_research,
    blueprints,
    contracts,
    mining,
    mail,
    orders,
    wallet,
    industry,
    assets;
}

enum class SecondParameters {
    transactions,
    locations,
    names,
    jobs,
    journal,
    extractions;
}

enum class CorporationFirstParameters {
    bookmarks,
    contracts,
    blueprints,
    divisions,
    facilities,
    members,
    roles,
    shareholders,
    structures;
}




