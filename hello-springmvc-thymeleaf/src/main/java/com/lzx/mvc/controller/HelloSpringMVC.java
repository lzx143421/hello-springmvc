package com.lzx.mvc.controller;

import com.lzx.mvc.pojo.Student;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Controller
public class HelloSpringMVC {

    @RequestMapping("/")
    public String toIndex(){
        return "index";
    }

    @RequestMapping("/hello")
    public String toShow(){
        return "show";
    }

    @RequestMapping("/test")
    public String toBean(Student student){
        System.out.println(student);
        return "show";
    }

    @RequestMapping("/testrequest")

    public ModelAndView testRequest(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("context","asdfasdf");
        mav.addObject("context","大发噶发");
        mav.setViewName("show");
        return mav;
    }

    @RequestMapping("/testDownload")
    public ResponseEntity<byte[]> testDownload(HttpSession session) throws IOException {
        ServletContext context = session.getServletContext();
        String realPath = context.getRealPath("/static/img/1.png");
        System.out.println(realPath);
        FileInputStream is = new FileInputStream(realPath);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        MultiValueMap<String,String> headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment;filename=图片.png");
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode);
        is.close();
        return responseEntity;
    }

    @RequestMapping("/testUpload")
    public String testUpload(MultipartFile photo,HttpSession session) throws IOException {
        String fileName = photo.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID().toString()+suffix;
        ServletContext c = session.getServletContext();
        String photoPath = c.getRealPath("photo");
        File file = new File(photoPath);
        if(!file.exists()){
            file.mkdir();
        }
        String finalPath = photoPath+File.separator+fileName;
        photo.transferTo(new File(finalPath));
        return "show";
    }
}
