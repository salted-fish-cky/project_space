package com.cky.demo;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@WebServlet(name = "VerifyCodeServlet", urlPatterns = "/code")
public class VerifyCodeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int width = 100;
        int height = 30;
        String data = "abcdefghijklmnopqrstuvwxyz1234567890";
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.gray);
        graphics.fillRect(0,0,width,height);
        graphics.setColor(Color.black);
        for (int i = 0; i <4 ; i++) {
            int postion = (int) (Math.random()*data.length());
            String randomStr = data.substring(postion,postion+1);
            graphics.drawString(randomStr,width/5*(i+1),15);

            System.out.println("---"+randomStr);
        }

        ImageIO.write(bufferedImage,"jpg",response.getOutputStream());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
