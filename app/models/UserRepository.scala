package models

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class UserTableDef(tag: Tag) extends Table[User](tag, "user"){
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")
  def age = column[Long]("age")

  override def * =
    (id, name, age) <>(User.tupled, User.unapply)
}

class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                              (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile]{
  // DB設定の読み込み
  // HasDatabaseConfigProviderに含まれているので不要
  // private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // dbをスコープに取り込みます。これにより、実際のdb操作が可能になります。
  // HasDatabaseConfigProviderに含まれているので不要
  // import dbConfig._
  // Slick DSLをスコープに取り込みます。これにより、テーブルやその他のクエリを定義できます。
  import profile.api._


  val users = TableQuery[UserTableDef]


  def add(user: User): Future[String] = {
    dbConfig.db.run(users += user).map(res => "User successfully added").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(users.filter(_.id === id).delete)
  }

  def findById(id: Long): Future[Option[User]] = {
    dbConfig.db.run(users.filter(_.id === id).result.headOption)
  }

  def findAll: Future[Seq[User]] = {
    dbConfig.db.run(users.result)
  }
}
