package io.github.xiaoyureed.springbootdemos.freemarkerh2jps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/15
 */
@Controller
public class StudentController {

    @Autowired
    private StudentRepo studentRepo;

    @RequestMapping("/list")
    public String list(Map<String, Object> map) {
        map.put("students", studentRepo.findAll());
        return "list";
    }
}
