package com.myhome.server.Service;

import com.myhome.server.Service.MyModel.MyModel;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

@Service
public class MailService {

    private String htmlString ;

    public void MailService(){
    }

//    public MailService(String url){
//        this.addTemplates(url);
//    }

    public String addTemplates(String url){
        try{
            URL webUrl = new URL(url);
            URLConnection urlConn = webUrl.openConnection();
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0"); // https를 호출시 user-agent 필요
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine;
            StringBuilder result = new StringBuilder();
            while ((inputLine = in.readLine()) != null){
                result.append(inputLine);
            }
            in.close();
            this.htmlString = result.toString();
            return result.toString();
        }catch(Exception e){
            System.out.println("exception : " + e);

        }
        return null;
    }
    public String addTemplates(String url, MyModel myModel){
        try{
            URL webUrl = new URL(url);
            URLConnection urlConn = webUrl.openConnection();
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0"); // https를 호출시 user-agent 필요
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine;
            StringBuilder result = new StringBuilder();
            while ((inputLine = in.readLine()) != null){
                result.append(inputLine);
            }
            String replaceValue = null;
            for (Iterator<?> iter = myModel.Models().entrySet().iterator(); iter.hasNext();) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
                String key = entry.getKey();
                Object value = entry.getValue();

                // 20160705 : ECN메일 변경되면서 PARTS 정보는 없어짐.
                // if(key.equals("PARTS")){
                // value = StringUtils.replace((String) value, ",", "<br>");
                // }
                if (value != null) {
                    replaceValue = StringUtils.replace(result.toString(), "${" + key + "}", value.toString());
                }
            }

            in.close();
            this.htmlString = replaceValue;
            return replaceValue;

        }catch(Exception e){
            System.out.println("exception : " + e);

        }
        return null;
    }

    public String getHTMLString(){
        return this.htmlString;
    }


}
