package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;

@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CreateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
      //tokenに値が保存されてなかったり、違う値の場合にはデータの登録をしないようにしている。
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();
            em.getTransaction().begin();

            Task m = new Task();
            //idは、自動採番される。
            String content = request.getParameter("content"); //入力された"content"をセットする。
            m.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis()); //投稿時間、更新時間をセットする。
            m.setCreated_at(currentTime);
            m.setUpdated_at(currentTime);

            em.persist(m); //データベースに保存する
            em.getTransaction().commit(); //データベースへの保存を確定する
            em.close();
            response.sendRedirect(request.getContextPath() + "/index"); //indexページにリダイレクトする
        }
    }

}
