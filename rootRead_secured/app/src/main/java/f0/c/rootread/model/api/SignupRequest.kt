package f0.c.rootread.model.api

data class SignupRequest(
    val user_id:String,
    val user_pw:String,
    val user_phone:String,
    val user_email:String
)
