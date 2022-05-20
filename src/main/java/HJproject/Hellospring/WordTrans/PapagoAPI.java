package HJproject.Hellospring.WordTrans;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PapagoAPI {

    public String translator(String word, String source, String target) {
        String clientId = APIdata.naverPaPagoSearchID; //애플리케이션 클라이언트 아이디값";
        String clientSecret = APIdata.naverPaPagoSecret; //애플리케이션 클라이언트 시크릿값";

        String apiURL = "https://openapi.naver.com/v1/papago/n2mt"; // 검색 url
        String text;
        try {
            text = URLEncoder.encode(word, "UTF-8"); // 번역할 내용
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String responseBody = post(apiURL, requestHeaders, text, source, target);


        return parsingTrans(responseBody);
//        return responseBody;
    }

    private String parsingTrans(String responseBody){
        // JSON 파싱하는 객체
        JSONParser parser = new JSONParser();
        String transText = null;
        try {
            // response 파싱
            JSONObject parsing = (JSONObject)parser.parse(responseBody);

            // response 파싱한 결과에서 message 에 대한 값을 가져와서 obj 에 저장
            JSONObject obj = (JSONObject) parsing.get("message");
            System.out.println("obj : "+obj.toString());
            // obj 에서 다시 result 에 대한 값을 가져와서 저장
            JSONObject result = (JSONObject)obj.get("result");
            System.out.println("result : "+result.toString());
            // result 에서 translatedText 를 가져와서 text 로 저장한 후 return
            transText = result.get("translatedText").toString();
            //System.out.println(text);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return transText;
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text, String source, String target){
        HttpURLConnection con = connect(apiUrl);
        String postParams = "source=en&target=ko&text=" + text; // source=원본언어&target=번역언어
        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();

        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

//    public static void main(String[] args) {
//        String word = "My drawing was not a picture of  hat. It was a\n" +
//                "picture of a boa constrictor digesting an elephant,\n" +
//                "Then | drew the inside of the boa constrictor, so\n" +
//                "the grownups could understand.\n" +
//                "\n" +
//                "They always need explanations. My drawing\n" +
//                "Number Two looked like this.\n";
//
//        String api  = new PapagoAPI().translator(word, "eng", "kor");
//        System.out.println(api);
        // {"message":{"result":{"tarLangType":"ko","translatedText":"내가 그린 그림은 모자 그림이 아니었다. 그것은였다.\n보아뱀이 코끼리를 소화시키는 사진\n그리고 나서 |는 보아 구속자의 내부를 그렸습니다, 그래서\n어른들은 이해할 수 있었다.\n\n그들은 항상 설명이 필요하다. 내 그림\n2번은 이랬어요.","tarDict":null,"srcLangType":"en","engineType":"UNDEF_MULTI_SENTENCE","pivot":null,"dict":null},"@type":"response","@version":"1.0.0","@service":"naverservice.nmt.proxy"}}

//        String response = new PapagoAPI().translator(word, "eng", "kor");
//
//        // JSON 파싱하는 객체
//        JSONParser parser = new JSONParser();
//        try {
//            // response 파싱
//            JSONObject parsing = (JSONObject)parser.parse(response);
//
//            // response 파싱한 결과에서 message 에 대한 값을 가져와서 obj 에 저장
//            JSONObject obj = (JSONObject) parsing.get("message");
//            // obj 에서 다시 result 에 대한 값을 가져와서 저장
//            JSONObject result = (JSONObject)obj.get("result");
//            // result 에서 translatedText 를 가져와서 text 로 저장한 후 return
//            String text = result.get("translatedText").toString();
//            System.out.println(text);

//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//    }
}
