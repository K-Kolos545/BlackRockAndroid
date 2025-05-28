package hu.bme.aut.android.hf.model

data class ProductModel(
    var id : String = "",
    val title: String = "",
    val description : String = "",
//    val price : String = "",
//    val actualPrice : String = "",
    val category : String = "",
    val images : List<String> = emptyList(),
    val otherDetail : Map<String, String> = mapOf(),
    //atirni detail-re, a firebase-ben is
    //ez hasznalni az az egyeb/fontos tudnivalokra
    val comments : Map<String, String> = mapOf(), //ez a userId, comment
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val placeToTaste : String = "",
)