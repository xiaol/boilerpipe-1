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
package de.l3s.boilerpipe.filters.chinese;

import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.labels.DefaultLabels;

/**
 * Finds blocks which are potentially indicating the end of an article text and
 * marks them with {@link de.l3s.boilerpipe.labels.DefaultLabels#INDICATES_END_OF_TEXT}. This can be used
 * in conjunction with a downstream {@link de.l3s.boilerpipe.filters.english.IgnoreBlocksAfterContentFilter}.
 *
 * @author Christian Kohlsch��tter
 * @see de.l3s.boilerpipe.filters.english.IgnoreBlocksAfterContentFilter
 */
public class TerminatingBlocksFinder implements BoilerpipeFilter {
	public static final TerminatingBlocksFinder INSTANCE = new TerminatingBlocksFinder();

	/**
	 * Returns the singleton instance for TerminatingBlocksFinder.
	 */
	public static TerminatingBlocksFinder getInstance() {
		return INSTANCE;
	}

	// public static long timeSpent = 0;

	public boolean process(TextDocument doc)
			throws BoilerpipeProcessingException {
		boolean changes = false;

		// long t = System.currentTimeMillis();

		for (TextBlock tb : doc.getTextBlocks()) {
			final int numWords = tb.getNumWords();
			if (numWords < 50) {
				final String text = tb.getText().trim();
				final int len = text.length();
				if (len >= 4) {
					final String textLC = text.toLowerCase();
					if (textLC.startsWith("comments")
							|| startsWithNumber(textLC, len, " comments",
									" users responded in")
							|| textLC.startsWith("�� reuters")
                            || textLC.contains("关于我们")
							|| textLC.startsWith("please rate this")
							|| textLC.startsWith("post a comment")
							|| textLC.contains("what you think...")
                            || textLC.contains("联系我们")
                            || textLC.contains("禁止转载")
                            || textLC.contains("未经授权")
                            || textLC.contains("我要留言")
                            || textLC.contains("进入讨论区")
                            || textLC.contains("使用其他账号登录")
                            || textLC.contains("发言请遵守新闻跟帖服务协议")
                            || textLC.contains("文章内容不代表")
                            || textLC.contains("此文属于")
                            || textLC.contains("网站声明")
                            || textLC.contains("欢迎关注")
                            || textLC.contains("热点新闻")
                            || textLC.contains("热点关注")
                            || textLC.contains("热门评论")
                            || textLC.contains("热门跟帖")
                            || textLC.contains("网友评论")
                            || textLC.contains("热门图片")
                            || textLC.contains("阅读推荐")
                            || (textLC.contains("下载") && textLC.contains("客户端"))
                            || textLC.contains("延伸阅读")
                            || textLC.contains("精彩图片推荐")
                            || textLC.contains("热点图片")
                            || textLC.contains("条评论")
                            || textLC.contains("频道精选")
                            || textLC.contains("我要反馈")
                            || textLC.contains("下方二维码")
                            || textLC.contains("下一页")
                            || textLC.contains("更多精彩")
                            || textLC.contains("更多评论")
                            || textLC.contains("热门关键词")
                            || textLC.contains("版权作品")
                            || textLC.contains("相关图集")
                            || textLC.contains("相关报道")
                            || textLC.contains("查看更多")
                            || textLC.contains("特别提醒")
                            || textLC.contains("版权与免责声明")
                            || textLC.contains("免责声明")
                            || textLC.contains("版权所有")
                            || textLC.contains("注明出处")
                            || textLC.contains("相关新闻")
                            || textLC.contains("严禁转载")
                            || textLC.contains("进行评论")
                            || textLC.contains("正文已结束")
                            || textLC.contains("本文")
                            //|| textLC.contains("http://")
							|| textLC.contains("add your comment")
							|| textLC.contains("add comment")
							|| textLC.contains("reader views")
							|| textLC.contains("have your say")
							|| textLC.contains("reader comments")
							|| textLC.contains("r��tta artikeln")
							|| textLC.contains("Réagir")
							|| textLC.contains("Vos réactions ")
							|| textLC
									.equals("thanks for your comments - this feedback is now closed")) {
						tb.addLabel(DefaultLabels.INDICATES_END_OF_TEXT);
						changes = true;
					}
				} else if(tb.getLinkDensity() == 1.0) {
					if(text.equals("Comment")) {
						tb.addLabel(DefaultLabels.INDICATES_END_OF_TEXT);
					}
				}
			}
		}

		// timeSpent += System.currentTimeMillis() - t;

		return changes;
	}

	/**
	 * Checks whether the given text t starts with a sequence of digits,
	 * followed by one of the given strings.
	 * 
	 * @param t
	 *            The text to examine
	 * @param len
	 *            The length of the text to examine
	 * @param str
	 *            Any strings that may follow the digits.
	 * @return true if at least one combination matches
	 */
	private static boolean startsWithNumber(final String t, final int len,
			final String... str) {
		int j = 0;
		while (j < len && isDigit(t.charAt(j))) {
			j++;
		}
		if (j != 0) {
			for (String s : str) {
				if (t.startsWith(s, j)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isDigit(final char c) {
		return c >= '0' && c <= '9';
	}

}
