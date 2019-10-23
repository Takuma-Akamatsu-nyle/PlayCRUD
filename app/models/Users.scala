//package models
//
//import javax.inject.Inject
//import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
//import slick.jdbc.JdbcProfile
//import slick.jdbc.MySQLProfile.api._
//
//import scala.concurrent.{ExecutionContext, Future}
//
//class UserTableDef(tag: Tag) extends Table[User](tag, "user"){
//  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
//  def firstName = column[String]("first_name")
//  def lastName = column[String]("last_name")
//  def mobile = column[Long]("mobile")
//  def email = column[String]("email")
//
//  override def * =
//    (id, firstName, lastName, mobile, email) <>(User.tupled, User.unapply)
//}
//
//class Users @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
//                     (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{
//  // the HasDatabaseConfigProvider trait gives access to the
//  // dbConfig object that we need to run the slick queries
//
//  val users = TableQuery[UserTableDef]
//
//  def add(user: User): Future[String] = {
//    dbConfig.db.run(users += user).map(res => "User successfully added").recover{
//      case ex: Exception => ex.getCause.getMessage
//    }
//  }
//
//  def delete(id: Long): Future[Int] = {
//    dbConfig.db.run(users.filter(_.id === id).delete)
//  }
//
//  def get(id: Long): Future[Option[User]] = {
//    dbConfig.db.run(users.filter(_.id === id).result.headOption)
//  }
//
//  def listAll: Future[Seq[User]] = {
//    dbConfig.db.run(users.result)
//  }
//}