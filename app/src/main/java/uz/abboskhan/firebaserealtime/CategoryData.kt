package uz.abboskhan.firebaserealtime

class CategoryData {
    var id: String? = ""
    var category: String? = ""
    var timesTamp: Long? = 0


    constructor()
    constructor(id: String?, category: String?, timesTamp: Long?) {
        this.id = id
        this.category = category
        this.timesTamp = timesTamp

    }
}
