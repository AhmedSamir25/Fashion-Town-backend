import com.fashiontown.models.UserModel
import com.fashiontown.db.DatabaseConnection
import com.fashiontown.entities.UserEntity
import com.fashiontown.models.ResponseApp
import com.fashiontown.models.UserSignIn
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

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
                       HttpStatusCode.BadRequest, ResponseApp(
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
                       HttpStatusCode.OK, ResponseApp(
                           data = "done sign up",
                           success = true,)
                   )
        }
        }
        post("/signin"){
            val user = call.receive<UserModel>()
            val email = user.email.toLowerCase()
            val password = user.password
            val userCheck = db.from(UserEntity).select().where(
                UserEntity.email eq email).map{
                   val email =  it[UserEntity.email]!!
                val password = it[UserEntity.password]!!
                val  name = it[UserEntity.name]!!
                val  id = it[UserEntity.id]!!
                it[UserEntity.password]
                UserSignIn(id,name,email,password)
                }.firstOrNull()

            if(userCheck == null ){
                call.respond(
                    HttpStatusCode.BadRequest, ResponseApp(
                        data = "email and password is wrong",
                        success = false,))
                return@post
            }
            val  passwordMatch = BCrypt.checkpw(password,userCheck?.password)
            if ( !passwordMatch){
                call.respond(
                    HttpStatusCode.BadRequest, ResponseApp(
                        data = "email and password is wrong",
                        success = false,))
                return@post

            }

                call.respond(
                    HttpStatusCode.OK, ResponseApp(
                        data = "good",
                        success = true,))


        }
    }}