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
                if (textLC.contains("手机看新闻")
                        || textLC.contains("点击数")
                        || textLC.contains("来源")
                        || textLC.contains("关键字")
                        || textLC.contains("关键词")
                        || textLC.contains("字号")
                        || textLC.contains("原标题")
                        || textLC.contains("http://")
                        || textLC.contains("参与")
                        || textLC.contains("我要举报")
                        || textLC.contains("收藏本文")
                        || textLC.contains("当前位置")
                        || textLC.contains("分享到")
                        || textLC.contains("标签")
                        || textLC.contains("简介")
                        || textLC.contains("频道")
                        || ((textLC.contains("在线")|| textLC.contains("作者")) && textLC.contains("("))
                        || textLC.matches("(.*[0-9]{4})[-./]([0-9]{2})[-./]([0-9]{2}.*)")
                        || (textLC.contains("记者") && textLC.contains("报道") && (textLC.contains("（") || textLC.contains("(")))
                        || (/*textLC.length()< 20 && */(textLC.contains("记者") || textLC.contains("报道") || textLC.contains("责编")) && (textLC.contains("【") || textLC.contains("（") || textLC.contains("(") || textLC.contains(":") || textLC.contains("：")))
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
