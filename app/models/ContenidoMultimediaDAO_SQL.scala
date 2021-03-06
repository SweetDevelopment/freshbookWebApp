package models

import java.util.{ Date }

import play.api._
import play.api.mvc._
import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._
import models._

import org.h2.jdbc.JdbcSQLException

class ContenidoMultimediaDAO_SQL extends ContenidoMultimediaDAO {

  val parser = {
    get[Long]("contenido_multimedia.id") ~
      get[String]("contenido_multimedia.ruta") ~
      get[Date]("contenido_multimedia.fecha_subida") ~
      get[String]("contenido_multimedia.red_social") ~
      get[String]("contenido_multimedia.ruta_tumbnail") ~
      get[Long]("contenido_multimedia.fk_album") map {
        case id ~ ruta ~ fechaSubida ~ redSocial ~ rutaTumbnail ~ inAlbumId =>
          ContenidoMultimedia(id, ruta, fechaSubida, redSocial, rutaTumbnail, inAlbumId)
      }
  }

  override def insertContenidoMultimedia(newContenidoMultimedia: ContenidoMultimedia) {

    try {
      DB.withConnection { implicit connection =>
        SQL(
          """
				INSERT INTO CONTENIDO_MULTIMEDIA VALUES( nextval('seq_contenido'),{ruta},NOW(),
				{redSocial},{rutaTumbnail},{inAlbumId})
				""").on(
            'ruta -> newContenidoMultimedia.getRuta,
            'redSocial -> newContenidoMultimedia.getRedSocial,
            'rutaTumbnail -> newContenidoMultimedia.getRutaTumbnail,
            'inAlbumId -> newContenidoMultimedia.getInAlbumId).executeUpdate()

      }
    } catch {
      case e: JdbcSQLException => throw DAOException.create(e.getMessage())
    }

  }

  override def getParser(): RowParser[ContenidoMultimedia] = {

    return this.parser;
  }

  def deleteContenidoMultimedia(contentId: Long) = {

    try {
      DB.withConnection { implicit connection =>
        SQL("delete from contenido_multimedia where id = {contentId}").on(
          'contentId -> contentId).executeUpdate()

      }
    } catch {
      case e: JdbcSQLException => throw DAOException.create(e.getMessage())
    }

  }

}