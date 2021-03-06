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

import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.labels.DefaultLabels;

import java.util.Iterator;
import java.util.List;

/**
 * Removes {@link de.l3s.boilerpipe.document.TextBlock}s which have explicitly been marked as
 * "not content".
 *
 * @author Christian Kohlschütter
 */
public final class BoilerplateBlockImageFilter implements BoilerpipeFilter {
	public static final BoilerplateBlockImageFilter INSTANCE = new BoilerplateBlockImageFilter(
			null);
	public static final BoilerplateBlockImageFilter INSTANCE_KEEP_TITLE = new BoilerplateBlockImageFilter(
			DefaultLabels.TITLE);
    public static final BoilerplateBlockImageFilter INSTANCE_TRIM = new BoilerplateBlockImageFilter(
            true);

	private  String labelToKeep;
    private  boolean isTrim = false;

	/**
	 * Returns the singleton instance for BoilerplateBlockFilter.
	 */
	public static BoilerplateBlockImageFilter getInstance() {
		return INSTANCE;
	}

	public BoilerplateBlockImageFilter(final String labelToKeep) {
		this.labelToKeep = labelToKeep;
	}

    public BoilerplateBlockImageFilter(boolean isTrim) {
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
                if(textLC.length() > 120 && !textLC.contains("摘要") && textLC.contains("，"))
                    continue;

                if (textLC.contains("手机看新闻")
                        || (textLC.length() > 120 && !textLC.contains("，"))
                        || (textLC.length() < 30 && textLC.startsWith("• "))
                        || textLC.contains("点击数")
                        || textLC.contains("来源")
                        || textLC.contains("关键字")
                        || textLC.contains("关键词")
                        || textLC.contains("字号")
                        || textLC.contains("字体")
                        || textLC.contains("字体")
                        || (textLC.length() - textLC.replaceAll("【","").length()) > 4
                        //|| textLC.contains("原标题")
                        || (textLC.length()< 70 && textLC.contains("http://"))
                        || textLC.contains("参与")
                        || textLC.contains("我要举报")
                        || textLC.contains("收藏本文")
                        || textLC.contains("在线投稿")
                        //|| textLC.contains(titleLC)
                        //|| titleLC.contains(textLC)
                        || textLC.contains("当前位置")
                        || textLC.contains("本网站")
                        || textLC.contains("未经授权")
                        || textLC.contains("正在浏览")
                        || textLC.contains("发表成功")
                        || textLC.contains("个人中心")
                        || textLC.contains("修改密码")
                        || textLC.contains("文字大小")
                        || textLC.contains("公网安备")
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
                        /*|| (textLC.length()< 70 && (textLC.contains(" 摄") || textLC.contains("图)")
                                                                           || textLC.contains("图）")
                                                                           || textLC.contains("组图")
                                                                           || textLC.contains("海报")
                                                                           || textLC.contains("配图")
                                                                            || textLC.contains("资料图")
                                                                            || textLC.contains("查看原图")
                                                                            || textLC.contains("截图")
                                                                            || textLC.contains("供图")
                                                                            || textLC.contains("图片作者")
                                                                           || textLC.contains("摄影：")  || textLC.contains("摄影:")))*/
                        || ((textLC.contains("在线")|| textLC.contains("作者")) && textLC.contains("("))
                        || textLC.matches("(.*[0-9]{4})[-./]([0-9]{2})[-./]([0-9]{2}.*)")
                        || textLC.matches("(.*[0-9]{2})[-./]([0-9]{2})[-./]([0-9]{2}.*)")
                        || textLC.matches("([0-9]{2})[-./]([0-9]{2}.*)")
                        || (textLC.contains("记者") && textLC.contains("报道") && (textLC.contains("（") || textLC.contains("(")))
                        || (/*textLC.length()< 20 && */(textLC.contains("编辑") || textLC.contains("记者") || textLC.contains("报道") || textLC.contains("责编")) && (textLC.contains("【") || textLC.contains("/") || textLC.contains("[") || textLC.contains("（") || textLC.contains("(") || textLC.contains(":") || textLC.contains("：")))
                        ){
                    it.remove();
                    //tb.setIsContent(false);
                    hasChanges = true;
                }
            }
		}

		return hasChanges;
	}

}
