package by.dima00138.coursework

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import by.dima00138.coursework.Models.IModel
import by.dima00138.coursework.Models.ScheduleItem
import by.dima00138.coursework.Models.Station
import by.dima00138.coursework.Models.Ticket
import by.dima00138.coursework.Models.Train
import by.dima00138.coursework.Models.User
import by.dima00138.coursework.viewModels.Tables
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

class Firebase @Inject constructor(private var context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun createUserWithEmail(
        user: User,
        onCompleteListener: (AuthResult) -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        try {
            val result = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            user.id = result.user!!.uid
            db.collection("users").document(result.user!!.uid)
                .set(user.toFirebase())
            onCompleteListener(result)
        } catch (e: Exception) {
            onFailureListener(e)
            Log.d("Error", "register")
            Log.d("Error", e.message.toString())
            Log.d("Error", "user.fullName ${user.fullName}")
            Log.d("Error", "user.uid ${user.id}")
            Log.d("Error", "user.password ${user.password}")
            Log.d("Error", "user.passport ${user.passport}")
            Log.d("Error", "user.email ${user.email}")
            Log.d("Error", "user.birthdate ${user.birthdate}")
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun signUpWithEmail(
        user: User,
        onCompleteListener: (AuthResult, User?) -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        try {
            val result = auth.signInWithEmailAndPassword(user.email, user.password).await()
            val us = getUser()
            onCompleteListener(result, us)
        } catch (e: Exception) {
            onFailureListener(e)
            Log.d("Error", "signInEmail")
            Log.d("Error", e.message.toString())
        }
    }

    /*    suspend fun signInWithGithub(activity: Activity, onCompleteListener: (AuthResult) -> Unit, onFailureListener: (Exception) -> Unit) {
        val provider = OAuthProvider.newBuilder("github.com")

        try{

            auth.startActivityForSignInWithProvider(activity, provider.build())

        }catch (e: Exception) {
            onFailureListener(e)
            Log.d("Error", "signInGithub")
            Log.d("Error", e.message.toString())
        }
    }*/

    fun getCredentials(context: Context): GetCredentials {
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
        onFailureListener: (Exception) -> Unit
    ) {
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
                id = resultAuth.user!!.uid,
                fullName = resultAuth.user!!.displayName.toString(),
                email = resultAuth.user!!.email.toString(),
                role = "user"
            )
            db.collection("users").document(resultAuth.user!!.uid).set(user.toFirebase())
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

    fun createOrReplaceItem(table: Tables, item: IModel) {
        try {
            db.collection(table.str.lowercase()).document(item.getField("id").toString())
                .set(item.toFirebase())
        } catch (e: Exception) {
            Log.d("D", e.message.toString())
        }
    }

    suspend fun getStations(): List<Station>? {
        val arr: MutableList<Station> = mutableListOf()
        return try {
            val documents = db.collection("stations").get().await()
            for (doc in documents) {
                arr.add(doc.toObject<Station>())
            }
            arr
        } catch (e: Exception) {
            Log.d("E", e.message.toString())
            null
        }
    }

    suspend fun getTrains(): List<Train>? {
        val arr: MutableList<Train> = mutableListOf()
        return try {
            val documents = db.collection("trains").get().await()
            for (doc in documents) {
                arr.add(doc.toObject<Train>())
            }
            arr
        } catch (e: Exception) {
            Log.d("E", e.message.toString())
            null
        }
    }

    suspend fun getTickets(): List<Ticket>? {
        val arr: MutableList<Ticket> = mutableListOf()
        return try {
            val documents = db.collection("tickets").get().await()
            for (doc in documents) {
                arr.add(doc.toObject<Ticket>())
            }
            arr
        } catch (e: Exception) {
            Log.d("E", e.message.toString())
            null
        }
    }


    suspend fun getSchedule(): List<ScheduleItem>? {
        val arr: MutableList<ScheduleItem> = mutableListOf()
        return try {
            val documents = db.collection("schedule").get().await()
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            for (doc in documents) {
                val item = ScheduleItem(
                    id = doc["id"].toString(),
                    from = doc["from"].toString(),
                    to = doc["to"].toString(),
                    date = LocalDateTime.ofEpochSecond(
                        doc["date"].toString().toLong(),
                        0,
                        ZoneOffset.UTC
                    ).format(formatter)
                )
                arr.add(item)
            }
            arr
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            null
        }
    }

    suspend fun getSchedule(
        direction: String,
        station: Station
    ): List<ScheduleItem>? {
        val arr: MutableList<ScheduleItem> = mutableListOf()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val equal = when (direction) {
            "arrival" -> "from"
            else -> "to"
        }
        return try {
            val documentsSchedule = db.collection("schedule")
                .whereEqualTo(equal, station.id).get().await()

            val stationMap = mutableMapOf<String, Station>()
            val documentsStations = db.collection("stations")
                .whereIn("id", documentsSchedule.map { it["to"].toString() } + documentsSchedule.map { it["from"].toString() }).get().await()
            for (st in documentsStations) {
                stationMap[st.id] = st.toObject<Station>()
            }

            for (doc in documentsSchedule) {
                val fromStation = stationMap[doc["from"].toString()]
                val toStation = stationMap[doc["to"].toString()]
                val item = ScheduleItem(
                    id = doc["id"].toString(),
                    from = fromStation?.name ?: doc["from"].toString(),
                    to = toStation?.name ?: doc["to"].toString(),
                    date = LocalDateTime.ofEpochSecond(
                        doc["date"].toString().toLong(),
                        0,
                        ZoneOffset.UTC
                    ).format(formatter)
                )
                arr.add(item)
            }
            arr
        } catch (e: Exception) {
            Log.d("D", e.message.toString())
            null
        }
    }

    suspend fun getUsers() : List<User>? {
        val arr: MutableList<User> = mutableListOf()
        return try {
            val documents = db.collection("users").get().await()
            for (doc in documents) {
                arr.add(doc.toObject<User>())
            }
            arr
        } catch (e: Exception) {
            Log.d("D", e.message.toString())
            null
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


    data class GetCredentials(
        val credentialManager: CredentialManager,
        val request: GetCredentialRequest
    )
}