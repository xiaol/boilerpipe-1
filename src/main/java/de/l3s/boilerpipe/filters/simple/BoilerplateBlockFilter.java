/**
 * Copyright (C) 2013 Christian Kohlschütter (ckkohl79@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.l3s.boilerpipe.filters.simple;

import java.util.Iterator;
import java.util.List;


import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.labels.DefaultLabels;
import de.l3s.boilerpipe.util.SuffixArray;

/**
 * Removes {@link TextBlock}s which have explicitly been marked as
 * "not content".
 * 
 * @author Christian Kohlschütter
 */
public final class BoilerplateBlockFilter implements BoilerpipeFilter {
	public static final BoilerplateBlockFilter INSTANCE = new BoilerplateBlockFilter(
			null);
	public static final BoilerplateBlockFilter INSTANCE_KEEP_TITLE = new BoilerplateBlockFilter(
			DefaultLabels.TITLE);
    public static final BoilerplateBlockFilter INSTANCE_TRIM = new BoilerplateBlockFilter(
            true);

	private  String labelToKeep;
    private  boolean isTrim = false;

	/**
	 * Returns the singleton instance for BoilerplateBlockFilter.
	 */
	public static BoilerplateBlockFilter getInstance() {
		return INSTANCE;
	}

	public BoilerplateBlockFilter(final String labelToKeep) {
		this.labelToKeep = labelToKeep;
	}

    public BoilerplateBlockFilter(boolean isTrim) {
        this.isTrim = isTrim;
    }

	public boolean process(TextDocument doc)
			throws BoilerpipeProcessingException {
		List<TextBlock> textBlocks = doc.getTextBlocks();
		boolean hasChanges = false;

		for (Iterator<TextBlock> it = textBlocks.iterator(); it.hasNext();) {
			TextBlock tb = it.next();
			if (!tb.isContent()
					&& (labelToKeep == null || !tb
							.hasLabel(DefaultLabels.TITLE))) {
				it.remove();
				hasChanges = true;
			}else if(isTrim){
                final String text = tb.getText().trim();
                final String textLC = text.toLowerCase();
                final String titleLC = doc.getTitle().trim().toLowerCase();
                if(textLC.length() > 120 && !textLC.contains("摘要") && textLC.contains("，") && !textLC.contains("http://"))
                    continue;
                String lcs = "";
                if(textLC.length() < 30) {
                    // compute suffix array of concatenated text
                    SuffixArray suffix = new SuffixArray(textLC);

                    // search for longest common substring
                    for (int i = 1; i < textLC.length(); i++) {

                        // check if adjacent suffixes longer common substring
                        int length = suffix.lcp(i);
                        if (length > lcs.length()) {
                            lcs = text.substring(suffix.index(i), suffix.index(i) + length);
                        }
                    }
                }

                if (textLC.contains("手机看新闻")
                        || (textLC.length() > 120 && !textLC.contains("，"))
                        || (textLC.length() < 30 && (textLC.startsWith("• ") || textLC.startsWith("·")))
                        || textLC.contains("点击数")
                        || textLC.contains("来源")
                        || textLC.contains("关键字")
                        || (textLC.startsWith("·") && textLC.length() < 30)
                        || textLC.contains("发送到手机")
                        || textLC.contains("关键词")
                        || (textLC.endsWith("讯") && textLC.length() < 8)
                        || textLC.contains("字号")
                        || textLC.contains("字体")
                        || textLC.contains("字体")
                        || (textLC.length() - textLC.replaceAll("【","").length()) > 4
                        || ((textLC.length() - textLC.replaceAll("\\||>","").length()) > 3 )
                        || (lcs.length() > 1  && (textLC.length() - textLC.replaceAll(lcs,"").length()) < 8 )
                        || textLC.contains("原标题")
                        || (textLC.length()< 70 && textLC.contains("http://"))
                        || textLC.contains("参与")
                        || textLC.contains("我要举报")
                        || textLC.contains("收藏本文")
                        || textLC.contains("通讯员")
                        || ((textLC.contains("浏览") || textLC.contains("回复")) && (textLC.contains("(") || textLC.contains("（")))
                        || textLC.contains("联系方式")
                        || textLC.contains("实习生")
                        || textLC.contains("该帖被浏览")
                        || textLC.contains("在线投稿")
                        || textLC.contains(titleLC)
                        || titleLC.contains(textLC)
                        || textLC.contains("当前位置")
                        || textLC.contains("本网站")
                        || (textLC.contains("report") && textLC.contains("true"))
                        || textLC.contains("未经授权")
                        || textLC.contains("点击下载")
                        || (textLC.contains("下载") && textLC.contains("客户端"))
                        || textLC.contains("本页面")
                        || textLC.contains("正在浏览")
                        || textLC.contains("发表成功")
                        || textLC.contains("个人中心")
                        || (textLC.contains("不代表") && textLC.contains("观点"))
                        || textLC.contains("修改密码")
                        || textLC.contains("文字大小")
                        || textLC.contains("公网安备")
                        || textLC.contains("点击进入")
                        || textLC.contains("声明:")
                        || textLC.contains("声明：")
                        || textLC.contains("添加免费阅读")
                        || textLC.contains("分享到")
                        || textLC.contains("编辑短信")
                        || textLC.contains("移动发短信")
                        || (textLC.contains("短信") && textLC.contains("提醒"))
                        || (textLC.length()< 10 && textLC.contains("正在播放"))
                        || textLC.length()< 3
                        || textLC.contains("标签")
                        || textLC.contains("时长：")
                        || textLC.contains("类型：")
                        || textLC.contains("分类名称")
                        || textLC.contains("简介")
                        || (textLC.length()< 10 && textLC.contains("文/"))
                        || textLC.contains("条评论")
                        || textLC.contains("提要")
                        || textLC.contains("摘要")
                        || textLC.contains("频道")
                        || textLC.contains("copyright")
                        || (textLC.length()< 70 && (textLC.contains(" 摄") || textLC.contains("图)")
                                                                           || textLC.contains("图）")
                                                                           || textLC.contains("组图")
                                                                           || textLC.contains("海报")
                                                                           || textLC.contains("配图")
                                                                            || textLC.contains("资料图")
                                                                            || textLC.contains("查看原图")
                                                                            || textLC.contains("截图")
                                                                            || textLC.contains("翻拍")
                                                                            || textLC.contains("供图")
                                                                            || textLC.contains("图片作者")
                                                                           || textLC.contains("摄影：")  || textLC.contains("摄影:")))
                        || ((textLC.contains("在线")|| textLC.contains("作者")) && (textLC.contains("(") || textLC.contains(":") || textLC.contains("：")))
                        || textLC.matches("(.*[0-9]{4})[-./]([0-9]{2})[-./]([0-9]{2}.*)")
                        || textLC.matches("(.*[0-9]{2})[-./]([0-9]{2})[-./]([0-9]{2}.*)")
                        || textLC.matches("([0-9]{2})[-./]([0-9]{2}.*)")
                        || (textLC.matches("(图[0-9]{1,2}：.*)") && textLC.length() < 30)
                        || (textLC.contains("记者") && textLC.contains("报道") && (textLC.contains("（") || textLC.contains("(")))
                        || (textLC.length()< 50 && (textLC.contains("编辑") || textLC.contains("记者") || textLC.contains("报道") || textLC.contains("责编")) && (textLC.contains("【") || textLC.contains("/") || textLC.contains("[") || textLC.contains("（") || textLC.contains("(") || textLC.contains(":") || textLC.contains("：")))
                        ){
                    it.remove();
                    //tb.setIsContent(false);
                    hasChanges = true;
                }
            }
		}

		return hasChanges;
	}


    /*
* str_s = a b a a b a;
  Index = 0 1 2 3 4 5 6 7 8 9 10 11 12
  str[] = # a # b # a # a # b #  a #;
  P[]   = 0 0 0 2 0 1 5 1 0 3 0  1 0;
*/
    //最长公共子串，manacher 算法
    public static String longestPalindrome(String s) {
        if(s.length()==0||s== null) return "";
        if(s.length()==1) return s;
        int len=s.length();
        char[] ch= new char[2*len+1];
        int[] p= new int [2*len+1];
        //max是坐标id影响的最远的index，不是最大距离
        int max=0;
        //id表示的是影响到最远id的index，但不一定是最大的，所以后面还要寻找
        int id=0; //对称中心
        //添加特殊字符，一般是"#"
        for ( int i = 0; i < s.length(); i++) {
            ch[2*i+1]=s.charAt(i);
            ch[2*i]= '#';
        }
        ch[2*len]= '#';

        //进行判断
        for ( int i = 1; i < ch. length; i++) {
            //先把能进行扩展的p[i]值找到
            if(max>i)
                p[i]=Math. min(p[2*id-i], max-i);

            //对i点进行扩展
            while ((1+i+p[i])<ch.length&&(i-p[i]-1)>0&&(ch[1+i+p[i]]==ch[i-p[i]-1])) {
                p[i]++;
            }

            if(p[i]+i>max){
                max=p[i]+i;
                id=i;
            }
        }

        //寻找最大值
        max=0;
        id=0;
        for ( int i = 0; i < p. length; i++) {
            if(max<p[i]){
                max=p[i];
                id=i;
            }

        }
        //最大长度为原来的max+1
        StringBuffer sb= new StringBuffer();
        for ( int i = id-max; i <=id+max; i++) {
            if(ch[i]!= '#') sb.append(ch[i]);
        }
        return sb.toString();
    }


    /*
      * dp[i][j]表示从字符串i到j之间的字符串是否是回文串, dp[i][j]=0,i到j不是回文， dp[i][j]=1，i到j是回文
       * dp[i][j]=dp [i+1][j-1]  s[i]=s[j]
       * dp[i][j]=0 s[i]!=s[j] i-j的子串不是回文子串，不去理会，初试的时候就是0
       *
       *动态规划的时候，此时是按子串的长度进行的
       */

    public static String longestPalindrome1(String s) {
        if(s.length()==0||s== null) return "";
        if(s.length()==1) return s;

        int maxLen=1,maxIndex=0;
        int len=s.length();
        int[][] dp= new int[len][len];

        //长度为1的时候
        for ( int i = 0; i < len; i++) {
            dp[i][i]=1;
        }
        //长度为2
        for ( int i = 0; i < len-1; i++) {
            if(s.charAt(i)==s.charAt(i+1))
                dp[i][i+1]=1;
            maxIndex=i;
            maxLen=2;
        }
        //长度大于1的时候
        for ( int len1=3 ; len1<=len ; len1++) {
            for ( int i = 0; i <=len-len1; i++) {
                int j=i+len1-1;
                //dp[i+1][j-1]!=0判断不可少
                if(s.charAt(i)==s.charAt(j)&&dp[i+1][j-1]!=0)
                {dp[i][j]=1;
                    maxIndex=i;
                    maxLen=len1;
                }
            }
        }

        return s.substring(maxIndex,maxIndex+maxLen);
    }

}
