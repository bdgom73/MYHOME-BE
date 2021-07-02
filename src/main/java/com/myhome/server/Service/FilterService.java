package com.myhome.server.Service;

import com.myhome.server.Entity.Word.FilterText;
import com.myhome.server.Repository.Word.FilterTextRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FilterService {

    private final FilterTextRepository filterTextRepository;

    public FilterService(FilterTextRepository filterTextRepository) {
        this.filterTextRepository = filterTextRepository;
    }

    public String filter(String text){
        String ft = "";
        Optional<FilterText> account = filterTextRepository.findByKinds("account");
        if(account.isEmpty()) ft = "어드민,admin,master,운영자,마스터,mydomus,sibal,sival,씨발,시발,씹,좆,병신,병50,섹스,보지,자지,sex,cex,야동,지랄,오랄,ㄴㅇㅁ,애미,ㄴㄱㅁ,느금마,凸,새끼,아가리,아갈,호로";
        else ft = account.get().getText();
        String[] filter_text = ft.split("[,]");
        String buf = text;
        for (String s : filter_text) {
            if(text.contains(s)) buf = maskWord(buf,s);
        }
        return buf;
    }


    public static String maskWord(String text ,String str){
        StringBuffer buf = new StringBuffer();
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            buf.append("*");
        }
        String replace = text.replace(str, buf);
        return replace;
    }

}
