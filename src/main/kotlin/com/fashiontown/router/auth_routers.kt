import com.fashiontown.models.UserModel
import com.fashiontown.db.DatabaseConnection
import com.fashiontown.entities.UserEntity
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.ktorm.dsl.insert

fun Application.authRoutes (){
    val db = DatabaseConnection.database
    routing {
        post("signup"){
            val user = call.receive<UserModel>()
            val name = user.name.toLowerCase()
            val password = user.EncryptingPassword()
                db.insert(UserEntity){
                    set(it.name,name)
                    set(it.password,password)
                }
        }
    }}