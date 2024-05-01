package by.dima00138.coursework

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class Firebase @Inject constructor(private var context: Context) {
    private val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    suspend fun createUserWithEmail(user : User, onCompleteListener: (AuthResult) -> Unit, onFailureListener: (Exception) -> Unit) {
        try {
            val result = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            user.uid =result.user!!.uid
            db.collection("users").document(result.user!!.uid)
                .set(user)
            onCompleteListener(result)
        } catch (e: Exception) {
            onFailureListener(e)
            Log.d("Error", "register")
            Log.d("Error", e.message.toString())
            Log.d("Error", "user.fullName ${user.fullName}")
            Log.d("Error", "user.uid ${user.uid}")
            Log.d("Error", "user.password ${user.password}")
            Log.d("Error", "user.passport ${user.passport}")
            Log.d("Error", "user.email ${user.email}")
            Log.d("Error", "user.birthdate ${user.birthdate}")
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun signUpWithEmail(user : User, onCompleteListener: (AuthResult, User?) -> Unit, onFailureListener: (Exception) -> Unit) {
        try {
        val result = auth.signInWithEmailAndPassword(user.email, user.password).await()
            val user = getUser()
            onCompleteListener(result, user)
        }
        catch (e : Exception) {
            onFailureListener(e)
            Log.d("Error", "signInEmail")
            Log.d("Error", e.message.toString())
        }
    }

//    suspend fun signInWithGithub(activity: Activity, onCompleteListener: (AuthResult) -> Unit, onFailureListener: (Exception) -> Unit) {
//        val provider = OAuthProvider.newBuilder("github.com")
//
//        try{
//
//            auth.startActivityForSignInWithProvider(activity, provider.build())
//
//        }catch (e: Exception) {
//            onFailureListener(e)
//            Log.d("Error", "signInGithub")
//            Log.d("Error", e.message.toString())
//        }
//    }

    fun getCredentials(context: Context) : GetCredentials {
        val credentialManager = CredentialManager.create(context)
        val rawNonce = UUID.randomUUID().toString()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(rawNonce.toByteArray())
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setNonce(hashedNonce)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()

        return GetCredentials(credentialManager, request)
    }

    suspend fun signUpWithCredentials(
        context: Context,
        credentialManager: CredentialManager,
        request: GetCredentialRequest,
        onCompleteListener: (AuthResult) -> Unit,
        onFailureListener: (Exception) -> Unit) {
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            val credential = result.credential
            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
            val firebaseCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

            val resultAuth = auth.signInWithCredential(firebaseCredentials).await()
            val user = User(
                uid = resultAuth.user!!.uid,
                fullName = resultAuth.user!!.displayName.toString(),
                email = resultAuth.user!!.email.toString(),
                photoUrl = resultAuth.user!!.photoUrl.toString()
            )
            db.collection("users").document(resultAuth.user!!.uid).set(user)
            onCompleteListener(resultAuth)

        } catch (e: Exception) {
            onFailureListener(e)
            Log.d("Error", "signInGoogle")
            Log.d("Error", e.message.toString())
        }
    }
    suspend fun logout(onCompleteListener: () -> Unit, authStateListener: (Exception) -> Unit) {
        try {
            val result = auth.signOut()
            onCompleteListener()
            Log.d("DEBUG", auth.currentUser.toString())
        } catch (e: Exception) {
            authStateListener(e)
            Log.d("Error", "logout")
            Log.d("Error", e.message.toString())
        }
    }

    suspend fun getUser() : User? = auth.currentUser?.run {
        try {
        val doc = db.collection("users").document(uid).get().await()
        doc.toObject<User>()
        }catch (e: Exception) {
            Log.d("D", e.message.toString())
            null
        }
    }


    data class BoardList(val id: Int) {}
    data class User(var uid : String = "",
                    val fullName: String = "",
                    val passport: String = "",
                    val birthdate: String = "",
                    val email: String = "",
                    val password: String = "",
                    val photoUrl: String? = "" )

    data class GetCredentials(
        val credentialManager: CredentialManager,
        val request: GetCredentialRequest
    )
}

//class FirebasePagingSource(
//    private val query: Query,
//) : PagingSource<DocumentSnapshot, Firebase.BoardList>() {

//    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Firebase.BoardList> {
//        return try {
//            val currentPage = params.key ?: query.get().await()
//            val documents = currentPage.documents
//
//            val nextPage = if (documents.isNotEmpty()) {
//                val lastVisibleDocument = documents[documents.size - 1]
//                query.startAfter(lastVisibleDocument).get().await()
//            } else {
//                null
//            }
//
//            val data = documents.map { document ->
//                val yourProperty = document.getInt("id") ?: ""
//                Firebase.BoardList(yourProperty)
//            }
//
//            LoadResult.Page(
//                data = data,
//                prevKey = null,
//                nextKey = nextPage?.documents?.get(nextPage.documents.size - 1)
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }

//    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Firebase.BoardList>): DocumentSnapshot? {
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }
//}