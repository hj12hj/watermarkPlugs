import com.hj.core.enums.WaveMarkMode;
import com.hj.word.WordDocAddWaterMark;
import com.hj.word.WordDocxAddWaterMark;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws DocumentException, IOException {
        System.out.println(System.getProperty("user.dir"));
//        AddWaterMark addWaterMark = new AddWaterMarkUtil();
//        addWaterMark.transfer("/Users/hejie/Desktop/12.docx","/Users/hejie/Desktop/1122.docx","11111");


//        PdfAddWaterMark addWaterMark = new PdfAddWaterMark(WaveMarkMode.PICTURE,"/Users/hejie/Desktop/12.png");
//        addWaterMark.transfer("/Users/hejie/Desktop/123.pdf","/Users/hejie/Desktop/1234.pdf","");
//
//        WordDocAddWaterMark wordAddWaterMark = new WordDocAddWaterMark();
//        wordAddWaterMark.setWaveMarkMode(WaveMarkMode.WORDS);
//        wordAddWaterMark.setPicPath("/Users/hejie/Desktop/12.png");
//        wordAddWaterMark.transfer("/Users/hejie/Desktop/123.doc","/Users/hejie/Desktop/11/123.doc","22222");

        String name = "/private/var/folders/2m/kg6ykz4s0mx4c8skhzym8fnw0000gn/T/tomcat.8080.4186967469485980511/work/Tomcat/localhost/ROOT/upload_ba3e64c1_be80_4483_8204_691a3efa4127_00000000.tmp";
        int i = name.lastIndexOf(".");
        System.out.println(name.substring(0,i-1));

    }
}
