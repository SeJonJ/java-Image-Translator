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
        // ajax 에서 넘어온 값을 확인 후 변수에 저장
        String text = req.getParameter("text");
        String source = req.getParameter("source");
        String target = req.getParameter("target");
        //System.out.println("text : "+text);

        // 저장된 변수를 PapagoAPI 의 translator 메소드에 넣기 -> 번역된 내용을 trans 변수 안에 저장
        String trans = new PapagoAPI().translator(text, source, target);

        //System.out.println(trans);

        // resp 객체의 문자 타입을 utf-8 로 선언 후 요청 request 가 있었던 web 으로 전달
        res.setContentType("text/plain;charset=utf-8");
        PrintWriter out = res.getWriter();
        out.println(trans);

        //System.out.println(trans);

    }
}
