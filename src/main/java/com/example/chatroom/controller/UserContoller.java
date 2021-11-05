package com.example.chatroom.controller;

import ch.qos.logback.core.util.FileUtil;
import com.example.chatroom.Entity.User;
import com.example.chatroom.service.UserService;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserContoller {

  @Autowired
  UserService userService;
  public static final int A = 1;

  @GetMapping("users")
  public int insertUser() {
    User user = new User("fgz", 1, "good");
    return userService.insterUser(user);
  }

  @GetMapping("users/list")
  public int countByUserList() {
    Map map = new HashMap();
    User user1 = new User();
    user1.setId(11);
    User user2 = new User();
    user2.setId(12);
    User user3 = new User();
    user3.setId(13);
    User user4 = new User();
    user4.setId(24);
    List<User> users = Arrays.asList(user1, user2, user3, user4);
    map.put("idList", users);
    map.put("name", "fgz");
    return userService.countByUserList(map);
  }

  @GetMapping(value = "testdevice")
  public String getDevice(HttpServletRequest request, HttpServletResponse response) {
    return request.getHeader("user-agent");
  }

  @PostMapping(value = "uploadFile")
  public void uploadFile(MultipartFile upload) {
    //判断文件是否为空
    if (upload.isEmpty() != true) {
      //获得文件名
      String name = upload.getOriginalFilename();
      ApplicationHome app = new ApplicationHome(getClass());
      String dirpath = app.getSource().getParentFile().toString();
      System.out.println(name);
    } else {
      System.out.println("上传文件为空！");
    }
  }

  @RequestMapping(value = "download")
  public void download(
      @RequestParam("fileName") String filename
  ) throws IOException {
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes();
    HttpServletResponse response = requestAttributes.getResponse();
    // 设置信息给客户端不解析
    String type = new MimetypesFileTypeMap().getContentType(filename);
    // 设置contenttype，即告诉客户端所发送的数据属于什么类型
    response.setHeader("Content-type", type);
    // 设置编码
    String hehe = new String(filename.getBytes("utf-8"), "iso-8859-1");
    // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
    response.setHeader("Content-Disposition", "attachment;filename=" + hehe);
    download1(filename, response);
  }

  public void download1(String filename, HttpServletResponse res) throws IOException {
    // 发送给客户端的数据
    OutputStream outputStream = res.getOutputStream();
    byte[] buff = new byte[1024];
    BufferedInputStream bis = null;
    // 读取filename111
    ApplicationHome app = new ApplicationHome(getClass());
    String dirpath = app.getSource().getParentFile().toString();
    bis = new BufferedInputStream(new FileInputStream(new File(dirpath + "/file/" + filename)));
    int i = bis.read(buff);
    while (i != -1) {
      outputStream.write(buff, 0, buff.length);
      outputStream.flush();
      i = bis.read(buff);
    }
  }
}
