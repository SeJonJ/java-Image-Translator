package HJproject.Hellospring.Controller;

import HJproject.Hellospring.WordTrans.PapagoAPI;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Controller
@RequiredArgsConstructor
public class TransController {
    @GetMapping("/")
    public String home(){
        return "thymeleaf/dataplay/wordtrans";
    }

    @RequestMapping(value = "/transword", method = RequestMethod.GET)
    public void sendData(HttpServletResponse res, HttpServletRequest req) throws IOException {
        String text = req.getParameter("text");
        String source = req.getParameter("source");
        String target = req.getParameter("target");
        //System.out.println("text : "+text);

        String trans = new PapagoAPI().translator(text, source, target);

        //System.out.println(trans);

        res.setContentType("text/plain;charset=utf-8");
        PrintWriter out = res.getWriter();
        out.println(trans);

        //System.out.println(trans);

    }
}
