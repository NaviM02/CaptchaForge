package com.navi.captchaapi.parser_lexer.cc.obj.node.interfaces;

import com.navi.captchaapi.parser_lexer.cc.obj.node.Node;
import com.navi.captchaapi.parser_lexer.cc.obj.node.SymTable;

import java.util.List;

public interface TableHolder {
    SymTable getTable();
    List<Node> getBody();
}
