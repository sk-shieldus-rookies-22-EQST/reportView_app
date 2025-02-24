package f0.c.rootread.model.kms

data class GetKeyRequest(
    val rsa_public_key: String,
    val verify_url: String ?= null
)
