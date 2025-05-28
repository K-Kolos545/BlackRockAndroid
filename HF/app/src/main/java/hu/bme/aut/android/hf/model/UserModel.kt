package hu.bme.aut.android.hf.model

data class UserModel(
    val name : String = "",
    val email : String = "",
    val uid : String = "",
    val cartItems : Map<String,Long> = emptyMap(),
    val favoriteItems : List<String> = emptyList(),
    val tastedItems : List<String> = emptyList()

)