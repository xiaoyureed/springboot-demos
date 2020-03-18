/*
 * Copyright (c) 2018. Lemon tree lemon orz
 */

package io.github.xiaoyureed;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @auther: xiaoyu
 * @date: 2018/10/29 17:47
 * @description: new simple file upload api
 */
@WebServlet("/upload")
@MultipartConfig(maxFileSize = 16177215, location = "d:/upload/")    // upload file's size up to 16MB
// indicate that "file handling" servlet
// specific file base dir
@Log4j2
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.service(req, resp);
        log.debug(">>> Enter [FileUpload] servlet");

        Part   file              = req.getPart("file");// 二进制数据
        String contentType       = file.getContentType();
        String name              = file.getName();//name in form
        long   size              = file.getSize() / 1024L;// unit: KB
        String submittedFileName = file.getSubmittedFileName();

        String customName = req.getParameter("name");// 文本数据

        file.write(submittedFileName);


        PrintWriter writer = resp.getWriter();
        writer.write("file type: " + contentType + "<br>");
        writer.write("file name in form: " + name + "<br>");
        writer.write("file size: " + size + "KB<br>");
        writer.write("submit file name: " + submittedFileName + "<br>");
        writer.write("custom file name: " + customName + "<br>");

        writer.flush();
        writer.close();

    }
}
