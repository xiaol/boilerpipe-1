package de.l3s.boilerpipe;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.document.Image;
import de.l3s.boilerpipe.extractors.ChineseArticleExtractor;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.ImageExtractor;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Created by ivan liu on 15/5/11.
 */
public class demoMain {
    private final BoilerpipeExtractor extractor = CommonExtractors.CHINESE_ARTICLE_EXTRACTOR;
    private final ImageExtractor ie = ImageExtractor.INSTANCE;
    private final ChineseArticleExtractor ae = ChineseArticleExtractor.INSTANCE;

    public List<String> getImages(String address) throws Exception {
        URL url = new URL(address);
        List<Image> imgUrls = ie.process(url, extractor);
        Collections.sort(imgUrls);
        List<String> results=new ArrayList<String>();
        for (Image img : imgUrls) {
            results.add(img.getSrc());
        }
        Collections.reverse(results);
        return results;
    }

    public String getTexts(String address) throws Exception {
        URL url = new URL(address);
        return ae.getText(url);
    }

    public static void main(String[] args) throws Exception {
        demoMain app = new demoMain();
        //List<String> strs = app.getImages("http://hznews.hangzhou.com.cn/shehui/content/2015-03/25/content_5702863.htm");
        List<String> strs = app.getImages("http://world.huanqiu.com/hot/2015-05/6424288.html");
        for(int i = 0; i < strs.size(); i++) {
            System.out.println(strs.get(i));
        }
        //System.out.println(app.getTexts("http://www.chinanews.com/ty/2015/05-13/7274102.shtml"));
        //System.out.println(app.getTexts("http://ent.people.com.cn/n/2015/0529/c1012-27076469.html"));
        //System.out.println(app.getTexts("http://ent.sina.com.cn/y/ygangtai/2015-05-15/doc-icczmvup1760022.shtml"));
        //System.out.println(app.getTexts("http://finance.sina.com.cn/stock/t/20150529/023922294284.shtml"));
        System.out.println(app.getTexts("http://video.sina.com.cn/p/ent/s/m/2015-05-29/145664999477.html"));
        //System.out.println(app.getTexts("http://www.chinanews.com/gn/2015/05-29/7308687.shtml"));

        //System.out.println(app.getTexts("http://news.ifeng.com/a/20150514/43758870_0.shtml?tp=1431532800000"));
        //System.out.println(app.getTexts("http://news.vdfly.com/star/201505/441921.html"));
        //System.out.println(app.getTexts("http://news.southcn.com/community/content/2015-05/10/content_123972775.htm"));
        //System.out.println(app.getTexts("http://hznews.hangzhou.com.cn/shehui/content/2015-03/25/content_5702863.htm"));
    }
}
