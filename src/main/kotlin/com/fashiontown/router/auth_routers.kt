import com.fashiontown.models.UserModel
import com.fashiontown.db.DatabaseConnection
import com.fashiontown.entities.UserEntity
import com.fashiontown.models.ResponseUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.authRoutes (){
    val db = DatabaseConnection.database
    routing {
        post("/signup"){
            val user = call.receive<UserModel>()
            val name = user.name.toLowerCase()
            val email = user.email.toLowerCase()
            val password = user.EncryptingPassword()
            val emailVali = db.from(UserEntity).select().where(
                UserEntity.email eq email).map{it[UserEntity.email]}.firstOrNull()
               if (emailVali != null ){

                   call.respond(
                       HttpStatusCode.BadRequest, ResponseUser(
                           data = "email already exits",
                           success = false,))
                   return@post
        }else{
                   db.insert(UserEntity){
                       set(it.email,email)
                       set(it.name,name)
                       set(it.password,password)
                   }
                   call.respond(
                       HttpStatusCode.OK, ResponseUser(
                           data = "done sign up",
                           success = true,)
                   )
        }
        }
        post("/signin"){

        }
    }}