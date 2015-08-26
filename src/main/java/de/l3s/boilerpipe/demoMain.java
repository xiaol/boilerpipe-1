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
        //List<String> strs = app.getImages("http://world.huanqiu.com/hot/2015-05/6424288.html");
        //for(int i = 0; i < strs.size(); i++) {
         //   System.out.println(strs.get(i));
        //}
        /*System.out.println(app.getTexts("http://www.chinanews.com/ty/2015/05-13/7274102.shtml"));
        System.out.println(app.getTexts("http://ent.people.com.cn/n/2015/0529/c1012-27076469.html"));
        System.out.println(app.getTexts("http://ent.sina.com.cn/y/ygangtai/2015-05-15/doc-icczmvup1760022.shtml"));
        System.out.println(app.getTexts("http://finance.sina.com.cn/stock/t/20150529/023922294284.shtml"));
        System.out.println(app.getTexts("http://video.sina.com.cn/p/ent/s/m/2015-05-29/145664999477.html"));
        System.out.println(app.getTexts("http://shanghai.xinmin.cn/xmsq/2015/08/13/28368718.html"));
        System.out.println(app.getTexts("http://www.chinanews.com/gn/2015/08-13/7465396.shtml"));*/

        //System.out.println(app.getTexts("http://www.chinanews.com/gn/2015/05-29/7308687.shtml"));
        //System.out.println(app.getTexts("http://tech.huanqiu.com/science/2015-08/7281709.html"));
        //System.out.println(app.getTexts("http://news.sohu.com/20150815/n418913897.shtml"));
        //System.out.println(app.getTexts("http://i.ifeng.com/news/sharenews.f?aid=100545006"));
        //System.out.println(app.getTexts("http://world.people.com.cn/n/2015/0518/c1002-27014217.html"));
        //System.out.println(app.getTexts("http://news.hangzhou.com.cn/gjxw/content/2015-08/16/content_5885407.htm"));
        //System.out.println(app.getTexts("http://video.sina.com.cn/p/ent/s/h/2015-08-16/144965055599.html"));
        //System.out.println(app.getTexts("http://t.people.com.cn/globaltimes/128760559"));
        //多图情况
        //System.out.println(app.getTexts("http://society.people.com.cn/n/2015/0819/c136657-27482230.html"));
        System.out.println(app.getTexts("http://www.hinews.cn/news/system/2015/08/18/017770842.shtml"));
        System.out.println(app.getTexts("http://legal.dbw.cn/system/2015/08/26/056773962.shtml"));
        System.out.println(app.getTexts("http://www.chinanews.com/ty/shipin/2015/08-20/news592773.shtml"));
        System.out.println(app.getTexts("http://laoyaoba.com/ss6/html/59/n-569659.html"));
        System.out.println(app.getTexts("http://gd.sina.com.cn/fs/tushuo/2015-08-24/164738087.html"));
        System.out.println(app.getTexts("http://fun.youth.cn/2015/0825/1852997.shtml"));
        //乱码情况
        System.out.println(app.getTexts("http://www.xilu.com/news/jixiantiaozhanyanbo_2.html"));
        //System.out.println(app.getTexts("http://society.people.com.cn/n/2015/0815/c1008-27465689.html"));
        //System.out.println(app.getTexts("http://www.chinanews.com/yl/2015/08-14/7466874.shtml"));
        //System.out.println(app.getTexts("http://society.people.com.cn/n/2015/0814/c1008-27462647.html"));
        //System.out.println(app.getTexts("http://finance.sina.com.cn/china/dfjj/20150814/233822970717.shtml"));
        //System.out.println(app.getTexts("http://www.nbd.com.cn/articles/2015-08-15/938657.html"));
        //System.out.println(app.getTexts("http://ent.people.com.cn/n/2015/0815/c1012-27466196.html"));
        //System.out.println(app.getTexts("http://www.chinanews.com/yl/2015/08-15/7469383.shtml"));

       //System.out.println(app.getTexts("http://news.ifeng.com/a/20150514/43758870_0.shtml?tp=1431532800000"));
        //System.out.println(app.getTexts("http://news.vdfly.com/star/201505/441921.html"));
        //System.out.println(app.getTexts("http://news.southcn.com/community/content/2015-05/10/content_123972775.htm"));
        //System.out.println(app.getTexts("http://hznews.hangzhou.com.cn/shehui/content/2015-03/25/content_5702863.htm"));
    }
}
