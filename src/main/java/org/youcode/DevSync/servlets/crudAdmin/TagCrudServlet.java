package org.youcode.DevSync.servlets.crudAdmin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.youcode.DevSync.dao.impl.TagDAOImpl;
import org.youcode.DevSync.domain.entities.Tag;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class TagCrudServlet extends HttpServlet {
    private TagDAOImpl tagDAO;

    @Override
    public void init() throws ServletException {
        tagDAO = new TagDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    UUID id = UUID.fromString(idParam);
                    boolean deleted = tagDAO.delete(id);
                    if (deleted) {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                } catch (IllegalArgumentException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Invalid UUID format.");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("ID parameter is missing or empty.");
            }
            response.sendRedirect(request.getContextPath() + "/TagCrud");
        } else {
            List<Tag> tags = tagDAO.findAll();
            request.setAttribute("tags", tags);
            request.getRequestDispatcher("/admin/tag/crud-tags.jsp").forward(request, response);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        if (name != null && !name.isEmpty()) {
            List<Tag> existingTags = tagDAO.findByName(name);
            if (existingTags.isEmpty()) {
                Tag tag = new Tag();
                tag.setName(name);
                tagDAO.save(tag);
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("Tag with the same name already exists.");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.sendRedirect(request.getContextPath() + "/TagCrud");
    }

//    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String idParam = request.getParameter("id");
//        if (idParam != null && !idParam.isEmpty()) {
//            UUID id = UUID.fromString(idParam);
//            boolean deleted = tagDAO.delete(id);
//            if (deleted) {
//                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
//            } else {
//                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            }
//        } else {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        }
//    }
}