package com.rhy.springaidemo.service.impl;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.collection.iteration.ReversiblePeekingIterator;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Service
public class MarkdownSplitter extends TextSplitter {
    @Override
    protected List<String> splitText(String text) {
        Parser parser = Parser.builder().build();
        Document markerDocument = parser.parse(text);

        List<String> result = new ArrayList<>();
        //获取文档所有子节点
        ReversiblePeekingIterator<Node> iterator = markerDocument.getChildren().iterator();
        // 创建一个可变字符串对象，用于存储当前正在处理的文本块
        StringBuilder builder = new StringBuilder();
        // 遍历文档的所有子节点
        while (iterator.hasNext()) {
            // 获取下一个节点
            Node node = iterator.next();
            // 如果当前节点是一个二级标题
            if (node instanceof Heading && ((Heading) node).getLevel() == 2) {
                // 如果 builder 不为空，则将其内容添加到结果列表中
                if (!builder.isEmpty()) {
                    result.add(builder.toString());
                }
                // 清空 builder
                builder.delete(0, builder.length());
                // 将当前二级标题的内容添加到 builder 中，并添加一个特殊标记 "==title=="
                builder.append("问："+node.getChars().replace(0, 2, ""));
                builder.append("\n答：");
            } else {
                // 如果当前节点不是二级标题，则将其内容添加到 builder 中
                builder.append(node.getChars());
            }
        }
        // 处理最后一个文本块
        if (!builder.isEmpty()) {
            result.add(builder.toString());
        }

        Iterator<String> iteratorFilter = result.iterator();
        while (iteratorFilter.hasNext()) {
            String filter = iteratorFilter.next();
            if(filter.equals("问： \n答：")){
                iteratorFilter.remove();
            }
        }

        return result;
    }
}
