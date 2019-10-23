package controllers

import javax.inject._

import models.{User, UserRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.data.validation.Constraints.{max, min}
import play.api.libs.json.Json
import play.api.mvc.Results.NotFound
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
 * ユーザ作成フォーム
 */
case class CreateUserForm(name: String, age: Int)

class HomeController @Inject()(cc: MessagesControllerComponents, ur: UserRepository)
                              (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  /**
   * The mapping for the person form.
   */
  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "age" -> number.verifying(min(0), max(140))
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }

  /**
   * 管理用ホームページ表示
   * @route GET /
   */
  def index() = Action.async { implicit request =>
    ur.findAll map {
      case users => Ok(views.html.index(users, userForm))
    }
  }

  def selectUser(id: Long) = Action.async { implicit request =>
    ur.findAll map {
      case users => Ok(views.html.index(users, userForm, id))
    }
  }

  def addUser() = Action.async { implicit request =>
    // 最初にフォームをバインドし、次に結果を折りたたみ、エラーを処理する関数と成功を処理する関数を渡します。
    userForm.bindFromRequest.fold(
      //エラー関数。エラーフォームを含むインデックスページを返し、エラーを表示します。
      //また、このアクションは同期的であるため、成功した未来の結果をラップしますが、返す必要があります
      //未来は、人物作成関数が未来を返すためです。
      errorForm => {
        val a = errorForm
        ur.findAll map {
          case Seq() => NotFound("Not Found")
          case users => Ok(views.html.index(users, errorForm))
        }
      },
      // There were no errors in the from, so create the person.
      user => {
        ur.add(User(0, user.name, user.age)).map { _ =>
          // If successful, we simply redirect to the index page.
          Redirect(routes.HomeController.index).flashing("success" -> "user.created")
        }
      }
    )
  }

  def updateUser(id: Long) = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      errorForm => {
        val a = errorForm
        ur.findAll map {
          case Seq() => NotFound("Not Found")
          case users => Ok(views.html.index(users, errorForm))
        }
      },
      user => {
        ur.update(User(id, user.name, user.age)).map { _ =>
          // If successful, we simply redirect to the index page.
          Redirect(routes.HomeController.index).flashing("success" -> "user.created")
        }
      }
    )
  }

  def deleteUser(id: Long) = Action.async { implicit request =>
    ur.deleteById(id).map{ _ =>
      Redirect(routes.HomeController.index).flashing("success" -> "user.deleted")
    }
  }
}
